package com.example.projeto.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @Column(nullable = false, length = 10)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String sku;
    @Column(nullable = false, length = 50)
    private String designation;
    @Column(nullable = false, length = 2048)
    private String description;

    @Transient
    private AggregatedRatingDTO aggregatedRating;


    protected Product() {
    }

    public Product(String designation, String description, String sku) throws IOException {
        setDesignation(designation);
        setDescription(description);
        setSku(sku);
    }

    public void setDesignation(String designation) {
        if (designation.length() >= 50){
            throw new IllegalArgumentException("Ultrapassou o limite de 50 caracteres.");
        }
        else if (designation == null || designation.isBlank()) {
            throw new IllegalArgumentException("É obrigatório atribuir uma designação ao produto.");
        }
        this.designation = designation;
    }

    public void setDescription(String description) {
        if (description.length() >= 1000){
            throw new IllegalArgumentException("Ultrapassou o limite de 100 caracteres.");
        }
        else if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Não pode deixar a descrição em branco.");
        }
        this.description = description;
    }

    public void setSku(String sku) {

        if (sku.length() < 10 || sku.length() > 10){
            throw new IllegalArgumentException("O sku tem de ter 10 carateres");
        }
        else if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("É obrigatória a existência de sku em todos os produtos.");
        }
        else if (!sku.matches(".*([a-zA-Z].*[0-9]|[0-9].*[a-zA-Z]).*")){
            throw new IllegalArgumentException("O sku é alfanumérico.");
        }

        this.sku = sku;
    }
}
