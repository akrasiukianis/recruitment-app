package com.example.recruitmentapp.entity;

import com.example.recruitmentapp.dto.JobOfferDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "job_offers")
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Category category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ManyToOne
    private User employer;

    protected JobOffer() {
    }

    public JobOffer(Category category, LocalDate startDate, LocalDate endDate, User employer) {
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.employer = employer;
    }

    public Long getId() {
        return id;
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

    public User getEmployer() {
        return employer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobOffer jobOffer = (JobOffer) o;
        return category == jobOffer.category && Objects.equals(startDate, jobOffer.startDate) && Objects.equals(endDate, jobOffer.endDate) && Objects.equals(employer, jobOffer.employer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, startDate, endDate, employer);
    }

    @Override
    public String toString() {
        return "JobOffer{" +
                "id=" + id +
                ", category=" + category +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", employer=" + employer +
                '}';
    }

    public JobOfferDto toJobOfferDto() {
        return new JobOfferDto(getCategory(), getStartDate(), getEndDate(), getEmployer().getName());
    }
}
