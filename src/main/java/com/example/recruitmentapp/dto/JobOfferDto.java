package com.example.recruitmentapp.dto;

import com.example.recruitmentapp.entity.Category;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class JobOfferDto {

    private final Category category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate endDate;

    private final String employer;

    public JobOfferDto(Category category, LocalDate startDate, LocalDate endDate, String employer) {
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.employer = employer;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getEmployer() {
        return employer;
    }
}
