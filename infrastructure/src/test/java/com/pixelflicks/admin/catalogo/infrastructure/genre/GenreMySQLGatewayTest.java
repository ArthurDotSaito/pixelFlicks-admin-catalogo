package com.pixelflicks.admin.catalogo.infrastructure.genre;

import com.pixelflicks.admin.catalogo.MySQLGatewayTest;
import com.pixelflicks.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import com.pixelflicks.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testeDependencyTest(){
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreRepository);
        Assertions.assertNotNull(genreGateway);
    }
}
