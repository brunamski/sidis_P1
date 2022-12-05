package com.example.projeto.domain.services;

import com.example.projeto.domain.models.*;
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

    ReviewDTO createReview(HttpServletRequest request, Review newReview) throws IOException, InterruptedException;

    ResponseEntity<Review> withdrawReview(final Long reviewId) throws IOException, InterruptedException;

    ReviewDTOStatus updateReviewStatus(final Long id, final Review review) throws IOException;

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
