package test.app_post_comment.enums;

public enum VoteType {

    LIKE(1),
    DISLIKE(-1);

    private final Integer rate;

    VoteType(Integer rate) {
        this.rate = rate;
    }

    public Integer getRate() {
        return rate;
    }
}
