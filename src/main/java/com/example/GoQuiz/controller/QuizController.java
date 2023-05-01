package com.example.GoQuiz.controller;

import com.example.GoQuiz.dto.AnswerDto;
import com.example.GoQuiz.dto.QuestionDto;
import com.example.GoQuiz.dto.QuizDto;
import com.example.GoQuiz.exceptions.QuizNotFoundException;
import com.example.GoQuiz.exceptions.UserNotFoundException;
import com.example.GoQuiz.model.Quiz;
import com.example.GoQuiz.model.User;
import com.example.GoQuiz.security.UserDetailsImpl;
import com.example.GoQuiz.service.QuizService;
import com.example.GoQuiz.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    private final UserService userService;
    private final QuizService quizService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<QuizDto> getAllQuizzes(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<User> currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (currentUser.isEmpty()) {
            throw new UserNotFoundException(String.format("User with username %s not found", userDetails.getUsername()));
        } else {
            return currentUser.get().getQuizzes().stream().map(QuizDto::fromModel).collect(Collectors.toList());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public QuizDto createQuiz(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody QuizDto quizDto) {
        Optional<User> currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (currentUser.isEmpty()) {
            throw new UserNotFoundException(String.format("User with username %s not found", userDetails.getUsername()));
        } else {
            quizService.createQuiz(mapQuizDtoToQuiz(quizDto, currentUser.get()));
            return quizDto;
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public QuizDto updateQuiz(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @Valid @RequestBody QuizDto quizDto) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        if (quiz.isEmpty()) {
            throw new QuizNotFoundException(String.format("Quiz with id %d not found", id));
        }
        Optional<User> currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (currentUser.isEmpty()) {
            throw new UserNotFoundException(String.format("User with username %s not found", userDetails.getUsername()));
        }
        if(!quiz.get().getUser().equals(currentUser.get())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner of this quiz");
        }
        quizService.updateQuiz(id, quizDto);
        return quizDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}/question")
    public QuizDto addQuestionToQuiz(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @Valid @RequestBody QuestionDto questionDto) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        if (quiz.isEmpty()) {
            throw new QuizNotFoundException(String.format("Quiz with id %d not found", id));
        }
        Optional<User> currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (currentUser.isEmpty()) {
            throw new UserNotFoundException(String.format("User with username %s not found", userDetails.getUsername()));
        }
        if(!quiz.get().getUser().equals(currentUser.get())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner of this quiz");
        }
        return QuizDto.fromModel(quizService.addQuestionToQuiz(id, questionDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}/question/{questionId}/answer")
    public QuizDto addAnswerToQuestion(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @PathVariable Long questionId, @Valid @RequestBody AnswerDto answerDto) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        if (quiz.isEmpty()) {
            throw new QuizNotFoundException(String.format("Quiz with id %d not found", id));
        }
        Optional<User> currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (currentUser.isEmpty()) {
            throw new UserNotFoundException(String.format("User with username %s not found", userDetails.getUsername()));
        }
        if(!quiz.get().getUser().equals(currentUser.get())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner of this quiz");
        }
        if (quiz.get().getQuestions().stream().noneMatch(question -> question.getId().equals(questionId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Question with id %d not associated with quiz with id %d", questionId, id));
        }
        return QuizDto.fromModel(quizService.addAnswerToQuestion(id, questionId, answerDto));
    }

    private Quiz mapQuizDtoToQuiz(QuizDto quizDto, User user) {
        Quiz quiz = new Quiz();
        quiz.setUser(user);
        quiz.setDescription(quizDto.getDescription());
        quiz.setTitle(quizDto.getTitle());
        return quiz;
    }
}
