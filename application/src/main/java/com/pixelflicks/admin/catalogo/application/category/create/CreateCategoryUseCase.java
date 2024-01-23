package com.pixelflicks.admin.catalogo.application.category.create;

import com.pixelflicks.admin.catalogo.application.UseCase;
import com.pixelflicks.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
