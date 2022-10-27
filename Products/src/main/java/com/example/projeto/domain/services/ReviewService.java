package com.example.projeto.domain.services;

import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.views.ReviewView;

import java.util.Optional;


public interface ReviewService {

    Iterable<Review> findAllPendingReviews();

    Iterable<Review> findReviewsBySku(String sku);

    Iterable<ReviewView> findReviewsBySkuSortedByDate(String sku);

    Iterable<ReviewView> findReviewsBySkuSortedByVotesAndDate(String sku);

    Iterable<Review> findReviewsByUserId(Long userId);

    Optional<Review> getReviewById(Long reviewId);

    /**
     * Create a new Review and assign its id.
     *
     * @param newReview
     * @return
     */
    Review create(Review newReview);

    void deleteById(Long reviewId);

    Review partialUpdate(final Long id, final Review review);
}
