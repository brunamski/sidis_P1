package com.example.projeto.controllers;

import com.example.projeto.domain.models.AggregatedRating;
import com.example.projeto.domain.models.AggregatedRatingDTO;
import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.models.VoteDTO;
import com.example.projeto.domain.services.ReviewService;
import com.example.projeto.domain.services.VoteService;
import com.example.projeto.domain.views.ReviewView;
import com.example.projeto.usermanagement.models.Role;

import com.example.projeto.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONArray;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name = "Reviews", description = "Endpoints for reviews")
@RestController
@RequestMapping("api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private Utils utils;

    @Operation(summary = "US05 - To obtain the reviews of a product. Sorted in reverse chronological publishing date")
    @GetMapping(value = "/public/review/product/{sku}")
    public Iterable<ReviewView> findReviewsBySkuSortedByDate(@PathVariable(value = "sku") final String sku) {
        return reviewService.findReviewsBySkuSortedByDate(sku);
    }

    @Operation(summary = "US05 - To obtain the reviews of a product. Sorted by number of votes and reverse chronological publishing date")
    @GetMapping(value = "/public/review/vote/product/{sku}")
    public Iterable<ReviewView> findReviewsBySkuSortedByVotesAndDate(@PathVariable(value = "sku") final String sku) throws IOException, InterruptedException {
        Iterable<Review> reviews = reviewService.findReviewsBySku(sku);

        for(Review r: reviews){
            int numberofvotes = getVotes(r.getReviewId());

        }

        return reviewService.findReviewsBySkuSortedByVotesAndDate(sku);
    }

    @Operation(summary = "US04 - To review and rate a product")
    @PostMapping(value = "/review")
    @RolesAllowed(Role.REGISTERED)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Review> create(HttpServletRequest request, @RequestBody Review newReview) {
        newReview.setUserId(utils.getUserIdByToken(request));
        final var review= reviewService.create(newReview);
        return ResponseEntity.ok().body(review);
    }

    @Operation(summary = "US07 - To withdraw one of my reviews")
    @DeleteMapping(value = "/review/{id}/withdraw")
    @RolesAllowed(Role.REGISTERED)
    public ResponseEntity<Review> withdrawReview(@PathVariable("id") @Parameter(description = "The id of the review to withdraw") final Long reviewId) {
        //passar para o service
        int voteCount = voteService.getVotesByReviewId(reviewId);
        if(voteCount == 0) {
            reviewService.deleteById(reviewId);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Votes done for this Review!");
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "US10 - To obtain all pending reviews")
    @GetMapping(value = "/review/pending")
    @RolesAllowed(Role.MODERATOR)
    public Iterable<Review> findAllPendingReviews() {
        return reviewService.findAllPendingReviews();
    }

    @Operation(summary = "US11 - To approve or reject a pending review")
    @PatchMapping(value = "/review/{id}")
    @RolesAllowed(Role.MODERATOR)
    public ResponseEntity<Review> updateReviewStatus(@PathVariable("id") @Parameter(description = "The id of the review we will update") final Long id,
                                                     @Valid @RequestBody final Review review) {
        Review newReview = reviewService.partialUpdate(id, review);
        return ResponseEntity.ok().body(newReview);
    }

    @Operation(summary = "US08 - To obtain all my reviews including their status")
    @RolesAllowed(Role.REGISTERED)
    @GetMapping(value = "/review/status")
    public Iterable<Review> findReviewsByUserId(HttpServletRequest request) {
        return reviewService.findReviewsByUserId(utils.getUserIdByToken(request));
    }

    @GetMapping(value = "/public/review/product/aggregatedrating/{sku}")
    public AggregatedRatingDTO getAggregatedRatingDTO(@PathVariable(value = "sku") final String sku) {
        Iterable<Review> reviews = reviewService.findReviewsBySku(sku);
        AggregatedRating agg = reviewService.getProductAggregatedRating(reviews);
        AggregatedRatingDTO aggDTO = new AggregatedRatingDTO(agg.getAverage(),agg.getTotalRatings(),agg.getFive_star(),agg.getFour_star(),
                                                              agg.getThree_star(),agg.getTwo_star(),agg.getOne_star());
        return aggDTO;
    }

    public int getVotes(@PathVariable(value = "reviewId") final Long reviewId) throws IOException, InterruptedException {

        String baseURL = "http://localhost:8082/api/public/vote/review/" + reviewId;

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        int numberofvotes = Integer.parseInt(response.toString());

        return numberofvotes;
    }
}
