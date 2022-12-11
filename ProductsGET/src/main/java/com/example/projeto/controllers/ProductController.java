package com.example.projeto.controllers;

import com.example.projeto.domain.models.AggregatedRating;
import com.example.projeto.domain.models.AggregatedRatingDTO;
import com.example.projeto.domain.models.ProductDTO;
import com.example.projeto.domain.models.ProductDTOcat;
import com.example.projeto.domain.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Tag(name = "Products", description = "Endpoints for products")
@RestController
@RequestMapping("api")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Operation(summary = "US01 - To obtain the catalog of products")
    @GetMapping(value = "/public/products")
    public List<ProductDTOcat> findCatalog() throws IOException {
        return productService.findCatalog();
    }

    @Operation(summary = "US02 - To obtain the details of a product")
    @GetMapping(value = "/public/product/{sku}")
    public ResponseEntity<ProductDTO> getDetails(@PathVariable(value = "sku") final String sku) throws IOException, InterruptedException {
        final var productDTO = productService.getDetails(sku)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));
        return ResponseEntity.ok().body(productDTO);
    }

    @Operation(summary = "US03 - To search the catalog of products by product name")
    @GetMapping(value = "/public/product/name/{name}")
    public ResponseEntity<ProductDTO> getProductsByProductName(@PathVariable(value = "name") final String name) throws IOException, InterruptedException {
        final var productDTO = productService.getProductsByProductName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));
        return ResponseEntity.ok().body(productDTO);
    }

    @Operation(summary = "US09 - To obtain the aggregated rating of a product")
    @GetMapping(value = "/public/product/rating/{sku}")
    public ResponseEntity<AggregatedRating> getProductAggregatedRating(@PathVariable("sku") final String sku) throws IOException, InterruptedException {
        final var aggregatedRating = productService.getProductAggregatedRating(sku)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));
        return ResponseEntity.ok().body(aggregatedRating);
    }

    @GetMapping(value = "/public/product/get/{sku}")
    public boolean productIsPresent(@PathVariable(value = "sku") final String sku) throws IOException, InterruptedException {
        final var optionalProduct = productService.findBySku(sku);
        if (optionalProduct.isPresent()) {
            return true;
        }
        else {
            return false;
        }
    }
}
