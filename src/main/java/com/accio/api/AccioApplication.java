package com.accio.api;

import com.accio.api.util.ColorJsonDeserializer;
import com.accio.api.util.ColorJsonSerializer;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SecurityScheme(name = "bearerAuth", scheme = "bearer", bearerFormat = "JWT", type = SecuritySchemeType.HTTP)
@EnableMethodSecurity
@EnableScheduling
@SpringBootApplication
public class AccioApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccioApplication.class, args);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.deserializers(new ColorJsonDeserializer());
            builder.serializers(new ColorJsonSerializer());
        };
    }

}
