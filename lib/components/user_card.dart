import 'package:flutter/material.dart';
import 'package:shoppingListAPI/api.dart';

class UserCard extends StatelessWidget {
  final User user;

  const UserCard({Key key, @required this.user}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 100,
      child: Card(
        elevation: 10,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ListTile(
              leading: Image.asset("assets/avatar_male.png"),
              title: Text("${user.firstName} ${user.lastName}"),
            ),
          ],
        ),
      ),
    );
  }
}
