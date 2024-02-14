package com.pixelflicks.admin.catalogo.application.category.update;

import com.pixelflicks.admin.catalogo.IntegrationTest;
import com.pixelflicks.admin.catalogo.domain.category.Category;
import com.pixelflicks.admin.catalogo.domain.category.CategoryGateway;
import com.pixelflicks.admin.catalogo.domain.category.CategoryID;
import com.pixelflicks.admin.catalogo.domain.exceptions.DomainException;
import com.pixelflicks.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.pixelflicks.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.times;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId(){
        final var aCategory = Category.newCategory("Film", null, true);

        save(aCategory);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getCreatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnAnDomainException(){
        final var aCategory = Category.newCategory("Film", null, true);

        save(aCategory);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "A 'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedId = aCategory.getId();

        final var aCommand =  UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Mockito.verify(categoryGateway, times(0)).update(Mockito.any());
    }

    @Test
    public void givenAInactivateValidCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId(){
        final var aCategory = Category.newCategory("Film", null, true);

        save(aCategory);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getCreatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsAnException_thenShouldReturnNotFoundException(){
        final var aCategory = Category.newCategory("Film", null, true);

        save(aCategory);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).update(Mockito.any());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

        final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive(), actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
    }

    @Test
    public void givenACommandWithInvalidID_whenGatewayThrowsAnException_thenShouldReturnNotFoundException(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = "1234";
        final var expectedErrorMessage = "Category with ID 1234 was not found";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    private void  save(final Category... aCategory){
        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJpaEntity::from)
                        .toList()
        );
    }
}
