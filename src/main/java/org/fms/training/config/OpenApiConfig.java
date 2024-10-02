package org.fms.training.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;


@OpenAPIDefinition(
        info = @Info(
                title = "fms-api",
                description = "This is fms api"
        ),
        servers = {
                @Server(
                        description = "Dev",
                        url = "http://100.100.112.9:8081/"
                ),
                @Server(
                        description = "Test",
                        url = "http://100.100.112.9:8083/"
                ),
                @Server(
                        description = "Prod",
                        url = "http://100.100.112.9:8082/"
                ),
                @Server(
                        description = "Local",
                        url = "http://localhost:8080/"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
