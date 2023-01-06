package com.example.projeto.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ProductDTO {

    public Long productId;
    public String sku;
    public String designation;
    public String description;
    public Set<String> setOfImages = new HashSet<String>();

    protected ProductDTO(){}

    public ProductDTO(Long productId,
                      String designation,
                      String sku,
                      String description,
                      Set<String> setOfImages){
        this.productId = productId;
        this.sku = sku;
        this.description = description;
        this.designation = designation;
        this.setOfImages = setOfImages;
    }
}
