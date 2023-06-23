import 'package:flutter/material.dart';
import 'package:goquiz_ui/constants/app_routes.dart';
import 'package:goquiz_ui/views/auth/login_screen.dart';
import 'package:goquiz_ui/views/auth/register_screen.dart';
// Import other screens

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Go Quiz',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      initialRoute: AppRoutes.login.toString(),
      routes: {
        AppRoutes.login.toString(): (context) => LoginScreen(),
        AppRoutes.register.toString(): (context) => RegisterScreen(),
        // Add other routes
      },
    );
  }
}
