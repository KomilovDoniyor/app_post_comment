package test.app_post_comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.app_post_comment.dto.CommunityDto;
import test.app_post_comment.service.impl.CommunityService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/communities")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService service;


    @PostMapping
    public ResponseEntity<CommunityDto> create(@Valid @RequestBody CommunityDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<CommunityDto>> findAll(){
        return ResponseEntity
                .status(200)
                .body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityDto> findById(@PathVariable("id") Integer id){
        return ResponseEntity
                .status(200)
                .body(service.findById(id));
    }
}

