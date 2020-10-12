import 'package:flutter/widgets.dart';
import 'package:shared_preferences/shared_preferences.dart';

/*
 * Default Values are the settings default values
 */
class SettingsStruct {
  String API_URL;

  SettingsStruct([this.API_URL = "http://192.168.1.100:3000"]) {
    print(API_URL);
  }

  SettingsStruct.fromSettings(SettingsStruct data) {
    API_URL = data.API_URL;
  }
}

class SettingsProvider extends ChangeNotifier {
  // BehaviorSubject<SettingsStruct> settings$ =
  //     new BehaviorSubject<SettingsStruct>.seeded(SettingsStruct());

  var settings = SettingsStruct();

  Future<SharedPreferences> prefsFuture = SharedPreferences.getInstance();

  SettingsProvider() {
    readPrefs();
  }

  // dispose() {
  //   settings$.close();
  // }

  readPrefs() async {
    SharedPreferences loadedPrefs = await prefsFuture;
    settings.API_URL = loadedPrefs.getString('API_URL') ?? settings.API_URL;
    // settings$.add(settings);
  }

  setAPI_URL(String url) async {
    settings.API_URL = url;
    commit();
  }

  commit() async {
    SharedPreferences loadedPrefs = await prefsFuture;
    loadedPrefs.setString('API_URL', settings.API_URL);
    notifyListeners();
  }
}
