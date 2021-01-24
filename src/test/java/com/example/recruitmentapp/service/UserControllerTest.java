package com.example.recruitmentapp.service;

import com.example.recruitmentapp.controller.UserController;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    public void shouldReturn200WhileCreatingUser() throws Exception {

        // given
        User user = testUsers().get(0);
        when(userService.create(Mockito.any())).thenReturn(user);

        // when
        MvcResult result = mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJsonString(user)))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void shouldReturn200WhileGettingAllUsers() throws Exception {

        // given
        List<User> users = testUsers();
        when(userService.getAll()).thenReturn(users);

        // when
        MvcResult result = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(JsonUtil.toJsonString(users), response.getContentAsString());
    }

//    TODO: add tests for deleting and updating user


    private List<User> testUsers() {
        return List.of(
                new User("john", "xxx", "John"),
                new User("mary", "yyy", "Mary")
        );
    }
}

