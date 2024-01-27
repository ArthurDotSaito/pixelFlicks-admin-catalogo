package com.pixelflicks.admin.catalogo.application.category.delete;

import com.pixelflicks.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk(){

    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOk(){

    }

    @Test
    public void givenAValidId_whenGatewayThrowsError_shouldReturnOk(){

    }

}
