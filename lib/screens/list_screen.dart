import 'package:LiveShoppingList/components/item_cart.dart';
import 'package:LiveShoppingList/components/list_card.dart';
import 'package:LiveShoppingList/providers/authentication_state.dart';
import 'package:flutter/material.dart';
import 'package:numberpicker/numberpicker.dart';
import 'package:provider/provider.dart';
import 'package:shoppingListAPI/api.dart';
import 'package:LiveShoppingList/providers/user_family_state.dart';

class ListScreen extends StatefulWidget {
  ListScreen({Key key}) : super(key: key);

  @override
  _ListScreenState createState() => _ListScreenState();
}

class _ListScreenState extends State<ListScreen> {
  final GlobalKey<AnimatedListState> _buyListKey = GlobalKey<AnimatedListState>();
  final GlobalKey<AnimatedListState> _boughtlistKey = GlobalKey<AnimatedListState>();

  int _page = 0; // 0 is unmarked, 1 is marked
  PageController _c;
  @override
  void initState() {
    _c = new PageController(
      initialPage: _page,
    );
    super.initState();
  }

  void _onItemTapped(int index) async {
    await _c.animateToPage(index, duration: Duration(milliseconds: 750), curve: Curves.easeInOut);
    setState(() {
      _page = _c.page.toInt();
    });
  }

