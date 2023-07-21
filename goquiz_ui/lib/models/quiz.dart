import 'package:goquiz_ui/models/question.dart';

class Quiz {
  final int id;
  final String title;
  final String description;
  final List<Question> questions;

  Quiz({
    required this.id,
    required this.title,
    required this.description,
    required this.questions,
  });
}