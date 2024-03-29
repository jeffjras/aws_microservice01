package br.com.challenge.aws_microservice01.controller;

import br.com.challenge.aws_microservice01.enums.EventType;
import br.com.challenge.aws_microservice01.model.Product;
import br.com.challenge.aws_microservice01.service.ProductNotifier;
import br.com.challenge.aws_microservice01.service.ProductPublisher;
import br.com.challenge.aws_microservice01.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.lang.String.format;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger LOG = LoggerFactory.getLogger(
            ProductNotifier.class);

    private ProductPublisher productPublisher;

    private ProductService productService;

    @Autowired
    public ProductController(ProductPublisher productPublisher, ProductService productService) {
        this.productPublisher = productPublisher;
        this.productService = productService;
    }

    @Operation(summary = "Make upload files", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload file is done successfully"),
            @ApiResponse(responseCode = "422", description = "Error with invalid data for request"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Error processing upload file"),
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadDocuments(@RequestPart MultipartFile file) {
        LOG.info(format("Upload file %s started!", file.getOriginalFilename()));

        return productService.uploadDocument(file);

    }

    @GetMapping
    public Iterable<Product> findAll() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable long id) {
        Optional<Product> optProduct = productService.findByIdProduct(id);
        if (optProduct.isPresent()) {
            return new ResponseEntity<Product>(optProduct.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(
            @RequestBody Product product) {
        Product productCreated = productService.saveProduct(product);

        productPublisher.publishProductEvent(productCreated, EventType.PRODUCT_CREATED, "matilde");

        return new ResponseEntity<Product>(productCreated,
                HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Product> updateProduct(
            @RequestBody Product product, @PathVariable("id") long id) {
        if (productService.exisitsById(id)) {
            product.setId(id);

            Product productUpdated = productService.saveProduct(product);

            productPublisher.publishProductEvent(productUpdated, EventType.PRODUCT_UPDATE, "doralice");

            return new ResponseEntity<Product>(productUpdated,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") long id) {
        Optional<Product> optProduct = productService.findByIdProduct(id);
        if (optProduct.isPresent()) {
            Product product = optProduct.get();

            productService.delete(product);

            productPublisher.publishProductEvent(product, EventType.PRODUCT_DELETED, "hannah");

            return new ResponseEntity<Product>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/bycode")
    public ResponseEntity<Product> findByCode(@RequestParam String code) {
        Optional<Product> optProduct = productService.findByCode(code);
        if (optProduct.isPresent()) {
            return new ResponseEntity<Product>(optProduct.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }














}
