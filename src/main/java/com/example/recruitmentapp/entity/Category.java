package com.example.recruitmentapp.entity;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public enum Category {

    IT("IT"),
    FOOD_AND_DRINKS("Food & Drinks"),
    OFFICE("Office"),
    COURIER("Courier"),
    SHOP_ASSISTANT("Shop assistant");

    private final String name;
    private static final Map<String, Category> INDEX = Arrays.stream(Category.values())
            .collect(toMap(Category::name, it -> it));

    Category(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public static Category from(String category) {
        return INDEX.get(category);
    }
}
