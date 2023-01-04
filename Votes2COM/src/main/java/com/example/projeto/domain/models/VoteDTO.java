package com.example.projeto.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
public class VoteDTO {

    public final Long voteId;
    public final Long userID;
    public final Long reviewID;
    public final Boolean vote;
    public final String reason;

    public VoteDTO(   Long voteId,
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