package com.example.blog.category.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponseDTO(
        UUID id,
        String name,
        Long postCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
