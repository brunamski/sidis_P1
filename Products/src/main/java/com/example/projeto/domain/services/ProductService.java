package com.example.projeto.domain.services;

import java.io.IOException;
import java.util.Optional;

import com.example.projeto.domain.models.AggregatedRatingDTO;
import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.ProductDTO;
import com.example.projeto.domain.views.CatalogView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


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

    ResponseEntity<ProductDTO> getDetails(final String sku) throws IOException, InterruptedException;

    ResponseEntity<ProductDTO> getProductsByProductName(final String sku) throws IOException, InterruptedException;

    AggregatedRatingDTO getProductAggregatedRating(final String sku) throws IOException, InterruptedException;

    AggregatedRatingDTO getAggFromReviews(final String sku) throws IOException, InterruptedException;

    ProductDTO createProduct(Product newProduct) throws IOException, InterruptedException;
}
