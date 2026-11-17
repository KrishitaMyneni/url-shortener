package dev.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateLinkRequest(

        @NotBlank(message = "URL is required") @Size(max = 2048, message = "URL must not exceed 2048 characters") String url,

        @Pattern(regexp = "^[a-zA-Z0-9]{4,16}$", message = "Custom code must be 4-16 alphanumeric characters") String customCode

) {
}