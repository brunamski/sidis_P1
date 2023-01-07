package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.ProductDTO;
import com.example.projeto.domain.services.ProductService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RPC {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private ProductService productService;

    public void helper() throws IOException {
        List<Product> productList = new ArrayList<>();
        List<ProductDTO> productDTOList = (List<ProductDTO>) amqpTemplate.convertSendAndReceive("rpc_prod", "key", "");

        for (ProductDTO p : productDTOList){
            Product product = new Product(p.getDesignation(), p.getDescription(), p.getSku());
            productList.add(product);
        }
        for (Product p: productList){
            productService.create(p);
        }
    }
}
