package com.pixelflicks.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixelflicks.admin.catalogo.ControllerTest;
import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.pixelflicks.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.pixelflicks.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.pixelflicks.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.pixelflicks.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.pixelflicks.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.pixelflicks.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.pixelflicks.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.pixelflicks.admin.catalogo.domain.category.Category;
import com.pixelflicks.admin.catalogo.domain.category.CategoryID;
import com.pixelflicks.admin.catalogo.domain.category.CategorySearchQuery;
import com.pixelflicks.admin.catalogo.domain.exceptions.DomainException;
import com.pixelflicks.admin.catalogo.domain.exceptions.NotFoundException;
import com.pixelflicks.admin.catalogo.domain.pagination.Pagination;
import com.pixelflicks.admin.catalogo.domain.validation.Error;
import com.pixelflicks.admin.catalogo.domain.validation.handler.Notification;
import com.pixelflicks.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.pixelflicks.admin.catalogo.infrastructure.category.models.UpdateCategoryApiInput;
import io.vavr.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception{
        //given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(API.Right(CreateCategoryOutput.from("123")));

        //when
        final var request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        //then
        this.mvc.perform(request).andDo(log()).andExpectAll(
                status().isCreated(),
                header().string("Location", "/categories/123"),
                jsonPath("$.id", equalTo("123"))

        );

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnAnDomainException() throws Exception{
        //given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "A 'name' should not be null";

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedMessage))));

        //when
        final var request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        //then
        this.mvc.perform(request).andDo(log()).andExpectAll(
                status().isUnprocessableEntity(),
                header().string("Location", nullValue()),
                jsonPath("$.errors", hasSize(1)),
                jsonPath("$.errors[0].message", equalTo(expectedMessage))
        );

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }
    @Test
    public void givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnANotification() throws Exception{
        //given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "A 'name' should not be null";

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedMessage)));

        //when
        final var request = post("/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        //then
        this.mvc.perform(request).andDo(log()).andExpectAll(
                status().isUnprocessableEntity(),
                header().string("Location", nullValue()),
                jsonPath("$.errors", hasSize(1)),
                jsonPath("$.errors[0].message", equalTo(expectedMessage))
        );

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception{
        //giver
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId().getValue();

        when(getCategoryByIdUseCase.execute(any()))
                        .thenReturn(CategoryOutput.from(aCategory));

        //when
        final var request = get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        final var response = this.mvc.perform(request).andDo(log());

        //then
        verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt()))
                );

    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception{
        //given
        final var expectedErrorMessage = "Category with Id 12345 was not found";
        final var expectedId = CategoryID.from("12345");

        when(getCategoryByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        //when
        final var request = get("/categories/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(log());

        //then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception{
        //given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(API.Right(UpdateCategoryOutput.from(expectedId)));

        final var aCommand = new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);
        //when
        final var request = put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));


        final var response = this.mvc.perform(request).andDo(log());
        //then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnANotification() throws Exception{
        //given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedErrorMessage))));

        final var aCommand = new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request).andDo(log());

        //then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnAnDomainException() throws Exception{
        //given
        final var expectedId = "not-found";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with Id not-found was not found";
        final var expectedErrorCount = 1;

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var aCommand = new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request).andDo(log());

        //then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk() throws Exception{
        //giver
        final var expectedId = "1234";

        doNothing().when(deleteCategoryUseCase).execute(any());

        //when
        final var request = delete("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        final var response = this.mvc.perform(request).andDo(log());

        //then
        response.andExpect(status().isNoContent());

        verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAValidTerm_whenCallsListCategory_shouldReturnCategories() throws Exception{
        //given
        final var aCategory = Category.newCategory("Movies", null,true);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItems = List.of(CategoryListOutput.from(aCategory));
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        when(listCategoriesUseCase.execute(any())).thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        //when
        final var request = get("/categories")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(log());

        //then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aCategory.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aCategory.getName())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aCategory.getDescription())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aCategory.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aCategory.getDeletedAt().toString())));

        verify(listCategoriesUseCase,times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page()) &&
                Objects.equals(expectedPerPage, query.perPage()) &&
                Objects.equals(expectedDirection, query.direction()) &&
                Objects.equals(expectedSort, query.sort()) &&
                Objects.equals(expectedTerms, query.terms())
                ))
    }
}
