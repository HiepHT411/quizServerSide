import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import 'package:goquiz_ui/constants/api_endpoints.dart';

class AuthenticationProvider {
  final String apiUrl = '$API_URL/auth';

  Future<bool> login(String email, String password) async {
    final response = await http.post(
      Uri.parse('$apiUrl/authenticate'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, String>{
        'email': email,
        'password': password,
      }),
    );

    if (response.statusCode == 200) {
      // Authentication successful
      // You can handle the response here, e.g., store the user token

      return true;
    } else {
      // Authentication failed
      // You can handle the response here, e.g., display an error message
      return false;
    }
  }

  Future<bool> register(String username, String email, String password) async {
    final response = await http.post(
      Uri.parse('$apiUrl/signup'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, String>{
        'username': username,
        'email': email,
        'password': password,
      }),
    );

    if (response.statusCode == 200) {
      // Registration successful
      // You can handle the response here, e.g., navigate to the login screen
      //TODO
      return true;
    } else {
      // Registration failed
      // You can handle the response here, e.g., display an error message
      //TODO
      return false;
    }
  }
}
