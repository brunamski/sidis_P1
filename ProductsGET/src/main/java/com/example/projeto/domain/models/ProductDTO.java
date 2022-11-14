package com.example.projeto.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ProductDTO {

    public Long productId;
    public String sku;
    public String designation;
    public String description;
    public AggregatedRatingDTO aggregatedRating;
    public Set<String> setOfImages = new HashSet<String>();

    protected ProductDTO(){}

    public ProductDTO (@JsonProperty("productId") Long productId,
                       @JsonProperty("designation") String designation,
                       @JsonProperty("sku") String sku,
                       @JsonProperty("description") String description,
                       @JsonProperty("aggregatedRating")AggregatedRatingDTO aggregatedRating,
                       @JsonProperty("setOfImages")Set<String> setOfImages){
        this.productId = productId;
        this.sku = sku;
        this.description = description;
        this.designation = designation;
        this.aggregatedRating = aggregatedRating;
        this.setOfImages = setOfImages;
    }
}
