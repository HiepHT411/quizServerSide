import 'package:flutter/material.dart';
import 'package:goquiz_ui/models/question.dart';

import '../../models/answer.dart';

class QuestionDetailScreen extends StatefulWidget {
  final Question question;

  const QuestionDetailScreen({Key? key, required this.question})
      : super(key: key);

  @override
  _QuestionDetailScreenState createState() => _QuestionDetailScreenState();
}

class _QuestionDetailScreenState extends State<QuestionDetailScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        actions: [
          IconButton(
              icon: Icon(Icons.edit),
              tooltip: "Edit Question",
              onPressed: () async {
                // TODO: Handle editing the question
              })
        ],
        title: const Text('Question Detail'),
      ),
      body: _buildQuestionDetail(),
    );
  }

  Widget _buildQuestionDetail() {
    return Column(
      children: [
        Wrap(
          alignment: WrapAlignment.start,
          children: [
            Padding(
                padding: const EdgeInsets.all(16.0),
                child: Text(widget.question.questionText,
                    style: const TextStyle(
                        fontSize: 20, fontWeight: FontWeight.bold))),
          ],
        ),
        const SizedBox(height: 16.0),
        const Text('Answers'),
        const SizedBox(height: 16.0),
        Expanded(
          child: _answerListBuilder(),
        ),
      ],
    );
  }

  Widget _answerListBuilder() {
    final List<Answer> answers = widget.question.answers;

    if (answers.isEmpty) {
      return const Center(
        child: Text('No answers available.'),
      );
    }

    return ListView.builder(
      itemCount: answers.length,
      itemBuilder: (context, index) {
        final answer = answers[index];
        return _buildAnswerTile(context, answer);
      },
    );
  }

  Widget _buildAnswerTile(BuildContext context, Answer answer) {
    return Card(
      child: ListTile(
        title: Text(answer.text),
        trailing: answer.correct
            ? const Icon(Icons.check, color: Colors.green)
            : const Icon(Icons.close, color: Colors.red),
      ),
    );
  }
}
