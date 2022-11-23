package com.example.projeto.controllers;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.services.ReviewService;
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
import java.util.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Reviews", description = "Endpoints for reviews")
@RestController
@RequestMapping("api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private Utils utils;

    /*@Operation(summary = "US05 - To obtain the reviews of a product. Sorted in reverse chronological publishing date")
    @GetMapping(value = "/public/review/product/{sku}")
    public List<ReviewDTOcat> findReviewsBySkuSortedByDate(@PathVariable(value = "sku") final String sku) {
        return reviewService.findReviewsBySkuSortedByDate(sku);
    }

    @Operation(summary = "US05 - To obtain the reviews of a product. Sorted by number of votes and reverse chronological publishing date")
    @GetMapping(value = "/public/review/vote/product/{sku}")
    public List<ReviewDTO> findReviewsBySkuSortedByVotesAndDate(@PathVariable(value = "sku") final String sku) throws IOException, InterruptedException {
        return reviewService.findReviewsBySkuSortedByVotesAndDate(sku);
    }*/

    @Operation(summary = "US04 - To review and rate a product")
    @PostMapping(value = "/review")
    @RolesAllowed(Role.REGISTERED)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewDTO> createReview(HttpServletRequest request, @RequestBody Review newReview) throws IOException, InterruptedException {
        return ResponseEntity.ok().body(reviewService.createReview(request, newReview));
    }

    @Operation(summary = "US07 - To withdraw one of my reviews")
    @DeleteMapping(value = "/review/{id}/withdraw")
    @RolesAllowed(Role.REGISTERED)
    public ResponseEntity<Review> withdrawReview(@PathVariable("id") @Parameter(description = "The id of the review to withdraw") final Long reviewId) throws IOException, InterruptedException {
        return reviewService.withdrawReview(reviewId);
    }

    /*@Operation(summary = "US10 - To obtain all pending reviews")
    @GetMapping(value = "/review/pending")
    @RolesAllowed(Role.MODERATOR)
    public List<ReviewDTOStatus> findAllPendingReviews() throws IOException, InterruptedException {
        return reviewService.findAllPendingReviews();
    }*/

    @Operation(summary = "US11 - To approve or reject a pending review")
    @PatchMapping(value = "/review/{id}")
    @RolesAllowed(Role.MODERATOR)
    public ResponseEntity<ReviewDTOStatus> updateReviewStatus(@PathVariable("id") @Parameter(description = "The id of the review we will update") final Long id,
                                                              @Valid @RequestBody final Review review) throws IOException {
        return ResponseEntity.ok().body(reviewService.updateReviewStatus(id, review));
    }

    /*@Operation(summary = "US08 - To obtain all my reviews including their status")
    @RolesAllowed(Role.REGISTERED)
    @GetMapping(value = "/review/status")
    public List<ReviewDTOStatus> findReviewsByUserId(HttpServletRequest request) throws IOException, InterruptedException {
        return reviewService.findReviewsByUserId(utils.getUserIdByToken(request));
    }

    @GetMapping(value = "/public/review/product/aggregatedrating/{sku}")
    public ResponseEntity<AggregatedRatingDTO> getAggregatedRatingDTO(@PathVariable(value = "sku") final String sku) {
        return ResponseEntity.ok().body(reviewService.getAggregatedRatingDTO(sku));
    }

    @GetMapping(value = "/public/review/get/{id}")
    public boolean reviewIsPresent(@PathVariable(value = "id") final Long reviewId) throws IOException, InterruptedException {
        final var optionalReview = reviewService.getReviewById(reviewId);
        if (optionalReview.isPresent()) {
            return true;
        }
        else {
            return false;
        }
    }

    @GetMapping(value = "/public/review/status/get/{id}")
    public List<ReviewDTOStatus> getReviewsByUserId(@PathVariable(value = "id") final Long userId) throws IOException, InterruptedException {
        return reviewService.findReviewsByUserId(userId);
    }

    @GetMapping(value = "/public/review/pending")
    public List<ReviewDTOStatus> findAllPendingReviews2() throws IOException, InterruptedException {
        return reviewService.findAllPendingReviews();
    }*/
}