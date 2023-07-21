import 'package:flutter/material.dart';
import 'package:goquiz_ui/models/quiz.dart';

import '../../providers/quiz_provider.dart';


class QuizListScreen extends StatelessWidget {
  const QuizListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final quizProvider = QuizProvider();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Quiz List'),
      ),
      body: FutureBuilder(
        future: quizProvider.fetchQuizzes(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(
              child: CircularProgressIndicator(),
            );
          } else if (snapshot.hasError) {
            return Center(
              child: Text('Error: ${snapshot.error}'),
            );
          } else {
            final quizzes = quizProvider.quizzes;
            if (quizzes.isEmpty) {
              return const Center(
                child: Text('No quizzes available.'),
              );
            }

            return ListView.builder(
              itemCount: quizzes.length,
              itemBuilder: (context, index) {
                final quiz = quizzes[index];
                return _buildQuizTile(context, quiz);
              },
            );
          }
        },
      ),
    );
  }

  Widget _buildQuizTile(BuildContext context, Quiz quiz) {
    return Card(
      child: ListTile(
        title: Text(quiz.title),
        subtitle: Text(quiz.description),
        onTap: () {
          // Navigator.pushNamed(context, AppRoutes.quizDetail.toString(), arguments: quiz.id);
        },
      ),
    );
  }
}
