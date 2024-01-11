package com.pixelflicks.admin.catalogo.application;

import com.pixelflicks.admin.catalogo.domain.Category;

public class UseCase {
    public Category execute(){
        return new Category();
    }
}