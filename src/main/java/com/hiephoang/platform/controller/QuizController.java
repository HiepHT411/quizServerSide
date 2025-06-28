package com.hiephoang.platform.controller;

import com.hiephoang.platform.dto.QuestionDto;
import com.hiephoang.platform.dto.QuizDto;
import com.hiephoang.platform.exceptions.QuizNotFoundException;
import com.hiephoang.platform.exceptions.UserNotFoundException;
import com.hiephoang.platform.model.Quiz;
import com.hiephoang.platform.model.User;
import com.hiephoang.platform.security.UserDetailsImpl;
import com.hiephoang.platform.service.QuizService;
import com.hiephoang.platform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
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
            if (currentUser.get().getRole().equals("ADMIN"))
                return quizService.getAllQuiz().stream().map(QuizDto::fromModel).collect(Collectors.toList());
            else
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
            Quiz toCreate = Quiz.fromDto(quizDto);
            toCreate.setUser(currentUser.get());
            return QuizDto.fromModel(quizService.createQuiz(toCreate));
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public QuizDto updateQuiz(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @Valid @RequestBody QuizDto quizDto) {
        checkQuizUserAssociation(userDetails, id);
        return QuizDto.fromModel(quizService.updateQuiz(id, quizDto));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/question")
    public QuizDto addQuestionToQuiz(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @Valid @RequestBody QuestionDto questionDto) {
        checkQuizUserAssociation(userDetails, id);
        return QuizDto.fromModel(quizService.addQuestionToQuiz(id, questionDto));

    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/question/{questionId}")
    public QuizDto updateQuestion(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @PathVariable Long questionId, @Valid @RequestBody QuestionDto questionDto) {
        checkQuizUserAssociation(userDetails, id);
        return QuizDto.fromModel(quizService.updateQuestion(id, questionId, questionDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteQuiz(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        Quiz quiz = checkQuizUserAssociation(userDetails, id);
        quizService.deleteQuiz(quiz);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{quizId}/question/{questionId}")
    public void deleteQuestion(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long quizId, @PathVariable Long questionId) {
        Quiz quiz = checkQuizUserAssociation(userDetails, quizId);
        if (quiz.getQuestions().stream().noneMatch(question -> question.getId().equals(questionId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Question with id %d not found in quiz with id %d", questionId, quizId));
        }
        quizService.deleteQuestion(quiz, questionId);
    }

    private Quiz checkQuizUserAssociation(@AuthenticationPrincipal UserDetailsImpl userDetails, Long quizId) {
        Optional<Quiz> quiz = quizService.getQuizById(quizId);
        if (quiz.isEmpty()) {
            throw new QuizNotFoundException(String.format("Quiz with id %d not found", quizId));
        }
        Optional<User> currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (currentUser.isEmpty()) {
            throw new UserNotFoundException(String.format("User with username %s not found", userDetails.getUsername()));
        }
        if(!quiz.get().getUser().equals(currentUser.get()) && !currentUser.get().getRole().equals("ADMIN")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner of this quiz");
        }
        return quiz.get();
    }
}
