package com.pixelflicks.admin.catalogo.application.category.retrieve.list;

import com.pixelflicks.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;

    void cleanUp(){
        Mockito.reset(categoryGateway);
    }
}
