package com.example.projeto.domain.repositories;

import com.example.projeto.domain.models.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {
    @Query(value = "SELECT COUNT(*) FROM VOTE v WHERE v.REVIEW_ID = ?1", nativeQuery = true)
    int getVotesByReviewId(Long reviewId);

}