package com.pixelflicks.admin.catalogo.infrastructure.configuration.useCases;

import com.pixelflicks.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.pixelflicks.admin.catalogo.application.genre.create.DefaultCreateGenreUseCase;
import com.pixelflicks.admin.catalogo.application.genre.delete.DefaultDeleteGenreUseCase;
import com.pixelflicks.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.pixelflicks.admin.catalogo.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import com.pixelflicks.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.pixelflicks.admin.catalogo.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.pixelflicks.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.pixelflicks.admin.catalogo.application.genre.update.DefaultUpdateGenreUseCase;
import com.pixelflicks.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.pixelflicks.admin.catalogo.domain.category.CategoryGateway;
import com.pixelflicks.admin.catalogo.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenreUseCaseConfig {

    private final CategoryGateway categoryGateway;

    private final GenreGateway genreGateway;

    public GenreUseCaseConfig(CategoryGateway categoryGateway, GenreGateway genreGateway) {
        this.categoryGateway = categoryGateway;
        this.genreGateway = genreGateway;
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase(){
        return new DefaultCreateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase(){
        return new DefaultDeleteGenreUseCase(genreGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase(){
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase(){
        return new DefaultListGenreUseCase(genreGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase(){
        return new DefaultUpdateGenreUseCase(categoryGateway,genreGateway);
    }
}
