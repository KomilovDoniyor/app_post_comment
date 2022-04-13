package test.app_post_comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.app_post_comment.dto.PostRequest;
import test.app_post_comment.dto.PostResponse;
import test.app_post_comment.service.impl.PostService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService service;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PostRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll(){
        return ResponseEntity.status(200).body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable("id") Integer postId){
        return ResponseEntity.status(200).body(service.findById(postId));
    }

    @GetMapping("/by_community/{id}")
    public ResponseEntity<List<PostResponse>> findAllByCommunity(@PathVariable("id") Integer communityId){
        return ResponseEntity.status(200).body(service.findAllByCommunity(communityId));
    }

    @GetMapping("/by_username/{username}")
    public ResponseEntity<List<PostResponse>> findAllByUsername(@PathVariable("username") String username){
        return ResponseEntity.status(200).body(service.findAllByUsername(username));
    }
}
