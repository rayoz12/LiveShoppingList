import 'package:dio/dio.dart';
import 'package:shoppingListAPI/api.dart';

class AuthenticatonAPI {
  String API_URL;
  String BASE_API;

  String token;

  Dio dio = new Dio();

  AuthenticatonAPI(String API) {
    this.API_URL = API + "/auth";
    this.BASE_API = API;
  }

  /// Returns the token from the login
  Future<String> login(String username, String password) async {
    var response = await dio.post(API_URL + "/login", data: {'username': username, 'password': password});
    token = response.data["token"];

    dio.interceptors.add(InterceptorsWrapper(onRequest: (RequestOptions options) async {
      var customHeaders = {
        'Authorization': "bearer $token"
        // other headers
      };
      options.headers.addAll(customHeaders);
      return options;
    }));

    return token;
  }

  Future<User> me({String token}) async {
    var response = await dio.get(API_URL + "/me");
    return User.fromJson(response.data["user"]);
  }

  /// Gets the user family from a logged in user. Requires that the user be logged in first
  Future<List<Family>> getUserFamilies() async {
    var response = await dio.get(BASE_API + "/family/user");
    print(response.data.toString());
    return Family.listFromJson(response.data);
  }
}
