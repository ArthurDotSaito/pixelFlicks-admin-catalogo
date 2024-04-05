package com.pixelflicks.admin.catalogo.domain.genre;

import com.pixelflicks.admin.catalogo.domain.exceptions.DomainException;
import com.pixelflicks.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallNewGenre_shouldInstantiateAGenre(){
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

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

    @Test
    public void givenValidParams_whenCallNewGenreAndValidate_shouldReceiveAError(){
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 0;
        final var expectedErrorMessage = "Name should not be null";

        final var actualGenre = Genre.newGenre(expectedName,expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () ->{
            actualGenre.validate(new ThrowsValidationHandler());
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

}
