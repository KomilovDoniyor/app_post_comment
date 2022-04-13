package test.app_post_comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private Integer id;
    private String postName;
    private String description;
    private String url;
    private String username;
    private String communityName;
    private Integer voteCount;
    private Integer commentCount;
    private boolean like;
    private boolean disLike;
}
