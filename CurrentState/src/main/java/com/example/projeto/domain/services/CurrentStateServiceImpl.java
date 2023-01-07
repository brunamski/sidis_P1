package com.example.projeto.domain.services;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.repositories.ProductRepository;
import com.example.projeto.domain.repositories.ReviewRepository;
import com.example.projeto.domain.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class CurrentStateServiceImpl implements CurrentStateService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public void createProduct(ProductDTO p) throws IOException {
        Product product = new Product(p.getDesignation(), p.getDescription(), p.getSku());
        productRepository.save(product);
    }

    @Override
    public void createReview(ReviewDTO r) throws IOException {
        Review review = new Review(r.getSku(), r.getRating(), r.getText());
        reviewRepository.save(review);
    }

    @Override
    public void createVote(VoteDTO v) throws IOException {
        Vote vote = new Vote(v.getVote(), v.getReason());
        voteRepository.save(vote);
    }

    @Override
    public Review partialUpdate(final ReviewDTOStatus review) {
        final var rev = reviewRepository.findById(review.getReviewId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found"));
        rev.applyPatchDTO(review);
        return reviewRepository.save(rev);
    }

    @Override
    public void deleteById(final Long reviewId) {
        reviewRepository.deleteByIdIfMatch(reviewId);
    }

    @Override
    public List<ProductDTO> getProductList() {
        Iterable<Product> productList = productRepository.findCatalog();
        List<ProductDTO> productDTOList = new ArrayList();
        for (Product p : productList) {
            ProductDTO productDTO = new ProductDTO(p.getProductId(), p.getDesignation(), p.getSku(), p.getDescription());
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    @Override
    public List<ReviewDTO> getReviewList() {
        Iterable<Review> reviewList = reviewRepository.findAll();
        List<ReviewDTO> reviewDTOList = new ArrayList();
        for (Review r : reviewList) {
            ReviewDTO reviewDTO = new ReviewDTO(r.getReviewId(), r.getSku(), r.getRating(), r.getText(), r.getPublishingDate(), r.getFunFact());
            reviewDTOList.add(reviewDTO);
        }
        return reviewDTOList;
    }

    @Override
    public List<VoteDTO> getVoteList() {
        Iterable<Vote> voteList = voteRepository.findAll();
        List<VoteDTO> voteDTOList = new ArrayList();
        for (Vote v : voteList) {
            VoteDTO voteDTO = new VoteDTO(v.getVoteId(), v.getUserId(), v.getReviewId(), v.getVote(), v.getReason());
            voteDTOList.add(voteDTO);
        }
        return voteDTOList;
    }
}
