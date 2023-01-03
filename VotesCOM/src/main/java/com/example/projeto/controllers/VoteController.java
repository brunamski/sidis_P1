package com.example.projeto.controllers;



import com.example.projeto.domain.models.Vote;
import com.example.projeto.domain.models.VoteDTO;

import com.example.projeto.domain.services.VoteService;
import com.example.projeto.rabbitmq.VotesCOMSender;
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
        VoteDTO voteDTO = voteService.vote(reviewId, request, newVote);
        return ResponseEntity.ok().body(voteDTO);
    }

    @Operation(summary = "US06 - To vote for a unexisting review by product")
    @PostMapping(value = "/vote/product/{sku}")
    @RolesAllowed(Role.REGISTERED)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VoteDTO> vote(@PathVariable(value = "sku") String sku, HttpServletRequest request, @RequestBody Vote newVote) throws IOException, InterruptedException {
        //VoteDTO voteDTO = voteService.vote(reviewId, request, newVote);
        return ResponseEntity.noContent().build();
    }
}
