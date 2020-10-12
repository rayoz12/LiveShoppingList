import 'package:LiveShoppingList/http/authentication.dart';
import 'package:dio/dio.dart';
import 'package:shoppingListAPI/api.dart';

class ShoppingListAPI {
  String API_URL;

  Dio dio;

  ShoppingListAPI(AuthenticatonAPI auth) {
    API_URL = auth.BASE_API + "/shopping-list";
    dio = auth.dio;
  }

  Future<ShoppingList> markItemOff(int listID, int itemID) async {
    var list = await dio.put("$API_URL/$listID/markoff/$itemID");
    return ShoppingList.fromJson(list.data);
  }

  Future<ShoppingList> addItem(int listID, AddItemDTO item) async {
    var list = await dio.post("$API_URL/$listID/add", data: item.toJson());
    return ShoppingList.fromJson(list.data);
  }

  Future<ShoppingList> deleteItem(int listID, int itemID) async {
    var list = await dio.delete("$API_URL/$listID/delete/$itemID");
    return ShoppingList.fromJson(list.data);
  }
}
