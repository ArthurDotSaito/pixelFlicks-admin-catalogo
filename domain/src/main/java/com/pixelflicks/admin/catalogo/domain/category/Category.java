package com.pixelflicks.admin.catalogo.domain.category;

import com.pixelflicks.admin.catalogo.domain.AggregateRoot;
import com.pixelflicks.admin.catalogo.domain.utils.InstantUtils;
import com.pixelflicks.admin.catalogo.domain.validation.ValidationHandler;
import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> implements Cloneable{
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
        this.createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should be not null");
        this.updatedAt = Objects.requireNonNull(aUpdateDDate,"'updatedAt' should be not null");
        this.deletedAt = aDeleteDate;
    }

    public static Category newCategory(final String aName, final String aDescription, final boolean aIsActive){
        final var id = CategoryID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = aIsActive ? null : now;
        return new Category(id, aName, aDescription, aIsActive, now, now, deletedAt);
    }

    public static Category with(final CategoryID anId,
                                final String name,
                                final String description,
                                final boolean active,
                                final Instant createdAt,
                                final Instant deletedAt,
                                final  Instant updatedAt) {
        return new Category(
                anId,
                name,
                description,
                active,
                createdAt,
                deletedAt,
                updatedAt
        );
    }

    public static Category with(final Category aCategory){
        return with(
                aCategory.getId(),
                aCategory.name,
                aCategory.description,
                aCategory.isActive(),
                aCategory.createdAt,
                aCategory.updatedAt,
                aCategory.deletedAt
        );
    }

    @Override
    public void validate(final ValidationHandler handler){
        new CategoryValidator(this, handler).validate();
    }
    public Category activate(){
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }
    public Category deactivate(){
        if(getDeletedAt() == null){
            this.deletedAt = InstantUtils.now();
        }

        this.active = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Category update(final String aName, final String aDescription, final boolean isActive){
        this.name = aName;
        this.description = aDescription;
        if(isActive){
            activate();
        }else{
            deactivate();
        }

        this.updatedAt = InstantUtils.now();
        return this;
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

    @Override
    public Category clone() {
        try {
            return(Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}