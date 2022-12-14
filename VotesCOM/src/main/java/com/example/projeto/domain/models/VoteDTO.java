package com.example.projeto.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
public class VoteDTO {

    public Long voteId;
    public Long userID;
    public Long reviewID;
    public Boolean vote;
    public String reason;
    public Status status;

    public VoteDTO(){}

    public VoteDTO(Long voteId,
                   Long userId,
                   Long reviewID,
                   Boolean vote,
                   String reason,
                   Status status){
        this.voteId = voteId;
        this.userID = userId;
        this.reviewID = reviewID;
        this.vote = vote;
        this.reason = reason;
        this.status = status;
    }
}
