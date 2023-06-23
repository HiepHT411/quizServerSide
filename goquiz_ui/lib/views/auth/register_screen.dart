import 'package:flutter/material.dart';
import 'package:goquiz_ui/constants/app_routes.dart';
import 'package:goquiz_ui/providers/authentication_provider.dart';

class RegisterScreen extends StatelessWidget {
  final AuthenticationProvider authProvider = AuthenticationProvider();

  final TextEditingController usernameController = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Register'),
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            TextField(
              controller: usernameController,
              decoration: InputDecoration(
                labelText: 'Username',
              ),
            ),
            SizedBox(height: 16.0),
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
                final username = usernameController.text;
                final email = emailController.text;
                final password = passwordController.text;
                final success =
                    await authProvider.register(username, email, password);
                if (success) {
                  // Navigate to the login screen upon successful registration
                  Navigator.pushReplacementNamed(
                      context, AppRoutes.login.toString());
                }
              },
              child: Text('Register'),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.pushReplacementNamed(
                    context, AppRoutes.login.toString());
              },
              child: Text('Already have an account? Login'),
            ),
          ],
        ),
      ),
    );
  }
}
