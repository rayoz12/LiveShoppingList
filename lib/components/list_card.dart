import 'package:flutter/material.dart';
import 'package:shoppingListAPI/api.dart';

class ShoppingListCard extends StatelessWidget {
  final ShoppingList shoppingList;
  final void Function() selectedcb;

  const ShoppingListCard({Key key, @required this.shoppingList, this.selectedcb}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 100,
      child: Card(
        elevation: 10,
        child: InkWell(
          onTap: selectedcb,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ListTile(
                leading: Image.asset("assets/avatar_male.png"),
                title: Text(shoppingList.name),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
