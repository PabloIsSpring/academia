package com.facens.academia.service;

import com.facens.academia.dto.request.CreatePlanoRequest;
import com.facens.academia.dto.request.UpdatePlanoRequest;
import com.facens.academia.dto.response.PlanoResponse;
import com.facens.academia.exception.BusinessException;
import com.facens.academia.exception.ResourceNotFoundException;
import com.facens.academia.model.Plano;
import com.facens.academia.repository.PlanoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// @Service identifica a camada responsável pelas regras de negócio de Plano.
@Service
public class PlanoService {

    private static final Logger log = LoggerFactory.getLogger(PlanoService.class);
    private final PlanoRepository planoRepository;

    public PlanoService(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    @Transactional
    public PlanoResponse cadastrar(CreatePlanoRequest request) {
        log.info("Cadastrando plano: {}", request.nome());
        planoRepository.findByNomeIgnoreCase(request.nome()).ifPresent(plano -> {
            throw new BusinessException("Já existe um plano cadastrado com este nome.");
        });
        Plano plano = Plano.builder()
                .nome(request.nome().trim())
                .modalidade(request.modalidade().trim())
                .valorMensal(request.valorMensal())
                .ativo(request.ativo() == null || request.ativo())
                .build();
        return toResponse(planoRepository.save(plano));
    }

    @Transactional(readOnly = true)
    public List<PlanoResponse> listarTodos() {
        return planoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PlanoResponse> listarAtivos() {
        return planoRepository.findByAtivoTrueOrderByNomeAsc().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PlanoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadePorId(id));
    }

    @Transactional
    public PlanoResponse atualizar(Long id, UpdatePlanoRequest request) {
        Plano plano = buscarEntidadePorId(id);
        planoRepository.findByNomeIgnoreCase(request.nome()).ifPresent(planoExistente -> {
            if (!planoExistente.getId().equals(id)) {
                throw new BusinessException("Já existe outro plano com este nome.");
            }
        });
        plano.setNome(request.nome().trim());
        plano.setModalidade(request.modalidade().trim());
        plano.setValorMensal(request.valorMensal());
        plano.setAtivo(request.ativo());
        return toResponse(planoRepository.save(plano));
    }

    @Transactional
    public void excluir(Long id) {
        Plano plano = buscarEntidadePorId(id);
        if (!plano.getAlunos().isEmpty()) {
            throw new BusinessException("Não é possível excluir plano com alunos vinculados.");
        }
        planoRepository.delete(plano);
    }

    @Transactional(readOnly = true)
    public Plano buscarEntidadePorId(Long id) {
        return planoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado."));
    }

    private PlanoResponse toResponse(Plano plano) {
        return new PlanoResponse(plano.getId(), plano.getNome(), plano.getModalidade(), plano.getValorMensal(), plano.getAtivo());
    }
}