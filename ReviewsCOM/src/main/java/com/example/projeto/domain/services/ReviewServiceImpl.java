package com.example.projeto.domain.services;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.repositories.ProductRepository;
import com.example.projeto.domain.repositories.ReviewRepository;
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
    private ProductRepository productRepository;

    @Autowired
    private Utils utils;

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
        final var rev = reviewRepository.findById(reviewId);
        if(rev.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        deleteById(reviewId);
        return ResponseEntity.noContent().build();
    }
    @Override
    public ReviewDTOStatus updateReviewStatus(final Long id, final Review review) throws IOException {
        Review newReview = partialUpdate(id, review);
        ReviewDTOStatus newReviewDTOStatus = new ReviewDTOStatus(newReview.getReviewId(), newReview.getSku(), newReview.getRating(), newReview.getText(), newReview.getPublishingDate(), newReview.getFunFact(), newReview.getStatus());
        return newReviewDTOStatus;
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

    public Optional<Product> findBySku(final String sku) {
        return productRepository.findBySku(sku);
    }

    public boolean productIsPresent(String sku) {
        final var optionalProduct = findBySku(sku);
        if (optionalProduct.isPresent()) {
            return true;
        }
        return false;
    }
}