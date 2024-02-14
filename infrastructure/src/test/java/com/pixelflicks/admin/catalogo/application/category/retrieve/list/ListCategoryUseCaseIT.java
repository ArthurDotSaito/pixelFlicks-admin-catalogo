package com.pixelflicks.admin.catalogo.application.category.retrieve.list;

import com.pixelflicks.admin.catalogo.IntegrationTest;
import com.pixelflicks.admin.catalogo.domain.category.Category;
import com.pixelflicks.admin.catalogo.domain.category.CategorySearchQuery;
import com.pixelflicks.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.pixelflicks.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
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
                Category.newCategory("Kids", null, true),
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

        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());

    }
}
