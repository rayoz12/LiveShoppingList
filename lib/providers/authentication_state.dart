import 'package:LiveShoppingList/http/authentication.dart';
import 'package:LiveShoppingList/providers/setttings.dart';
import 'package:flutter/widgets.dart';
import 'package:rxdart/rxdart.dart';
import 'package:shoppingListAPI/api.dart';

class AuthenticationProvider extends ChangeNotifier {
  // BehaviorSubject<bool> isAuthenticated$;
  // BehaviorSubject<User> loggedInUser$;

  bool isAuthenticated = false;
  User loggedInUser;

  List<Family> userFamilies;
  Family selectedUserFamily;

  String get token => _token;

  String _token;
  SettingsProvider _settings;
  AuthenticatonAPI API;

  AuthenticationProvider() {
    // isAuthenticated$ = new BehaviorSubject<bool>.seeded(false);
    // loggedInUser$ = new BehaviorSubject<User>.seeded(null);
  }

  update(SettingsProvider settingsProvider) {
    _settings = settingsProvider;
    API = AuthenticatonAPI(_settings.settings.API_URL);
    return this;
  }

  Future<User> login(String username, String password) async {
    var token = await API.login(username, password);
    // Dart should error and not continue if the auth fails
    _token = token;
    var user = await API.me();

    var families = await API.getUserFamilies();

    // loggedInUser$.add(user);
    // isAuthenticated$.add(true);
    setAuthenticationState(user, families);
    return user;
  }

  logout() {
    // isAuthenticated$.add(false);
    // loggedInUser$.add(null);

    loggedInUser = null;
    isAuthenticated = false;
    notifyListeners();
  }

  // When Authentication is successful
  setAuthenticationState(User user, List<Family> families) {
    loggedInUser = user;
    userFamilies = families;
    isAuthenticated = true;
    notifyListeners();
  }
}
