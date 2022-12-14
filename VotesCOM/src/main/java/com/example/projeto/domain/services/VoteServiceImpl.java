package com.example.projeto.domain.services;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.repositories.ProductRepository;
import com.example.projeto.domain.repositories.ReviewRepository;
import com.example.projeto.domain.repositories.VoteRepository;
import com.example.projeto.rabbitmq.VotesCOMSender;
import com.example.projeto.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class VoteServiceImpl implements VoteService{

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VotesCOMSender votesCOMSender;

    @Autowired
    private Utils utils;

   /* @Override
    public int getVotesByReviewId(Long reviewId){return voteRepository.getVotesByReviewId(reviewId);}*/

    @Override
    public Vote create(Vote newVote){
        return voteRepository.save(newVote);
    }

    @Override
    public void create(VoteDTO newVote){
        boolean voteIsPresent = voteRepository.existsById(newVote.voteId);
        if(voteIsPresent == false) {
            Vote v = new Vote(newVote.getVote(), newVote.getReason());
            v.setUserId(newVote.getUserID());
            v.setReviewId(newVote.getReviewID());
            v.setStatus(newVote.getStatus());
            voteRepository.save(v);
        }
    }

    @Override
    public VoteDTO vote(Long reviewId, HttpServletRequest request, Vote newVote) throws IOException, InterruptedException {
        boolean reviewIsPresent = reviewIsPresent(reviewId);
        if (reviewIsPresent == true) {
            Review review = reviewRepository.findReviewById(reviewId).get();
            Status status = review.getStatus();
            if (status == Status.APPROVED) {
                newVote.setUserId(utils.getUserIdByToken(request));
                newVote.setReviewId(reviewId);
                final var vote = create(newVote);
                VoteDTO voteDTO = new VoteDTO(vote.getVoteId(),vote.getUserId(), vote.getReviewId(), vote.getVote(), vote.getReason(), vote.getStatus());
                votesCOMSender.send(voteDTO);
                return voteDTO;
            }
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Review not APPROVED");
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Review not found!");
    }

    @Override
    public VoteDTO voteSKU(String sku, HttpServletRequest request, Vote newVote) throws IOException, InterruptedException {
        boolean productIsPresent = productIsPresent(sku);
        if (productIsPresent == true) {
            Review review = new Review(sku, 0, "");
            reviewRepository.save(review);
            ReviewDTO reviewDTO = new ReviewDTO(review.getReviewId(), review.getSku(), review.getRating(), review.getText(), review.getPublishingDate(), review.getFunFact());
            votesCOMSender.sendReview(reviewDTO);

            newVote.setUserId(utils.getUserIdByToken(request));
            newVote.setReviewId(review.getReviewId());
            newVote.setStatus(Status.PENDING);
            final var vote = create(newVote);
            VoteDTO voteDTO = new VoteDTO(vote.getVoteId(),vote.getUserId(), vote.getReviewId(), vote.getVote(), vote.getReason(), vote.getStatus());
            votesCOMSender.send(voteDTO);
            return voteDTO;
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Product not found!");
    }

    public boolean reviewIsPresent(Long reviewId) {
        final var optionalReview = reviewRepository.findReviewById(reviewId);
        if(optionalReview.isPresent()){
            return true;
        }
        return false;
    }

    public boolean productIsPresent(String sku) {
        final var optionalProduct = productRepository.findBySku(sku);
        if(optionalProduct.isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public Review create(ReviewDTO newReviewDTO) throws IOException {
        boolean checkReview = reviewRepository.existsById(newReviewDTO.getReviewId());
        if (checkReview == false) {
            Review review = new Review(newReviewDTO.getSku(), newReviewDTO.getRating(), newReviewDTO.getText());
            return reviewRepository.save(review);
        }
        return null;
    }

    @Override
    public Product create(ProductDTO newProductDTO) throws IOException {
        Product product = new Product(newProductDTO.getDesignation(), newProductDTO.getDescription(), newProductDTO.getSku());
        return productRepository.save(product);
    }

    @Override
    public Review partialUpdate(final ReviewDTOStatus reviewDTOStatus) {
        // first let's check if the object exists so we don't create a new object with
        // save
        final var rev = reviewRepository.findById(reviewDTOStatus.getReviewId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found"));

        // since we got the object from the database we can check the version in memory
        // and apply the patch
        rev.applyPatch(reviewDTOStatus);

        // in the meantime some other user might have changed this object on the
        // database, so concurrency control will still be applied when we try to save
        // this updated object
        return reviewRepository.save(rev);
    }

    @Override
    public void deleteById(final Long reviewId) {
        reviewRepository.deleteByIdIfMatch(reviewId);
    }
}
