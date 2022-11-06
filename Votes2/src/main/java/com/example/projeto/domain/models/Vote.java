package com.example.projeto.domain.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    private Long userId;

    private Long reviewId;

    @Column(nullable = false)
    private boolean vote;

    @Column(nullable = false, length = 1000)
    private String reason;

    protected Vote() {}

    public Vote(boolean vote, String reason) {
        this.vote = vote;
        this.reason = reason;
    }
}
