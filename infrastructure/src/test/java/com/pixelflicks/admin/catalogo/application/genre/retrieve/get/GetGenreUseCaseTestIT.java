package com.pixelflicks.admin.catalogo.application.genre.retrieve.get;

import com.pixelflicks.admin.catalogo.IntegrationTest;
import com.pixelflicks.admin.catalogo.domain.category.Category;
import com.pixelflicks.admin.catalogo.domain.category.CategoryGateway;
import com.pixelflicks.admin.catalogo.domain.category.CategoryID;
import com.pixelflicks.admin.catalogo.domain.exceptions.NotFoundException;
import com.pixelflicks.admin.catalogo.domain.genre.Genre;
import com.pixelflicks.admin.catalogo.domain.genre.GenreGateway;
import com.pixelflicks.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
public class GetGenreUseCaseTestIT {
    @Autowired
    private GetGenreByIdUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetGenre_shouldReturnGenre(){
        //given
        final var expectedName = "Ação";
        final var expectedIsActive = true;

        final var series = categoryGateway.create(Category.newCategory("Séries", null, true));
        final var filmes =  categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var expectedCategories = List.of(
                series.getId(),
                filmes.getId()
        );

        final var aGenre = genreGateway.create(Genre.newGenre(expectedName,expectedIsActive).addCategories(expectedCategories));
        final var expectedId = aGenre.getId();

        //when
        final var actualGenre = useCase.execute(expectedId.getValue());

        //then
        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.categories().size() &&
                asString(expectedCategories).containsAll(actualGenre.categories()));
        Assertions.assertEquals(aGenre.getCreatedAt()  , actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt()  , actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt()  , actualGenre.deletedAt());
    }

    @Test
    public void givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound(){
        //given
        final var expectedId = GenreID.from("123");
        final var expectedErrorMessage = "Genre with Id 123 was not found";

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () ->
                useCase.execute(expectedId.getValue())
        );

        //then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private List<String> asString(List<CategoryID> categoriesIds){
        return categoriesIds.stream().map(CategoryID::getValue).toList();
    }
}
