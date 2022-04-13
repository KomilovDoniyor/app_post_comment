package test.app_post_comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.app_post_comment.dto.VoteDto;
import test.app_post_comment.service.impl.VoteService;

@RestController
@RequestMapping("/api/v1/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody VoteDto dto){
        service.create(dto);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
