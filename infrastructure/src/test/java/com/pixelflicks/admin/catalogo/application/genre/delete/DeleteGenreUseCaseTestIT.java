package com.pixelflicks.admin.catalogo.application.genre.delete;

import com.pixelflicks.admin.catalogo.IntegrationTest;
import com.pixelflicks.admin.catalogo.domain.genre.Genre;
import com.pixelflicks.admin.catalogo.domain.genre.GenreGateway;
import com.pixelflicks.admin.catalogo.domain.genre.GenreID;
import com.pixelflicks.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class DeleteGenreUseCaseTestIT {


    @Autowired
    private DeleteGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre(){
        //given
        final var aGenre = genreGateway.create(Genre.newGenre("Ação", true));
        final var expectedId = aGenre.getId();

        Assertions.assertEquals(1, genreRepository.count());

        //when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //then
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldDoNothing(){
        //given
        final var expectedId = GenreID.from("123");
        genreGateway.create(Genre.newGenre("Ação", true));

        Assertions.assertEquals(1, genreRepository.count());
        //when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //then
        Assertions.assertEquals(1, genreRepository.count());
    }
}
