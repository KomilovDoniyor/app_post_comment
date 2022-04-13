package test.app_post_comment.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import test.app_post_comment.domain.Community;
import test.app_post_comment.domain.Post;
import test.app_post_comment.domain.User;
import test.app_post_comment.dto.PostRequest;
import test.app_post_comment.dto.PostResponse;
import test.app_post_comment.mapper.PostMapper;
import test.app_post_comment.repository.CommunityRepository;
import test.app_post_comment.repository.PostRepository;
import test.app_post_comment.repository.UserRepository;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostService postService;

    @Captor
    private ArgumentCaptor<Post> argumentCaptor;

    @BeforeEach
    public void setup(){
        postService =
                new PostService(postRepository, postMapper, communityRepository, authService, userRepository);
    }

    @Test
    @DisplayName(value = "Post id orqali data olamiz")
    public void findById() {
        Post post = new Post(
                1,
                "Spring boot Mockito testing",
                "Testing",
                "https://site.mockito.com",
                null,
                Instant.now(),
                0,
                null);
        PostResponse testingResponse = new PostResponse(
                1,
                "Spring boot Mockito testing",
                "Testing",
                "https://site.mockito.com",
                "Test user",
                "Test Community",
                0,
                0,
                false,
                false);
        Mockito.when(postRepository.findById(1)).thenReturn(Optional.of(post));
        Mockito.when(postMapper.toDto(Mockito.any(Post.class))).thenReturn(testingResponse);
        PostResponse actualResponse = postService.findById(1);

        assertThat(actualResponse.getId()).isEqualTo(testingResponse.getId());
        assertThat(actualResponse.getPostName()).isEqualTo(testingResponse.getPostName());
    }
    @Test
    @DisplayName(value = "Post saqlash testing")
    public void create(){
        User currentUser = new User(
                1,
                "testing_username",
                "testing@gmail.com",
                "asd123",
                Instant.now(),
                true);
        Community community = new Community(
                1,
                "community name",
                "testing description",
                currentUser,
                Instant.now(),
                Collections.emptySet()
        );
        Post post = new Post(
                null,
                "post name",
                "testing description",
                "https://site.mockito.com",
                currentUser,
                Instant.now(),
                0,
                community);
        PostRequest postRequest = new PostRequest(
                null,
                "post name",
                "testing description",
                "https://site.mockito.com",
                "community name");

        Mockito.when(communityRepository.findByName("community name")).thenReturn(Optional.of(community));
        Mockito.when(authService.getCurrentUser()).thenReturn(currentUser);
        Mockito.when(postMapper.map(postRequest,community,currentUser));

        postService.create(postRequest);
        Mockito.verify(postRepository,Mockito.times(1)).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getId()).isEqualTo(1);
        assertThat(argumentCaptor.getValue().getPostName()).isEqualTo("post name");
    }

}