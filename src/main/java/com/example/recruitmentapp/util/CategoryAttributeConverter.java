package com.example.recruitmentapp.util;

import com.example.recruitmentapp.entity.Category;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class CategoryAttributeConverter implements AttributeConverter<Category, String> {

    @Override
    public String convertToDatabaseColumn(Category category) {
        if (category == null) {
            return null;
        }
        return category.getName();
    }

    @Override
    public Category convertToEntityAttribute(String name) {
        if (name == null) {
            return null;
        }

        return Stream.of(Category.values())
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
