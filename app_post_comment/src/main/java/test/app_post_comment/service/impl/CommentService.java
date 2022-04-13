package test.app_post_comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import test.app_post_comment.domain.Post;
import test.app_post_comment.domain.User;
import test.app_post_comment.dto.CommentRequest;
import test.app_post_comment.exception.PostNotFoundException;
import test.app_post_comment.mapper.CommentMapper;
import test.app_post_comment.repository.CommentRepository;
import test.app_post_comment.repository.PostRepository;
import test.app_post_comment.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final PostRepository postRepository;
    private final CommentMapper mapper;
    private final UserRepository userRepository;

    public void create(CommentRequest request, Jwt jwt) {
        User user = userRepository.findByUsername(jwt.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User not found username: " + jwt.getSubject()));
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with id{" + request.getPostId() + "}"));
        repository.save(mapper.fromRequest(request, post, user));
    }

    public List<CommentRequest> findAllByPost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id{" + postId + "}"));
        return repository.findAllByPost(post)
                .stream()
                .map(mapper::toRequest)
                .collect(Collectors.toList());
    }

    public List<CommentRequest> findAllByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return repository.findAllByOwner(user)
                .stream()
                .map(mapper::toRequest)
                .collect(Collectors.toList());
    }
}
