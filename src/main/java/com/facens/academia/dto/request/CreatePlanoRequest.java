package com.facens.academia.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreatePlanoRequest(
        @NotBlank(message = "Nome do plano é obrigatório")
        @Size(min = 3, max = 100, message = "Nome do plano deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank(message = "Modalidade é obrigatória")
        @Size(min = 3, max = 80, message = "Modalidade deve ter entre 3 e 80 caracteres")
        String modalidade,

        @NotNull(message = "Valor mensal é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor mensal deve ser maior que zero")
        @DecimalMax(value = "9999.99", message = "Valor mensal deve ser menor que 9999.99")
        BigDecimal valorMensal,

        Boolean ativo
) {
}
