import 'package:flutter/material.dart';
import 'package:goquiz_ui/models/question.dart';
import 'package:goquiz_ui/models/quiz.dart';

class QuizDetailScreen extends StatelessWidget {
  late Quiz quiz;

  QuizDetailScreen({super.key});

  @override
  Widget build(BuildContext context) {
    quiz = ModalRoute.of(context)!.settings.arguments as Quiz;
    return Scaffold(
      appBar: AppBar(
        title: Text(quiz.title),
        actions: [
          IconButton(
            icon: const Icon(Icons.edit),
            onPressed: () {
              // TODO: Handle modifying the quiz
            },
          ),
          IconButton(
            icon: const Icon(Icons.add),
            onPressed: () {
              // TODO: Handle adding a question
            },
          )
        ],
      ),
      body: _buildQuizQuestionsList(),
    );
  }

  Widget _buildQuizQuestionsList() {
    final List<Question> questions = quiz.questions;

    if (questions.isEmpty) {
      return Center(
        child: Text('No questions available.'),
      );
    }

    return ListView.builder(
      itemCount: questions.length,
      itemBuilder: (context, index) {
        final question = questions[index];
        return _buildQuestionTile(context, question);
      },
    );
  }

  Widget _buildQuestionTile(BuildContext context, Question question) {
    return Card(
      child: ListTile(
        title: Text(question.questionText),
        onTap: () {
          // TODO: Handle tapping on a question item
        },
      ),
    );
  }
}
