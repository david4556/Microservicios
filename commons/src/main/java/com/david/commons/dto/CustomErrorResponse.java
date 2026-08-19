package com.david.commons.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) {
}
