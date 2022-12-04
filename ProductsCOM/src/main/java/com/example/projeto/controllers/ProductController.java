package com.example.projeto.controllers;

import com.example.projeto.domain.models.AggregatedRatingDTO;
import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.ProductDTO;
import com.example.projeto.domain.models.ProductDTOcat;
import com.example.projeto.domain.services.FileStorageService;
import com.example.projeto.domain.services.ProductService;
import com.example.projeto.rabbitmq.ProductsCOMSender;
import com.example.projeto.usermanagement.models.Role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
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
    private FileStorageService fileStorageService;

    @Autowired
    private ProductsCOMSender productsCOMSender;

    private String fanout = "products";

    @Operation(summary = "Creates a product")
    @RolesAllowed(Role.ADMIN)
    @PostMapping(value = "/admin/product")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDTO> createProduct(HttpServletRequest request, @RequestBody Product newProduct) throws IOException, InterruptedException {
        ProductDTO p = productService.createProduct(newProduct);
        productsCOMSender.send(newProduct);
        return ResponseEntity.ok().body(p);
    }

    /*
     * Handling files as subresources
     */

    /**
     * Upload a new image.
     *
     * <p>
     * code based on
     * https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example
     *
     * @param sku
     * @param file
     * @return
     */
    @Operation(summary = "Uploads an image")
    @PostMapping("/admin/product/{sku}/image")
    @RolesAllowed(Role.ADMIN)
    @ResponseStatus(HttpStatus.CREATED)
    public UploadFileResponse uploadFile( @PathVariable("sku") final String sku, @RequestParam("file") final MultipartFile file) {
        final String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        productService.addImage(fileName, sku);

        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    /**
     * Upload a set of images.
     *
     * <p>
     * code based on
     * https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example
     *
     * @param sku
     * @param files
     * @return
     */
    @Operation(summary = "Upload a set of images")
    @PostMapping("/admin/product/{sku}/images")
    @RolesAllowed(Role.ADMIN)
    @ResponseStatus(HttpStatus.CREATED)
    public List<UploadFileResponse> uploadMultipleFiles(@PathVariable("sku") final String sku, @RequestParam("files") final MultipartFile[] files) {
        return Arrays.asList(files).stream().map(f -> uploadFile(sku,f)).collect(Collectors.toList());
    }
}
