/*
 * Projeto: sisgestor
 * Cria��o: 11/02/2008 por Thiago
 */
package br.com.ucb.sisgestor.negocio.impl;

import br.com.ucb.sisgestor.entidade.Atividade;
import br.com.ucb.sisgestor.entidade.Processo;
import br.com.ucb.sisgestor.negocio.ProcessoBO;
import br.com.ucb.sisgestor.negocio.exception.NegocioException;
import br.com.ucb.sisgestor.persistencia.ProcessoDAO;
import br.com.ucb.sisgestor.persistencia.impl.ProcessoDAOImpl;
import br.com.ucb.sisgestor.util.dto.PesquisaPaginadaDTO;
import br.com.ucb.sisgestor.util.dto.PesquisaProcessoDTO;
import br.com.ucb.sisgestor.util.hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Transaction;

/**
 * Objeto de neg�cio para {@link Processo}.
 * 
 * @author Thiago
 * @since 11/02/2009
 */
public class ProcessoBOImpl extends BaseBOImpl<Processo, Integer> implements ProcessoBO {

	private static final ProcessoBO	instancia	= new ProcessoBOImpl();
	private ProcessoDAO					dao;

	/**
	 * Cria uma nova inst�ncia do tipo {@link ProcessoBOImpl}.
	 */
	private ProcessoBOImpl() {
		this.dao = ProcessoDAOImpl.getInstancia();
	}

	/**
	 * Recupera a inst�ncia de {@link ProcessoBO}. <br />
	 * pattern singleton.
	 * 
	 * @return {@link ProcessoBO}
	 */
	public static ProcessoBO getInstancia() {
		return instancia;
	}

	/**
	 * {@inheritDoc}
	 */
	public void atualizar(Processo processo) throws NegocioException {
		Transaction transaction = this.beginTransaction();
		try {
			this.dao.atualizar(processo);
			HibernateUtil.commit(transaction);
		} catch (Exception e) {
			HibernateUtil.rollback(transaction);
			this.verificaExcecao(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void excluir(Processo processo) throws NegocioException {
		Transaction transaction = this.beginTransaction();
		try {
			List<Atividade> lista = processo.getAtividades();
			//N�o permite excluir um processo que cont�m atividades
			if ((lista != null) && !lista.isEmpty()) {
				throw new NegocioException("erro.processo.atividades");
			}
			this.dao.excluir(processo);
			HibernateUtil.commit(transaction);
		} catch (Exception e) {
			HibernateUtil.rollback(transaction);
			this.verificaExcecao(e);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public List<Processo> getByNomeDescricao(String nome, String descricao, Integer idWorkflow,
			Integer paginaAtual) {
		return this.dao.getByNomeDescricao(nome, descricao, idWorkflow, paginaAtual);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getTotalPesquisa(PesquisaPaginadaDTO parametros) {
		PesquisaProcessoDTO dto = (PesquisaProcessoDTO) parametros;
		return this.dao.getTotalRegistros(dto.getNome(), dto.getDescricao(), dto.getIdWorkflow());
	}

	/**
	 * {@inheritDoc}
	 */
	public Processo obter(Integer pk) {
		return this.dao.obter(pk);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Processo> obterTodos() {
		return this.dao.obterTodos();
	}

	/**
	 * {@inheritDoc}
	 */
	public void salvar(Processo processo) throws NegocioException {
		Transaction transaction = this.beginTransaction();
		try {
			this.dao.salvar(processo);
			HibernateUtil.commit(transaction);
		} catch (Exception e) {
			HibernateUtil.rollback(transaction);
			this.verificaExcecao(e);
		}
	}
}