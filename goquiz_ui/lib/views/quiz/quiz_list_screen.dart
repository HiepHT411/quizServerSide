import 'package:flutter/material.dart';
import 'package:goquiz_ui/models/quiz.dart';
import 'package:goquiz_ui/providers/quiz_provider.dart';

import '../../constants/app_routes.dart';

class QuizListScreen extends StatefulWidget {
  const QuizListScreen({super.key});

  @override
  _QuizListScreenState createState() => _QuizListScreenState();
}

class _QuizListScreenState extends State<QuizListScreen> {
  List<Quiz> _quizzes = [];

  @override
  void initState() {
    super.initState();
    _loadQuizzes();
  }

  Future<void> _loadQuizzes() async {
    final quizProvider = QuizProvider();
    await quizProvider.fetchQuizzes();
    setState(() {
      _quizzes = quizProvider.quizzes;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Quiz List'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _loadQuizzes,
          ),
          IconButton(
            icon: Icon(Icons.logout),
            onPressed: () {
              Navigator.pushReplacementNamed(
                  context, AppRoutes.login.toString());
            },
          )
        ],
      ),
      body: _buildQuizList(_quizzes),
    );
  }

  Widget _buildQuizList(List<Quiz> quizzes) {
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

  Widget _buildQuizTile(BuildContext context, Quiz quiz) {
    return Card(
      child: ListTile(
        title: Text(quiz.title),
        subtitle: Text(quiz.description),
        onTap: () {
          Navigator.pushNamed(context, AppRoutes.quizDetail.toString(),
              arguments: quiz);
        },
      ),
    );
  }
}
