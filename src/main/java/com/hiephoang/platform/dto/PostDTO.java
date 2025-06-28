package com.hiephoang.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiephoang.platform.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private long id;

    private String title;

    private String body;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("comments")
    private List<CommentDTO> comments;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.userId = post.getUser().getId();

        this.comments = post.getComments().stream()
                .map(CommentDTO::toCommentDTO)
                .toList();
    }
}
