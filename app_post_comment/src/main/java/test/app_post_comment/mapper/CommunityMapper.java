package test.app_post_comment.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.app_post_comment.domain.Community;
import test.app_post_comment.domain.Post;
import test.app_post_comment.dto.CommunityDto;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CommunityMapper {

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Community fromDto(CommunityDto dto);

    @Mapping(target = "countOfPosts", expression = "java(mapPosts(community.getPosts()))")
    CommunityDto toDto(Community community);

    default Integer mapPosts(Set<Post> posts){
        return posts.size();
    }
}
