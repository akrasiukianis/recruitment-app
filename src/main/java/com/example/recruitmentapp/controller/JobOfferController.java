package com.example.recruitmentapp.controller;

import com.example.recruitmentapp.dto.JobOfferDto;
import com.example.recruitmentapp.entity.Category;
import com.example.recruitmentapp.entity.JobOffer;
import com.example.recruitmentapp.service.JobOfferService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/offers")
public class JobOfferController {

    private final JobOfferService jobOfferService;

    public JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }

    @GetMapping
    public List<JobOfferDto> getOffers() {
        return toJobOfferDtos(jobOfferService.getAll());
    }

    @GetMapping("/search/category")
    public List<JobOfferDto> getOffersByCategory(@RequestParam("category") Category category) {
        return toJobOfferDtos(jobOfferService.getByCategory(category));
    }

    @GetMapping("/search/employer")
    public List<JobOfferDto> getOffersByEmployer(@RequestParam("employer") String employerName) {
        return toJobOfferDtos(jobOfferService.getByEmployerName(employerName));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobOfferDto createOffer(@RequestBody JobOfferDto jobOffer) {
        JobOffer offer = jobOfferService.convertToEntity(jobOffer);
        return jobOfferService.create(offer).toJobOfferDto();
    }

    private List<JobOfferDto> toJobOfferDtos(List<JobOffer> jobOffers) {
        return jobOffers.stream().map(JobOffer::toJobOfferDto).collect(toList());
    }
}
