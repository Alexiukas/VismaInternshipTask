package com.Aleksas.demo.controller;

import com.Aleksas.demo.entity.Book;
import com.Aleksas.demo.service.JsonFileHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(LibraryController.class)
class LibraryControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void getBook() throws Exception {
        this.mockMvc.perform(get("/book")
                .param("id", "6eff8a59-7f86-44ec-8f06-05754971aee5")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.guid").value("6eff8a59-7f86-44ec-8f06-05754971aee5"));
    }

    @Test
    void getBooks() throws Exception {
        this.mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books", hasSize(5)));
    }

    @Test
    void getBooksByAuthor() throws Exception {
        this.mockMvc.perform(get("/books/filter/author/{author}", "J.K. Rowling"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books", hasSize(3)));

    }

    @Test
    void getBooksByCategory() throws Exception {
        this.mockMvc.perform(get("/books/filter/category/{category}", "Fantasy"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books", hasSize(3)));
    }

    @Test
    void getBooksByLanguage() throws Exception {
        this.mockMvc.perform(get("/books/filter/language/{language}", "Russian"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books", hasSize(1)));
    }

    @Test
    void getBooksByISBN() throws Exception {
        this.mockMvc.perform(get("/books/filter/isbn/{isbn}", "1223334444"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books", hasSize(1)));
    }

    @Test
    void getBooksByName() throws Exception {
        this.mockMvc.perform(get("/books/filter/name/{name}", "Harry Potter"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books", hasSize(1)));
    }

    @Test
    void getBooksByAvailable() throws Exception {
        this.mockMvc.perform(get("/books/filter/available/{available}", "false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books", hasSize(3)));
    }

    @Test
    void takeBook() throws Exception {
        this.mockMvc.perform(get("/take").param("name","Jonas Ponas")
                .param("guid", "6eff8a59-7f86-44ec-8f06-05754971aee5")
                .param("until", "2022-01-10"))
                .andExpect(status().isOk());
    }


    @Test
    void addBook() throws Exception {
        Book book = new Book("The martian", "Andy", "Lithuanian", "sci-fi", "2014-01-20", 1234567777);

        this.mockMvc.perform(post("/add/book").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(book)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteBookError() throws Exception {

        MvcResult result = mockMvc.perform(delete("/delete/{guid}", "2270xd7e4-5a6e-485c-ad56-94bc6bc9be07").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertNotEquals(result.getResponse().getContentAsString(), "deleted!");

    }

    @Test
    //insert correct guid
    void deleteBook() throws Exception {

        MvcResult result = mockMvc.perform(delete("/delete/{guid}", "2270d7e4-5a6e-485c-ad56-94bc6bc9be07").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertNotEquals(result.getResponse().getContentAsString(), "deleted!");

    }
}