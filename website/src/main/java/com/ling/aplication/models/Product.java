package com.ling.aplication.models;

public record Product(
        Long id,
        String title,
        String description,
        int price,
        String city,
        String author) {
}
