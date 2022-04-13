package test.app_post_comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.app_post_comment.domain.Post;
import test.app_post_comment.domain.Vote;
import test.app_post_comment.dto.VoteDto;
import test.app_post_comment.enums.VoteType;
import test.app_post_comment.exception.CommunityNotFoundException;
import test.app_post_comment.exception.PostNotFoundException;
import test.app_post_comment.repository.PostRepository;
import test.app_post_comment.repository.VoteRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthServiceImpl authService;

    public void create(VoteDto dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found id{" + dto.getPostId() + "}"));
        Optional<Vote> optionalVote = voteRepository.findTopByPostAndOwnerOrderByIdDesc(post, authService.getCurrentUser());
        if(optionalVote.isPresent() && optionalVote.get().getType().equals(dto.getType()))
            throw new CommunityNotFoundException("Siz ushbu postga " + dto.getType() + " qilgansiz");

        if(dto.getType().equals(VoteType.LIKE)){
            post.setVoteCount(post.getVoteCount() + 1);
        }else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(map(dto,post));
        postRepository.save(post);
    }

    private Vote map(VoteDto dto, Post post) {
        return Vote.builder()
                .type(dto.getType())
                .post(post)
                .owner(authService.getCurrentUser())
                .build();
    }
}
