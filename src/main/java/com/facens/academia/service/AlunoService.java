package com.facens.academia.service;

import com.facens.academia.dto.request.CreateAlunoRequest;
import com.facens.academia.dto.request.UpdateAlunoRequest;
import com.facens.academia.dto.response.AlunoResponse;
import com.facens.academia.exception.BusinessException;
import com.facens.academia.exception.ResourceNotFoundException;
import com.facens.academia.model.Aluno;
import com.facens.academia.model.Plano;
import com.facens.academia.model.SituacaoAluno;
import com.facens.academia.repository.AlunoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

// Service concentra regras de negócio de aluno e impede que Controller acesse Repository diretamente.
@Service
public class AlunoService {

    private static final Logger log = LoggerFactory.getLogger(AlunoService.class);
    private final AlunoRepository alunoRepository;
    private final PlanoService planoService;
    private final Clock clock;

    public AlunoService(AlunoRepository alunoRepository, PlanoService planoService, Clock clock) {
        this.alunoRepository = alunoRepository;
        this.planoService = planoService;
        this.clock = clock;
    }

    @Transactional
    public AlunoResponse cadastrar(CreateAlunoRequest request) {
        log.info("Cadastrando aluno com email {}", request.email());
        validarEmailDisponivelParaCadastro(request.email());
        Plano plano = planoService.buscarEntidadePorId(request.planoId());
        if (!Boolean.TRUE.equals(plano.getAtivo())) {
            throw new BusinessException("Não é possível cadastrar aluno em plano inativo.");
        }
        Aluno aluno = Aluno.builder()
                .nome(request.nome().trim())
                .email(request.email().trim().toLowerCase())
                .idade(request.idade())
                .telefone(normalizarTelefone(request.telefone()))
                .situacao(SituacaoAluno.ATIVO)
                .dataCadastro(LocalDateTime.now(clock))
                .plano(plano)
                .build();
        return toResponse(alunoRepository.save(aluno));
    }

    @Transactional(readOnly = true)
    public List<AlunoResponse> listarTodos() {
        return alunoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<AlunoResponse> listarAtivos() {
        return alunoRepository.findBySituacaoOrderByNomeAsc(SituacaoAluno.ATIVO).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AlunoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadeComPlanoPorId(id));
    }

    @Transactional
    public AlunoResponse atualizar(Long id, UpdateAlunoRequest request) {
        Aluno aluno = buscarEntidadeComPlanoPorId(id);
        validarEmailDisponivelParaAtualizacao(request.email(), id);
        Plano plano = planoService.buscarEntidadePorId(request.planoId());
        aluno.setNome(request.nome().trim());
        aluno.setEmail(request.email().trim().toLowerCase());
        aluno.setIdade(request.idade());
        aluno.setTelefone(normalizarTelefone(request.telefone()));
        aluno.setSituacao(request.situacao());
        aluno.setPlano(plano);
        return toResponse(alunoRepository.save(aluno));
    }

    @Transactional
    public void excluir(Long id) {
        Aluno aluno = buscarEntidadeComPlanoPorId(id);
        alunoRepository.delete(aluno);
    }

    private void validarEmailDisponivelParaCadastro(String email) {
        alunoRepository.findByEmailIgnoreCase(email.trim()).ifPresent(aluno -> {
            throw new BusinessException("Já existe aluno cadastrado com este email.");
        });
    }

    private void validarEmailDisponivelParaAtualizacao(String email, Long id) {
        if (alunoRepository.existsByEmailIgnoreCaseAndIdNot(email.trim(), id)) {
            throw new BusinessException("Já existe outro aluno cadastrado com este email.");
        }
    }

    private Aluno buscarEntidadeComPlanoPorId(Long id) {
        return alunoRepository.findWithPlanoById(id).orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado."));
    }

    private String normalizarTelefone(String telefone) {
        return telefone == null || telefone.isBlank() ? null : telefone.trim();
    }

    private AlunoResponse toResponse(Aluno aluno) {
        return new AlunoResponse(
                aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getIdade(), aluno.getTelefone(),
                aluno.getSituacao(), aluno.getDataCadastro(), aluno.getPlano().getId(), aluno.getPlano().getNome()
        );
    }
}
