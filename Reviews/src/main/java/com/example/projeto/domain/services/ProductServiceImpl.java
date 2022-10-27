package com.example.projeto.domain.services;

import com.example.projeto.domain.models.AggregatedRating;
import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.repositories.ProductRepository;
import com.example.projeto.domain.views.CatalogView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Iterable<CatalogView> findCatalog() {
        return productRepository.findCatalog();
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
    public Product create(Product newProduct){
        return productRepository.save(newProduct);
    }

    @Override
    public AggregatedRating getProductAggregatedRating(Iterable<Review> reviews) {
        int soma = 0;
        float totalRatings = 0;
        float[][] ratingArray = new float[2][6];

        for(Review r: reviews){
            soma = soma + r.getRating();
            int rating = r.getRating();
            ratingArray[0][0]++;        //totalRatings
            ratingArray[0][rating]++;   //total of a star
        }

        totalRatings = ratingArray[0][0];
        ratingArray[1][0] = soma / totalRatings;
        ratingArray[1][1] = (ratingArray[0][1] / totalRatings) * 100;
        ratingArray[1][2] = (ratingArray[0][2] / totalRatings) * 100;
        ratingArray[1][3] = (ratingArray[0][3] / totalRatings) * 100;
        ratingArray[1][4] = (ratingArray[0][4] / totalRatings) * 100;
        ratingArray[1][5] = (ratingArray[0][5] / totalRatings) * 100;

        AggregatedRating aggregatedRating = new AggregatedRating(ratingArray[1][0], ratingArray[0][0], ratingArray[1][1], ratingArray[1][2], ratingArray[1][3], ratingArray[1][4], ratingArray[1][5]);
        return aggregatedRating;
    }
}