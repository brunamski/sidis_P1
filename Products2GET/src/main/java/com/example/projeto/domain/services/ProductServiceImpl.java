package com.example.projeto.domain.services;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.repositories.ProductRepository;
import com.example.projeto.domain.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<ProductDTOcat> findCatalog() throws IOException {
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

    public Iterable<Review> findReviewsBySku(String sku) {
        return reviewRepository.findReviewsBySku(sku);
    }


    //@Override
    //public Optional<Product> findByProductName(final String name) { return productRepository.findByProductName(name); }

    @Override
    public Optional<ProductDTO> getDetails(final String sku) {
        final var optionalProduct = findBySku(sku);
        if (optionalProduct.isPresent()) {
            Product p = optionalProduct.get();
            AggregatedRating agg = getAggregatedRating(sku);
            p.setAggregatedRating(agg);
            ProductDTO productDTO = new ProductDTO(p.getProductId(),p.getDesignation(),p.getSku(),p.getDescription(),p.getAggregatedRating(),p.getSetOfImages());
            return Optional.of(productDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ProductDTO> getProductsByProductName(final String name) {
        Optional<Product> optionalProduct = productRepository.findByProductName(name);
        if (!optionalProduct.isEmpty()) {
            Product product = optionalProduct.get();
            AggregatedRating agg = getAggregatedRating(product.getSku());
            product.setAggregatedRating(agg);
            ProductDTO productDTO = new ProductDTO(product.getProductId(), product.getDesignation(), product.getSku(), product.getDescription(), product.getAggregatedRating(), product.getSetOfImages());
            return Optional.of(productDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AggregatedRating> getProductAggregatedRating(final String sku) {
        final var optionalProduct = findBySku(sku);
        if (optionalProduct.isPresent()) {
            AggregatedRating agg = getAggregatedRating(sku);
            return Optional.of(agg);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public AggregatedRating getAggregatedRating(final String sku) {

        Iterable<Review> reviews = findReviewsBySku(sku);

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

    @Override
    public void create(ProductDTO p) throws IOException {
        Product product = new Product(p.getDesignation(), p.getDescription(), p.getSku());
        productRepository.save(product);
    }

    @Override
    public void createRev(Review r) throws IOException {
        reviewRepository.save(r);
    }

    @Override
    public void deleteRev(Long reviewId) throws IOException {
        reviewRepository.deleteByIdIfMatch(reviewId);
    }

    @Override
    public void partialUpdate(final Review review) {
        // first let's check if the object exists so we don't create a new object with
        // save
        final var rev = reviewRepository.findById(review.getReviewId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found"));

        // since we got the object from the database we can check the version in memory
        // and apply the patch
        rev.applyPatch(review);

        // in the meantime some other user might have changed this object on the
        // database, so concurrency control will still be applied when we try to save
        // this updated object

        reviewRepository.save(rev);
    }
}
