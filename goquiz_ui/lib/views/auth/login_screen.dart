import 'package:flutter/material.dart';
import 'package:goquiz_ui/constants/app_routes.dart';
import 'package:goquiz_ui/providers/authentication_provider.dart';

class LoginScreen extends StatelessWidget {
  final AuthenticationProvider authProvider = AuthenticationProvider();

  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('Login'),
        ),
        body: Padding(
            padding: EdgeInsets.all(16.0),
            child:
                Column(mainAxisAlignment: MainAxisAlignment.center, children: [
              TextField(
                controller: emailController,
                decoration: InputDecoration(
                  labelText: 'Email',
                ),
              ),
              SizedBox(height: 16.0),
              TextField(
                controller: passwordController,
                obscureText: true,
                decoration: InputDecoration(
                  labelText: 'Password',
                ),
              ),
              SizedBox(height: 16.0),
              ElevatedButton(
                onPressed: () async {
                  final email = emailController.text;
                  final password = passwordController.text;
                  final success = await authProvider.login(email, password);
                  if (success) {
                    // Navigate to the next screen upon successful login
                    Navigator.pushReplacementNamed(
                        context, AppRoutes.quizList.toString());
                  } else {
                    // Display an error message to the user if login failed
                    ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                      content: Text('Login failed'),
                    ));
                  }
                },
                child: Text('Login'),
              ),
              ElevatedButton(
                onPressed: () {
                  Navigator.pushReplacementNamed(context, AppRoutes.register.toString());
                },
                child: Text('Create an account'),
              ),
            ])));
  }
}
