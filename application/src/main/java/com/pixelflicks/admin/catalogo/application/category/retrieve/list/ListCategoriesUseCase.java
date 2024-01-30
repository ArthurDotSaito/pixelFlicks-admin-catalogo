package com.pixelflicks.admin.catalogo.application.category.retrieve.list;

import com.pixelflicks.admin.catalogo.application.UseCase;
import com.pixelflicks.admin.catalogo.domain.category.CategorySearchQuery;
import com.pixelflicks.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
