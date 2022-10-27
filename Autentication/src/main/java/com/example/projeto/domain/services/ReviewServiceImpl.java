package com.example.projeto.domain.services;

import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.repositories.ReviewRepository;
import com.example.projeto.domain.views.ReviewView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Iterable<Review> findAllPendingReviews(){
        return reviewRepository.findAllPendingReviews();
    }

    @Override
    public Iterable<Review> findReviewsBySku(String sku) {
        return reviewRepository.findReviewsBySku(sku);
    }

    @Override
    public Iterable<ReviewView> findReviewsBySkuSortedByDate(String sku) {
        return reviewRepository.findReviewsBySkuSortedByDate(sku);
    }

    @Override
    public Iterable<ReviewView> findReviewsBySkuSortedByVotesAndDate(String sku) {
        return reviewRepository.findReviewsBySkuSortedByVotesAndDate(sku);
    }

    @Override
    public Iterable<Review> findReviewsByUserId(Long userId){
        return reviewRepository.findReviewsByUserId(userId);
    }

    @Override
    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.findReviewById(reviewId);
    }

    @Override
    public Review create(Review newReview){
        return reviewRepository.save(newReview);
    }

    @Override
    public void deleteById(final Long reviewId) {
        reviewRepository.deleteByIdIfMatch(reviewId);
    }

    @Override
    public Review partialUpdate(final Long id, final Review review) {
        // first let's check if the object exists so we don't create a new object with
        // save
        final var rev = reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found"));

        // since we got the object from the database we can check the version in memory
        // and apply the patch
        rev.applyPatch(review);

        // in the meantime some other user might have changed this object on the
        // database, so concurrency control will still be applied when we try to save
        // this updated object
        return reviewRepository.save(rev);
    }
}