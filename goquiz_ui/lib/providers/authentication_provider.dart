import 'dart:convert';
import 'dart:developer';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import 'package:goquiz_ui/constants/api_endpoints.dart';

class AuthenticationResult {
  final bool success;
  final String errorMessage;

  AuthenticationResult({
    required this.success,
    this.errorMessage = '',
  });
}

class AuthenticationProvider with ChangeNotifier{
  String _accessToken = '';

  String get accessToken => _accessToken;

  Future<AuthenticationResult> login(String email, String password) async {
    final response = await http.post(
      Uri.parse('$API_URL/auth/authenticate'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, String>{
        'email': email,
        'password': password,
      }),
    );
    log("login: ${response.statusCode}");

    if (response.statusCode == 200) {
      final token = jsonDecode(response.body)['accessToken'];
      _accessToken = token;

      return AuthenticationResult(success: true);
    } else {
      const errorMessage = 'Authentication failed: Email or password is incorrect';
      return AuthenticationResult(success: false, errorMessage: errorMessage);
    }
  }

  Future<AuthenticationResult> register(String username, String email, String password) async {
    final response = await http.post(
      Uri.parse('$API_URL/auth/signup'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, String>{
        'username': username,
        'email': email,
        'password': password,
      }),
    );

    log('signup: ${response.statusCode}');

    if (response.statusCode == 201) {
      return AuthenticationResult(success: true);
    } else {
      final errorJson = jsonDecode(response.body);
      final errorMessage = errorJson['message'] ?? 'Registration failed';
      return AuthenticationResult(success: false, errorMessage: errorMessage);
    }
  }
}