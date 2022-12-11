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
    public AggregatedRating aggregatedRating;
    public Set<String> setOfImages = new HashSet<String>();

    protected ProductDTO(){}

    public ProductDTO (Long productId,
                       String designation,
                       String sku,
                       String description,
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
