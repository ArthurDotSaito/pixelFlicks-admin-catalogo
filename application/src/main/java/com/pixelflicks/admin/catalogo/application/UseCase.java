package com.pixelflicks.admin.catalogo.application;

import com.pixelflicks.admin.catalogo.domain.category.Category;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn);
}