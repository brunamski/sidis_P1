package com.example.projeto.domain.services;

import com.example.projeto.domain.models.Vote;
import com.example.projeto.domain.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService{

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public int getVotesByReviewId(Long reviewId){return voteRepository.getVotesByReviewId(reviewId);}

    @Override
    public Vote create(Vote newVote){
        return voteRepository.save(newVote);
    }

}