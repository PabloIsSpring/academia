package com.facens.academia.dto.response;

import com.facens.academia.model.SituacaoAluno;

import java.time.LocalDateTime;

// DTO de saída para exibir dados do aluno sem expor a entidade completa.
public record AlunoResponse(
        Long id,
        String nome,
        String email,
        Integer idade,
        String telefone,
        SituacaoAluno situacao,
        LocalDateTime dataCadastro,
        Long planoId,
        String nomePlano
) {}
