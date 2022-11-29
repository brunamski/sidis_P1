package com.example.projeto.domain.services;

import com.example.projeto.domain.models.AggregatedRatingDTO;
import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.ProductDTO;
import com.example.projeto.domain.models.ProductDTOcat;
import com.example.projeto.domain.repositories.ProductRepository;
import com.example.projeto.domain.views.CatalogView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTOcat> findCatalog() throws IOException, InterruptedException {
        Iterable<Product> products = productRepository.findCatalog();
        List<ProductDTOcat> productDTOcatList = new ArrayList();
        for (Product p : products) {
            ProductDTOcat productDTOcat = new ProductDTOcat(p.getSku(), p.getDesignation());
            productDTOcatList.add(productDTOcat);
        }
        List<ProductDTOcat> productDTOcats = getCatalog();
        for (ProductDTOcat p : productDTOcats) {
            productDTOcatList.add(p);
        }
        return productDTOcatList;
    }
    public List<ProductDTOcat> findMyCatalog() throws IOException, InterruptedException {
        Iterable<Product> products = productRepository.findCatalog();
        List<ProductDTOcat> productDTOcatList = new ArrayList();
        for (Product p : products) {
            ProductDTOcat productDTOcat = new ProductDTOcat(p.getSku(), p.getDesignation());
            productDTOcatList.add(productDTOcat);
        }
        return productDTOcatList;
    }

    @Override
    public Optional<Product> findBySku(final String sku) {
        return productRepository.findBySku(sku);
    }

    @Override
    public Optional<Product> findByProductName(final String name) { return productRepository.findByProductName(name); }

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


    public boolean checkProduct(String sku) throws IOException, InterruptedException {
        final var optionalProduct = findBySku(sku);
        if (optionalProduct.isPresent()) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean productIsPresent(String sku) throws IOException, InterruptedException {
        final var optionalProduct = findBySku(sku);
        if (optionalProduct.isPresent()) {
            return true;
        }
        else {
            String baseURL = "http://localhost:8080/api/public/product/get/" + sku;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseURL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            return Boolean.parseBoolean(response.body());
        }


    }

    @Override
    public Optional<ProductDTO> getDetails(final String sku) throws IOException, InterruptedException {
        final var optionalProduct = findBySku(sku);
        if (optionalProduct.isPresent()) {
            Product p = optionalProduct.get();
            AggregatedRatingDTO agg = getAggFromReviews(sku);
            p.setAggregatedRating(agg);
            ProductDTO productDTO = new ProductDTO(p.getProductId(),p.getDesignation(),p.getSku(),p.getDescription(),p.getAggregatedRating(),p.getSetOfImages());
            return Optional.of(productDTO);
        } else {
            String baseURL = "http://localhost:8080/api/public/product/" + sku;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseURL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();

            if(response.statusCode() != 200) {
                return Optional.empty();
            }

            ProductDTO productDTO = mapper.readValue(response.body(), ProductDTO.class);

            return Optional.of(productDTO);
        }
    }

    @Override
    public Optional<ProductDTO> getProductsByProductName(final String name) throws IOException, InterruptedException {
        Optional<Product> optionalProduct = productRepository.findByProductName(name);
        if (!optionalProduct.isEmpty()) {
            Product product = optionalProduct.get();
            AggregatedRatingDTO agg = getAggFromReviews(product.getSku());
            product.setAggregatedRating(agg);
            ProductDTO productDTO = new ProductDTO(product.getProductId(), product.getDesignation(), product.getSku(), product.getDescription(), product.getAggregatedRating(), product.getSetOfImages());
            return Optional.of(productDTO);
        } else {
            String baseURL = "http://localhost:8080/api/public/product/name/" + name;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseURL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();

            if(response.statusCode() != 200) {
                return null;
            }

            ProductDTO productDTO = mapper.readValue(response.body(), ProductDTO.class);

            return Optional.of(productDTO);
        }
    }

    @Override
    public Optional<ProductDTO> getMyProductsByProductName(final String name) throws IOException, InterruptedException {
        List<ProductDTO> productDTOList = new ArrayList();
        Optional<Product> optionalProduct = productRepository.findByProductName(name);
        if (!optionalProduct.isEmpty()) {
            Product product = optionalProduct.get();
            AggregatedRatingDTO agg = getAggFromReviews(product.getSku());
            product.setAggregatedRating(agg);
            ProductDTO productDTO = new ProductDTO(product.getProductId(), product.getDesignation(), product.getSku(), product.getDescription(), product.getAggregatedRating(), product.getSetOfImages());
            productDTOList.add(productDTO);
            return Optional.of(productDTO);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


    @Override
    public List<ProductDTOcat> getCatalog() throws IOException, InterruptedException {

        String baseURL = "http://localhost:8080/api/public/product";

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        List<ProductDTOcat> productDTOcats = mapper.readValue(response.body(), new TypeReference<List<ProductDTOcat>>() {});

        return productDTOcats;
    }

    @Override
    public AggregatedRatingDTO getProductAggregatedRating(final String sku) throws IOException, InterruptedException  {
        final var optionalProduct = findBySku(sku);
        if (optionalProduct.isPresent()) {
            Product p = optionalProduct.get();
            AggregatedRatingDTO agg = getAggFromReviews(p.getSku());
            return agg;
        } else {
            AggregatedRatingDTO agg = getAggFromReviews(sku);
            return agg;
        }
    }

    @Override
    public AggregatedRatingDTO getAggFromReviews(@PathVariable("sku") final String sku) throws IOException, InterruptedException {

        String baseURL = "http://localhost:8080/api/public/review/product/aggregatedrating/" + sku;

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
