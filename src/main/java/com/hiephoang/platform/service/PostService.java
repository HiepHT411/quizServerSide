package com.hiephoang.platform.service;

import com.hiephoang.platform.dto.PostDTO;
import com.hiephoang.platform.dto.PostRequest;
import com.hiephoang.platform.model.Post;
import com.hiephoang.platform.model.User;
import com.hiephoang.platform.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final UserService userService;

    private final PostRepository postRepository;

    public PostDTO createPost(PostRequest request) {
        User user = getAuthenticatedUser();

        if (Objects.isNull(user)) {
            return null;
        }

        Timestamp createAt = new Timestamp(System.currentTimeMillis());
        Post post = Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .user(user)
                .createAt(createAt)
                .updateAt(createAt)
                .build();

        log.info("Post created successfully: {}", post);
        return new PostDTO(postRepository.save(post));
    }

    public boolean removePost(String id) {
        User user = getAuthenticatedUser();

        if (Objects.isNull(user) || id.isEmpty()) {
            return false;
        }

        Integer removedPostCount = postRepository.deleteByIdAndUserId(Long.parseLong(id), user.getId());

        return removedPostCount > 0;
    }

    public PostDTO findByPostId(String pId) {

        long postId = Long.parseLong(pId);
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            return null;
        }
        Post post = postRepository.findById(postId).orElse( null);
        return Objects.isNull(post) ? null : new PostDTO(post);
    }

    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        log.info("Get all posts successfully: {}", posts);
        return posts.stream().map(PostDTO::new).toList();
    }

    private User getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        return userService.findByUsernameOrEmail(username);
    }

}
