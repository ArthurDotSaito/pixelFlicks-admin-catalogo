package com.pixelflicks.admin.catalogo.infrastructure.genre;

import com.pixelflicks.admin.catalogo.MySQLGatewayTest;
import com.pixelflicks.admin.catalogo.domain.category.Category;
import com.pixelflicks.admin.catalogo.domain.category.CategoryID;
import com.pixelflicks.admin.catalogo.domain.genre.Genre;
import com.pixelflicks.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import com.pixelflicks.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidGenre_whenCallsCreateGenre_shouldPersisGenre(){
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null,true));
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        final var actualGenre = genreGateway.create(aGenre);

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId,actualGenre.getId());
        Assertions.assertEquals(expectedName,actualGenre.getName());
        Assertions.assertEquals(expectedIsActive,actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(),actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(),actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName,persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive,persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories,persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(),persistedGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(),persistedGenre.getUpdatedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersisGenre(){
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null,true));
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        final var actualGenre = genreGateway.create(aGenre);

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId,actualGenre.getId());
        Assertions.assertEquals(expectedName,actualGenre.getName());
        Assertions.assertEquals(expectedIsActive,actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(),actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(),actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName,persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive,persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories,persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(),persistedGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(),persistedGenre.getUpdatedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }
}
