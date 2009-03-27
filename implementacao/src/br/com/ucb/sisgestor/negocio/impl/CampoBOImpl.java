/*
 * Projeto: sisgestor
 * Cria��o: 17/02/2009 por Thiago
 */
package br.com.ucb.sisgestor.negocio.impl;

import br.com.ucb.sisgestor.entidade.Campo;
import br.com.ucb.sisgestor.entidade.OpcaoCampo;
import br.com.ucb.sisgestor.entidade.Workflow;
import br.com.ucb.sisgestor.negocio.CampoBO;
import br.com.ucb.sisgestor.negocio.exception.NegocioException;
import br.com.ucb.sisgestor.persistencia.CampoDAO;
import br.com.ucb.sisgestor.persistencia.WorkflowDAO;
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
	private WorkflowDAO	workflowDAO;

	/**
	 * {@inheritDoc}
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void atualizar(Campo campo) throws NegocioException {
		Workflow workflow = this.workflowDAO.obter(campo.getWorkflow().getId());
		this.validarSePodeAlterarWorkflow(workflow);
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
		this.validarSePodeAlterarWorkflow(campo.getWorkflow());
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
		Workflow workflow = this.workflowDAO.obter(campo.getWorkflow().getId());
		this.validarSePodeAlterarWorkflow(workflow);
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
	 * Atribui o DAO de {@link Workflow}.
	 * 
	 * @param workflowDAO DAO de {@link Workflow}
	 */
	@Autowired
	public void setWorkflowDAO(WorkflowDAO workflowDAO) {
		this.workflowDAO = workflowDAO;
	}

	/**
	 * Verifica se o {@link Workflow} pode ser alterado, para n�o poder ocorrer altera��o nos campos.
	 * 
	 * @param workflow {@link Workflow} a verificar
	 * @throws NegocioException caso o {@link Workflow} n�o possa ser alterado
	 */
	private void validarSePodeAlterarWorkflow(Workflow workflow) throws NegocioException {
		if (workflow.getAtivo() || (workflow.getDataHoraExclusao() != null)) {
			throw new NegocioException("erro.workflow.alterar");
		}
	}
}