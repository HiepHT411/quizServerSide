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

  Future<Quiz> updateQuiz(int quizID, String title, String description) async {
    var authToken = await authProvider.accessToken;
    final response = await http.put(Uri.parse('$API_URL/quizzes/$quizID'),
        body: jsonEncode(<String, String>{
          'title': title,
          'description': description,
        }),
        headers: <String, String>{
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $authToken'
        });

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      notifyListeners();
      return Quiz(
        id: data['id'],
        title: data['title'],
        description: data['description'],
        questions: (data['questions'] as List<dynamic>)
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
      );
    } else {
      log('Failed to update quiz: ${response.statusCode}');
      throw Exception('Failed to update quiz');
    }
  }

  Future<Quiz> addQuestion(
      int quizID, String prompt, List<Answer> answers) async {
    var authToken = await authProvider.accessToken;
    final response = await http.post(
        Uri.parse('$API_URL/quizzes/$quizID/question'),
        body: jsonEncode(<String, Object>{
          'prompt': prompt,
          'answers': answers.map((answer) => answer.toJson()).toList(),
        }),
        headers: <String, String>{
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $authToken'
        });
    if (response.statusCode == 201) {
      final data = jsonDecode(response.body);
      notifyListeners();
      return Quiz(
        id: data['id'],
        title: data['title'],
        description: data['description'],
        questions: (data['questions'] as List<dynamic>)
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
      );
    } else {
      log('Failed to add question: ${response.statusCode}');
      throw Exception('Failed to add question');
    }
  }

  Future<Quiz> addQuiz(String title, String description) async {
    var authToken = await authProvider.accessToken;
    final response = await http.post(Uri.parse('$API_URL/quizzes'),
        body: jsonEncode(<String, String>{
          'title': title,
          'description': description,
        }),
        headers: <String, String>{
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $authToken'
        });

    if (response.statusCode == 201) {
      final data = jsonDecode(response.body);
      notifyListeners();
      return Quiz(
        id: data['id'],
        title: data['title'],
        description: data['description'],
        questions: (data['questions'] as List<dynamic>)
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
      );
    } else {
      log('Failed to add quiz: ${response.statusCode}');
      throw Exception('Failed to add quiz');
    }
  }
}
