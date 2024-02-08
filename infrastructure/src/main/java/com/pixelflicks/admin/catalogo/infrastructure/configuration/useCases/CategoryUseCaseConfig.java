package com.pixelflicks.admin.catalogo.infrastructure.configuration.useCases;

import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.pixelflicks.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.pixelflicks.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import com.pixelflicks.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.pixelflicks.admin.catalogo.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.pixelflicks.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.pixelflicks.admin.catalogo.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.pixelflicks.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.pixelflicks.admin.catalogo.application.category.update.DefaultUpdateCategoryUseCase;
import com.pixelflicks.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.pixelflicks.admin.catalogo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {
    private final CategoryGateway categoryGateway;
    public CategoryUseCaseConfig(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }
    @Bean
    public CreateCategoryUseCase createCategoryUseCase(){
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }
    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase(){
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }
    @Bean
    public GetCategoryByIdUseCase getCategoryUseCase(){
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }
    @Bean
    public ListCategoriesUseCase listCategoryUseCase(){
        return new DefaultListCategoriesUseCase(categoryGateway);
    }
    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(){
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }
}
