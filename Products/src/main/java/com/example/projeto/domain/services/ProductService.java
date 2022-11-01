package com.example.projeto.domain.services;

import java.util.Optional;

import com.example.projeto.domain.models.AggregatedRatingDTO;
import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.views.CatalogView;


public interface ProductService {

    Iterable<CatalogView> findCatalog();

    Optional<Product> findBySku(String sku);

    Optional<Product> findByProductName(String name);

    /**
     * Create a new Product and assign its id.
     *
     * @param newProduct
     * @return
     */

    Product create(Product newProduct);

    void addImage(String filename, String sku);
}
