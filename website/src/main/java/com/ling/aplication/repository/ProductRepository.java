package com.ling.aplication.repository;

import java.util.*;

import com.ling.aplication.models.*;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private List<Product> products = new ArrayList<>(
            List.of(
                    new Product(1L, "Car", "Heel duur auto", 1000, "Paris", "Ik"),
                    new Product(2L, "Carersrsrsf", "Heel duur aufsdfto", 10002, "Parisd", "Iks")
            )
    );
    private Long id = 3L;


    /**
     * Adds new product to repository
     *
     * @param product not null
     * @throws NullPointerException if product is null
     */
    public void add(Product product) {
        Objects.requireNonNull(product);
        products.add(product);
    }

    public void addDTO(ProductDTO productDTO) {
        this.add(
                new Product(
                        this.id++,
                        productDTO.title(),
                        productDTO.description(),
                        productDTO.price(),
                        productDTO.city(),
                        productDTO.author())
        );
    }

    /**
     * Deletes a product by id if it exists,
     * otherwise ignore.
     *
     * @param id id of the product
     */
    public void delete(Long id) {
        products.removeIf(product -> product.id().equals(id));
    }

    /**
     * Get product by id
     *
     * @param id id of the product
     * @return null if product is not found
     */
    public Product getProductById(Long id) {

        return this.products.stream()
            .filter(p -> p.id().equals(id))
            .findFirst()
            .orElse(null);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
}
