package com.pixelflicks.admin.catalogo.infrastructure;

import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.pixelflicks.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.pixelflicks.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.pixelflicks.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.pixelflicks.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.pixelflicks.admin.catalogo.domain.category.Category;
import com.pixelflicks.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.pixelflicks.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.pixelflicks.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }

    @Bean
    @DependsOnDatabaseInitialization
    ApplicationRunner runner(
            @Autowired CreateCategoryUseCase createCategoryUseCase,
            @Autowired UpdateCategoryUseCase updateCategoryUseCase,
            @Autowired DeleteCategoryUseCase deleteCategoryUseCase,
            @Autowired ListCategoriesUseCase listCategoriesUseCase,
            @Autowired GetCategoryByIdUseCase getCategoryByIdUseCase
            ){
        return args -> {

        };
    }
}