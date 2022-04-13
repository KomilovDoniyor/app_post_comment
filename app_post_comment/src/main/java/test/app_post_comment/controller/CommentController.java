package test.app_post_comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import test.app_post_comment.config.aop.CurrentUser;
import test.app_post_comment.dto.CommentRequest;
import test.app_post_comment.service.impl.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CommentRequest request, @ApiIgnore @CurrentUser Jwt jwt){
        service.create(request,jwt);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("by_post/{postId}")
    public ResponseEntity<List<CommentRequest>> findAllByPost(@PathVariable("postId") Integer postId){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllByPost(postId));
    }

    @GetMapping("/by_username/{username}")
    public ResponseEntity<List<CommentRequest>> findAllByUsername(@PathVariable("username") String username){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllByUsername(username));
    }

}
