package test.app_post_comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import test.app_post_comment.domain.Community;
import test.app_post_comment.domain.Post;
import test.app_post_comment.domain.User;
import test.app_post_comment.domain.Vote;
import test.app_post_comment.dto.PostRequest;
import test.app_post_comment.dto.PostResponse;
import test.app_post_comment.enums.VoteType;
import test.app_post_comment.repository.CommentRepository;
import test.app_post_comment.repository.VoteRepository;
import test.app_post_comment.service.impl.AuthServiceImpl;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private CommentRepository commentRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "postName", source = "request.postName")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "url", source = "request.url")
    @Mapping(target = "owner", source = "currentUser")
    @Mapping(target = "date", expression = "java(java.time.Instant.now())")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "community", source = "community")
    public abstract Post map(PostRequest request, Community community, User currentUser);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "postName", source = "entity.postName")
    @Mapping(target = "description", source = "entity.description")
    @Mapping(target = "url", source = "entity.url")
    @Mapping(target = "username", source = "entity.owner.username")
    @Mapping(target = "communityName", source = "entity.community.name")
    @Mapping(target = "commentCount", expression = "java(getCommentCount(entity))")
    @Mapping(target = "like", expression = "java(isLike(entity))")
    @Mapping(target = "disLike", expression = "java(isDislike(entity))")
    public abstract PostResponse toDto(Post entity);

    boolean isLike(Post post){
        return checkoutVote(post, VoteType.LIKE);
    }

    boolean isDislike(Post post) {
        return checkoutVote(post, VoteType.DISLIKE);
    }

    Integer getCommentCount(Post post){
        return commentRepository.findAllByPost(post).size();
    }

    private boolean checkoutVote(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> optionalVote =
                    voteRepository.findTopByPostAndOwnerOrderByIdDesc(post, authService.getCurrentUser());
            return optionalVote.filter(vote -> vote.getType().equals(voteType)).isPresent();
        }
        return false;
    }

}
