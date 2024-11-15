package com.example.GoQuiz.service;

import com.example.GoQuiz.dto.AnswerDto;
import com.example.GoQuiz.dto.QuestionDto;
import com.example.GoQuiz.dto.QuizDto;
import com.example.GoQuiz.model.Quiz;

import java.util.List;
import java.util.Optional;

public interface QuizService {
    List<Quiz> getAllQuiz();
    Quiz createQuiz(Quiz quiz);
    List<Quiz> getAllQuizOfUser(Long userId);
    Quiz updateQuiz(Long id, QuizDto quizDto);
    Optional<Quiz> getQuizById(Long id);
    void deleteQuiz(Quiz quiz);
    Quiz addQuestionToQuiz(Long id, QuestionDto questionDto);
    Quiz addAnswerToQuestion(Long id, Long questionId, AnswerDto answerDto);
    Quiz updateQuestion(Long id, Long questionId, QuestionDto questionDto);
    void deleteQuestion(Quiz quiz, Long questionId);
}
