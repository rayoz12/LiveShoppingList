import 'package:LiveShoppingList/providers/authentication_state.dart';
import 'package:LiveShoppingList/providers/user_family_state.dart';
import 'package:LiveShoppingList/shared/constants.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shoppingListAPI/api.dart';

class LoginScreen extends StatefulWidget {
  LoginScreen({Key key}) : super(key: key);

  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  bool _rememberMe = false;

  String username = "";
  String password = "";

  // Form
  final _loginFormKey = GlobalKey<FormState>();

  @override
  void initState() {
    super.initState();

    // Provider.of<AuthenticationProvider>(context, listen: false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      body: Stack(children: <Widget>[
        Container(
          height: double.infinity,
          width: double.infinity,
          decoration: BoxDecoration(
            gradient: LinearGradient(begin: Alignment.topLeft, end: Alignment.bottomRight, colors: [
              Color(0xFF73AEF5),
              Color(0xFF61A4F1),
              Color(0xFF478DE0),
              Color(0xFF398AE5),
            ], stops: [
              0.1,
              0.4,
              0.7,
              0.9,
            ]),
          ),
        ),
        Container(
          height: double.infinity,
          child: Column(
            children: [
              Container(
                child: IconButton(
                  icon: Icon(
                    Icons.settings_outlined,
                    size: 30,
                  ),
                  tooltip: "Settings",
                  onPressed: () {
                    print("Settings");
                    Navigator.pushNamed(context, "/settings");
                  },
                  splashRadius: 200.0,
                  splashColor: Colors.green,
                ),
                padding: EdgeInsets.only(top: 30.0, left: 10.0),
                alignment: Alignment.topLeft,
              ),
              SingleChildScrollView(
                physics: AlwaysScrollableScrollPhysics(),
                padding: EdgeInsets.symmetric(horizontal: 40, vertical: 90),
                child: Form(
                  key: _loginFormKey,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      Text(
                        "Sign In",
                        style: TextStyle(
                          color: Colors.white,
                          fontFamily: "OpenSans",
                          fontSize: 30.0,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      SizedBox(
                        height: 30.0,
                      ),
                      _buildEmailTF(),
                      SizedBox(
                        height: 30.0,
                      ),
                      _buildPasswordTF(),
                      _buildForgotPassword(),
                      _buildRememberMeCheckbox(),
                      _buildLoginButton(),
                      _buildSignInWithText(),
                      SizedBox(
                        height: 30.0,
                      ),
                      _buildLoginInWithIcon(AssetImage("assets/logos/google.jpg"), () => print("Google Tapped"))
                    ],
                  ),
                ),
              ),
            ],
          ),
        )
      ]),
    );
  }

  Widget _buildLoginInWithIcon(AssetImage logo, Function onTap) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        height: 60.0,
        width: 60.0,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: Colors.white,
          boxShadow: [
            BoxShadow(
              color: Colors.black26,
              offset: Offset(0, 2),
              blurRadius: 6.0,
            ),
          ],
          image: DecorationImage(
            image: logo,
          ),
        ),
      ),
    );
  }

  Column _buildSignInWithText() {
    return Column(
      children: <Widget>[
        Text(
          "- OR -",
          style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.w400,
          ),
        ),
        SizedBox(
          height: 20.0,
        ),
        Text(
          "Sign in with",
          style: kLabelStyle,
        )
      ],
    );
  }

  Container _buildLoginButton() {
    return Container(
      padding: EdgeInsets.symmetric(vertical: 25.0),
      width: double.infinity,
      child: Builder(builder: (BuildContext context) {
        return RaisedButton(
          elevation: 5.0,
          onPressed: () async {
            if (_loginFormKey.currentState.validate()) {
              FocusScope.of(context).unfocus();
              User user;
              try {
                var auth = Provider.of<AuthenticationProvider>(context, listen: false);
                final UserFamilyStateProvider familyState =
                    Provider.of<UserFamilyStateProvider>(context, listen: false);

                user = await auth.login(username, password);
                familyState.setUserFamilies(auth.userFamilies);

                Scaffold.of(context).showSnackBar(
                  SnackBar(
                    content: Text("Successfully Logged in!"),
                    backgroundColor: Colors.lightGreen,
                  ),
                );
                Navigator.pushNamed(context, "/family-choice");
              } on DioError catch (e) {
                // The request was made and the server responded with a status code
                // that falls out of the range of 2xx and is also not 304.
                if (e.response != null) {
                  print(e.response.data);
                  print(e.response.headers);
                  print(e.response.request);
                  Scaffold.of(context).showSnackBar(
                    SnackBar(
                      content: Text("Failed to Login, check your details"),
                      backgroundColor: Colors.redAccent,
                    ),
                  );
                } else {
                  // Something happened in setting up or sending the request that triggered an Error
                  print(e.request);
                  print(e.message);
                }
              }
            }
          },
          padding: EdgeInsets.all(15.0),
          color: Colors.white,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(30.0),
          ),
          child: Text(
            "LOGIN",
            style: TextStyle(
              color: Color(0xFF527DAA),
              letterSpacing: 1.5,
              fontSize: 18.0,
              fontWeight: FontWeight.bold,
              fontFamily: "OpenSans",
            ),
          ),
        );
      }),
    );
  }

  Widget _buildRememberMeCheckbox() {
    return Container(
      height: 20.0,
      child: Row(
        children: <Widget>[
          Theme(
            data: ThemeData(unselectedWidgetColor: Colors.white),
            child: Checkbox(
              value: _rememberMe,
              checkColor: Colors.green,
              activeColor: Colors.white,
              onChanged: (value) {
                setState(() {
                  _rememberMe = value;
                });
              },
            ),
          ),
          Text(
            'Remember me',
            style: kLabelStyle,
          ),
        ],
      ),
    );
  }

  Widget _buildForgotPassword() {
    return Container(
      alignment: Alignment.centerRight,
      child: FlatButton(
        onPressed: () => print("Forgot Password Clicked"),
        padding: EdgeInsets.only(right: 0),
        child: Text(
          "Forgot Password?",
          style: kLabelStyle,
        ),
      ),
    );
  }

  Widget _buildEmailTF() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Text(
          "Email",
          style: kLabelStyle,
        ),
        SizedBox(
          height: 10.0,
        ),
        Container(
          alignment: Alignment.centerLeft,
          decoration: kBoxDecorationStyle,
          height: 60.0,
          child: TextFormField(
            keyboardType: TextInputType.emailAddress,
            style: TextStyle(color: Colors.white, fontFamily: "OpenSans"),
            initialValue: "rayoz12@yahoo.com.au", // FIXME: Remove me
            decoration: InputDecoration(
              border: InputBorder.none,
              contentPadding: EdgeInsets.only(top: 14.0),
              prefixIcon: Icon(
                Icons.email,
                color: Colors.white,
              ),
              hintText: "Enter your Email",
              hintStyle: kHintTextStyle,
            ),
            onChanged: (value) => username = value.trim(),
          ),
        )
      ],
    );
  }

  Widget _buildPasswordTF() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Text(
          "Password",
          style: kLabelStyle,
        ),
        SizedBox(
          height: 10.0,
        ),
        Container(
          alignment: Alignment.centerLeft,
          decoration: kBoxDecorationStyle,
          height: 60.0,
          child: TextFormField(
            obscureText: true,
            keyboardType: TextInputType.text,
            style: TextStyle(color: Colors.white, fontFamily: "OpenSans"),
            initialValue: "Codemaster11!", // FIXME: Remove me
            decoration: InputDecoration(
              border: InputBorder.none,
              contentPadding: EdgeInsets.only(top: 14.0),
              prefixIcon: Icon(
                Icons.lock,
                color: Colors.white,
              ),
              hintText: "Enter your Email",
              hintStyle: kHintTextStyle,
            ),
            onChanged: (value) => password = value.trim(),
          ),
        )
      ],
    );
  }
}
