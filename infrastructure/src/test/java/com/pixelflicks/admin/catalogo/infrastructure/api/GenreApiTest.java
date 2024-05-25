package com.pixelflicks.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixelflicks.admin.catalogo.ControllerTest;
import com.pixelflicks.admin.catalogo.application.genre.create.CreateGenreOutput;
import com.pixelflicks.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.pixelflicks.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;

@ControllerTest(controllers = GenreAPI.class)
public class GenreApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateGenreUseCase createGenreUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() throws Exception{
        //given
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123","456");
        final var expectedIsActive = true;
        final var expectedId = "123";

        final var aCommand = new CreateGenreRequest(expectedName, expectedCategories,expectedIsActive);

        when(createGenreUseCase.execute(any()))
                .thenReturn(CreateGenreOutput.from(expectedId));

        //when
        final var aRequest = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var aResponse = this.mvc.perform(aRequest).andDo(print());

        //then
        aResponse.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/genres" + expectedId))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        verify(createGenreUseCase).execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedCategories, cmd.categories())
                && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }
}
