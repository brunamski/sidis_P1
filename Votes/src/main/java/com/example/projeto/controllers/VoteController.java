package com.example.projeto.controllers;

import com.example.projeto.domain.models.AggregatedRating;
import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.models.Vote;
import com.example.projeto.domain.models.VoteDTO;
import com.example.projeto.domain.services.ProductService;
import com.example.projeto.domain.services.VoteService;
import com.example.projeto.usermanagement.models.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Tag(name = "Votes", description = "Endpoints for votes")
@RestController
@RequestMapping("api")
public class VoteController {

    @Autowired
    private VoteService voteService;

    /*@Operation(summary = "US06 - To vote for a review")
    @PostMapping(value = "/review/{id}/vote")
    @RolesAllowed(Role.REGISTERED)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Vote> vote(@PathVariable(value = "id") Long reviewId, HttpServletRequest request, @RequestBody Vote newVote) {
        Optional<Review> review = reviewService.getReviewById(reviewId);
        if (review.isPresent()) {
            newVote.setUserId(utils.getUserIdByToken(request));
            newVote.setReviewId(reviewId);
            final var vote = voteService.create(newVote);
            return ResponseEntity.ok().body(vote);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Review not found!");
    }*/

    @GetMapping(value = "/public/vote/review/{reviewId}")
    public int getVotes(@PathVariable(value = "reviewId") final Long reviewId) {
        int numberofvotes = voteService.getVotesByReviewId(reviewId);
                return numberofvotes;
    }
}
