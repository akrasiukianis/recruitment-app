package com.example.recruitmentapp.service;

import com.example.recruitmentapp.RecruitmentAppApplication;
import com.example.recruitmentapp.dto.JobOfferDto;
import com.example.recruitmentapp.entity.Category;
import com.example.recruitmentapp.entity.JobOffer;
import com.example.recruitmentapp.entity.User;
import com.example.recruitmentapp.repository.JobOfferRepository;
import com.example.recruitmentapp.repository.UserRepository;
import com.example.recruitmentapp.service.util.JsonUtil;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = RecruitmentAppApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class JobOfferControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @After
    public void resetDb() {
        jobOfferRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getRequestShouldReturnAllCurrentOffers() throws Exception {

        // given
        List<JobOffer> offers = currentJobOffers();
        offers.forEach(this::saveOfferToDb);

        // when
        MvcResult result = mvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(JsonUtil.toJsonString(toJobOffersDtos(offers)), response.getContentAsString());
    }

    @Test
    public void getRequestShouldReturnNoOffersWhenTheyAreObsolete() throws Exception {

        // given
        JobOffer offer = obsoleteOffer();
        saveOfferToDb(offer);

        // when
        MvcResult result = mvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(JsonUtil.toJsonString(List.of()), response.getContentAsString());
    }

    @Test
    public void getRequestShouldReturnAllUsersByEmployer() throws Exception {

        // given
        String employerName = "Mary";
        List<JobOffer> offers = currentJobOffers();
        offers.forEach(this::saveOfferToDb);

        // when
        MvcResult result = mvc.perform(get("/offers/search/employer")
                .contentType(MediaType.APPLICATION_JSON)
                .param("employer", employerName))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        List<JobOfferDto> actualOffers = offers.stream()
                .filter(jobOffer -> jobOffer.getEmployer().getName().equals(employerName))
                .map(JobOffer::toJobOfferDto)
                .collect(toList());
        assertEquals(JsonUtil.toJsonString(actualOffers), response.getContentAsString());
    }

    @Test
    public void getRequestShouldReturnAllUsersByCategory() throws Exception {

        // given
        Category category = Category.FOOD_AND_DRINKS;
        List<JobOffer> offers = currentJobOffers();
        offers.forEach(this::saveOfferToDb);

        // when
        MvcResult result = mvc.perform(get("/offers/search/category")
                .contentType(MediaType.APPLICATION_JSON)
                .param("category", category.name()))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        List<JobOfferDto> actualOffers = offers.stream()
                .filter(jobOffer -> jobOffer.getCategory().equals(category))
                .map(JobOffer::toJobOfferDto)
                .collect(toList());
        assertEquals(JsonUtil.toJsonString(actualOffers), response.getContentAsString());
    }

    @Test
    public void postRequestShouldCreateJob() throws Exception {

        // given
        List<JobOffer> offers = currentJobOffers();
        JobOfferDto offerDto = offers.get(0).toJobOfferDto();

        // when
        MvcResult result = mvc.perform(post("/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonString(offerDto)))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(JsonUtil.toJsonString(offerDto), response.getContentAsString());
    }

    private List<JobOffer> currentJobOffers() {

        User firstEmployer = saveUserToDb(new User("john", "xxx", "John"));
        User secondEmployer = saveUserToDb(new User("mary", "yyy", "Mary"));
        User thirdEmployer = saveUserToDb(new User("peter", "zzz", "Peter"));

        return List.of(
                currentOffer(firstEmployer, Category.FOOD_AND_DRINKS),
                currentOffer(secondEmployer, Category.COURIER),
                currentOffer(secondEmployer, Category.SHOP_ASSISTANT),
                currentOffer(thirdEmployer, Category.FOOD_AND_DRINKS)
        );
    }

    private User saveUserToDb(User user) {
        return userRepository.saveAndFlush(user);
    }

    private JobOffer currentOffer(User employer, Category category) {
        return new JobOffer(
                category,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                employer
        );
    }

    private JobOffer obsoleteOffer() {
        User employer = saveUserToDb(new User("john", "xxx", "John"));
        return new JobOffer(
                Category.FOOD_AND_DRINKS,
                LocalDate.of(2020, 1, 8),
                LocalDate.of(2020, 2, 8),
                employer
        );
    }

    private void saveOfferToDb(JobOffer offer) {
        jobOfferRepository.saveAndFlush(offer);
    }

    private List<JobOfferDto> toJobOffersDtos(List<JobOffer> offers) {
        return offers.stream()
                .map(JobOffer::toJobOfferDto)
                .collect(Collectors.toUnmodifiableList());
    }

}