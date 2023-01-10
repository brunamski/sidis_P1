package com.example.projeto.domain.services;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.repositories.ReviewRepository;
import com.example.projeto.domain.repositories.VoteRepository;
import com.example.projeto.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private Utils utils;

    @Override
    public List<ReviewDTOStatus> findAllPendingReviews() throws IOException, InterruptedException {
        Iterable<Review> reviews = reviewRepository.findAllPendingReviews();
        List<ReviewDTOStatus> reviewDTOStatusList = new ArrayList();
        for (Review r : reviews) {
            ReviewDTOStatus reviewDTO = new ReviewDTOStatus(r.getReviewId(), r.getSku(), r.getRating(), r.getText(), r.getPublishingDate(), r.getFunFact(), r.getStatus());
            reviewDTOStatusList.add(reviewDTO);
        }
        return reviewDTOStatusList;
    }

    @Override
    public Iterable<Review> findReviewsBySku(String sku) {
        return reviewRepository.findReviewsBySku(sku);
    }

    @Override
    public List<ReviewDTOcat> findReviewsBySkuSortedByDate(String sku) throws IOException, InterruptedException {
        Iterable<Review> reviews = findReviewsBySku(sku);
        List<ReviewDTOcat> reviewDTOcatList = new ArrayList();
        for (Review r : reviews) {
            ReviewDTOcat reviewDTOcat = new ReviewDTOcat(r.getSku(), r.getRating(), r.getText(), r.getPublishingDate(), r.getFunFact());
            reviewDTOcatList.add(reviewDTOcat);
        }
        reviewDTOcatList.sort(Comparator.comparing(ReviewDTOcat::getPublishingDate).reversed());
        return reviewDTOcatList;
    }

    @Override
    public List<ReviewDTO> findReviewsBySkuSortedByVotesAndDate(final String sku) throws IOException, InterruptedException {
        Iterable<Review> reviews = findReviewsBySku(sku);
        List<ReviewDTO> reviewDTOS = new ArrayList();
        for (Review r : reviews) {
            int numbervotes = voteRepository.getVotesByReviewId(r.getReviewId());
            ReviewDTO reviewDTO = new ReviewDTO(r.getReviewId(), r.getSku(), r.getRating(), r.getText(), r.getPublishingDate(), r.getFunFact());
            reviewDTO.setNumberOfVotes(numbervotes);
            reviewDTOS.add(reviewDTO);
        }
        reviewDTOS.sort(Comparator.comparing(ReviewDTO::getNumberOfVotes)
                .thenComparing(ReviewDTO::getPublishingDate).reversed());

        return reviewDTOS;
    }

    /*@Override
    public Iterable<ReviewView> findReviewsBySkuSortedByVotesAndDate(String sku) {
        return reviewRepository.findReviewsBySkuSortedByVotesAndDate(sku);
    }*/

    @Override
    public ResponseEntity<Review> withdrawReview(final Long reviewId) throws IOException, InterruptedException {

        deleteById(reviewId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public List<ReviewDTOStatus> findReviewsByUserId(Long userId) throws IOException, InterruptedException {
        Iterable<Review> reviews = reviewRepository.findReviewsByUserId(userId);
        List<ReviewDTOStatus> reviewDTOStatuses = new ArrayList();
        for (Review r : reviews) {
            int numbervotes = voteRepository.getVotesByReviewId(r.getReviewId());
            ReviewDTOStatus reviewDTO = new ReviewDTOStatus(r.getReviewId(), r.getSku(), r.getRating(), r.getText(), r.getPublishingDate(), r.getFunFact(), r.getStatus());
            reviewDTO.setNumberOfVotes(numbervotes);
            reviewDTOStatuses.add(reviewDTO);
        }
        return reviewDTOStatuses;
    }

    @Override
    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.findReviewById(reviewId);
    }

    @Override
    public Review create(ReviewDTO newReview) throws IOException {
        Review review = new Review(newReview.getSku(), newReview.getRating(), newReview.getText());
        return reviewRepository.save(review);
    }

    @Override
    public void deleteById(final Long reviewId) {
        reviewRepository.deleteByIdIfMatch(reviewId);
    }

    @Override
    public Review partialUpdate(final ReviewDTOStatus review) {
        // first let's check if the object exists so we don't create a new object with
        // save
        final var rev = reviewRepository.findById(review.getReviewId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found"));

        // since we got the object from the database we can check the version in memory
        // and apply the patch
        rev.applyPatchDTO(review);

        // in the meantime some other user might have changed this object on the
        // database, so concurrency control will still be applied when we try to save
        // this updated object
        return reviewRepository.save(rev);
    }

    @Override
    public AggregatedRatingDTO getAggregatedRatingDTO(final String sku) throws IOException, InterruptedException {
        Iterable<Review> reviews = findReviewsBySku(sku);
        List<ReviewDTOcat> reviewDTOcatList = new ArrayList();
        for (Review r : reviews) {
            ReviewDTOcat reviewDTOcat = new ReviewDTOcat(r.getSku(), r.getRating(), r.getText(), r.getPublishingDate(), r.getFunFact());
            reviewDTOcatList.add(reviewDTOcat);
        }
        AggregatedRating agg = getProductAggregatedRating(reviewDTOcatList);
        AggregatedRatingDTO aggDTO = new AggregatedRatingDTO(agg.getAverage(), agg.getTotalRatings(), agg.getFive_star(), agg.getFour_star(),
                agg.getThree_star(), agg.getTwo_star(), agg.getOne_star());
        return aggDTO;
    }

    @Override
    public AggregatedRating getProductAggregatedRating(List<ReviewDTOcat> reviews) {
        int soma = 0;
        float totalRatings = 0;
        float[][] ratingArray = new float[2][6];

        for(ReviewDTOcat r: reviews){
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

    @Override
    public void create(VoteDTO newVote){
        boolean voteIsPresent = voteRepository.existsById(newVote.voteId);
        if(voteIsPresent == false) {
            Vote v = new Vote(newVote.getVote(), newVote.getReason());
            v.setUserId(newVote.getUserID());
            v.setReviewId(newVote.getReviewID());
            voteRepository.save(v);
        }
    }
}