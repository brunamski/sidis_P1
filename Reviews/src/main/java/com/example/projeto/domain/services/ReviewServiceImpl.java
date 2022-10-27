package com.example.projeto.domain.services;

import com.example.projeto.domain.models.AggregatedRating;
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

    @Override
    public AggregatedRating getProductAggregatedRating(Iterable<Review> reviews) {
        int soma = 0;
        float totalRatings = 0;
        float[][] ratingArray = new float[2][6];

        for(Review r: reviews){
            soma = soma + r.getRating();
            int rating = r.getRating();
            ratingArray[0][0]++;        //totalRatings
            ratingArray[0][rating]++;   //total of a star
        }

        totalRatings = ratingArray[0][0];
        ratingArray[1][0] = soma / totalRatings;
        ratingArray[1][1] = (ratingArray[0][1] / totalRatings) * 100;
        ratingArray[1][2] = (ratingArray[0][2] / totalRatings) * 100;
        ratingArray[1][3] = (ratingArray[0][3] / totalRatings) * 100;
        ratingArray[1][4] = (ratingArray[0][4] / totalRatings) * 100;
        ratingArray[1][5] = (ratingArray[0][5] / totalRatings) * 100;

        AggregatedRating aggregatedRating = new AggregatedRating(ratingArray[1][0], ratingArray[0][0], ratingArray[1][1], ratingArray[1][2], ratingArray[1][3], ratingArray[1][4], ratingArray[1][5]);
        return aggregatedRating;
    }
}