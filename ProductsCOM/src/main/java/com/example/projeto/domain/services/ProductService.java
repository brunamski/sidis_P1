package com.example.projeto.domain.services;

import java.io.IOException;
import java.util.Optional;

import com.example.projeto.domain.models.AggregatedRatingDTO;
import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.ProductDTO;

public interface ProductService {

    Optional<Product> findBySku(String sku);

    /**
     * Create a new Product and assign its id.
     *
     * @param newProduct
     * @return
     */

    Product create(Product newProduct);

    AggregatedRatingDTO getAggFromReviews(final String sku) throws IOException, InterruptedException;

    ProductDTO createProduct(Product newProduct) throws IOException, InterruptedException;
}
