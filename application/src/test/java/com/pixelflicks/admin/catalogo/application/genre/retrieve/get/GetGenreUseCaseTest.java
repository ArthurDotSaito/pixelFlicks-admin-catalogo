package com.pixelflicks.admin.catalogo.application.genre.retrieve.get;

import com.pixelflicks.admin.catalogo.application.UseCaseTest;
import com.pixelflicks.admin.catalogo.domain.category.CategoryID;
import com.pixelflicks.admin.catalogo.domain.genre.Genre;
import com.pixelflicks.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class GetGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetGenre_shouldReturnGenre(){
        //given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        when(genreGateway.findById(any())).thenReturn(Optional.of(aGenre));
        //when
        final var actualGenre = useCase.execute(expectedId.getValue());

        //then
        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(asString(expectedCategories)  , actualGenre.categories());
        Assertions.assertEquals(aGenre.getCreatedAt()  , actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt()  , actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt()  , actualGenre.deletedAt());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));
    }

    private List<String> asString(List<CategoryID> categoriesIds){
        return categoriesIds.stream().map(CategoryID::getValue).toList();
    }
}
