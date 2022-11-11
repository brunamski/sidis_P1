package com.example.projeto.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReviewDTO {

    public Long reviewId;

    public String sku;

    public int rating;

    public String text;

    public String publishingDate;

    public String funFact;

    public int numberOfVotes = 0;

    public ReviewDTO(){}

    public ReviewDTO(Long reviewId, String sku, int rating, String text, String publishingDate, String funFact) {
        this.reviewId = reviewId;
        this.sku = sku;
        this.rating = rating;
        this.text = text;
        this.publishingDate = publishingDate;
        this.funFact = funFact;
    }

    public void setNumberOfVotes(int numberofvotes){
        this.numberOfVotes = numberofvotes;
    }

    public int getNumberOfVotes(){
        return this.numberOfVotes;
    }

    public String getPublishingDate(){
        return this.publishingDate;
    }
}
