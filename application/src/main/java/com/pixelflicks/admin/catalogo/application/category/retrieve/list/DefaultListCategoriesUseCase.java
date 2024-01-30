package com.pixelflicks.admin.catalogo.application.category.retrieve.list;

import com.pixelflicks.admin.catalogo.domain.category.CategorySearchQuery;
import com.pixelflicks.admin.catalogo.domain.pagination.Pagination;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase{
    @Override
    public Pagination<CategoryListOutput> execute(final CategorySearchQuery anIn) {
        return null;
    }
}