  @override
  Widget build(BuildContext context) {
    final AuthenticationProvider authProvider = Provider.of<AuthenticationProvider>(context);
    final UserFamilyStateProvider userFamilyStateProvider = Provider.of<UserFamilyStateProvider>(context);
    final ShoppingList list = userFamilyStateProvider.selectedList;

    // final AnimatedList buyList = buildUnboughtItems(list, userFamilyStateProvider);
    // final AnimatedList boughtList = buildBoughtItems(list, userFamilyStateProvider);

    final AnimatedList buyList = buildItems(list, userFamilyStateProvider, false);
    final AnimatedList boughtList = buildItems(list, userFamilyStateProvider, true);

    return Scaffold(
      appBar: AppBar(title: Text("${list.name}")),
      floatingActionButton: FloatingActionButton(
        onPressed: () async {
          AddItemDTO newItem = await showDialog(
            context: context,
            barrierDismissible: false,
            builder: (context) => buildAddItemDialog(),
          );
          if (newItem == null) {
            return;
          }

          print(newItem);

          var newList = await userFamilyStateProvider.addItem(list.id, newItem);
          // Find the inserted location of the new item and add it to the animated list
          int itemIdx = newList.items.indexWhere((element) =>
              element.name == newItem.name &&
              element.quantity == newItem.quantity &&
              element.comments == newItem.comments);
          if (itemIdx > 0) {
            addItemToAnimatedList(_buyListKey, itemIdx);
          } else {
            // Just add to the end
            addItemToAnimatedList(_buyListKey, newList.items.length - 1);
          }
        },
        tooltip: "Add Item",
        child: Icon(Icons.add),
      ),
      bottomNavigationBar: BottomNavigationBar(
        items: const <BottomNavigationBarItem>[
          BottomNavigationBarItem(
            icon: Icon(Icons.shopping_cart),
            title: Text('Buy'),
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.check),
            title: Text('Bought Items'),
          ),
        ],
        currentIndex: _page,
        onTap: _onItemTapped,
      ),
      body: Stack(
        children: <Widget>[
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              ShoppingListCard(
                shoppingList: list,
              ),
              Divider(
                height: 20,
                color: Colors.grey,
                indent: 10,
                endIndent: 10,
              ),
              Expanded(
                child: PageView(
                  controller: _c,
                  onPageChanged: (newPage) {
                    setState(() {
                      this._page = newPage;
                    });
                  },
                  children: [buyList, boughtList],
                ),
              )
            ],
          )
        ],
      ),
    );
  }

  AnimatedList buildItems(ShoppingList list, UserFamilyStateProvider stateProvider, bool shouldBuildMarkedOffItems) {
    int itemCount = 0;
    List<Item> items;
    GlobalKey<AnimatedListState> key = shouldBuildMarkedOffItems ? _boughtlistKey : _buyListKey;
    if (shouldBuildMarkedOffItems) {
      items = list.items.where((element) => element.markedOff).toList();
      itemCount = items.length;
    } else {
      items = list.items.where((element) => !element.markedOff).toList();
      itemCount = items.length;
    }
    return AnimatedList(
      key: key,
      initialItemCount: itemCount,
      itemBuilder: (context, index, animation) {
        var item = items.elementAt(index);
        return SizeTransition(
          sizeFactor: animation,
          child: ItemCard(
            item: item,
            selectedcb: () async {
              print("Tapped $item");
              await stateProvider.markItemOff(list.id, item.id);
              removeItemFromAnimatedList(key, index, item);
            },
            onLongPress: () async {
              print(index);
              bool shouldDelete = await showDialog<bool>(
                context: context,
                barrierDismissible: true,
                builder: (context) {
                  return AlertDialog(
                    title: Text("Delete Item?"),
                    content: Text("Are you sure you want to delete this item?"),
                    actions: <Widget>[
                      FlatButton(
                        onPressed: () => Navigator.of(context).pop(false),
                        child: Text("Cancel"),
                      ),
                      FlatButton(
                        onPressed: () => Navigator.of(context).pop(true),
                        child: Text("Yes"),
                      ),
                    ],
                  );
                },
              );

              if (shouldDelete) {
                print("Deleting Item: $item");
                var newList = await stateProvider.deleteItem(list.id, item.id);
                removeItemFromAnimatedList(key, index, item);
              }
            },
          ),
        );
      },
    );
  }

  void removeItemFromAnimatedList(GlobalKey<AnimatedListState> key, int index, Item item) {
    key.currentState.removeItem(index, (context, animation) {
      return FadeTransition(
        opacity: CurvedAnimation(parent: animation, curve: Interval(0.5, 1.0)),
        child: SizeTransition(
          sizeFactor: CurvedAnimation(parent: animation, curve: Interval(0.0, 1.0)),
          axisAlignment: 0.0,
          child: ItemCard(item: item),
        ),
      );

      // return FadeTransition(opacity: animation, child: ItemCard(item: item));
      // return SizeTransition(sizeFactor: animation, child: ItemCard(item: item));
    });
  }

  void addItemToAnimatedList(GlobalKey<AnimatedListState> key, int index) {
    key.currentState.insertItem(index);
  }

  AnimatedList buildUnboughtItems(ShoppingList list, UserFamilyStateProvider stateProvider) {
    return AnimatedList(
      key: _buyListKey,
      initialItemCount: list.items.where((element) => !element.markedOff).length,
      itemBuilder: (context, index, animation) {
        var item = list.items.where((element) => !element.markedOff).elementAt(index);
        return SizeTransition(
          sizeFactor: animation,
          child: ItemCard(
            item: item,
            selectedcb: () {
              print("Tapped $item");
              stateProvider.markItemOff(list.id, item.id);
            },
          ),
        );
      },
    );
  }

  AnimatedList buildBoughtItems(ShoppingList list, UserFamilyStateProvider stateProvider) {
    return AnimatedList(
      key: _boughtlistKey,
      initialItemCount: list.items.where((element) => element.markedOff).length,
      itemBuilder: (context, index, animation) {
        var item = list.items.where((element) => element.markedOff).elementAt(index);
        return SizeTransition(
          sizeFactor: animation,
          child: ItemCard(
            item: item,
            selectedcb: () {
              print("Tapped $item");
              stateProvider.markItemOff(list.id, item.id);
            },
          ),
        );
      },
    );
  }

  AlertDialog buildAddItemDialog() {
    //var newItem = {'name': "", 'quantity': 0, 'comments': ''};
    AddItemDTO newItem = AddItemDTO();
    newItem.quantity = 1;
    return AlertDialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12.0)),
      title: Text("Add an Item"),
      content: Container(
        height: 300.0,
        width: 300.0,
        child: Column(
          children: [
            TextFormField(
              decoration: InputDecoration(hintText: "Name"),
              onChanged: (value) {
                newItem.name = value;
              },
            ),
            SizedBox(height: 10),
            Text("Quantity"),
            NumberPicker.integer(
              initialValue: 1,
              minValue: 1,
              maxValue: 100,
              infiniteLoop: true,
              decoration: BoxDecoration(
                border: new Border(
                  top: new BorderSide(
                    style: BorderStyle.solid,
                    color: Colors.black26,
                  ),
                  bottom: new BorderSide(
                    style: BorderStyle.solid,
                    color: Colors.black26,
                  ),
                ),
              ),
              onChanged: (value) {
                newItem.quantity = value;
              },
            ),
            TextFormField(
              decoration: InputDecoration(hintText: "Comments"),
              onChanged: (value) {
                newItem.comments = value;
              },
            ),
          ],
        ),
      ),
      actions: <Widget>[
        FlatButton(
          child: Text("Cancel"),
          onPressed: () {
            Navigator.pop(context, null);
          },
        ),
        FlatButton(
          child: Text("OK"),
          onPressed: () {
            Navigator.pop(context, newItem);
          },
        ),
      ],
    );
  }
}
