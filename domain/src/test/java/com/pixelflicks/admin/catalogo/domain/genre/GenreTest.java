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
        final var expectedCategoriesSize = 0;

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategoriesSize, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
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

    @Test
    public void givenInvalidNameGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveAError(){
        final var expectedName = """
                Desde ontem a noite o deploy automatizado no Heroku deletou todas as entradas de estados estáticos nos componentes da UI.
                Com este commit, a disposição dos elementos HTML deletou todas as entradas da execução parelela de funções em multi-threads.
                AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                """;

        final var expectedIsActive = true;
        final var expectedErrorCount = 0;
        final var expectedErrorMessage = " 'Name' must be between 1 and 255 characters";

        final var actualGenre = Genre.newGenre(expectedName,expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () ->{
            actualGenre.validate(new ThrowsValidationHandler());
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

}
