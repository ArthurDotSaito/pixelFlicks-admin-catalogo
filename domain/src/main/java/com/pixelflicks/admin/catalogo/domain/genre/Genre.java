package com.pixelflicks.admin.catalogo.domain.genre;

import com.pixelflicks.admin.catalogo.domain.AggregateRoot;
import com.pixelflicks.admin.catalogo.domain.category.CategoryID;
import com.pixelflicks.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genre extends AggregateRoot<GenreID> {
    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Genre(
            final GenreID anId,
            final String aName,
            final List<CategoryID> categories,
            final boolean isActive,
            final Instant deletedAt,
            final Instant updatedAt,
            final Instant createdAt) {

        super(anId);
        this.name = aName;
        this.categories = categories;
        this.active = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Genre newGenre(final String aName, final boolean isActive){
        final var anId = GenreID.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null:now;
        return new Genre(anId, aName, new ArrayList<>(), isActive, now, now, deletedAt);
    }

    public static Genre with(
            final GenreID anId,
            final String aName,
            final List<CategoryID> categories,
            final boolean isActive,
            final Instant deletedAt,
            final Instant updatedAt,
            final Instant createdAt) {

       return new Genre(anId, aName, categories, isActive, updatedAt, createdAt, deletedAt);
    }

    @Override
    public void validate(ValidationHandler handler) {

    }

    public String getName() {
        return name;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
