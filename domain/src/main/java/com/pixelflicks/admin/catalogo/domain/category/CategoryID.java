package com.pixelflicks.admin.catalogo.domain.category;

import com.pixelflicks.admin.catalogo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoryID extends Identifier {
    private final String value;
    private CategoryID(final String value) {
        this.value = Objects.requireNonNull(value);
    }
    public static CategoryID unique(){
        return CategoryID.from(UUID.randomUUID());
    }
    public static CategoryID from(final String aId){
        return new CategoryID(aId);
    }
    public static CategoryID from(final UUID aId){
        return new CategoryID(aId.toString().toLowerCase());
    }

    @Override
    public String getValue(){
        return value;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CategoryID that = (CategoryID) o;
        return getValue().equals(that.getValue());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
