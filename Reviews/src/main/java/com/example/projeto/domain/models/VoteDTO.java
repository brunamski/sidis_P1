package com.example.projeto.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
public class VoteDTO {

    public final Long voteId;
    public final Long reviewID;
    public final boolean vote;
    public final String reason;

    public VoteDTO(   @JsonProperty("voteId") Long voteId,
                      @JsonProperty("reviewId") Long reviewID,
                      @JsonProperty("vote") boolean vote,
                      @JsonProperty("reason") String reason){
        this.voteId = voteId;
        this.reviewID = reviewID;
        this.vote = vote;
        this.reason = reason;
    }
}
