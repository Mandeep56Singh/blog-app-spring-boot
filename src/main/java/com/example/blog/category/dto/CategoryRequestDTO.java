package com.example.blog.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(

        @NotBlank(message = "name is required")
        @Size(min = 2, max = 50, message = "name must be between {min} and {max}")
        String name
) { }
