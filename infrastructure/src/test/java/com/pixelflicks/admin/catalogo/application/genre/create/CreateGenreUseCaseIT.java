package com.pixelflicks.admin.catalogo.application.genre.create;

import com.pixelflicks.admin.catalogo.IntegrationTest;
import com.pixelflicks.admin.catalogo.domain.category.Category;
import com.pixelflicks.admin.catalogo.domain.category.CategoryGateway;
import com.pixelflicks.admin.catalogo.domain.category.CategoryID;
import com.pixelflicks.admin.catalogo.domain.exceptions.NotificationException;
import com.pixelflicks.admin.catalogo.domain.genre.GenreGateway;
import com.pixelflicks.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateGenreUseCaseIT {

    @Autowired
    private CreateGenreUseCase useCase;

    @SpyBean
    private CategoryGateway categoryGateway;

    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId(){
        //given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes",null, true));
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(filmes.getId());

        final var aCommand = CreateGenreCommand.with(expectedName,expectedIsActive, asString(expectedCategories));

        //when
        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size()
        && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
    }

    @Test
    public void givenAValidCommandWithoutCategories_whenCallsCreateGenre_shouldReturnGenreId(){
        //given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.with(expectedName,expectedIsActive, asString(expectedCategories));

        //when
        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
    }

    @Test
    public void givenAValidCommandWithInactive_whenCallsCreateGenre_shouldReturnGenreId(){
        //given
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.with(expectedName,expectedIsActive, asString(expectedCategories));
        //when
        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException(){
        //given
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "A 'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand = CreateGenreCommand.with(expectedName,expectedIsActive, asString(expectedCategories));

        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, () ->{
            useCase.execute(aCommand);
        });

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException(){
        //given
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "A 'name' should not be null";
        final var expectedErrorCount = 1;


        final var aCommand = CreateGenreCommand.with(expectedName,expectedIsActive, asString(expectedCategories));

        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, () ->{
            useCase.execute(aCommand);
        });

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }
    
    @Test
    public void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExist_shouldReturnDomainException(){
        //given
        final var series = categoryGateway.create(Category.newCategory("Séries",null, true));

        final var expectedName = " ";
        final var expectedIsActive = true;
        final var filmes = CategoryID.from("123");
        final var documentarios = CategoryID.from("789");
        final var expectedCategories = List.of(filmes, series.getId(), documentarios);
        final var expectedErrorMessageOne = "Some categories could not be found: 123, 789";
        final var expectedErrorMessageTwo = "A 'name' should not be empty";
        final var expectedErrorCount = 2;

        final var aCommand = CreateGenreCommand.with(expectedName,expectedIsActive, asString(expectedCategories));

        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, () ->{
            useCase.execute(aCommand);
        });

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
    }

    private List<String> asString(final List<CategoryID> categoriesIds){
        return categoriesIds.stream().map(CategoryID::getValue).toList();
    }
}
