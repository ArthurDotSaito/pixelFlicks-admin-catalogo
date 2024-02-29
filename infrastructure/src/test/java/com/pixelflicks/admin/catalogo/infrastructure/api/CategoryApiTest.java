package com.pixelflicks.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixelflicks.admin.catalogo.ControllerTest;
import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.pixelflicks.admin.catalogo.domain.validation.Error;
import com.pixelflicks.admin.catalogo.domain.validation.handler.Notification;
import com.pixelflicks.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception{
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(CreateCategoryOutput.from("123")));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request).andDo(MockMvcResultHandlers.log()).andExpectAll(
                MockMvcResultMatchers.status().isCreated(),
                MockMvcResultMatchers.header().string("Location", "/categories/123"),
                MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo("123"))

        );

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnAnDomainException() throws Exception{
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "A 'name' should not be null";

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedMessage))));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request).andDo(MockMvcResultHandlers.log()).andExpectAll(
                MockMvcResultMatchers.status().isUnprocessableEntity(),
                MockMvcResultMatchers.header().string("Location", Matchers.nullValue()),
                MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)),
                MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage))
        );

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }



}
