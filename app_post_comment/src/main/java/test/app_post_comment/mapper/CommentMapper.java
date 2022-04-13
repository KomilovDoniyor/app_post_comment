package test.app_post_comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.app_post_comment.domain.Comment;
import test.app_post_comment.domain.Post;
import test.app_post_comment.domain.User;
import test.app_post_comment.dto.CommentRequest;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "request.text")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "owner", source = "user")
    Comment fromRequest(CommentRequest request, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getId())")
    @Mapping(target = "username", expression = "java(comment.getOwner().getUsername())")
    CommentRequest toRequest(Comment comment);
}
