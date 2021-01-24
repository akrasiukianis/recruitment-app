package com.example.recruitmentapp.util;


import com.example.recruitmentapp.entity.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements Converter<String, Category> {

    @Override
    public Category convert(String name) {
        return Category.from(name);
    }

}
