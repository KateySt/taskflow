package org.taskflow.com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Builder
@Validated
public record NewUser(
        @NotBlank
        @Pattern(regexp = "^[A-Za-z\\s'-]+$", message = "must not contain special characters")
        @JsonProperty("full_name")
        @Schema(defaultValue = "John Doe")
        String fullName,

        @NotBlank
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$", message = "must be a valid email")
        @Schema(defaultValue = "example@email.com")
        String email,

        @NotBlank
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)\\S+$",
                message = "must be between 8 and 128 characters, must contain at least one letter and one number")
        @Schema(defaultValue = "Password123")
        String password
) {
}