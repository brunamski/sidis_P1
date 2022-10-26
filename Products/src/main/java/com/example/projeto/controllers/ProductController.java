package com.example.projeto.controllers;

import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.ProductDTO;
import com.example.projeto.domain.services.FileStorageService;
import com.example.projeto.domain.services.ProductService;
import com.example.projeto.domain.views.CatalogView;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Products", description = "Endpoints for products")
@RestController
@RequestMapping("api")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private FileStorageService fileStorageService;

    @Operation(summary = "US01 - To obtain the catalog of products")
    @GetMapping(value = "/public/product")
    public Iterable<CatalogView> findCatalog() {
        return productService.findCatalog();
    }

    @Operation(summary = "US02 - To obtain the details of a product")
    @GetMapping(value = "/public/product/{sku}")
    public ResponseEntity<ProductDTO> getDetails(@PathVariable(value = "sku") final String sku) throws IOException, InterruptedException {
        final var optionalProduct = productService.findBySku(sku);
        Product pr;
        if (optionalProduct.isPresent()) {
            Product p = optionalProduct.get();
            //Iterable<Review> reviews = reviewService.findReviewsBySku(sku);
            //AggregatedRating agg = productService.getProductAggregatedRating(reviews);
            //p.setAggregatedRating(agg);
            ProductDTO productDTO = new ProductDTO(p.getProductId(),p.getDesignation(),p.getSku(),p.getDescription(),p.getSetOfImages());
            return ResponseEntity.ok().body(productDTO);
        } else {
            String baseURL = "http://localhost:8082/api/public/product/" + sku;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseURL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();


            Product p = mapper.readValue(response.body(), Product.class);
            ProductDTO productDTO = new ProductDTO(p.getProductId(),p.getDesignation(),p.getSku(),p.getDescription(),//p.getAggregatedRating(),
                    p.getSetOfImages());

            return ResponseEntity.ok().body(productDTO);
        }

    }

    @Operation(summary = "US03 - To search the catalog of products by product name")
    @GetMapping(value = "/public/product/name/{name}")
    public ResponseEntity<Product> getProductsByProductName(@PathVariable("name") final String name) {
        final var product = productService.findByProductName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));
        //Iterable<Review> reviews = reviewService.findReviewsBySku(product.getSku());
        //AggregatedRating agg = productService.getProductAggregatedRating(reviews);
        //product.setAggregatedRating(agg);
        return ResponseEntity.ok().body(product);
    }

    @Operation(summary = "US03 - To search the catalog of products by bar code")
    @GetMapping(value = "/public/product/bar_code/{sku}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> getProductsByBarCode(@PathVariable("sku") final String sku) throws Exception{
        BufferedImage image = fileStorageService.getBarcode(sku);
        return ResponseEntity.ok().body(image);
    }

    @Operation(summary = "US09 - To obtain the aggregated rating of a product")
    @GetMapping(value = "/public/product/rating/{sku}")
    public ResponseEntity<Product> getProductAggregatedRating(@PathVariable("sku") final String sku) {
        Optional<Product> optionalProduct = productService.findBySku(sku);
        if (optionalProduct.isPresent()) {
            Product p = optionalProduct.get();
            //Iterable<Review> reviews = reviewService.findReviewsBySku(sku);
            //AggregatedRating agg = productService.getProductAggregatedRating(reviews);
            //p.setAggregatedRating(agg);
            return ResponseEntity.ok().body(p);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found!");
    }

    @Operation(summary = "Creates a product")
    @PostMapping(value = "/admin/product")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> create(@RequestBody Product newProduct) {
        final var product = productService.create(newProduct);
        return ResponseEntity.ok().body(product);
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
    @ResponseStatus(HttpStatus.CREATED)
    public List<UploadFileResponse> uploadMultipleFiles(@PathVariable("sku") final String sku, @RequestParam("files") final MultipartFile[] files) {
        return Arrays.asList(files).stream().map(f -> uploadFile(sku,f)).collect(Collectors.toList());
    }

    /**
     *
     * <p>
     * code based on
     * https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example
     *
     * @param fileName
     * @param request
     * @return
     */
    @Operation(summary = "Gets an image")
    @GetMapping("/public/product/{sku}/images/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable final String fileName,
                                                 final HttpServletRequest request) {
        // Load file as Resource
        final Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (final IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    public ResponseEntity<Product> getAggFromReviews(@PathVariable(value = "sku") final String sku) throws IOException, InterruptedException {

        String baseURL = "http://localhost:8081/api/public/review/product/aggregatedRating/" + sku;

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        Product p = mapper.readValue(response.body(), Product.class);

        return ResponseEntity.ok().body(p);
    }
}
