package com.example.recruitmentapp.specifications;

import com.example.recruitmentapp.entity.Category;
import com.example.recruitmentapp.entity.JobOffer;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import static org.springframework.data.jpa.domain.Specification.where;

public class JobOfferSpecifications {

    public static Specification<JobOffer> allCurrent() {
        return where(startDateIsCurrent())
                .and(endDateIsCurrent());
    }

    public static Specification<JobOffer> currentByCategory(Category category) {
        return where(allCurrent())
                .and(getByCategory(category));
    }

    public static Specification<JobOffer> currentByEmployerName(String name) {
        return where(allCurrent())
                .and(getByEmployerName(name));
    }

    private static Specification<JobOffer> startDateIsCurrent() {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.greaterThan(root.get("startDate"), LocalDate.now());
    }

    private static Specification<JobOffer> endDateIsCurrent() {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.greaterThan(root.get("endDate"), LocalDate.now());
    }

    private static Specification<JobOffer> getByCategory(Category category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), category);
    }

    private static Specification<JobOffer> getByEmployerName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("employer").get("name"), name);
    }

}
