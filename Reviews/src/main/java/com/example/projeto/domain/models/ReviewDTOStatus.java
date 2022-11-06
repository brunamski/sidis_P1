package com.example.projeto.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;

@Getter
@Setter
public class ReviewDTOStatus {
    public Long reviewId;

    public String sku;

    public int rating;

    public String text;

    public LocalDate publishingDate;

    public String funFact;

    public int numberOfVotes = 0;

    public Status status;

    public ReviewDTOStatus(){}

    public ReviewDTOStatus(Long reviewId, String sku, int rating, String text, LocalDate publishingDate, String funFact, Status status) throws IOException {
        this.reviewId = reviewId;
        this.sku = sku;
        this.rating = rating;
        this.text = text;
        this.publishingDate = publishingDate;
        this.funFact = funFact;
        this.status = status;
    }
}
