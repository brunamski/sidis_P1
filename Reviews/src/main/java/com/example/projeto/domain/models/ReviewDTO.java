package com.example.projeto.domain.models;

import javax.persistence.Column;
import java.io.IOException;
import java.time.LocalDate;

public class ReviewDTO {

    public Long reviewId;

    public int rating;

    public String text;

    public LocalDate publishingDate;

    public String funFact;

    public int numberOfVotes = 0;

    public ReviewDTO(Long reviewId, int rating, String text, LocalDate publishingDate, String funFact) throws IOException {
        this.reviewId = reviewId;
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

    public LocalDate getPublishingDate(){
        return this.publishingDate;
    }
}
