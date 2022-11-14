package com.example.projeto.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class VoteDTO {

    public final Long voteId;
    public final Long userID;
    public final Long reviewID;
    public final boolean vote;
    public final String reason;

    public VoteDTO(   @JsonProperty("voteId") Long voteId,
                      @JsonProperty("userId") Long userId,
                      @JsonProperty("reviewId") Long reviewID,
                      @JsonProperty("vote") boolean vote,
                      @JsonProperty("reason") String reason){
        this.voteId = voteId;
        this.userID = userId;
        this.reviewID = reviewID;
        this.vote = vote;
        this.reason = reason;
    }
}
