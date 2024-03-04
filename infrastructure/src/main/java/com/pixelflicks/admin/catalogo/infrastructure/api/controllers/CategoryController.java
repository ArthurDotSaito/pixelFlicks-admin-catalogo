package com.pixelflicks.admin.catalogo.infrastructure.api.controllers;

import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.pixelflicks.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.pixelflicks.admin.catalogo.domain.pagination.Pagination;
import com.pixelflicks.admin.catalogo.domain.validation.handler.Notification;
import com.pixelflicks.admin.catalogo.infrastructure.api.CategoryAPI;
import com.pixelflicks.admin.catalogo.infrastructure.category.models.CategoryApiOutput;
import com.pixelflicks.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
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

    public CategoryController(final CreateCategoryUseCase createCategoryUsecase,GetCategoryByIdUseCase getCategoryByIdUseCase){
        this.createCategoryUsecase = Objects.requireNonNull(createCategoryUsecase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
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
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

    @Override
    public CategoryApiOutput getById(final String id) {
        return CategoryApiPresenter.present.compose(this.getCategoryByIdUseCase::execute).apply(id);
    }
}
