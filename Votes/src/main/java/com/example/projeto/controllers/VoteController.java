package com.example.projeto.controllers;



import com.example.projeto.domain.models.Vote;
import com.example.projeto.domain.models.VoteDTO;

import com.example.projeto.domain.services.VoteService;
import com.example.projeto.usermanagement.models.Role;
import com.example.projeto.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Tag(name = "Votes", description = "Endpoints for votes")
@RestController
@RequestMapping("api")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Operation(summary = "US06 - To vote for a review")
    @PostMapping(value = "/review/{id}/vote")
    @RolesAllowed(Role.REGISTERED)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VoteDTO> vote(@PathVariable(value = "id") Long reviewId, HttpServletRequest request, @RequestBody Vote newVote) throws IOException, InterruptedException {
            return ResponseEntity.ok().body(voteService.vote(reviewId, request, newVote));
    }

    @GetMapping(value = "/public/vote/review/{reviewId}")
    public int getVotes(@PathVariable(value = "reviewId") final Long reviewId) throws IOException, InterruptedException {
        return voteService.getVotesByReviewId(reviewId);
    }
}
