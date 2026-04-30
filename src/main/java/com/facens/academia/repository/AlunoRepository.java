package com.facens.academia.repository;

import com.facens.academia.model.Aluno;
import com.facens.academia.model.SituacaoAluno;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    // Busca email sem diferenciar letras maiúsculas e minúsculas.
    Optional<Aluno> findByEmailIgnoreCase(String email);

    // Verifica duplicidade de email desconsiderando o próprio aluno em edição.
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    // Carrega aluno junto com plano para evitar LazyInitializationException no DTO.
    @EntityGraph(attributePaths = "plano")
    Optional<Aluno> findWithPlanoById(Long id);

    // Lista alunos por situação, também carregando o plano.
    @EntityGraph(attributePaths = "plano")
    List<Aluno> findBySituacaoOrderByNomeAsc(SituacaoAluno situacao);

}
