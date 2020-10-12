import 'package:flutter/material.dart';

// class AppColors {
//   static const primary = const Color.fromARGB(255, 30, 32, 34);
//   static const primaryDark = const Color.fromARGB(255, 20, 22, 23);
//   static const accent = Color.fromARGB(255, 58, 134, 146);
//   static const white = Colors.white;
//   static const textGrey = Colors.grey;
//   static const textBlack = Colors.black;
//   static const lightGrey = Color(0xffE8E8E8);
//   static const lightGreyDarkMode = Colors.black;
//   static const darkPink = Color(0xFFFf0266);
//   static const grey2 = Color(0xFFEEEEEE);
//   static const yellow = Color(0xFFFFFCB6);
//   static const blue = Color(0xFFB4FFED);
//   static const dark_card_bg = Color(0xFF5F79B9);
//   static const dark_card_forground = Color(0xFF042B3D);
//   static const purple = Color(0xFF411846);
//   static const lightThemeShadowColor = Color(0xFF000014);
//   static const darkThemeShadowColor = Color(0xFF1E2247);
// }

class AppTheme {
  get darkTheme => ThemeData(
        primarySwatch: Colors.grey,
        appBarTheme: AppBarTheme(
          brightness: Brightness.dark,
          color: Colors.black,
        ),
        inputDecorationTheme: InputDecorationTheme(
          hintStyle: TextStyle(color: Colors.grey),
          labelStyle: TextStyle(color: Colors.white),
        ),
        brightness: Brightness.dark,
        canvasColor: Colors.black,
        accentColor: Color(0xFFFf0266),
        accentIconTheme: IconThemeData(color: Colors.white),
        visualDensity: VisualDensity.adaptivePlatformDensity,
        snackBarTheme: SnackBarThemeData(behavior: SnackBarBehavior.floating),
      );

  get lightTheme => ThemeData(
        primarySwatch: Colors.grey,
        appBarTheme: AppBarTheme(
          brightness: Brightness.light,
          color: Color(0xFFEEEEEE),
        ),
        inputDecorationTheme: InputDecorationTheme(
          hintStyle: TextStyle(color: Colors.grey),
          labelStyle: TextStyle(color: Colors.white),
        ),
        canvasColor: Colors.white,
        brightness: Brightness.light,
        accentColor: Color(0xFFEEEEEE),
        accentIconTheme: IconThemeData(color: Colors.black),
        visualDensity: VisualDensity.adaptivePlatformDensity,
        snackBarTheme: SnackBarThemeData(behavior: SnackBarBehavior.floating),
      );
}
