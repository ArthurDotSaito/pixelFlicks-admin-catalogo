package com.pixelflicks.admin.catalogo.domain.category;

import com.pixelflicks.admin.catalogo.domain.AggregateRoot;

import java.time.Instant;
import java.util.UUID;

public class Category extends AggregateRoot<CategoryID> {
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final CategoryID aId,
                    final String aName,
                    final String aDescription,
                    final boolean aIsActive,
                    final Instant aCreationDate,
                    final Instant aUpdateDDate,
                    final Instant aDeleteDate) {
        super(aId);
        this.name = aName;
        this.description = aDescription;
        this.active = aIsActive;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDDate;
        this.deletedAt = aDeleteDate;
    }

    public static Category newCategory(final String aName, final String aDescription, final boolean aIsActive){
        final var id = CategoryID.unique();
        final var now = Instant.now();
        return new Category(id, aName, aDescription, aIsActive, now, now, null);
    }

    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
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