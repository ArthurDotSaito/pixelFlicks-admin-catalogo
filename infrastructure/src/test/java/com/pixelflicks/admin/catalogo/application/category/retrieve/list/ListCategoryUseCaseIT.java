package com.pixelflicks.admin.catalogo.application.category.retrieve.list;

import com.pixelflicks.admin.catalogo.IntegrationTest;
import com.pixelflicks.admin.catalogo.domain.category.Category;
import com.pixelflicks.admin.catalogo.domain.pagination.SearchQuery;
import com.pixelflicks.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.pixelflicks.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

@IntegrationTest
public class ListCategoryUseCaseIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void mockup(){
        final var categories = Stream.of(
                Category.newCategory("Filmes", null, true),
                Category.newCategory("PixelFlix Originals", "Titulos de autoria da pixelFlix", true),
                Category.newCategory("Netflix Originals", "Titulos de autoria da Netflix", true),
                Category.newCategory("Amazon Originals", "Titulos de autoria da Amazon Prime", true),
                Category.newCategory("Documentarios", null, true),
                Category.newCategory("Kids", "Categoria para crianças", true),
                Category.newCategory("Series", null, true)
        )
                .map(CategoryJpaEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    public void givenAValidTerm_whenTermDoesNotMatchPrePersisted_shouldReturnEmptyPage(){
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "jiasdjiasdas adsa";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,Filmes",
            "net,0,10,1,1,Netflix Originals",
            "ZON,0,10,1,1,Amazon Originals",
            "KI,0,10,1,1,Kids",
            "crian,0,10,1,1,Kids",
            "da Ama,0,10,1,1,Amazon Originals",
    })
    public void givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ){
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name, asc, 0, 10, 7, 7, Amazon Originals",
            "name, desc, 0, 10, 7, 7, Series",
            "createdAt, asc, 0, 10, 7, 7, Filmes",
            "createdAt, desc, 0, 10, 7, 7, Series",
    })
    public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ){
        final var expectedTerms = "";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 2, 2, 7, Amazon Originals;Documentarios",
            "1, 2, 2, 7, Filmes;Kids",
            "2, 2, 2, 7, Netflix Originals;PixelFlix Originals",
            "3, 2, 1, 7, Series"
    })
    public void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoriesName
    ){
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (final String expectedName : expectedCategoriesName.split(";")) {
            final var actualName =  actualResult.items().get(index).name();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }
}
