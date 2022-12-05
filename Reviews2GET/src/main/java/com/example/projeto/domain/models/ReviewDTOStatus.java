package com.example.projeto.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class ReviewDTOStatus {
    public Long reviewId;

    public String sku;

    public int rating;

    public String text;

    public String publishingDate;

    public String funFact;

    public int numberOfVotes = 0;

    public Status status;

    public ReviewDTOStatus() {}

    public ReviewDTOStatus(Long reviewId, String sku, int rating, String text, String publishingDate, String funFact, Status status) {
        this.reviewId = reviewId;
        this.sku = sku;
        this.rating = rating;
        this.text = text;
        this.publishingDate = publishingDate;
        this.funFact = funFact;
        this.status = status;
    }
}
