package com.pixelflicks.admin.catalogo.infrastructure.api.controllers;

import com.pixelflicks.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.pixelflicks.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.pixelflicks.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.pixelflicks.admin.catalogo.application.genre.update.UpdateGenreCommand;
import com.pixelflicks.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.pixelflicks.admin.catalogo.domain.pagination.Pagination;
import com.pixelflicks.admin.catalogo.infrastructure.api.GenreAPI;
import com.pixelflicks.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.pixelflicks.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.pixelflicks.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.pixelflicks.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.pixelflicks.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import com.pixelflicks.admin.catalogo.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIdUseCase getGenreById;

    private final UpdateGenreUseCase updateGenre;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final GetGenreByIdUseCase getGenreById,
            final UpdateGenreUseCase updateGenre) {
        this.createGenreUseCase = createGenreUseCase;
        this.getGenreById = getGenreById;
        this.updateGenre = updateGenre;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest request) {
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
    public GenreResponse getById(final String id) {
        return GenreApiPresenter.present(this.getGenreById.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest bodyParams)
    {
        final var aCommand = UpdateGenreCommand.with(
                id,
                bodyParams.name(),
                bodyParams.isActive(),
                bodyParams.categories()
        );

        final var output = this.updateGenre.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(String id, UpdateCategoryRequest request) {

    }
}
