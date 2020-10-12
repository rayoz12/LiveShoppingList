import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:shoppingListAPI/api.dart';

class FamilyCard extends StatelessWidget {
  final Family family;
  final void Function() selectedcb;

  const FamilyCard({Key key, @required this.family, this.selectedcb}) : super(key: key);

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
                title: Text(family.name),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
