package test.app_post_comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private Integer id;
    private Integer postId;
    private String text;
    private Instant createdAt;
    private String username;
}
