package com.example.projeto.domain.services;

import com.example.projeto.domain.models.Vote;
import com.example.projeto.domain.models.VoteDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface VoteService {

    int getVotesByReviewId(Long reviewId) throws IOException, InterruptedException;

    /*/**
     * Create a new Vote and assign its id.
     *
     * @param newVote
     * @return
     */
    //Vote create(Vote newVote);

    //VoteDTO vote(Long reviewId, HttpServletRequest request, Vote newVote) throws IOException, InterruptedException;
}
