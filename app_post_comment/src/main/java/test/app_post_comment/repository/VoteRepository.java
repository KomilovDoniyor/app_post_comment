package test.app_post_comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.app_post_comment.domain.Post;
import test.app_post_comment.domain.User;
import test.app_post_comment.domain.Vote;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Optional<Vote> findTopByPostAndOwnerOrderByIdDesc(Post post, User owner);
}
