package com.pixelflicks.admin.catalogo.domain.genre;

import com.pixelflicks.admin.catalogo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class GenreID extends Identifier {
    private final String value;
    private GenreID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }
    public static GenreID unique(){
        return GenreID.from(UUID.randomUUID());
    }
    public static GenreID from(final String aId){
        return new GenreID(aId);
    }
    public static GenreID from(final UUID aId){
        return new GenreID(aId.toString().toLowerCase());
    }

    @Override
    public String getValue(){
        return value;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreID that = (GenreID) o;
        return Objects.equals(getValue(), that.getValue());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
