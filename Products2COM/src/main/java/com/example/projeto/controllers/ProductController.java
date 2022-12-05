package com.example.projeto.controllers;

import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.ProductDTO;
import com.example.projeto.domain.services.ProductService;
import com.example.projeto.rabbitmq.Products2COMSender;
import com.example.projeto.usermanagement.models.Role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Products", description = "Endpoints for products")
@RestController
@RequestMapping("api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private Products2COMSender products2COMSender;

    @Operation(summary = "Creates a product")
    @RolesAllowed(Role.ADMIN)
    @PostMapping(value = "/admin/product")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDTO> createProduct(HttpServletRequest request, @RequestBody Product newProduct) throws IOException, InterruptedException {
        ProductDTO p = productService.createProduct(newProduct);
        products2COMSender.send(newProduct);
        return ResponseEntity.ok().body(p);
    }
}
