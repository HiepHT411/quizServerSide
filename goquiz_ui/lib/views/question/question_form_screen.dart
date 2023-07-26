import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:goquiz_ui/models/answer.dart';
import 'package:goquiz_ui/models/quiz.dart';
import 'package:goquiz_ui/providers/quiz_provider.dart';

class QuestionFormScreen extends StatefulWidget {
  final int quizID;

  const QuestionFormScreen({Key? key, required this.quizID}) : super(key: key);

  @override
  _QuestionFormScreenState createState() => _QuestionFormScreenState();
}

class _QuestionFormScreenState extends State<QuestionFormScreen> {
  int answerCount = 1;
  String prompt = '';
  List<Answer> answers = [];

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    final TextEditingController promptController =
        TextEditingController(text: prompt);
    return Scaffold(
        appBar: AppBar(
          title: const Text('Add Question'),
        ),
        body: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            children: [
              TextFormField(
                controller: promptController,
                onChanged: (value) => prompt = value,
                decoration: const InputDecoration(
                  labelText: 'Question Text',
                  hintText: 'Enter the question prompt',
                ),
              ),
              const SizedBox(height: 16.0),
              const Text('Answers'),
              const SizedBox(height: 16.0),
              Expanded(
                child: _answerListBuilder(),
              ),
              const SizedBox(height: 16.0),
              ElevatedButton(
                onPressed: () async {
                  Quiz? tmpQuiz = await _addQuestion(prompt, answers);
                  if (tmpQuiz != null) {
                    ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(content: Text('Question added')));
                    Navigator.pop(context, tmpQuiz);
                  }
                },
                child: const Text('Add Question'),
              )
            ],
          ),
        ));
  }

  // We have a list of answers, at the beginning it's empty and there is a disabled text field that when clicked will add a new answer to the list
  Widget _answerListBuilder() {
    return ListView.builder(
      itemCount: answerCount,
      itemBuilder: (context, index) {
        // if last item, show the add answer button
        if (index == answerCount - 1) {
          return Card(
            child: ListTile(
              title: const Text('Add Answer'),
              onTap: () {
                setState(() {
                  answerCount++;
                });
              },
            ),
          );
        }
        return _answerTileBuilder(index);
      },
    );
  }

  Widget _answerTileBuilder(int index) {
    if (answerCount - 1 > answers.length) {
      answers.add(Answer(correct: false, text: ''));
    }
    final TextEditingController answerController =
        TextEditingController(text: answers[index].text);
    return Card(
        child: ListTile(
            title: TextFormField(
              controller: answerController,
              onChanged: (value) => answers[index] =
                  Answer(correct: answers[index].correct, text: value),
              decoration: const InputDecoration(
                labelText: 'Answer Text',
                hintText: 'Enter the answer text',
              ),
            ),
            trailing: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                Switch(
                  value: answers[index].correct,
                  onChanged: (value) => {
                    setState(() {
                      answers[index] =
                          Answer(correct: value, text: answers[index].text);
                    })
                  },
                ),
                IconButton(
                    icon: const Icon(Icons.delete),
                    onPressed: () {
                      setState(() {
                        answerCount--;
                        answers.removeAt(index);
                      });
                    })
              ],
            )));
  }

  Future<Quiz>? _addQuestion(String prompt, List<Answer> answers) {
    QuizProvider quizProvider = QuizProvider();

    if (prompt.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Please enter a question prompt')));
      return null;
    }
    log(answerCount.toString());
    log(answers.toString());
    for (final answer in answers) {
      if (answer.text.isEmpty) {
        ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Please enter all answers')));
        return null;
      }
    }

    try {
      return quizProvider.addQuestion(widget.quizID, prompt, answers);
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Failed to add question')));
      return null;
    }
  }
}
