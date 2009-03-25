/*
 * Projeto: sisgestor
 * Cria��o: 17/02/2009 por Thiago
 */
package br.com.ucb.sisgestor.negocio.impl;

import br.com.ucb.sisgestor.entidade.Campo;
import br.com.ucb.sisgestor.entidade.OpcaoCampo;
import br.com.ucb.sisgestor.entidade.Workflow;
import br.com.ucb.sisgestor.negocio.CampoBO;
import br.com.ucb.sisgestor.negocio.WorkflowBO;
import br.com.ucb.sisgestor.negocio.exception.NegocioException;
import br.com.ucb.sisgestor.persistencia.CampoDAO;
import br.com.ucb.sisgestor.util.Utils;
import br.com.ucb.sisgestor.util.dto.PesquisaCampoDTO;
import br.com.ucb.sisgestor.util.dto.PesquisaPaginadaDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Objeto de neg�cio para {@link Campo}.
 * 
 * @author Thiago
 * @since 17/02/2009
 */
@Service("campoBO")
public class CampoBOImpl extends BaseBOImpl<Campo, Integer> implements CampoBO {

	private CampoDAO		campoDAO;
	private WorkflowBO	workflowBO;

	/**
	 * {@inheritDoc}
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void atualizar(Campo campo) throws NegocioException {
		this.validarSeWorkflowAtivo(campo.getWorkflow());
		Campo campoAtual = this.campoDAO.obter(campo.getId());
		if (campoAtual.getOpcoes() != null) { // excluindo as op��es pois o cascade n�o suporta para atualizar
			for (OpcaoCampo opcao : campoAtual.getOpcoes()) {
				this.campoDAO.excluirOpcao(opcao);
			}
		}
		try {
			Utils.copyProperties(campoAtual, campo);
			campoAtual.setOpcoes(campo.getOpcoes());
		} catch (Exception e) {
			throw new NegocioException(e);
		}
		this.campoDAO.atualizar(campoAtual);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void excluir(Campo campo) throws NegocioException {
		this.validarSeWorkflowAtivo(campo.getWorkflow());
		this.campoDAO.excluir(campo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public List<Campo> getByNomeTipo(String nome, Integer idTipo, Integer idWorkflow, Integer paginaAtual) {
		return this.campoDAO.getByNomeTipo(nome, idTipo, idWorkflow, paginaAtual);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Integer getTotalPesquisa(PesquisaPaginadaDTO parametros) {
		PesquisaCampoDTO dto = (PesquisaCampoDTO) parametros;
		return this.campoDAO.getTotalRegistros(dto.getNome(), dto.getTipo(), dto.getIdWorkflow());
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public Campo obter(Integer pk) {
		return this.campoDAO.obter(pk);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public List<Campo> obterTodos() {
		return this.campoDAO.obterTodos();
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void salvar(Campo campo) throws NegocioException {
		this.validarSeWorkflowAtivo(campo.getWorkflow());
		this.campoDAO.salvar(campo);
	}

	/**
	 * Atribui o DAO de {@link Campo}.
	 * 
	 * @param campoDAO DAO de {@link Campo}
	 */
	@Autowired
	public void setCampoDAO(CampoDAO campoDAO) {
		this.campoDAO = campoDAO;
	}

	/**
	 * Atribui o BO de {@link Workflow}.
	 * 
	 * @param workflowBO BO de {@link Workflow}
	 */
	@Autowired
	public void setWorkflowBO(WorkflowBO workflowBO) {
		this.workflowBO = workflowBO;
	}

	/**
	 * Verifica se o {@link Workflow} est� ativo, caso esteja n�o pode ocorrer altera��o nos processos.
	 * 
	 * @param workflow {@link Workflow} a verificar
	 * @throws NegocioException caso o {@link Workflow} esteja ativo
	 */
	private void validarSeWorkflowAtivo(Workflow workflow) throws NegocioException {
		Workflow workflowAtivo = this.workflowBO.obter(workflow.getId());
		if (workflowAtivo.getAtivo()) {
			throw new NegocioException("erro.workflowAtivo");
		}
	}
}