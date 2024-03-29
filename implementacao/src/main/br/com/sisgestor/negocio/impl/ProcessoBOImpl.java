/*
 * Projeto: sisgestor
 * Cria��o: 11/02/2009 por Thiago
 */
package br.com.sisgestor.negocio.impl;

import br.com.sisgestor.entidade.Atividade;
import br.com.sisgestor.entidade.Processo;
import br.com.sisgestor.entidade.TransacaoProcesso;
import br.com.sisgestor.entidade.Workflow;
import br.com.sisgestor.negocio.ProcessoBO;
import br.com.sisgestor.negocio.WorkflowBO;
import br.com.sisgestor.negocio.exception.NegocioException;
import br.com.sisgestor.persistencia.ProcessoDAO;
import br.com.sisgestor.util.dto.PesquisaManterProcessoDTO;
import br.com.sisgestor.util.dto.PesquisaPaginadaDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Objeto de neg�cio para {@link Processo}.
 * 
 * @author Thiago
 * @since 11/02/2009
 */
@Service("processoBO")
public class ProcessoBOImpl extends BaseWorkflowBOImpl<Processo> implements ProcessoBO {

	private WorkflowBO workflowBO;
	private ProcessoDAO processoDAO;

	/**
	 * {@inheritDoc}
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizar(Processo processo) throws NegocioException {
		this.validarSePodeAlterarWorkflow(processo.getWorkflow());
		this.processoDAO.atualizar(processo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarTransacoes(Integer idWorkflow, String[] fluxos, String[] posicoes) throws NegocioException {
		Workflow workflow = this.getWorkflowBO().obter(idWorkflow);
		this.validarSePodeAlterarWorkflow(workflow);

		List<TransacaoProcesso> transacoes = this.getTransacoes(fluxos);
		this.validarFluxo(transacoes, idWorkflow);

		List<TransacaoProcesso> atual = this.processoDAO.recuperarTransacoesDoWorkflow(idWorkflow);

		if (atual != null) {
			//excluindo as transa��es atuais
			for (TransacaoProcesso transacao : atual) {
				this.processoDAO.excluirTransacao(transacao);
			}
			this.flush();
		}
		//inserindo as transa��es enviadas
		for (TransacaoProcesso transacao : transacoes) {
			this.processoDAO.salvarTransacao(transacao);
		}

		//Salvando as novas posi��es dos processos
		Processo processo;
		String[] processos;
		for (String posicao : posicoes) {
			processos = posicao.split(","); //Formato: <id>,<left>,<top>
			processo = this.processoDAO.obter(Integer.valueOf(processos[0]));

			processo.setLeft(Integer.valueOf(processos[1]));
			processo.setTop(Integer.valueOf(processos[2]));

			this.processoDAO.atualizar(processo);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(Processo processo) throws NegocioException {
		this.validarSePodeAlterarWorkflow(processo.getWorkflow());
		List<Atividade> lista = processo.getAtividades();
		//N�o permite excluir um processo que cont�m atividades
		if ((lista != null) && !lista.isEmpty()) {
			throw new NegocioException("erro.processo.atividades");
		}
		this.processoDAO.excluir(processo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public List<Processo> getByNomeDescricao(String nome, String descricao, Integer idWorkflow, Integer paginaAtual) {
		return this.processoDAO.getByNomeDescricao(nome, descricao, idWorkflow, paginaAtual);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public List<Processo> getByWorkflow(Integer workflow) {
		return this.processoDAO.getByWorkflow(workflow);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Integer getTotalPesquisa(PesquisaPaginadaDTO parametros) {
		PesquisaManterProcessoDTO dto = (PesquisaManterProcessoDTO) parametros;
		return this.processoDAO.getTotalRegistros(dto.getNome(), dto.getDescricao(), dto.getIdWorkflow());
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public Processo obter(Integer pk) {
		return this.processoDAO.obter(pk);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public List<Processo> obterTodos() {
		return this.processoDAO.obterTodos();
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer salvar(Processo processo) throws NegocioException {
		Workflow workflow = this.getWorkflowBO().obter(processo.getWorkflow().getId());
		this.validarSePodeAlterarWorkflow(workflow);
		return this.processoDAO.salvar(processo);
	}

	/**
	 * Atribui o DAO de {@link Processo}.
	 * 
	 * @param processoDAO DAO de {@link Processo}
	 */
	@Autowired
	public void setProcessoDAO(ProcessoDAO processoDAO) {
		this.processoDAO = processoDAO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public boolean temFluxoDefinido(Integer idWorkflow) {
		List<Processo> processos = this.getByWorkflow(idWorkflow);
		List<TransacaoProcesso> anteriores;
		List<TransacaoProcesso> posteriores;
		//caso tenha apenas um processo, ele j� � inicial e final.
		if ((processos != null) && (processos.size() > 1)) {
			for (Processo processo : processos) {
				anteriores = processo.getTransacoesAnteriores();
				posteriores = processo.getTransacoesPosteriores();
				if (CollectionUtils.isEmpty(anteriores) && CollectionUtils.isEmpty(posteriores)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Recupera a lista de transa��es criadas para os processos.
	 * 
	 * @param fluxos fluxos definidos pelo usu�rio
	 * @return {@link List} de {@link TransacaoProcesso}
	 */
	private List<TransacaoProcesso> getTransacoes(String[] fluxos) {
		List<TransacaoProcesso> lista = new ArrayList<TransacaoProcesso>();
		if (fluxos != null) {
			TransacaoProcesso transacao;
			Processo processoAnterior;
			Processo processoPosterior;
			for (String fluxo : fluxos) {
				String[] processos = fluxo.split(","); //Formato: <origem>,<destino>

				transacao = new TransacaoProcesso();
				processoAnterior = new Processo();
				processoPosterior = new Processo();

				processoAnterior.setId(Integer.parseInt(processos[0]));
				processoPosterior.setId(Integer.parseInt(processos[1]));

				transacao.setAnterior(processoAnterior);
				transacao.setPosterior(processoPosterior);
				lista.add(transacao);
			}
		}
		return lista;
	}

	/**
	 * Recupera o BO de {@link Workflow}.
	 * 
	 * @return BO de {@link Workflow}
	 */
	private WorkflowBO getWorkflowBO() {
		if (this.workflowBO == null) {
			this.workflowBO = utils.getBean(WorkflowBO.class);
		}
		return this.workflowBO;
	}

	/**
	 * Inicializa os mapas para a valida��o do fluxo.
	 * 
	 * @param transacoes transa��es de processo
	 * @param mapAnteriores {@link Map} para armazenar as transa��es anteriores
	 * @param mapPosteriores {@link Map} para armazenar as transa��es posteriores
	 * @param listaProcessos {@link List} com os processos
	 * @throws NegocioException caso n�o haja fluxo definido
	 */
	private void inicializarValidacaoFluxo(List<TransacaoProcesso> transacoes, Map<Integer, Integer> mapAnteriores,
			Map<Integer, Integer> mapPosteriores, List<Processo> listaProcessos) throws NegocioException {
		//Valida para que haja defini��o de fluxos
		if ((listaProcessos != null) && !listaProcessos.isEmpty() && ((transacoes != null) && transacoes.isEmpty())) {
			throw new NegocioException("erro.fluxo.definicao.processo");
		}

		for (Processo processo : listaProcessos) {
			mapAnteriores.put(processo.getId(), null);
			mapPosteriores.put(processo.getId(), null);
		}

		Integer anterior;
		Integer posterior;
		for (TransacaoProcesso transacaoProcesso : transacoes) {
			anterior = transacaoProcesso.getAnterior().getId();
			posterior = transacaoProcesso.getPosterior().getId();

			mapAnteriores.put(posterior, anterior);
			mapPosteriores.put(anterior, posterior);
		}
	}

	/**
	 * Realiza as valida��es das {@link TransacaoProcesso} informadas.
	 * 
	 * @param transacoes transa��es definidas pelo usu�rio
	 * @param idWorkflow c�digo identificador do workflow
	 * @throws NegocioException caso regra de neg�cio seja violada
	 */
	private void validarFluxo(List<TransacaoProcesso> transacoes, Integer idWorkflow) throws NegocioException {
		List<Processo> lista = this.processoDAO.getByWorkflow(idWorkflow);

		if ((lista == null) || lista.isEmpty()) {
			throw new NegocioException("erro.fluxo.definicao.processo.vazio");
		}
		if (lista.size() == 1) {
			return;
		}

		NegocioException exceptionInicial = new NegocioException("erro.fluxo.inicial.processo");
		NegocioException exceptionIsolado = new NegocioException("erro.fluxo.isolado.processo");
		NegocioException exceptionFinal = new NegocioException("erro.fluxo.final.processo");
		Map<Integer, Integer> mapAnteriores = new HashMap<Integer, Integer>();
		Map<Integer, Integer> mapPosteriores = new HashMap<Integer, Integer>();

		this.inicializarValidacaoFluxo(transacoes, mapAnteriores, mapPosteriores, lista);

		this.validarFluxos(lista, exceptionIsolado, exceptionInicial, exceptionFinal, mapAnteriores, mapPosteriores);
	}
}
