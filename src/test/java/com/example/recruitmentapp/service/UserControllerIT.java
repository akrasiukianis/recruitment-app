package com.example.recruitmentapp.service;

import com.example.recruitmentapp.RecruitmentAppApplication;
import com.example.recruitmentapp.entity.User;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = RecruitmentAppApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository repository;

    @After
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void postRequestShouldCreateUser() throws Exception {

        // given
        User user = testUsers().get(0);

        // when
        MvcResult result = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonString(user)))
                .andReturn();

        // then
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        List<User> users = repository.findAll();
        assertEquals(users.get(0).getLogin(), user.getLogin());
    }

    @Test
    public void getRequestShouldReturnAllUsers() throws Exception {

        // given
        List<User> users = testUsers();
        users.forEach(this::saveUserToDb);

        // when
        MvcResult result = mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(JsonUtil.toJsonString(users), response.getContentAsString());
    }

    @Test
    public void putRequestShouldUpdateUser() throws Exception {

        // given
        User oldUser = saveUserToDb(testUsers().get(0));
        User changedUser = new User(
                oldUser.getLogin(),
                "new-password",
                oldUser.getName()
        );

        // when

        MvcResult result = mvc.perform(put("/users/" + oldUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonString(changedUser)))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        List<User> users = repository.findAll();
        assertEquals(users.get(0).getPassword(), changedUser.getPassword());
    }

    @Test
    public void deleteRequestShouldRemoveUser() throws Exception {

        // given
        User user = saveUserToDb(testUsers().get(0));

        // when
        MvcResult result = mvc.perform(delete("/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(repository.findAll().isEmpty());
    }

    private User saveUserToDb(User user) {
        return repository.saveAndFlush(user);
    }

    private List<User> testUsers() {
        return List.of(
                new User("john", "xxx", "John"),
                new User("mary", "yyy", "Mary")
        );
    }

}