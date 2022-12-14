package com.example.projeto.domain.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.projeto.domain.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


public interface ProductService {

    List<ProductDTOcat> findCatalog() throws IOException;

    Optional<Product> findBySku(String sku);

    //Optional<Product> findByProductName(String name);

    Optional<ProductDTO> getDetails(final String sku) throws IOException, InterruptedException;

    Optional<ProductDTO> getProductsByProductName(final String name) throws IOException, InterruptedException;

    Optional<AggregatedRating> getProductAggregatedRating(final String sku);

    AggregatedRating getAggregatedRating(final String sku);

    void create(Product p) throws IOException;

    void createRev(Review r) throws IOException;

    void deleteRev(Long reviewId) throws IOException;

    void partialUpdate(final Review review);
}
