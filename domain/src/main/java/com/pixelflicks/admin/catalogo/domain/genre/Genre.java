package com.pixelflicks.admin.catalogo.domain.genre;

import com.pixelflicks.admin.catalogo.domain.AggregateRoot;
import com.pixelflicks.admin.catalogo.domain.category.CategoryID;
import com.pixelflicks.admin.catalogo.domain.exceptions.NotificationException;
import com.pixelflicks.admin.catalogo.domain.utils.InstantUtils;
import com.pixelflicks.admin.catalogo.domain.validation.ValidationHandler;
import com.pixelflicks.admin.catalogo.domain.validation.handler.Notification;

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
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {

        super(anId);
        this.name = aName;
        this.categories = categories;
        this.active = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

        selfValidate();
    }

    public static Genre newGenre(final String aName, final boolean isActive){
        final var anId = GenreID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;
        return new Genre(anId, aName, new ArrayList<>(), isActive, now, now, deletedAt);
    }

    public static Genre with(
            final GenreID anId,
            final String aName,
            final List<CategoryID> categories,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {

       return new Genre(anId, aName, categories, isActive, createdAt, updatedAt, deletedAt);
    }

    public static Genre with(final Genre aGenre) {
        return new Genre(aGenre.id, aGenre.name, new ArrayList<>(aGenre.categories) , aGenre.active, aGenre.updatedAt, aGenre.createdAt, aGenre.deletedAt);
    }

    public Genre addCategory(final CategoryID aCategoryId){
        if(aCategoryId == null){
            return this;
        }
        this.categories.add(aCategoryId);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre removeCategory(final CategoryID aCategoryId){
        if(aCategoryId == null){
            return this;
        }
        this.categories.remove(aCategoryId);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public Genre update(final String aName, final boolean isActive, final List<CategoryID> categories){
        this.name = aName;
        this.categories = new ArrayList<>(categories != null ? categories: Collections.emptyList());
        this.updatedAt = InstantUtils.now();
        if(isActive){
            activate();
        }else {
            deactivate();
        }
        selfValidate();
        return this;
    }

    public Genre deactivate(){
        if(getDeletedAt() == null){
            this.deletedAt = InstantUtils.now();
        }
        this.active = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre activate(){
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public String getName() {
        return name;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
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

    public void selfValidate(){
        final var notification = Notification.create();
        validate(notification);

        if(notification.hasErrors()){
            throw new NotificationException("Failed to create a Aggregate Genre", notification);
        }
    }

}
