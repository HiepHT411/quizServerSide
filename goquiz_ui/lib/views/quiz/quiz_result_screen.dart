import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:goquiz_ui/constants/app_routes.dart';
import 'package:goquiz_ui/models/quiz_result.dart';

class QuizResultScreen extends StatefulWidget {
  final QuizResult quizResult;

  const QuizResultScreen({Key? key, required this.quizResult})
      : super(key: key);

  @override
  _QuizResultScreenState createState() => _QuizResultScreenState();
}

class _QuizResultScreenState extends State<QuizResultScreen> {
  late QuizResult _quizResult;

  @override
  void initState() {
    _quizResult = widget.quizResult;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_quizResult.quiz.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              "You answered ${_quizResult.correctAnswers} out of ${_quizResult.totalQuestions} questions correctly",
              style: const TextStyle(fontSize: 20),
            ),
            ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pushNamedAndRemoveUntil(
                      AppRoutes.quizList, (route) => false);
                },
                child: const Text("Back to Quiz List"))
          ],
        ),
      ),
    );
  }
}
