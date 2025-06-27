package com.hiephoang.platform.model;

import com.hiephoang.platform.dto.QuestionDto;
import com.hiephoang.platform.dto.QuizDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "quiz_id")
    private List<Question> questions = new ArrayList<>();

    public static Quiz fromDto(QuizDto quizDto) {
        Quiz quiz = new Quiz();
        quiz.setId(quizDto.getId());
        quiz.setTitle(quizDto.getTitle());
        quiz.setDescription(quizDto.getDescription());
        for(QuestionDto questionDto : quizDto.getQuestions()) {
            quiz.getQuestions().add(Question.fromDto(questionDto));
        }
        return quiz;
    }
}
