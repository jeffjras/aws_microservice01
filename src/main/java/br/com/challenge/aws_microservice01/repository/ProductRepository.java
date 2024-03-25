package br.com.challenge.aws_microservice01.repository;

import br.com.challenge.aws_microservice01.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByCode(String code);
}
