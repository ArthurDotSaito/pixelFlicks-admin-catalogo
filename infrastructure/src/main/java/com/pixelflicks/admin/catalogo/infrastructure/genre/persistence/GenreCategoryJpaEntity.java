package com.pixelflicks.admin.catalogo.infrastructure.genre.persistence;

import com.pixelflicks.admin.catalogo.domain.category.CategoryID;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "genre_categories")
public class GenreCategoryJpaEntity {

    @EmbeddedId
    private GenreCategoryID id;

    @ManyToOne
    @MapsId("genreId")
    private GenreJpaEntity genre;

    public GenreCategoryJpaEntity(){}

    private GenreCategoryJpaEntity(final GenreJpaEntity aGenre, final CategoryID aCategoryId){
        this.id = GenreCategoryID.from(aGenre.getId(), aCategoryId.getValue());
        this.genre = aGenre;
    }

    private static GenreCategoryJpaEntity from(final GenreJpaEntity aGenre, final CategoryID aCategoryId){
        return new GenreCategoryJpaEntity(aGenre, aCategoryId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreCategoryJpaEntity that = (GenreCategoryJpaEntity) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public GenreCategoryID getId() {
        return id;
    }

    public void setId(GenreCategoryID id) {
        this.id = id;
    }
}