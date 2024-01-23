package com.pixelflicks.admin.catalogo.application.category.update;

import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.pixelflicks.admin.catalogo.domain.category.Category;
import com.pixelflicks.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {
    private DefaultUpdateCategoryUseCase useCase;
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId(){
        final var aCategory = Category.newCategory("Film", null, true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(aCategory));

        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        Mockito.verify(categoryGateway, times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(categoryGateway, times(1)).update(Mockito.argThat(aUpdatedCategory ->{
            return Objects.equals(expectedName, aUpdatedCategory.getName())
                    && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                    && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                    && Objects.equals(expectedId, aUpdatedCategory.getId())
                    && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                    && aCategory.getCreatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                    && Objects.isNull(aUpdatedCategory.getDeletedAt());
        }));

    }
}
