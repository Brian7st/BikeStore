package com.bikeStore.demo.dto.response;

import java.util.UUID;

public record LoginResponseDTO(
        String token,
        String username,
        String role,
        UUID userId
) {
}
