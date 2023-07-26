import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:goquiz_ui/models/question.dart';
import 'package:goquiz_ui/models/quiz.dart';
import 'package:goquiz_ui/providers/quiz_provider.dart';

import '../../constants/app_routes.dart';

class QuizDetailScreen extends StatefulWidget {
  final Quiz quiz;

  const QuizDetailScreen({super.key, required this.quiz});

  @override
  _QuizDetailScreenState createState() => _QuizDetailScreenState();
}

class _QuizDetailScreenState extends State<QuizDetailScreen> {
  late Quiz _quiz;

  @override
  void initState() {
    _quiz = widget.quiz;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
        onWillPop: () async {
          Navigator.pop(context, _quiz);
          return true;
        },
        child: Scaffold(
          appBar: AppBar(
            title: Text(_quiz.title),
            actions: [
              IconButton(
                icon: const Icon(Icons.edit),
                tooltip: "Edit Quiz",
                onPressed: _showEditDialog,
              ),
              IconButton(
                  icon: const Icon(Icons.play_arrow),
                  tooltip: "Start Quiz",
                  onPressed: () {
                    // TODO: Handle starting the quiz
                  })
            ],
          ),
          body: _buildQuizQuestionsList(),
          floatingActionButton: FloatingActionButton(
              tooltip: "Add Question",
              onPressed: () async {
                Quiz tmpQuiz = await Navigator.pushNamed(
                        context, AppRoutes.questionForm, arguments: _quiz.id)
                    as Quiz;
                setState(() {
                  _quiz = tmpQuiz;
                });
              },
              child: const Icon(Icons.add)),
        ));
  }

  Widget _buildQuizQuestionsList() {
    final List<Question> questions = _quiz.questions;

    if (questions.isEmpty) {
      return const Center(
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
    return GestureDetector(
      child: Card(
        child: ListTile(
          title: Text(question.questionText),
          onTap: () {
            Navigator.pushNamed(context, AppRoutes.questionDetail,
                arguments: question);
          },
        ),
      ),
      onTapDown: (TapDownDetails details) {
        final screenSize = MediaQuery.of(context).size;
        showMenu(
          context: context,
          position: RelativeRect.fromLTRB(
              details.globalPosition.dx,
              details.globalPosition.dy,
              screenSize.width - details.globalPosition.dx,
              screenSize.height - details.globalPosition.dy),
          items: <PopupMenuEntry>[
            PopupMenuItem(
              child: Row(
                children: const <Widget>[
                  Icon(Icons.delete),
                  Text("Delete"),
                ],
              ),
              onTap: () {
                // TODO: Handle deleting the question
              },
            )
          ],
        );
      },
    );
  }

  Future<void> _saveEdit(String title, String description) async {
    final quizProvider = QuizProvider();
    try {
      Quiz tmpQuiz =
          await quizProvider.updateQuiz(_quiz.id, title, description);
      setState(() {
        _quiz = tmpQuiz;
      });
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text(e.toString()),
      ));
    } finally {
      Navigator.of(context).pop();
    }
  }

  void _showEditDialog() {
    showDialog(
        context: context,
        builder: (BuildContext context) {
          final TextEditingController titleController =
              TextEditingController(text: _quiz.title);
          final TextEditingController descriptionController =
              TextEditingController(text: _quiz.description);

          return AlertDialog(
              content: Stack(
            children: <Widget>[
              const Text("Edit Quiz",
                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
              Form(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: <Widget>[
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: TextFormField(
                        controller: titleController,
                        decoration: const InputDecoration(
                          labelText: 'Title',
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: TextFormField(
                        controller: descriptionController,
                        decoration: const InputDecoration(
                          labelText: 'Description',
                        ),
                      ),
                    ),
                    Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: ElevatedButton(
                            child: const Text("Save"),
                            onPressed: () {
                              _saveEdit(titleController.text,
                                  descriptionController.text);
                            }))
                  ],
                ),
              ),
            ],
          ));
        });
  }
}
