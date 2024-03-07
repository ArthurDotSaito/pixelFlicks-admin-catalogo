package com.pixelflicks.admin.catalogo.infrastructure.configuration.json;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public enum Json {
    INSTANCE;

    public static ObjectMapper mapper(){
        return INSTANCE.mapper.copy();
    }

    private final ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
            .dateFormat(new StdDateFormat())
            .featuresToDisable(
                    DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                    DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
                    DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,
                    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
            )
            .modules(new JavaTimeModule(), new Jdk8Module(), afterBurnerModule())
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .build();

    private AfterburnerModule afterBurnerModule() {
        var module = new AfterburnerModule();
        // Make afterburner generate byteCode only for public get/set and fields.
        // Java 9+ complains of "Illegal reflective access".

        module.setUseValueClassLoader(false);

        return module;
    }
}
