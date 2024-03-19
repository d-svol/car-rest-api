package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Registration")
public class SingUpRequest {
    @Schema(description = "User name", example = "Jon")
    @NotBlank(message = "Username can't by empty")
    private String username;

    @Schema(description = "Password", example = "my_1secret1_password")
    private String password;
}
