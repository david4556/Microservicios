package com.david.auth.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) {
}
