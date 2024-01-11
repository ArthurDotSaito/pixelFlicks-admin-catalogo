package com.pixelflicks.admin.catalogo.application;

import com.pixelflicks.admin.catalogo.domain.category.Category;

public class UseCase {
    public Category execute(){
        return new Category();
    }
}