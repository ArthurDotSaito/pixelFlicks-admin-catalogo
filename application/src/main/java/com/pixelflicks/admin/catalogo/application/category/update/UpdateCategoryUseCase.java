package com.pixelflicks.admin.catalogo.application.category.update;

import com.pixelflicks.admin.catalogo.application.UseCase;
import com.pixelflicks.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
