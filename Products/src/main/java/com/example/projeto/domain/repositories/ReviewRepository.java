package com.example.projeto.domain.repositories;

import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.views.ReviewView;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

    @Query(value = "SELECT * FROM Review r WHERE r.status = 0", nativeQuery = true)
    Iterable<Review> findAllPendingReviews();

    @Query(value = "SELECT * FROM Review r WHERE r.sku = ?1 AND r.STATUS = 1", nativeQuery = true)
    List<Review> findReviewsBySku(String sku);

    @Query(value = "SELECT r.RATING AS RATING, r.TEXT AS text, r.PUBLISHING_DATE AS publishingDate, r.FUN_FACT AS funFact FROM Review r WHERE r.sku = ?1 AND r.STATUS = 1 ORDER BY r.PUBLISHING_DATE desc", nativeQuery = true)
    List<ReviewView> findReviewsBySkuSortedByDate(String sku);

    @Query(value = "SELECT r.RATING AS RATING, r.TEXT AS text, r.PUBLISHING_DATE AS publishingDate, r.FUN_FACT AS funFact, COUNT(*) AS voteNumber FROM Review r, Vote v1 WHERE r.sku = ?1 AND v1.REVIEW_ID = r.REVIEW_ID AND r.STATUS = 1 GROUP BY r.REVIEW_ID ORDER BY COUNT(*) desc, r.PUBLISHING_DATE desc", nativeQuery = true)
    List<ReviewView> findReviewsBySkuSortedByVotesAndDate(String sku);

    @Query(value = "SELECT * FROM Review r WHERE r.USER_ID = ?1", nativeQuery = true)
    Iterable<Review> findReviewsByUserId(Long userId);

    @Query(value = "SELECT * FROM Review r WHERE r.REVIEW_ID = ?1 AND r.STATUS = 1", nativeQuery = true)
    Optional<Review> findReviewById(Long reviewId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Review r WHERE r.reviewId = ?1")
    void deleteByIdIfMatch(Long reviewId);
}