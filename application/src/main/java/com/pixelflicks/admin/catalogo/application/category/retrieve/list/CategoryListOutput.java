package com.pixelflicks.admin.catalogo.application.category.retrieve.list;

import java.time.Instant;

public record CategoryListOutput(
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
}
