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
    public List<ReviewDTOcat> findReviewsBySkuSortedByDate(@PathVariable(value = "sku") final String sku) throws IOException, InterruptedException {
        return reviewService.findReviewsBySkuSortedByDate(sku);
    }

    @Operation(summary = "US05 - To obtain the reviews of a product. Sorted by number of votes and reverse chronological publishing date")
    @GetMapping(value = "/public/review/vote/product/{sku}")
    public List<ReviewDTO> findReviewsBySkuSortedByVotesAndDate(@PathVariable(value = "sku") final String sku) throws IOException, InterruptedException {
        return reviewService.findReviewsBySkuSortedByVotesAndDate(sku);
    }

    @Operation(summary = "US10 - To obtain all pending reviews")
    @GetMapping(value = "/review/pending")
    @RolesAllowed(Role.MODERATOR)
    public List<ReviewDTOStatus> findAllPendingReviews() throws IOException, InterruptedException {
        return reviewService.findAllPendingReviews();
    }

    @Operation(summary = "US08 - To obtain all my reviews including their status")
    @RolesAllowed(Role.REGISTERED)
    @GetMapping(value = "/review/status")
    public List<ReviewDTOStatus> findReviewsByUserId(HttpServletRequest request) throws IOException, InterruptedException {
        return reviewService.findReviewsByUserId(utils.getUserIdByToken(request));
    }

    @GetMapping(value = "/public/review/product/aggregatedrating/{sku}")
    public ResponseEntity<AggregatedRatingDTO> getAggregatedRatingDTO(@PathVariable(value = "sku") final String sku) throws IOException, InterruptedException {
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
}
