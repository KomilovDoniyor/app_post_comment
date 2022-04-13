package test.app_post_comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.app_post_comment.domain.Comment;
import test.app_post_comment.domain.Post;
import test.app_post_comment.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByOwner(User owner);
}
