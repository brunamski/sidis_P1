package com.example.projeto.domain.services;

import com.example.projeto.domain.models.Vote;
import com.example.projeto.domain.models.VoteDTO;
import com.example.projeto.domain.repositories.VoteRepository;
import com.example.projeto.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class VoteServiceImpl implements VoteService{

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private Utils utils;

   /* @Override
    public int getVotesByReviewId(Long reviewId){return voteRepository.getVotesByReviewId(reviewId);}*/

    @Override
    public Vote create(Vote newVote){
        return voteRepository.save(newVote);
    }

    @Override
    public VoteDTO vote(Long reviewId, HttpServletRequest request, Vote newVote) throws IOException, InterruptedException {
        boolean review = reviewIsPresent(reviewId);
        if (review == true) {
            newVote.setUserId(utils.getUserIdByToken(request));
            newVote.setReviewId(reviewId);
            final var vote = create(newVote);
            VoteDTO voteDTO = new VoteDTO(vote.getVoteId(),vote.getUserId(), vote.getReviewId(), vote.isVote(), vote.getReason());
            return voteDTO;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found!");
    }

    public boolean reviewIsPresent(@PathVariable(value = "id") final Long reviewId) throws IOException, InterruptedException {

        String baseURL = "http://localhost:8081/api/public/review/get/" + reviewId;

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return  Boolean.parseBoolean(response.body());
    }

}
