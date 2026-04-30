package com.facens.academia.dto.request;
import com.facens.academia.model.SituacaoAluno;
import jakarta.validation.constraints.*;

// DTO de entrada para atualização de aluno.
public record UpdateAlunoRequest(
        @NotBlank(message = "Nome do aluno é obrigatório")
        @Size(min = 3, max = 120, message = "Nome deve ter entre 3 e 120 caracteres")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotNull(message = "Idade é obrigatória")
        @Min(value = 12, message = "Idade mínima é 12 anos")
        @Max(value = 100, message = "Idade máxima é 100 anos")
        Integer idade,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        String telefone,

        @NotNull(message = "Situação é obrigatória")
        SituacaoAluno situacao,

        @NotNull(message = "Plano é obrigatório")
        Long planoId
) {}
