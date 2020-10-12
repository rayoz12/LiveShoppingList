import 'package:LiveShoppingList/components/family_card.dart';
import 'package:LiveShoppingList/components/list_card.dart';
import 'package:LiveShoppingList/components/user_card.dart';
import 'package:LiveShoppingList/providers/authentication_state.dart';
import 'package:LiveShoppingList/providers/user_family_state.dart';
import 'package:LiveShoppingList/shared/constants.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shoppingListAPI/api.dart';

class ListChoiceScreen extends StatefulWidget {
  ListChoiceScreen({Key key}) : super(key: key);

  @override
  _ListChoiceScreenState createState() => _ListChoiceScreenState();
}

class _ListChoiceScreenState extends State<ListChoiceScreen> {
  @override
  Widget build(BuildContext context) {
    final UserFamilyStateProvider userFamilyStateProvider = Provider.of<UserFamilyStateProvider>(context);
    final Family selectedFamily = userFamilyStateProvider.selectedUserFamily;
    String familyName = userFamilyStateProvider.selectedUserFamily.name;
    var lists = selectedFamily.lists;

    return Scaffold(
      appBar: AppBar(title: Text("$familyName Shopping Lists")),
      floatingActionButton: FloatingActionButton.extended(
        icon: Icon(Icons.add),
        label: Text("New List"),
        onPressed: () {
          print("New List");
        },
      ),
      body: Stack(
        children: <Widget>[
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              FamilyCard(
                family: selectedFamily,
              ),
              Divider(
                height: 20,
                color: Colors.grey,
                indent: 10,
                endIndent: 10,
              ),
              Container(
                padding: EdgeInsets.symmetric(horizontal: 5),
                child: Column(
                  children: [
                    Text(
                      "Here are the Shopping Lists",
                      style: TextStyle(
                        color: Colors.white,
                        fontFamily: "OpenSans",
                        fontSize: 24.0,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    ListView.builder(
                      itemCount: lists.length,
                      shrinkWrap: true,
                      itemBuilder: (context, index) {
                        return ShoppingListCard(
                          shoppingList: lists[index],
                          selectedcb: () {
                            var list = lists[index];
                            userFamilyStateProvider.selectList(list);
                            print("Tapped $list");
                            Navigator.pushNamed(context, "/list");
                          },
                        );
                      },
                    )
                  ],
                ),
              )
            ],
          )
        ],
      ),
    );
  }
}
