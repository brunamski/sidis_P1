package com.example.projeto.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDTO {

    public Long voteId;
    public Long userID;
    public Long reviewID;
    public Boolean vote;
    public String reason;

    protected VoteDTO() {}

    public VoteDTO(Long voteId,
                   Long userId,
                   Long reviewID,
                   Boolean vote,
                   String reason){
        this.voteId = voteId;
        this.userID = userId;
        this.reviewID = reviewID;
        this.vote = vote;
        this.reason = reason;
    }
}
