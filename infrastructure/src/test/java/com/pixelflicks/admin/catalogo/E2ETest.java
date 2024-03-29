package com.pixelflicks.admin.catalogo;

import com.pixelflicks.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-e2e")
@SpringBootTest(classes =  WebServerConfig.class)
@ExtendWith(MySQLCleanUpExtension.class)
@Tag("integration-test")
public @interface E2ETest {
}
