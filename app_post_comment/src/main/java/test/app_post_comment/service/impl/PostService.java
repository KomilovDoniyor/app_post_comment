package test.app_post_comment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.app_post_comment.domain.Community;
import test.app_post_comment.domain.Post;
import test.app_post_comment.domain.User;
import test.app_post_comment.dto.PostRequest;
import test.app_post_comment.dto.PostResponse;
import test.app_post_comment.exception.CommunityNotFoundException;
import test.app_post_comment.exception.PostNotFoundException;
import test.app_post_comment.mapper.PostMapper;
import test.app_post_comment.repository.CommunityRepository;
import test.app_post_comment.repository.PostRepository;
import test.app_post_comment.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;
    private final PostMapper mapper;
    private final CommunityRepository communityRepository;
    private final AuthServiceImpl authService;
    private final UserRepository userRepository;

    public PostRequest create(PostRequest request) {
        Community community = communityRepository.findByName(request.getCommunityName())
                .orElseThrow(
                        () -> new CommunityNotFoundException(
                                "Community not found with name: " + request.getCommunityName()));
        Post savedPost = repository.save(mapper.map(request, community, authService.getCurrentUser()));
        request.setId(savedPost.getId());
        return request;
    }


    @Transactional
    public List<PostResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse findById(Integer postId) {
        Post post = repository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id {" + postId + "}"));
        return mapper.toDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findAllByCommunity(Integer communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityNotFoundException("Community nor found with id: " + communityId));
        List<Post> postList = repository.findAllByCommunity(community);
        return postList
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> findAllByUsername(String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username{" + username + "}"));
        List<Post> postList = repository.findAllByOwner(owner);
        return postList
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());


    }
}
