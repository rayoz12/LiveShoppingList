import 'package:LiveShoppingList/providers/setttings.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:settings_ui/settings_ui.dart';

class SettingsScreen extends StatefulWidget {
  SettingsScreen({Key key}) : super(key: key);

  @override
  _SettingsScreenState createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  SettingsProvider _settingsProvider;

  @override
  Widget build(BuildContext context) {
    _settingsProvider = Provider.of<SettingsProvider>(context);

    return Scaffold(
      appBar: AppBar(
        title: Text("Settings"),
      ),
      body: SettingsList(
        sections: [
          SettingsSection(
            title: 'Connection',
            tiles: [
              SettingsTile(
                title: 'Server URL',
                subtitle: _settingsProvider.settings.API_URL,
                leading: Icon(Icons.language),
                onTap: () => _ConnectionPressed(context),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Future<void> _ConnectionPressed(BuildContext ctx) async {
    String newURL = _settingsProvider.settings.API_URL;
    String result = await showDialog(
      context: ctx,
      barrierDismissible: true,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text("Server URL"),
          content: TextFormField(
            decoration: InputDecoration(
              border: OutlineInputBorder(),
              labelText: 'Server URL',
            ),
            onChanged: (value) => newURL = value,
            initialValue: newURL,
          ),
          actions: <Widget>[
            TextButton(
              child: Text("OK"),
              onPressed: () => Navigator.of(context).pop(newURL),
            ),
            TextButton(
              child: Text("Cancel"),
              onPressed: () => Navigator.of(context).pop(null),
            )
          ],
        );
      },
    );

    if (result != null) {
      print(result);
      _settingsProvider.setAPI_URL(result);
    }
  }
}
