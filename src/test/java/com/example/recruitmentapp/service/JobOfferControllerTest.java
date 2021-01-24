package com.example.recruitmentapp.service;

import com.example.recruitmentapp.controller.JobOfferController;
import com.example.recruitmentapp.dto.JobOfferDto;
import com.example.recruitmentapp.entity.Category;
import com.example.recruitmentapp.entity.JobOffer;
import com.example.recruitmentapp.entity.User;
import com.example.recruitmentapp.service.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@WebMvcTest(value = JobOfferController.class)
public class JobOfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobOfferService jobOfferService;


    @Test
    public void shouldReturn200WhileFindingAllOffers() throws Exception {

        // given
        List<JobOffer> offers = testOffers();
        when(jobOfferService.getAll()).thenReturn(offers);

        // when
        MvcResult result = mockMvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        List<JobOfferDto> expectedOffers = toJobOffersDtos(offers);
        assertEquals(JsonUtil.toJsonString(expectedOffers), response.getContentAsString());
    }

    @Test
    public void shouldReturn200WhileFindingOffersByCategory() throws Exception {

        // given
        Category category = Category.IT;
        List<JobOffer> offers = testOffers()
                .stream()
                .filter(jobOffer -> jobOffer.getCategory().equals(category))
                .collect(toList());
        when(jobOfferService.getByCategory(category)).thenReturn(offers);

        // when
        MvcResult result = mockMvc.perform(get("/offers/search/category")
                .contentType(MediaType.APPLICATION_JSON)
                .param("category", category.getName()))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        List<JobOfferDto> expectedOffers = toJobOffersDtos(offers);
        assertEquals(JsonUtil.toJsonString(expectedOffers), response.getContentAsString());
    }

    @Test
    public void shouldReturn200WhileFindingOffersByEmployerName() throws Exception {

        // given
        String employer = "Jack";
        List<JobOffer> offers = testOffers()
                .stream()
                .filter(jobOffer -> jobOffer.getEmployer().getName().equals(employer))
                .collect(toList());
        when(jobOfferService.getByEmployerName(employer)).thenReturn(offers);

        // when
        MvcResult result = mockMvc.perform(
                get("/offers/search/employer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("employer", employer))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        List<JobOfferDto> expectedOffers = toJobOffersDtos(offers);
        assertEquals(JsonUtil.toJsonString(expectedOffers), response.getContentAsString());
    }

    @Test
    public void shouldReturn200WhileCreatingAJobOffer() throws Exception {

        // given
        JobOffer offer = testOffers().get(0);
        when(jobOfferService.create(Mockito.any())).thenReturn(offer);

        // when
        MvcResult result = mockMvc.perform(post("/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonString(offer.toJobOfferDto())))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        JobOfferDto expectedOffer = offer.toJobOfferDto();
        assertEquals(JsonUtil.toJsonString(expectedOffer), response.getContentAsString());
    }


    private List<User> testUsers() {
        return List.of(
                new User("Jack", "xxx", "Jack"),
                new User("Chloe", "yyy", "Chloe")
        );
    }

    private List<JobOffer> testOffers() {
        List<User> users = testUsers();
        return List.of(
                new JobOffer(
                        Category.FOOD_AND_DRINKS,
                        LocalDate.now(),
                        LocalDate.now(),
                        users.get(0)
                ),
                new JobOffer(Category.COURIER,
                        LocalDate.now(),
                        LocalDate.now(),
                        users.get(1)
                ),
                new JobOffer(
                        Category.IT,
                        LocalDate.now(),
                        LocalDate.now(),
                        users.get(1)
                )
        );
    }

    private List<JobOfferDto> toJobOffersDtos(List<JobOffer> offers) {
        return offers.stream()
                .map(JobOffer::toJobOfferDto)
                .collect(Collectors.toUnmodifiableList());
    }
}

