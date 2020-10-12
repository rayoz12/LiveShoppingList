import 'package:LiveShoppingList/components/family_card.dart';
import 'package:LiveShoppingList/components/user_card.dart';
import 'package:LiveShoppingList/providers/authentication_state.dart';
import 'package:LiveShoppingList/providers/user_family_state.dart';
import 'package:LiveShoppingList/shared/constants.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shoppingListAPI/api.dart';

class FamilyChoiceScreen extends StatefulWidget {
  FamilyChoiceScreen({Key key}) : super(key: key);

  @override
  _FamilyChoiceScreenState createState() => _FamilyChoiceScreenState();
}

class _FamilyChoiceScreenState extends State<FamilyChoiceScreen> {
  @override
  Widget build(BuildContext context) {
    final AuthenticationProvider authProvider = Provider.of<AuthenticationProvider>(context);
    final UserFamilyStateProvider userFamilyStateProvider = Provider.of<UserFamilyStateProvider>(context);
    final User user = authProvider.loggedInUser;
    final List<Family> families = userFamilyStateProvider.userFamilies;
    print([user, families]);

    return Scaffold(
      appBar: AppBar(title: Text("Choose a Family")),
      body: Stack(
        children: <Widget>[
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              UserCard(
                user: user,
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
                      "Families you're part of",
                      style: TextStyle(
                        color: Colors.white,
                        fontFamily: "OpenSans",
                        fontSize: 24.0,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    ListView.builder(
                      itemCount: families.length,
                      shrinkWrap: true,
                      itemBuilder: (context, index) {
                        return FamilyCard(
                          family: families[index],
                          selectedcb: () {
                            var family = families[index];
                            userFamilyStateProvider.selectUserFamily(family);
                            print("Tapped $family");
                            Navigator.pushNamed(context, "/list-choice");
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
