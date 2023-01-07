package com.example.projeto.domain.services;

import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.ProductDTO;
import com.example.projeto.domain.repositories.ProductRepository;
import com.example.projeto.rabbitmq.ProductsCOMSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductsCOMSender productsCOMSender;

    @Override
    public Optional<Product> findBySku(final String sku) {
        return productRepository.findBySku(sku);
    }

    @Override
    public Product create(Product newProduct){ return productRepository.save(newProduct); }

    @Override
    public void create(ProductDTO p) throws IOException {
        boolean checkProduct = productIsPresent(p.getSku());
        if (checkProduct == false) {
            Product product = new Product(p.getDesignation(), p.getDescription(), p.getSku());
            productRepository.save(product);
        }
    }

    @Override
    public ProductDTO createProduct(Product newProduct) {

        boolean checkProduct = productIsPresent(newProduct.getSku());
        if (checkProduct == true) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product exists!");
        }
        final var optionalProduct = findBySku(newProduct.getSku());
        if(!optionalProduct.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product exists!");
        }
        final var product = create(newProduct);
        ProductDTO productDTO = new ProductDTO(product.getProductId(),product.getDesignation(),product.getSku(),product.getDescription(),product.getSetOfImages());
        productsCOMSender.send(productDTO);
        return productDTO;
    }

    public boolean productIsPresent(String sku) {
        final var optionalProduct = findBySku(sku);
        if (optionalProduct.isPresent()) {
            return true;
        }
        return false;
    }
}
