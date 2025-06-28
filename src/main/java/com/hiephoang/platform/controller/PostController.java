package com.hiephoang.platform.controller;

import com.hiephoang.platform.dto.*;
import com.hiephoang.platform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    @PostMapping
    public ResponseEntity<ResponseMetaData> creatPost(@RequestBody PostRequest postRequest) {
        try {
            PostDTO post = postService.createPost(postRequest);

            if (post == null) {
                return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.BAD_REQUEST), null));
            }
            return ResponseEntity.ok(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS), post));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMetaData(new MetaDTO(MetaData.INTERNAL_SERVER_ERROR), null));
        }
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<ResponseMetaData> removePost(@PathVariable("id") String postId) {
        try {
            boolean result = postService.removePost(postId);

            if (!result) {
                return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.BAD_REQUEST), null));
            }
            return ResponseEntity.ok(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMetaData(new MetaDTO(MetaData.INTERNAL_SERVER_ERROR), null));
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ResponseMetaData> getPost(@PathVariable("postId") String postId) {
        try {
            PostDTO post = postService.findByPostId(postId);

            if (post == null) {
                return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.BAD_REQUEST), null));
            }
            return ResponseEntity.ok(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS), post));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMetaData(new MetaDTO(MetaData.INTERNAL_SERVER_ERROR), null));
        }
    }

    @GetMapping
    public ResponseEntity<ResponseMetaData> getPost() {
        try {
            List<PostDTO> posts = postService.getAllPosts();

            if (posts == null) {
                return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.BAD_REQUEST), null));
            }
            return ResponseEntity.ok(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS), posts));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMetaData(new MetaDTO(MetaData.INTERNAL_SERVER_ERROR), null));
        }
    }
}
