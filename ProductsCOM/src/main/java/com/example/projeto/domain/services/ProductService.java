package com.example.projeto.domain.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.projeto.domain.models.AggregatedRatingDTO;
import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.ProductDTO;
import com.example.projeto.domain.models.ProductDTOcat;
import com.example.projeto.domain.views.CatalogView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


public interface ProductService {

    //List<ProductDTOcat> findCatalog() throws IOException, InterruptedException;
    //List<ProductDTOcat> findMyCatalog() throws IOException, InterruptedException;

    //List<ProductDTOcat> getCatalog() throws IOException, InterruptedException;

    Optional<Product> findBySku(String sku);

    //Optional<Product> findByProductName(String name);

    /**
     * Create a new Product and assign its id.
     *
     * @param newProduct
     * @return
     */

    Product create(Product newProduct);

    void addImage(String filename, String sku);

    //Optional<ProductDTO> getDetails(final String sku) throws IOException, InterruptedException;

    //Optional<ProductDTO> getProductsByProductName(final String name) throws IOException, InterruptedException;
    //Optional<ProductDTO> getMyProductsByProductName(final String name) throws IOException, InterruptedException;

    //AggregatedRatingDTO getProductAggregatedRating(final String sku) throws IOException, InterruptedException;

    AggregatedRatingDTO getAggFromReviews(final String sku) throws IOException, InterruptedException;

    ProductDTO createProduct(Product newProduct) throws IOException, InterruptedException;
}
