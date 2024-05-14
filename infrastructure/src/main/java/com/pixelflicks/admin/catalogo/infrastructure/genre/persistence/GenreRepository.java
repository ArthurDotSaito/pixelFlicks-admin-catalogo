package com.pixelflicks.admin.catalogo.infrastructure.genre.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreJpaEntity, String> {
}
