package com.pixelflicks.admin.catalogo.infrastructure.api.controllers;

import com.pixelflicks.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.pixelflicks.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.pixelflicks.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.pixelflicks.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.pixelflicks.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.pixelflicks.admin.catalogo.application.genre.update.UpdateGenreCommand;
import com.pixelflicks.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.pixelflicks.admin.catalogo.domain.pagination.Pagination;
import com.pixelflicks.admin.catalogo.domain.pagination.SearchQuery;
import com.pixelflicks.admin.catalogo.infrastructure.api.GenreAPI;
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
    private final DeleteGenreUseCase deleteGenre;
    private final ListGenreUseCase listGenreUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final GetGenreByIdUseCase getGenreById,
            final UpdateGenreUseCase updateGenre,
            final DeleteGenreUseCase deleteGenre,
            final ListGenreUseCase listGenreUseCase) {
        this.createGenreUseCase = createGenreUseCase;
        this.getGenreById = getGenreById;
        this.updateGenre = updateGenre;
        this.deleteGenre = deleteGenre;
        this.listGenreUseCase = listGenreUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest request) {
        final var aCommand = CreateGenreCommand.with(request.name(), request.isActive(), request.categories());

        final var response = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + response.id()))
                .body(response);
    }

    @Override
    public Pagination<GenreListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction) {
        return this.listGenreUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(GenreApiPresenter::present);
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
    public void deleteById(final String id) {
        this.deleteGenre.execute(id);
    }
}
