package com.example.projeto.domain.services;

import com.example.projeto.domain.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface VoteService {

    /**
     * Create a new Vote and assign its id.
     *
     * @param newVote
     * @return
     */
    Vote create(Vote newVote);

    void create(VoteDTO newVote);

    VoteDTO vote(Long reviewId, HttpServletRequest request, Vote newVote) throws IOException, InterruptedException;

    VoteDTO voteSKU(String sku, HttpServletRequest request, Vote newVote) throws IOException, InterruptedException;

    Review create(ReviewDTO newReviewDTO) throws IOException;

    void deleteById(Long reviewId);

    Product create(ProductDTO newProductDTO) throws IOException;

    Review partialUpdate(final ReviewDTOStatus reviewDTOStatus) throws IOException;
}
