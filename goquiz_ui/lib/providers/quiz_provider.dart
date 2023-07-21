import 'dart:convert';
import 'dart:developer';
import 'package:flutter/foundation.dart';
import 'package:goquiz_ui/providers/authentication_provider.dart';
import 'package:http/http.dart' as http;
import 'package:goquiz_ui/constants/api_endpoints.dart';

import '../models/answer.dart';
import '../models/question.dart';
import '../models/quiz.dart';

class QuizProvider with ChangeNotifier {
  final authProvider = AuthenticationProvider();

  List<Quiz> _quizzes = [];

  List<Quiz> get quizzes => _quizzes;

  Future<void> fetchQuizzes() async {
    var _authToken = await authProvider.accessToken;
    final response = await http.get(
      Uri.parse('$API_URL/quizzes'),
      headers: <String, String>{
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $_authToken'
      },
    );

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      log(data.toString());
      _quizzes = data
          .map((json) => Quiz(
                id: json['id'],
                title: json['title'],
                description: json['description'],
                questions: (json['questions'] as List<dynamic>)
                    .map((json) => Question(
                          id: json['id'],
                          questionText: json['prompt'],
                          answers: (json['answers'] as List<dynamic>)
                              .map((json) => Answer(
                                    id: json['id'],
                                    text: json['text'],
                                    correct: json['correct'],
                                  ))
                              .toList(),
                          isMultipleChoice: true,
                        ))
                    .toList(),
              ))
          .toList();

      notifyListeners();
    } else {
      log('Failed to fetch quizzes: ${response.statusCode}');
      throw Exception('Failed to fetch quizzes');
    }
  }
}
