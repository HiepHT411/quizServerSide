import 'package:flutter/material.dart';
import 'package:goquiz_ui/constants/app_routes.dart';
import 'package:goquiz_ui/views/auth/login_screen.dart';
import 'package:goquiz_ui/views/auth/register_screen.dart';
import 'package:goquiz_ui/views/quiz/quiz_list_screen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

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
        AppRoutes.quizList.toString(): (context) => QuizListScreen(),
        // TODO: Add other routes
      },
    );
  }
}
