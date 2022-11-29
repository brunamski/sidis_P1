package com.example.projeto.domain.services;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.views.ReviewView;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface ReviewService {

    List<ReviewDTOStatus> findAllPendingReviews() throws IOException, InterruptedException;

    List<ReviewDTOStatus> findAllMyPendingReviews() throws IOException, InterruptedException;

    Iterable<Review> findReviewsBySku(String sku);

    List<ReviewDTOcat> findMyReviewsBySku(String sku);

    List<ReviewDTOcat> findReviewsBySkuSortedByDate(String sku) throws IOException, InterruptedException;

    List<ReviewDTO> findReviewsBySkuSortedByVotesAndDate(final String sku) throws IOException, InterruptedException;

    //Iterable<ReviewView> findReviewsBySkuSortedByVotesAndDate(String sku);

    ReviewDTO createReview(HttpServletRequest request, Review newReview) throws IOException, InterruptedException;

    ResponseEntity<Review> withdrawReview(final Long reviewId) throws IOException, InterruptedException;

    ReviewDTOStatus updateReviewStatus(final Long id, final Review review) throws IOException;

    List<ReviewDTOStatus> findReviewsByUserId(Long userId) throws IOException, InterruptedException;

    Optional<Review> getReviewById(Long reviewId);

    List<ReviewDTOcat> getReviewsCat(String sku) throws IOException, InterruptedException;

    List<ReviewDTO> getReviews(String sku) throws IOException, InterruptedException;

    /**
     * Create a new Review and assign its id.
     *
     * @param newReview
     * @return
     */
    Review create(Review newReview);

    void deleteById(Long reviewId);

    Review partialUpdate(final Long id, final Review review);

    AggregatedRatingDTO getAggregatedRatingDTO(final String sku);

    AggregatedRating getProductAggregatedRating(Iterable<Review> reviews);

    int getVotes(final Long reviewId) throws IOException, InterruptedException;
}
