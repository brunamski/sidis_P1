package com.example.projeto.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {

    public Long productId;
    public String sku;
    public String designation;
    public String description;

    protected ProductDTO(){}

    public ProductDTO (Long productId,
                       String designation,
                       String sku,
                       String description){
        this.productId = productId;
        this.sku = sku;
        this.description = description;
        this.designation = designation;
    }
}
