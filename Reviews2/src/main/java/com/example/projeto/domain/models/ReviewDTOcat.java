package com.example.projeto.domain.models;

import java.time.LocalDate;

public class ReviewDTOcat {

    public String sku;

    public int rating;

    public String text;

    public LocalDate publishingDate;

    public String funFact;

    protected ReviewDTOcat() {
    }

    public ReviewDTOcat(String sku, int rating, String text, LocalDate publishingDate, String funFact) {
        this.sku = sku;
        this.rating = rating;
        this.text = text;
        this.publishingDate = publishingDate;
        this.funFact = funFact;
    }
}
