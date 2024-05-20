package com.pixelflicks.admin.catalogo.infrastructure.genre;

import com.pixelflicks.admin.catalogo.MySQLGatewayTest;
import com.pixelflicks.admin.catalogo.domain.category.Category;
import com.pixelflicks.admin.catalogo.domain.category.CategoryID;
import com.pixelflicks.admin.catalogo.domain.genre.Genre;
import com.pixelflicks.admin.catalogo.domain.genre.GenreID;
import com.pixelflicks.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import com.pixelflicks.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.pixelflicks.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsUpdateWithCategories_shouldPersisGenre(){
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null,true));
        final var series = categoryGateway.create(Category.newCategory("Séries", null,true));
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var aGenre = Genre.newGenre("ac", expectedIsActive);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals("ac", aGenre.getName());
        Assertions.assertEquals(0, aGenre.getCategories().size());

        final var actualGenre = genreGateway.update(Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId,actualGenre.getId());
        Assertions.assertEquals(expectedName,actualGenre.getName());
        Assertions.assertEquals(expectedIsActive,actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(),actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName,persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive,persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories,persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(),persistedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithCategories_whenCallsUpdateCleaningCategories_shouldPersisGenre(){
        final var filmes =
                categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var series =
                categoryGateway.create(Category.newCategory("Séries", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre("ac", expectedIsActive);
        aGenre.addCategories(List.of(filmes.getId(), series.getId()));

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals("ac", aGenre.getName());
        Assertions.assertEquals(2, aGenre.getCategories().size());

        final var actualGenre = genreGateway.update(
                Genre.with(aGenre)
                        .update(expectedName, expectedIsActive, expectedCategories)
        );

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId,actualGenre.getId());
        Assertions.assertEquals(expectedName,actualGenre.getName());
        Assertions.assertEquals(expectedIsActive,actualGenre.isActive());
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName,persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive,persistedGenre.isActive());
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(persistedGenre.getCategoryIDs()));
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }


    @Test
    public void givenAValidInactiveGenre_whenCallsUpdateToActivate_shouldPersisGenre(){
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, false);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertFalse(aGenre.isActive());
        Assertions.assertNotNull(aGenre.getDeletedAt());

        final var actualGenre = genreGateway.update(Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId,actualGenre.getId());
        Assertions.assertEquals(expectedName,actualGenre.getName());
        Assertions.assertEquals(expectedIsActive,actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(),actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName,persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive,persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories,persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(),persistedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidActiveGenre_whenCallsUpdateToInactivate_shouldPersisGenre(){
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, true);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());

        final var actualGenre = genreGateway.update(Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId,actualGenre.getId());
        Assertions.assertEquals(expectedName,actualGenre.getName());
        Assertions.assertEquals(expectedIsActive,actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(),actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName,persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive,persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories,persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(),persistedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertNotNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAPrePersisteddGenre_whenCallsDeleteById_shouldReturnOk(){
        //given
        final var aGenre = Genre.newGenre("Ação", true);
        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        Assertions.assertEquals(1, genreRepository.count());
        //when
        genreGateway.deleteById(aGenre.getId());

        //then
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAInvalidGenre_whenCallsDeleteById_shouldReturnOk(){
        //given
        Assertions.assertEquals(0, genreRepository.count());
        //when
        genreGateway.deleteById(GenreID.from("123"));

        //then
        Assertions.assertEquals(0, genreRepository.count());
    }

    private List<CategoryID> sorted(final List<CategoryID> expectedCategories){
        return expectedCategories.stream().sorted(Comparator.comparing(CategoryID::getValue)).toList();
    }

    @Test
    public void givenAPrePersistedGenre_whenCallsFindById_shouldReturnGenre(){
        //given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null,true));
        final var series = categoryGateway.create(Category.newCategory("Series", null,true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName,expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals(1, genreRepository.count());

        //when
        final var actualGenre = genreGateway.findById(expectedId).get();

        //then
        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidGenreId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var expectedId = GenreID.from("123");

        Assertions.assertEquals(0, genreRepository.count());

        // when
        final var actualGenre = genreGateway.findById(expectedId);

        // then
        Assertions.assertTrue(actualGenre.isEmpty());
    }

}
