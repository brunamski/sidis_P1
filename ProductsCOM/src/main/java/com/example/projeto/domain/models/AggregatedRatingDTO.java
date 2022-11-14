package com.example.projeto.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AggregatedRatingDTO {
    public float average;
    public int totalRatings;
    public String five_star;
    public String four_star;
    public String three_star;
    public String two_star;
    public String one_star;

    public AggregatedRatingDTO(float average, float totalRatings, float one_star, float two_star, float three_star, float four_star, float five_star) {
        this.average = Float.isNaN(average) ? 0 : average;
        this.totalRatings = Math.round(totalRatings);
        this.five_star = Float.isNaN(five_star) ? "0%" : five_star + "%";
        this.four_star = Float.isNaN(four_star) ? "0%" : four_star + "%";
        this.three_star = Float.isNaN(three_star) ? "0%" : three_star + "%";
        this.two_star = Float.isNaN(two_star) ? "0%" : two_star + "%";
        this.one_star = Float.isNaN(one_star) ? "0%" : one_star + "%";
    }

    private AggregatedRatingDTO(){}
}
