package com.example.projeto.domain.services;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.repositories.ReviewRepository;
import com.example.projeto.domain.views.ReviewView;
import com.example.projeto.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    private Utils utils;

    @Override
    public List<ReviewDTOStatus> findAllPendingReviews() throws IOException, InterruptedException {
        Iterable<Review> reviews = reviewRepository.findAllPendingReviews();
        List<ReviewDTOStatus> reviewDTOStatusList = new ArrayList();
        for (Review r : reviews) {
            ReviewDTOStatus reviewDTO = new ReviewDTOStatus(r.getReviewId(), r.getSku(), r.getRating(), r.getText(), r.getPublishingDate(), r.getFunFact(), r.getStatus());
            reviewDTOStatusList.add(reviewDTO);
        }

        List<ReviewDTOStatus> reviewDTOStatusList1 = getPendingReviews();
        for (ReviewDTOStatus r : reviewDTOStatusList1) {
            reviewDTOStatusList.add(r);
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
        List<ReviewDTOcat> reviewDTOcats = getReviewsCat(sku);
        for (ReviewDTOcat p : reviewDTOcats) {
            reviewDTOcatList.add(p);
        }

        reviewDTOcatList.sort(Comparator.comparing(ReviewDTOcat::getPublishingDate).reversed());
        return reviewDTOcatList;
    }

    @Override
    public List<ReviewDTO> findReviewsBySkuSortedByVotesAndDate(final String sku) throws IOException, InterruptedException {
        Iterable<Review> reviews = findReviewsBySku(sku);
        List<ReviewDTO> reviewDTOS = new ArrayList();
        for (Review r : reviews) {
            int numbervotes = getVotes(r.getReviewId());
            ReviewDTO reviewDTO = new ReviewDTO(r.getReviewId(), r.getSku(), r.getRating(), r.getText(), r.getPublishingDate(), r.getFunFact());
            reviewDTO.setNumberOfVotes(numbervotes);
            reviewDTOS.add(reviewDTO);
        }
        List<ReviewDTO> reviewDTOS1 = getReviews(sku);
        for (ReviewDTO p : reviewDTOS1) {
            reviewDTOS.add(p);
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
    public ReviewDTO createReview(HttpServletRequest request, Review newReview) throws IOException, InterruptedException {
        boolean product = productIsPresent(newReview.getSku());
        if(product == true) {
            newReview.setUserId(utils.getUserIdByToken(request));
            final var review = create(newReview);
            ReviewDTO reviewDTO = new ReviewDTO(review.getReviewId(), review.getSku(), review.getRating(), review.getText(), review.getPublishingDate(), review.getFunFact());
            return reviewDTO;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found!");
    }

    @Override
    public ResponseEntity<Review> withdrawReview(final Long reviewId) throws IOException, InterruptedException {
        int voteCount = getVotes(reviewId);
        if (voteCount == 0) {
            deleteById(reviewId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Votes done for this Review!");
        }
        return ResponseEntity.noContent().build();
    }
    @Override
    public ReviewDTOStatus updateReviewStatus(final Long id, final Review review) throws IOException {
        Review newReview = partialUpdate(id, review);
        ReviewDTOStatus newReviewDTOStatus = new ReviewDTOStatus(newReview.getReviewId(), newReview.getSku(), newReview.getRating(), newReview.getText(), newReview.getPublishingDate(), newReview.getFunFact(), newReview.getStatus());
        return newReviewDTOStatus;
    }

    @Override
    public List<ReviewDTOStatus> findReviewsByUserId(Long userId) throws IOException, InterruptedException {
        Iterable<Review> reviews = reviewRepository.findReviewsByUserId(userId);
        List<ReviewDTOStatus> reviewDTOStatuses = new ArrayList();
        for (Review r : reviews) {
            int numbervotes = getVotes(r.getReviewId());
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
    public AggregatedRatingDTO getAggregatedRatingDTO(final String sku){
        Iterable<Review> reviews = findReviewsBySku(sku);
        AggregatedRating agg = getProductAggregatedRating(reviews);
        AggregatedRatingDTO aggDTO = new AggregatedRatingDTO(agg.getAverage(), agg.getTotalRatings(), agg.getFive_star(), agg.getFour_star(),
                agg.getThree_star(), agg.getTwo_star(), agg.getOne_star());
        return aggDTO;
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
    @Override
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

    public boolean productIsPresent(final String sku) throws IOException, InterruptedException {

        String baseURL = "http://localhost:8080/api/public/product/get/" + sku;

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return  Boolean.parseBoolean(response.body());
    }

    @Override
    public List<ReviewDTO> getReviews(String sku) throws IOException, InterruptedException {

        String baseURL = "http://localhost:8086/api/public/review/vote/product/" + sku;

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        List<ReviewDTO> reviewDTOS = mapper.readValue(response.body(), new TypeReference<List<ReviewDTO>>() {});

        return reviewDTOS;
    }

    @Override
    public List<ReviewDTOcat> getReviewsCat(String sku) throws IOException, InterruptedException {

        String baseURL = "http://localhost:8086/api/public/review/product/" + sku;

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        List<ReviewDTOcat> reviewDTOcats = mapper.readValue(response.body(), new TypeReference<List<ReviewDTOcat>>() {});

        return reviewDTOcats;
    }

    public List<ReviewDTOStatus> getReviewsByUserId(Long userId) throws IOException, InterruptedException {
        String baseURL = "http://localhost:8086/api/public/review/status/get/" + userId;

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        List<ReviewDTOStatus> reviewDTOStatusList = mapper.readValue(response.body(), new TypeReference<List<ReviewDTOStatus>>() {});

        return reviewDTOStatusList;
    }

    public List<ReviewDTOStatus> getPendingReviews() throws IOException, InterruptedException {
        String baseURL = "http://localhost:8086/api/public/review/pending";

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        List<ReviewDTOStatus> reviewDTOStatusList = mapper.readValue(response.body(), new TypeReference<List<ReviewDTOStatus>>() {});

        return reviewDTOStatusList;
    }
}