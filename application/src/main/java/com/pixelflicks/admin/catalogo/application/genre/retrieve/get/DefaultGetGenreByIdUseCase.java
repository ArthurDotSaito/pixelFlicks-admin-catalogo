package com.pixelflicks.admin.catalogo.application.genre.retrieve.get;

import com.pixelflicks.admin.catalogo.domain.exceptions.NotFoundException;
import com.pixelflicks.admin.catalogo.domain.genre.Genre;
import com.pixelflicks.admin.catalogo.domain.genre.GenreGateway;
import com.pixelflicks.admin.catalogo.domain.genre.GenreID;

import java.util.Objects;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase{

    private final GenreGateway genreGateway;

    public DefaultGetGenreByIdUseCase(GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }


    @Override
    public GenreOutput execute(final String anId) {
        final var genreId = GenreID.from(anId);
        return this.genreGateway.findById(genreId)
                .map(GenreOutput::from)
                .orElseThrow(() -> NotFoundException.with(Genre.class, genreId));
    }
}
