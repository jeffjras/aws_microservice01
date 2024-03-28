package br.com.challenge.aws_microservice01.service;

import br.com.challenge.aws_microservice01.model.Product;
import br.com.challenge.aws_microservice01.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Component
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(
            ProductNotifier.class);
    @Value("${path.documents}")
    private String path;

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<String> uploadDocument(MultipartFile file) {
        try{
            //notNull(file, "arquivo é obrigatório!");

            String rootFile = path + "/" + file.getOriginalFilename();
            File newDocument = new File(rootFile);
            FileOutputStream fileOutputStream = new FileOutputStream(newDocument, true);

            file.getInputStream().transferTo(fileOutputStream);

            LOG.info("Arquivo importato.");

            return ResponseEntity.ok("Arquivo carregado " + file.getName());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar arquivo");
        }
    }

    public Iterable<Product> buscaPrdoductPor(String name, String code) {
        Iterable<Product> productList = productRepository.findAll();
        List<Product> products = new ArrayList<>();
        while (productList.iterator().hasNext()) {
            products.add(productList.iterator().next());
        }
        //List<Product> products = (List<Product>) productList.iterator();
        productList.forEach(products::add);
        return products.stream()
                .filter(Objects::nonNull)
                .filter(product -> product.getName().equals(name))
                .filter(product -> Objects.equals(product.getCode(), code))
                .collect(Collectors.toList());
    }

    public Optional<Product> findByCode(String code) {
        Iterable<Product> productList = productRepository.findAll();
        List<Product> products = new ArrayList<>();
        while (productList.iterator().hasNext()) {
            products.add(productList.iterator().next());
        }
        return productRepository.findByCode(code)
                .filter(Objects::nonNull)
                .filter(product -> product.getCode().equals(code));
    }

    public Iterable<Product> findAllProducts() {
        Iterable<Product> productList = productRepository.findAll();
        List<Product> products = new ArrayList<>();
        while (productList.iterator().hasNext()) {
            products.add(productList.iterator().next());
        }
        productList.forEach(products::add);
        return products.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public boolean exisitsById(long id) {
        return productRepository.existsById(id);
    }

    public Optional<Product> findByIdProduct(long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }

}
