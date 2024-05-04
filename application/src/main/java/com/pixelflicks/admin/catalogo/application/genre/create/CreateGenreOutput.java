package com.pixelflicks.admin.catalogo.application.genre.create;

import com.pixelflicks.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.pixelflicks.admin.catalogo.domain.category.Category;
import com.pixelflicks.admin.catalogo.domain.genre.Genre;

public record CreateGenreOutput(
        String id
) {

    public static CreateGenreOutput from(final String anId){
        return new CreateGenreOutput(anId);
    }
    public static CreateGenreOutput from(final Genre aGenre){
        return new CreateGenreOutput(aGenre.getId().getValue());
    }
}
