package com.pixelflicks.admin.catalogo.domain.genre;

import org.junit.jupiter.api.Assertions;

public class GenreTest {

    public void givenValidParams_whenCallNewGenre_shouldInstantiateAGenre(){
        final var expectedName = "Ação";
        final var expectedIsActive = true;

        final var actualGenre = Genre.newGenre(expectedName,expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertNotNull(expectedName, actualGenre.getName());
        Assertions.assertNotNull(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(expectedIsCategories, actualGenre.getCategories());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }
}
