package com.example.recruitmentapp.service;

import com.example.recruitmentapp.dto.JobOfferDto;
import com.example.recruitmentapp.entity.Category;
import com.example.recruitmentapp.entity.JobOffer;
import com.example.recruitmentapp.exception.UserNotFoundException;
import com.example.recruitmentapp.repository.JobOfferRepository;
import com.example.recruitmentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.recruitmentapp.specifications.JobOfferSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final UserRepository userRepository;

    @Autowired
    public JobOfferService(JobOfferRepository jobOfferRepository, UserRepository userRepository) {
        this.jobOfferRepository = jobOfferRepository;
        this.userRepository = userRepository;
    }

    public JobOffer create(JobOffer jobOffer) {
        return jobOfferRepository.save(jobOffer);
    }

    public List<JobOffer> getAll() {
        return jobOfferRepository.findAll(
                where(allCurrent())
        );
    }

    public List<JobOffer> getByCategory(Category category) {
        return jobOfferRepository.findAll(
                where(currentByCategory(category))
        );
    }

    public List<JobOffer> getByEmployerName(String name) {
        return jobOfferRepository.findAll(
                where(currentByEmployerName(name))
        );
    }

    public JobOffer convertToEntity(JobOfferDto dto) {
        String employerName = dto.getEmployer();

        return userRepository.findFirstByName(employerName)
                .map(user -> new JobOffer(
                        dto.getCategory(),
                        dto.getStartDate(),
                        dto.getEndDate(),
                        user))
                .orElseThrow(() -> new UserNotFoundException(employerName));
    }
}
