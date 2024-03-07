package com.pixelflicks.admin.catalogo.infrastructure.api.controllers;

import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.pixelflicks.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.pixelflicks.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.pixelflicks.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.pixelflicks.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.pixelflicks.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.pixelflicks.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.pixelflicks.admin.catalogo.domain.category.CategorySearchQuery;
import com.pixelflicks.admin.catalogo.domain.pagination.Pagination;
import com.pixelflicks.admin.catalogo.domain.validation.handler.Notification;
import com.pixelflicks.admin.catalogo.infrastructure.api.CategoryAPI;
import com.pixelflicks.admin.catalogo.infrastructure.category.models.CategoryApiOutput;
import com.pixelflicks.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.pixelflicks.admin.catalogo.infrastructure.category.models.UpdateCategoryApiInput;
import com.pixelflicks.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {
    private final CreateCategoryUseCase createCategoryUsecase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUsecase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase,
            final ListCategoriesUseCase listCategoriesUseCase){
        this.createCategoryUsecase = Objects.requireNonNull(createCategoryUsecase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
    }
    @Override
    public ResponseEntity<?> createCategory(CreateCategoryApiInput input) {

        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;

        final Function<CreateCategoryOutput, ResponseEntity> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUsecase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(final String search, final int page, final int perPage, final String sort, final String direction) {
        return listCategoriesUseCase.execute(new CategorySearchQuery(page, perPage, search, sort, direction));
    }

    @Override
    public CategoryApiOutput getById(final String id) {
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryApiInput input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(final String anId) {
        this.deleteCategoryUseCase.execute(anId);
    }
}
