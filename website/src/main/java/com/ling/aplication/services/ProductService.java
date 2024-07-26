package com.ling.aplication.services;

import com.ling.aplication.models.Product;
import com.ling.aplication.models.ProductDTO;

import com.ling.aplication.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Adds new product to repository
     *
     * @param productDTO not null
     * @throws NullPointerException if product is null
     */
    public void add(ProductDTO productDTO) {

        productRepository.addDTO(productDTO);
    }

    /**
     * Deletes a product by id if it exists,
     * otherwise ignore.
     *
     * @param id id of the product
     */
    public void delete(Long id) {

        productRepository.delete(id);
    }

    /**
     * Get product by id
     *
     * @param id id of the product
     * @return null if product is not found
     */
    public Product getProductById(Long id) {

        return productRepository.getProductById(id);
    }

    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

}
