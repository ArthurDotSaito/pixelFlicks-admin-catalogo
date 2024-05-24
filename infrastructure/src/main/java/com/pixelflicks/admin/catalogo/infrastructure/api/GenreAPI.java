package com.pixelflicks.admin.catalogo.infrastructure.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "genres")
@Tag(name = "genre")
public interface GenreAPI {

    ResponseEntity<?> create(@RequestBody Object a);
}
