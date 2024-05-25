package com.pixelflicks.admin.catalogo.infrastructure.api.controllers;

import com.pixelflicks.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.pixelflicks.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.pixelflicks.admin.catalogo.domain.pagination.Pagination;
import com.pixelflicks.admin.catalogo.infrastructure.api.GenreAPI;
import com.pixelflicks.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.pixelflicks.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.pixelflicks.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.pixelflicks.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.pixelflicks.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;

    public GenreController(CreateGenreUseCase createGenreUseCase) {
        this.createGenreUseCase = createGenreUseCase;
    }

    @Override
    public ResponseEntity<?> create(CreateGenreRequest request) {
        final var aCommand = CreateGenreCommand.with(request.name(), request.isActive(), request.categories());

        final var response = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + response.id()))
                .body(response);
    }

    @Override
    public Pagination<GenreListResponse> list(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

    @Override
    public GenreResponse getById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateGenreRequest bodyParams) {
        return null;
    }

    @Override
    public void deleteById(String id, UpdateCategoryRequest request) {

    }
}
