package com.example.recruitmentapp;

import com.example.recruitmentapp.entity.Category;
import com.example.recruitmentapp.entity.JobOffer;
import com.example.recruitmentapp.entity.User;
import com.example.recruitmentapp.repository.JobOfferRepository;
import com.example.recruitmentapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;


@ConditionalOnProperty(
        prefix = "command.line.runner",
        value = "enabled",
        havingValue = "true",
        matchIfMissing = true)
@Configuration
public class LoadDatabase {

    @Bean
    public CommandLineRunner initDatabase(UserRepository repository, JobOfferRepository jobOfferRepository) {
        return (args) -> {
            User first = new User("peter", "xxx", "Peter");
            repository.save(first);
            User second = new User("molly", "yyy", "Molly");
            repository.save(second);

            jobOfferRepository.save(
                    new JobOffer(
                            Category.FOOD_AND_DRINKS,
                            LocalDate.of(2020, 1, 8),
                            LocalDate.of(2020, 2, 8),
                            first)
            );
            jobOfferRepository.save(
                    new JobOffer(
                            Category.COURIER,
                            LocalDate.now().plusDays(4),
                            LocalDate.now().plusDays(8),
                            second));
        };
    }
}
