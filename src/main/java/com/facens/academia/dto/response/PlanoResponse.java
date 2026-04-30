package com.facens.academia.dto.response;

import java.math.BigDecimal;

public record PlanoResponse(
        Long id,
        String nome,
        String modalidade,
        BigDecimal valorMensal,
        Boolean ativo
) {
}
