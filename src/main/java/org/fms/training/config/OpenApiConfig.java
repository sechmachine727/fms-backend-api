package org.fms.training.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "fms-api",
                description = "This is fms api"
        ),
        servers = {
                @Server(
                        description = "Dev",
                        url = "http://100.100.112.9:8080/"
                )
        }
)
public class OpenApiConfig {
}
