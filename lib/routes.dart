import 'package:LiveShoppingList/screens/family_choice_screen.dart';
import 'package:LiveShoppingList/screens/list_choice_screen.dart';
import 'package:LiveShoppingList/screens/list_screen.dart';
import 'package:LiveShoppingList/screens/login_screen.dart';
import 'package:LiveShoppingList/screens/settings.dart';
import 'package:flutter/widgets.dart';

final Map<String, WidgetBuilder> routes = <String, WidgetBuilder>{
  "/settings": (BuildContext context) => SettingsScreen(),
  "/": (BuildContext context) => LoginScreen(),
  "/family-choice": (BuildContext context) => FamilyChoiceScreen(),
  "/list-choice": (BuildContext context) => ListChoiceScreen(),
  "/list": (BuildContext context) => ListScreen(),
};
