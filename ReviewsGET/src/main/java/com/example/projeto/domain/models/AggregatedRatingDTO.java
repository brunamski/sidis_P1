package com.example.projeto.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;

//import java.util.HashSet;
import java.util.Set;

public class AggregatedRatingDTO {

    public final float average;
    public final int totalRatings;
    public final String five_star;
    public final String four_star;
    public final String three_star;
    public final String two_star;
    public final String one_star;

    public AggregatedRatingDTO (@JsonProperty("average") float average,
                                @JsonProperty("totalRatings") int totalRatings,
                                @JsonProperty("five_star") String five_star,
                                @JsonProperty("four_star") String four_star,
                                @JsonProperty("three_star") String three_star,
                                @JsonProperty("two_star") String two_star,
                                @JsonProperty("one_star") String one_star) {
        this.average = average;
        this.totalRatings = totalRatings;
        this.five_star = five_star;
        this.four_star = four_star;
        this.three_star = three_star;
        this.two_star = two_star;
        this.one_star = one_star;
    }
}
