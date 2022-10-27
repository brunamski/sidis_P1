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
}