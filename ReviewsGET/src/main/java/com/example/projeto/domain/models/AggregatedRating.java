package com.example.projeto.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AggregatedRating {
    private float average;
    private int totalRatings;
    private String five_star;
    private String four_star;
    private String three_star;
    private String two_star;
    private String one_star;

    public AggregatedRating(float average, float totalRatings, float one_star, float two_star, float three_star, float four_star, float five_star) {
        this.average = Float.isNaN(average) ? 0 : average;
        this.totalRatings = Math.round(totalRatings);
        this.five_star = Float.isNaN(five_star) ? "0%" : five_star + "%";
        this.four_star = Float.isNaN(four_star) ? "0%" : four_star + "%";
        this.three_star = Float.isNaN(three_star) ? "0%" : three_star + "%";
        this.two_star = Float.isNaN(two_star) ? "0%" : two_star + "%";
        this.one_star = Float.isNaN(one_star) ? "0%" : one_star + "%";
    }

    @Override
    public String toString() {
        return "{" +
                "'average':" + average +
                ", 'totalRatings':" + totalRatings +
                ", 'five_star':'" + five_star + '\'' +
                ", 'four_star':'" + four_star + '\'' +
                ", 'three_star':'" + three_star + '\'' +
                ", 'two_star':'" + two_star + '\'' +
                ", 'one_star':'" + one_star + '\'' +
                '}';
    }

    private AggregatedRating(){}
}
