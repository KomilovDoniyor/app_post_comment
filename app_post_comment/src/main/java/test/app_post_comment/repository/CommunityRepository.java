package test.app_post_comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.app_post_comment.domain.Community;

import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Integer> {
    Optional<Community> findByName(String name);
}
