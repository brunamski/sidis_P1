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

    List<ProductDTOcat> findCatalog() throws IOException;

    Optional<Product> findBySku(String sku);

    //Optional<Product> findByProductName(String name);

    Optional<ProductDTO> getDetails(final String sku) throws IOException, InterruptedException;

    Optional<ProductDTO> getProductsByProductName(final String name) throws IOException, InterruptedException;

    Optional<AggregatedRatingDTO> getProductAggregatedRating(final String sku) throws IOException, InterruptedException;

    AggregatedRatingDTO getAggFromReviews(final String sku) throws IOException, InterruptedException;
}
