package com.pixelflicks.admin.catalogo.application.genre.update;

import com.pixelflicks.admin.catalogo.application.UseCaseTest;
import com.pixelflicks.admin.catalogo.domain.category.CategoryGateway;
import com.pixelflicks.admin.catalogo.domain.category.CategoryID;
import com.pixelflicks.admin.catalogo.domain.exceptions.NotificationException;
import com.pixelflicks.admin.catalogo.domain.genre.Genre;
import com.pixelflicks.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class UpdateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId(){
        //given
        final var aGenre = Genre.newGenre("acao", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));
        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());
        //when
        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(genreGateway, times(1)).update(argThat(aUpdatedGenre ->
                Objects.equals(expectedId, aUpdatedGenre.getId())
                && Objects.equals(expectedName, aUpdatedGenre.getName())
                && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                && Objects.isNull(aUpdatedGenre.getDeletedAt())
                && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId(){
        //given
        final var aGenre = Genre.newGenre("acao", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));
        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        Assertions.assertNull(aGenre.getDeletedAt());
        Assertions.assertTrue(aGenre.isActive());
        //when
        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(genreGateway, times(1)).update(argThat(aUpdatedGenre ->
                Objects.equals(expectedId, aUpdatedGenre.getId())
                        && Objects.equals(expectedName, aUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                        && Objects.nonNull(aUpdatedGenre.getDeletedAt())
                        && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId(){
        //given
        final var aGenre = Genre.newGenre("acao", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(CategoryID.from("123"),CategoryID.from("456"));

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));
        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));
        when(categoryGateway.existsByIds(any())).thenReturn(expectedCategories);
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());
        //when
        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).existsByIds(expectedCategories);

        verify(genreGateway, times(1)).update(argThat(aUpdatedGenre ->
                Objects.equals(expectedId, aUpdatedGenre.getId())
                        && Objects.equals(expectedName, aUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                        && Objects.isNull(aUpdatedGenre.getDeletedAt())
                        && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException(){
        //given
        final var aGenre = Genre.newGenre("acao", true);
        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "A 'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));
        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));

        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, () ->
                useCase.execute(aCommand)
        );

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(0)).existsByIds(expectedCategories);
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException(){
        //given
        final var filmes = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");

        final var aGenre = Genre.newGenre("acao", true);
        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes, series, documentarios);
        final var expectedErrorCount = 2;
        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "A 'name' should not be null";

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));
        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));
        when(categoryGateway.existsByIds(any())).thenReturn(List.of(filmes));

        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, () ->
                useCase.execute(aCommand)
        );

        //then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).existsByIds(expectedCategories);
    }


    private List<String> asString(final List<CategoryID> ids){
        return ids.stream().map(CategoryID::getValue).toList();
    }
}
