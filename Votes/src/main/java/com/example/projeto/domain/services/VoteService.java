package com.example.projeto.domain.services;

import com.example.projeto.domain.models.Vote;
import com.example.projeto.domain.models.VoteDTO;

public interface VoteService {

    int getVotesByReviewId(Long reviewId);

    /**
     * Create a new Vote and assign its id.
     *
     * @param newVote
     * @return
     */
    Vote create(Vote newVote);
}