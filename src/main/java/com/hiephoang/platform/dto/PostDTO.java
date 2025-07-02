package com.hiephoang.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiephoang.platform.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private long id;

    private String title;

    private String body;

    private String username;

    @JsonProperty("comments")
    private List<CommentDTO> comments;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.username = post.getUser().getUsername();

        this.comments = Objects.isNull(post.getComments()) ? Collections.emptyList() : post.getComments().stream()
                .map(CommentDTO::toCommentDTO)
                .toList();
    }
}
