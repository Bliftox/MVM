package com.ling.aplication.models;

public record ProductDTO(
        String title,
        String description,
        int price,
        String city,
        String author) {
}
