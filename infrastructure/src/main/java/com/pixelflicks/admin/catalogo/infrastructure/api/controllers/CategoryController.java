package com.pixelflicks.admin.catalogo.infrastructure.api.controllers;

import com.pixelflicks.admin.catalogo.domain.pagination.Pagination;
import com.pixelflicks.admin.catalogo.infrastructure.api.CategoryAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryAPI {
    @Override
    public ResponseEntity<?> createCategory() {
        return null;
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }
}
