package com.example.projeto.controllers;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.services.ReviewService;
import com.example.projeto.rabbitmq.ReviewsCOMSender;
import com.example.projeto.usermanagement.models.Role;

import com.example.projeto.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Reviews", description = "Endpoints for reviews")
@RestController
@RequestMapping("api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private Utils utils;

    @Autowired
    private ReviewsCOMSender reviewsCOMSender;

    @Operation(summary = "US04 - To review and rate a product")
    @PostMapping(value = "/review")
    @RolesAllowed(Role.REGISTERED)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewDTO> createReview(HttpServletRequest request, @RequestBody Review newReview) throws IOException, InterruptedException {
        ReviewDTO reviewDTO = reviewService.createReview(request, newReview);
        Review review = new Review(reviewDTO.getSku(),reviewDTO.getRating(),reviewDTO.getText());
        review.setUserId(utils.getUserIdByToken(request));
        reviewsCOMSender.send(review);
        return ResponseEntity.ok().body(reviewDTO);
    }

    @Operation(summary = "US07 - To withdraw one of my reviews")
    @DeleteMapping(value = "/review/{id}/withdraw")
    @RolesAllowed(Role.REGISTERED)
    public ResponseEntity<Review> withdrawReview(@PathVariable("id") @Parameter(description = "The id of the review to withdraw") final Long reviewId) throws IOException, InterruptedException {
        return reviewService.withdrawReview(reviewId);
    }

    @Operation(summary = "US11 - To approve or reject a pending review")
    @PatchMapping(value = "/review/{id}")
    @RolesAllowed(Role.MODERATOR)
    public ResponseEntity<ReviewDTOStatus> updateReviewStatus(@PathVariable("id") @Parameter(description = "The id of the review we will update") final Long id,
                                                              @Valid @RequestBody final Review review) throws IOException {
        return ResponseEntity.ok().body(reviewService.updateReviewStatus(id, review));
    }
}
