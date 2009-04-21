/*
 * Projeto: sisgestor
 * Cria��o: 30/03/2009 por Jo�o L�cio
 */
package br.com.ucb.sisgestor.persistencia;

import br.com.ucb.sisgestor.entidade.CampoUsoWorkflow;
import br.com.ucb.sisgestor.entidade.HistoricoUsoWorkflow;
import br.com.ucb.sisgestor.entidade.UsoWorkflow;
import br.com.ucb.sisgestor.entidade.Usuario;
import java.util.List;

/**
 * Interface para acesso aos dados de {@link UsoWorkflow}.
 * 
 * @author Jo�o L�cio
 * @since 30/03/2009
 */
public interface UsoWorkflowDAO extends BaseDAO<UsoWorkflow> {

	/** Quantidade de registros por pagina��o */
	Integer	QTD_REGISTROS_PAGINA	= Integer.valueOf(17);

	/**
	 * Recupera um {@link List} de {@link CampoUsoWorkflow} pelo id do uso workflow.
	 * 
	 * @param idUsoWorkflow C�digo identificador do uso workflow
	 * @return lista de campos usados
	 */
	List<CampoUsoWorkflow> getCamposUsoWorkflowByIdUsoWorkflow(Integer idUsoWorkflow);

	/**
	 * Recupera o total de registros retornados pela consulta. <br />
	 * 
	 * @param usuario usu�rio com tarefas pendentes
	 * @return total de registros encontrados
	 */
	Integer getTotalRegistros(Usuario usuario);

	/**
	 * Recupera todos os workflows que est�o sendo usados, dependendo de tarefa do usu�rio.
	 * 
	 * @param usuario usu�rio com tarefas pendentes
	 * @param paginaAtual p�gina atual da pesquisa
	 * @return {@link List} de {@link UsoWorkflow}
	 */
	List<UsoWorkflow> recuperarPendentesUsuario(Usuario usuario, Integer paginaAtual);

	/**
	 * Recupera o �ltimo n�mero de {@link UsoWorkflow} iniciado no ano da data informada.
	 * 
	 * @param ano ano da data informada
	 * @return n�mero mais recente do ano
	 */
	Integer recuperarUltimoNumeroDoAno(Integer ano);

	/**
	 * Salva um hist�rico do {@link UsoWorkflow}.
	 * 
	 * @param historicoUsoWorkflow hist�rico a salvar
	 */
	void salvarHistorico(HistoricoUsoWorkflow historicoUsoWorkflow);
}
