package com.example.projeto.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTOcat {
    public String sku;
    public String designation;

    public ProductDTOcat(@JsonProperty("sku") String sku,
                         @JsonProperty("designation") String designation) {
        this.sku = sku;
        this.designation = designation;
    }
}
