package com.example.projeto.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class ProductDTO {

    public final Long productId;
    public final String sku;
    public final String designation;
    public final String description;
    public final AggregatedRating aggregatedRating;
    public Set<String> setOfImages = new HashSet<String>();

    public ProductDTO (@JsonProperty("productId") Long productId,
                       @JsonProperty("designation") String designation,
                       @JsonProperty("sku") String sku,
                       @JsonProperty("description") String description,
                       AggregatedRating aggregatedRating,
                       Set<String> setOfImages){
        this.productId = productId;
        this.sku = sku;
        this.description = description;
        this.designation = designation;
        this.aggregatedRating = aggregatedRating;
        this.setOfImages = setOfImages;
    }
}
