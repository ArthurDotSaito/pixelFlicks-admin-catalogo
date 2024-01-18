package com.pixelflicks.admin.catalogo.domain.category;

import com.pixelflicks.admin.catalogo.domain.validation.Error;
import com.pixelflicks.admin.catalogo.domain.validation.ValidationHandler;
import com.pixelflicks.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {
    private final Category category;
    private static final int MAX_NAME_LENGTH = 255;
    private static final int MIN_NAME_LENGTH = 3;

    public CategoryValidator(final Category category, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.category.getName();

        if (name == null) {
            this.validationHandler().append(new Error("A 'name' should not be null"));
            return;
        }

        if (name.trim().isEmpty()) {
            this.validationHandler().append(new Error("A 'name' should not be empty"));
            return;
        }

        final var nameLength = name.trim().length();
        if (nameLength > MAX_NAME_LENGTH || nameLength < MIN_NAME_LENGTH) {
            this.validationHandler().append(new Error("A 'name' must be between 3 and 255 characters"));
        }
    }
}
