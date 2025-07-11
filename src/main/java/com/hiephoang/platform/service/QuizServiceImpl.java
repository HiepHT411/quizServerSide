package com.hiephoang.platform.service;

import com.hiephoang.platform.dto.AnswerDto;
import com.hiephoang.platform.dto.QuestionDto;
import com.hiephoang.platform.dto.QuizDto;
import com.hiephoang.platform.model.Answer;
import com.hiephoang.platform.model.Question;
import com.hiephoang.platform.model.Quiz;
import com.hiephoang.platform.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuizServiceImpl implements QuizService{
    private final QuizRepository repository;

    @Override
    public List<Quiz> getAllQuiz() {
        return repository.findAll();
    }

    @Override
    public Quiz createQuiz(Quiz quiz) {
        return repository.save(quiz);
    }

    @Override
    public List<Quiz> getAllQuizOfUser(Long userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public Optional<Quiz> getQuizById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Quiz updateQuiz(Long id, QuizDto quizDto) {
        Optional<Quiz> quiz = repository.findById(id);
        if (quiz.isEmpty()) {
            return null;
        }
        quiz.get().setTitle(quizDto.getTitle());
        quiz.get().setDescription(quizDto.getDescription());
        return repository.save(quiz.get());
    }

    @Override
    public Quiz addQuestionToQuiz(Long id, QuestionDto questionDto) {
        Optional<Quiz> quiz = repository.findById(id);
        if (quiz.isEmpty()) {
            return null;
        }
        quiz.get().getQuestions().add(Question.fromDto(questionDto));
        return repository.save(quiz.get());
    }

    @Override
    public Quiz addAnswerToQuestion(Long id, Long questionId, AnswerDto answerDto) {
        Optional<Quiz> quiz = repository.findById(id);
        if (quiz.isEmpty()) {
            return null;
        }
        for (Question question : quiz.get().getQuestions()) {
            if (question.getId().equals(questionId)) {
                question.getAnswers().add(Answer.fromDto(answerDto));
                break;
            }
        }
        return repository.save(quiz.get());
    }

    @Override
    public Quiz updateQuestion(Long id, Long questionId, QuestionDto questionDto) {
        Optional<Quiz> quiz = repository.findById(id);
        if (quiz.isEmpty()) {
            return null;
        }
        for (Question question : quiz.get().getQuestions()) {
            if (question.getId().equals(questionId)) {
                question.setPrompt(questionDto.getPrompt());
                question.getAnswers().clear();
                for(AnswerDto answerDto : questionDto.getAnswers()) {
                    question.getAnswers().add(Answer.fromDto(answerDto));
                }
            }
        }
        return repository.save(quiz.get());
    }

    @Override
    public void deleteQuestion(Quiz quiz, Long questionId) {
        for (Question question : quiz.getQuestions()) {
            if (question.getId().equals(questionId)) {
                quiz.getQuestions().remove(question);
                break;
            }
        }
        repository.save(quiz);
    }

    @Override
    public void deleteQuiz(Quiz quiz) {
        repository.delete(quiz);
    }
}
