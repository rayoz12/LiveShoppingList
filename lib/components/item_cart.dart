import 'package:flutter/material.dart';
import 'package:shoppingListAPI/api.dart';

class ItemCard extends StatelessWidget {
  final Item item;
  final void Function() selectedcb;
  final void Function() onLongPress;

  const ItemCard({Key key, @required this.item, this.selectedcb, this.onLongPress}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 100,
      child: Card(
        elevation: 10,
        child: InkWell(
          onTap: selectedcb,
          onLongPress: onLongPress,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ListTile(
                leading: Image.asset("assets/avatar_male.png"),
                title: Text(item.quantity > 1 ? "${item.quantity} ${item.name}" : item.name),
                subtitle: Text(item.comments),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
