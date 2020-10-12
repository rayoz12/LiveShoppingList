import 'package:LiveShoppingList/app_theme.dart';
import 'package:LiveShoppingList/providers/authentication_state.dart';
import 'package:LiveShoppingList/providers/setttings.dart';
import 'package:LiveShoppingList/providers/user_family_state.dart';
import 'package:LiveShoppingList/routes.dart';
import 'package:LiveShoppingList/services/theme_notifier.dart';
import 'package:provider/provider.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MultiProvider(
    providers: [
      ChangeNotifierProvider<ThemeNotifier>(
        create: (_) => ThemeNotifier(ThemeMode.system),
      ),
      ChangeNotifierProvider<SettingsProvider>(
        create: (_) => SettingsProvider(),
      ),
      ChangeNotifierProxyProvider<SettingsProvider, AuthenticationProvider>(
        create: (_) => AuthenticationProvider(),
        update: (context, settingsProvider, authProvider) => authProvider.update(settingsProvider),
      ),
      ChangeNotifierProxyProvider<AuthenticationProvider, UserFamilyStateProvider>(
        create: (_) => UserFamilyStateProvider(),
        update: (context, authProvider, userFamilyProvider) => userFamilyProvider.update(authProvider.API),
      ),
    ],
    child: MyApp(),
  ));

  // runApp(
  //   ChangeNotifierProvider<ThemeNotifier>(
  //     create: (_) => ThemeNotifier(ThemeMode.system),
  //     child: MyApp(),
  //   ),
  // );
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Live Shopping List',
        theme: AppTheme().lightTheme,
        darkTheme: AppTheme().darkTheme,
        // themeMode: ThemeMode.light,
        themeMode: context.watch<ThemeNotifier>().getThemeMode(),
        initialRoute: "/",
        routes: routes);
  }
}
