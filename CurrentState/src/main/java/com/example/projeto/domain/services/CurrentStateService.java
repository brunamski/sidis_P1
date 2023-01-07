package com.example.projeto.domain.services;

import java.io.IOException;
import java.util.List;

import com.example.projeto.domain.models.*;

public interface CurrentStateService {

    void createProduct(ProductDTO productDTO) throws IOException;

    void createReview(ReviewDTO reviewDTO) throws IOException;

    void createVote(VoteDTO voteDTO) throws IOException;


    Review partialUpdate(ReviewDTOStatus review);

    void deleteById(Long reviewId);

    List<ProductDTO> getProductList() throws IOException;

    List<ReviewDTO> getReviewList();

    List<VoteDTO> getVoteList();
}
