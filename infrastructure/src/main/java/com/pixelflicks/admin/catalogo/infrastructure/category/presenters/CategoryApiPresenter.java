package com.pixelflicks.admin.catalogo.infrastructure.category.presenters;

import com.pixelflicks.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.pixelflicks.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.pixelflicks.admin.catalogo.infrastructure.category.models.CategoryRespose;
import com.pixelflicks.admin.catalogo.infrastructure.category.models.CategoryListResponse;

import java.util.function.Function;

public interface CategoryApiPresenter {

    Function<CategoryOutput, CategoryRespose> present = output -> new CategoryRespose(
            output.id().getValue(),
            output.name(),
            output.description(),
            output.isActive(),
            output.createdAt(),
            output.updatedAt(),
            output.deletedAt()
    );

    static CategoryRespose present(final CategoryOutput output){
        return new CategoryRespose(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output){
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
