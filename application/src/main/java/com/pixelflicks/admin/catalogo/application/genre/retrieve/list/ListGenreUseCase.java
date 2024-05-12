package com.pixelflicks.admin.catalogo.application.genre.retrieve.list;

import com.pixelflicks.admin.catalogo.application.UseCase;
import com.pixelflicks.admin.catalogo.domain.pagination.Pagination;
import com.pixelflicks.admin.catalogo.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
