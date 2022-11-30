package com.example.projeto.domain.services;

import com.example.projeto.domain.models.AggregatedRatingDTO;
import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.ProductDTO;
import com.example.projeto.domain.models.ProductDTOcat;
import com.example.projeto.domain.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Optional<Product> findBySku(final String sku) {
        return productRepository.findBySku(sku);
    }

    @Override
    public void addImage(String filename, String sku) {
        Optional<Product> optionalProduct = productRepository.findBySku(sku);

        if (!optionalProduct.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
        }
        optionalProduct.get().addImages(filename);
        productRepository.save(optionalProduct.get());
    }

    @Override
    public Product create(Product newProduct){ return productRepository.save(newProduct); }

    @Override
    public ProductDTO createProduct(Product newProduct) throws IOException, InterruptedException {

        boolean checkProduct = productIsPresent(newProduct.getSku());
        if (checkProduct == true) {
            throw new ResponseStatusException(HttpStatus.FOUND, "Product exists!");
        }
        final var optionalProduct = findBySku(newProduct.getSku());
        if(!optionalProduct.isEmpty()){
            throw new ResponseStatusException(HttpStatus.FOUND, "Product exists!");
        }
        final var product = create(newProduct);
        AggregatedRatingDTO agg = getAggFromReviews(product.getSku());
        product.setAggregatedRating(agg);
        ProductDTO productDTO = new ProductDTO(product.getProductId(),product.getDesignation(),product.getSku(),product.getDescription(),product.getAggregatedRating()
                ,product.getSetOfImages());
        return productDTO;
    }
    public boolean productIsPresent(String sku) throws IOException, InterruptedException {
        final var optionalProduct = findBySku(sku);
        if (optionalProduct.isPresent()) {
            return true;
        }
        else {
            /*String baseURL = "http://localhost:8080/api/public/product/get/" + sku;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseURL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            return Boolean.parseBoolean(response.body());*/
            return false;
        }
    }

    @Override
    public AggregatedRatingDTO getAggFromReviews(@PathVariable("sku") final String sku) throws IOException, InterruptedException {

        String baseURL = "http://localhost:8081/api/public/review/product/aggregatedrating/" + sku;

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        AggregatedRatingDTO aggregatedRatingDTO = mapper.readValue(response.body(), AggregatedRatingDTO.class);

        return aggregatedRatingDTO;
    }
}
