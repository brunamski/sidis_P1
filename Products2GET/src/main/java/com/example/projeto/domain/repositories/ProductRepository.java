package com.example.projeto.domain.repositories;

import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.views.CatalogView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query(value = "SELECT * FROM Product p", nativeQuery = true)
    List<Product> findCatalog();

    @Query(value = "SELECT * FROM Product p WHERE p.sku = ?1", nativeQuery = true)
    Optional<Product> findBySku(String sku);

    @Query(value = "SELECT * FROM Product p WHERE p.DESIGNATION = ?1", nativeQuery = true)
    Optional<Product> findByProductName(String designation);
}
