package com.example.projeto.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class ReviewDTOcat {

    public String sku;

    public int rating;

    public String text;

    public String publishingDate;

    public String funFact;

    protected ReviewDTOcat() {
    }

    public ReviewDTOcat(String sku, int rating, String text, String publishingDate, String funFact) {
        this.sku = sku;
        this.rating = rating;
        this.text = text;
        this.publishingDate = publishingDate;
        this.funFact = funFact;
    }
}
