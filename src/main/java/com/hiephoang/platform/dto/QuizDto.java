package com.hiephoang.platform.dto;

import com.hiephoang.platform.model.Question;
import com.hiephoang.platform.model.Quiz;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuizDto {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;

    private List<QuestionDto> questions = new ArrayList<>();

    public static QuizDto fromModel(Quiz quiz) {
        QuizDto quizDto = new QuizDto();
        quizDto.setId(quiz.getId());
        quizDto.setTitle(quiz.getTitle());
        quizDto.setDescription(quiz.getDescription());
        for(Question question : quiz.getQuestions()) {
            quizDto.getQuestions().add(QuestionDto.fromModel(question));
        }
        return quizDto;
    }
}
