package com.facens.academia.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "alunos", uniqueConstraints = {@UniqueConstraint(name = "uk_aluno_email", columnNames = "email")})

public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false)
    private Integer idade;

    @Column(length = 20)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SituacaoAluno situacao;

    @Column(nullable = false)
    private LocalDateTime dataCadastro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plano_id", nullable = false, foreignKey = @ForeignKey(name = "fk_alunos_planos"))
    private Plano plano;
}
