package com.example.projeto.controllers;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.services.ReviewService;
import com.example.projeto.domain.views.ReviewView;
import com.example.projeto.usermanagement.models.Role;

import com.example.projeto.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import java.time.LocalDate;
import java.util.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "US05 - To obtain the reviews of a product. Sorted in reverse chronological publishing date")
    @GetMapping(value = "/public/review/product/{sku}")
    public Iterable<ReviewView> findReviewsBySkuSortedByDate(@PathVariable(value = "sku") final String sku) {
        return reviewService.findReviewsBySkuSortedByDate(sku);
    }

    @Operation(summary = "US05 - To obtain the reviews of a product. Sorted by number of votes and reverse chronological publishing date")
    @GetMapping(value = "/public/review/vote/product/{sku}")
    public List<ReviewDTO> findReviewsBySkuSortedByVotesAndDate(@PathVariable(value = "sku") final String sku) throws IOException, InterruptedException {
        Iterable<Review> reviews = reviewService.findReviewsBySku(sku);
        List<ReviewDTO> reviewDTOS = new ArrayList();
        for (Review r : reviews) {
            int numbervotes = getVotes(r.getReviewId());
            ReviewDTO reviewDTO = new ReviewDTO(r.getReviewId(), r.getRating(), r.getText(), r.getPublishingDate(), r.getFunFact());
            reviewDTO.setNumberOfVotes(numbervotes);
            reviewDTOS.add(reviewDTO);
        }
        reviewDTOS.sort(Comparator.comparing(ReviewDTO::getNumberOfVotes)
                .thenComparing(ReviewDTO::getPublishingDate).reversed());

        return reviewDTOS;
    }

    @Operation(summary = "US04 - To review and rate a product")
    @PostMapping(value = "/review")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewDTO> create(HttpServletRequest request, @RequestBody Review newReview) throws IOException {
        newReview.setUserId(utils.getUserIdByToken(request));
        final var review = reviewService.create(newReview);
        ReviewDTO reviewDTO = new ReviewDTO(review.getReviewId(), review.getRating(), review.getText(), review.getPublishingDate(), review.getFunFact());
        return ResponseEntity.ok().body(reviewDTO);
    }

    @Operation(summary = "US07 - To withdraw one of my reviews")
    @DeleteMapping(value = "/review/{id}/withdraw")
    @RolesAllowed(Role.REGISTERED)
    public ResponseEntity<Review> withdrawReview(@PathVariable("id") @Parameter(description = "The id of the review to withdraw") final Long reviewId) throws IOException, InterruptedException {
        //passar para o service
        int voteCount = getVotes(reviewId);
        if (voteCount == 0) {
            reviewService.deleteById(reviewId);
        } else {
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
    public ResponseEntity<ReviewDTOStatus> updateReviewStatus(@PathVariable("id") @Parameter(description = "The id of the review we will update") final Long id,
                                                              @Valid @RequestBody final Review review) throws IOException {
        Review newReview = reviewService.partialUpdate(id, review);
        ReviewDTOStatus newReviewDTOStatus = new ReviewDTOStatus(newReview.getReviewId(), newReview.getRating(), newReview.getText(), newReview.getPublishingDate(), newReview.getFunFact(), newReview.getStatus());
        return ResponseEntity.ok().body(newReviewDTOStatus);
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
        AggregatedRatingDTO aggDTO = new AggregatedRatingDTO(agg.getAverage(), agg.getTotalRatings(), agg.getFive_star(), agg.getFour_star(),
                agg.getThree_star(), agg.getTwo_star(), agg.getOne_star());
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
        return Integer.parseInt(response.body());
    }



    @GetMapping(value = "/public/review/get/{id}")
    public boolean reviewIsPresent(@PathVariable(value = "id") final Long reviewId) throws IOException, InterruptedException {
        final var optionalReview = reviewService.getReviewById(reviewId);
        if (optionalReview.isPresent()) {

            return true;
        }
        else {
            String baseURL = "http://localhost:8081/api/public/review/get/" + reviewId;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseURL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());


            return Boolean.parseBoolean(response.body());
        }
    }
}
