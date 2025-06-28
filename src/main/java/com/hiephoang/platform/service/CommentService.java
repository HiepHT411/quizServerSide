package com.hiephoang.platform.service;

import com.hiephoang.platform.dto.CommentDTO;
import com.hiephoang.platform.dto.CommentRequest;
import com.hiephoang.platform.model.Comment;
import com.hiephoang.platform.model.Post;
import com.hiephoang.platform.model.User;
import com.hiephoang.platform.repository.CommentRepository;
import com.hiephoang.platform.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    public CommentDTO createComment(CommentRequest request) {
        User user = getAuthenticatedUser();

        if (Objects.isNull(user)) {
            return null;
        }
        Optional<Post> postOpt = postRepository.findById(Long.parseLong(request.getPostId()));
        if (postOpt.isEmpty()) {
            return null;
        }

        Timestamp createAt = new Timestamp(System.currentTimeMillis());
        Comment comment = Comment.builder()
                .body(request.getBody())
                .post(postOpt.get())
                .user(user)
                .createAt(createAt)
                .updateAt(createAt)
                .build();

        return new CommentDTO(comment, user.getId(), postOpt.get().getId());
    }

    public boolean removeComment(String id) {
        User user = getAuthenticatedUser();

        if (Objects.isNull(user) || id.isEmpty()) {
            return false;
        }

        Integer removedCommentCount = commentRepository.deleteByIdAndUserId(Long.parseLong(id), user.getId());

        return removedCommentCount > 0;
    }

    public List<CommentDTO> findAllByPostId(String pId) {
        User user = getAuthenticatedUser();
        if (Objects.isNull(user) || pId.isEmpty()) {
            return null;
        }
        long postId = Long.parseLong(pId);
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            return null;
        }

        long userId = user.getId();

        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> new CommentDTO(comment, userId, postId))
                .toList();

    }

    private User getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        return userService.findByUsernameOrEmail(username);
    }
}
