package com.ling.aplication.controllers;

import com.ling.aplication.models.ProductDTO;
import com.ling.aplication.services.ProductService;
import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProductController {

//    private final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @GetMapping("/")
    public String getProducts(Model model) {

        model.addAttribute("products", service.getProducts());
        return "products";
    }

    @PostMapping("/product/create")
    public String createProduct(ProductDTO product) {

//        LOGGER.info("Creating new product {}", product);
        service.add(product);
        return "redirect:/";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {

//        LOGGER.info("Deleting product by id {}", id);
        service.delete(id);
        return "redirect:/";
    }

    @GetMapping("/product/{id}")
    public String getInfo(@PathVariable Long id, Model model) {

//        LOGGER.info("New product search with id {}", id);
        var product = service.getProductById(id);

        if (product == null)
            return "404";

        model.addAttribute("product", service.getProductById(id));
        return "product-info";
    }

}
