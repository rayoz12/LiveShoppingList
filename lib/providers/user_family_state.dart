import 'package:LiveShoppingList/http/authentication.dart';
import 'package:LiveShoppingList/http/shopping_list_API.dart';
import 'package:flutter/widgets.dart';
import 'package:shoppingListAPI/api.dart';

class UserFamilyStateProvider extends ChangeNotifier {
  List<Family> userFamilies;
  Family selectedUserFamily;

  List<ShoppingList> get familyLists => selectedUserFamily.lists;
  ShoppingList selectedList;

  ShoppingListAPI API;

  UserFamilyStateProvider();

  update(AuthenticatonAPI auth) {
    API = ShoppingListAPI(auth);
    return this;
  }

  setUserFamilies(List<Family> families) {
    userFamilies = families;
  }

  selectUserFamily(Family family) {
    selectedUserFamily = family;
    notifyListeners();
  }

  selectList(ShoppingList list) {
    selectedList = list;
    notifyListeners();
  }

  markItemOff(int listID, int itemID) async {
    var newList = await API.markItemOff(listID, itemID);
    selectList(newList);
  }

  Future<ShoppingList> addItem(int listID, AddItemDTO item) async {
    var newList = await API.addItem(listID, item);
    print(newList);
    selectList(newList);
    return newList;
  }

  Future<ShoppingList> deleteItem(int listID, int itemID) async {
    var newList = await API.deleteItem(listID, itemID);
    print(newList);
    selectList(newList);
    return newList;
  }
}
