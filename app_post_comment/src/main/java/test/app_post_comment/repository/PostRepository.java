package test.app_post_comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.app_post_comment.domain.Community;
import test.app_post_comment.domain.Post;
import test.app_post_comment.domain.User;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByCommunity(Community community);
    List<Post> findAllByOwner(User owner);
}
