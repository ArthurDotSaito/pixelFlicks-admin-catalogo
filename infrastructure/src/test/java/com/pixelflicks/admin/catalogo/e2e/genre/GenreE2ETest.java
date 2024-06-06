package com.pixelflicks.admin.catalogo.e2e.genre;

import com.pixelflicks.admin.catalogo.E2ETest;
import com.pixelflicks.admin.catalogo.domain.category.CategoryID;
import com.pixelflicks.admin.catalogo.domain.genre.GenreID;
import com.pixelflicks.admin.catalogo.e2e.MockDsl;
import com.pixelflicks.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.pixelflicks.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import com.pixelflicks.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GenreRepository genreRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:8.0")
            .withUsername("root")
            .withPassword("12345")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry){
        final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Container is running on port %s\n", mappedPort);
        registry.add("mysql.port", () -> mappedPort);
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception{
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualId = givenAGenre(expectedName,expectedIsActive,expectedCategories);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size()
        && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        Assertions.assertNotNull( actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull( actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception{
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes);

        final var actualId = givenAGenre(expectedName,expectedIsActive,expectedCategories);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        Assertions.assertNotNull( actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull( actualGenre.getDeletedAt());
    }

    public void asACatalogAdminIShouldBeAbleToNavigateThruAllGenres() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Ação",true, List.of());
        givenAGenre("Esportes",true,List.of());
        givenAGenre("Drama",true,List.of());

        listGenres(0,1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")));

        listGenres(1,1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Esportes")));

        listGenres(2,1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Drama")));

        listGenres(3,1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllGenres() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Ação",true, List.of());
        givenAGenre("Esportes",true,List.of());
        givenAGenre("Drama",true,List.of());

        listGenres(0,1,"dra")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Drama")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllGenreByDescriptionDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Ação",true, List.of());
        givenAGenre("Esportes",true,List.of());
        givenAGenre("Drama",true,List.of());

        listGenres(0,3,"", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Esportes")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Drama")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Ação")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes);

        final var actualId = givenAGenre(expectedName,expectedIsActive,expectedCategories);

        final var actualGenre = retrieveAGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.categories().size()
        && mapTo(expectedCategories, CategoryID::getValue).containsAll(actualGenre.categories()));
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertNotNull( actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull( actualGenre.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var aRequest = get("/genres/123").contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Genre with Id 123 was not found")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes);

        final var actualId = givenAGenre("Acao",expectedIsActive, expectedCategories);

        final var requestBody = new UpdateGenreRequest(expectedName,mapTo(expectedCategories, CategoryID::getValue),expectedIsActive);

        updateAGenre(actualId, requestBody).andExpect(status().isOk());

        var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull( actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull( actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToInactivateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(filmes);

        final var actualId = givenAGenre(expectedName,true, expectedCategories);

        final var requestBody = new UpdateGenreRequest(expectedName,mapTo(expectedCategories, CategoryID::getValue),expectedIsActive);

        updateAGenre(actualId, requestBody).andExpect(status().isOk());

        var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToActivateACategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes);

        final var actualId = givenAGenre(expectedName,false, expectedCategories);

        final var requestBody = new UpdateGenreRequest(expectedName,mapTo(expectedCategories, CategoryID::getValue),expectedIsActive);

        updateAGenre(actualId, requestBody).andExpect(status().isOk());

        var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);

        final var actualId = givenAGenre("Ação",true, List.of(filmes));

        deleteAGenre(actualId)
                .andExpect(status().isNoContent());

        Assertions.assertFalse(this.genreRepository.existsById(actualId.getValue()));
    }

    @Test
    public void asACatalogAdminIShouldBNotSeeAErrorByDeletingAExistentGenre() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        deleteAGenre(GenreID.from("123"))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(0, genreRepository.count());
    }
}
