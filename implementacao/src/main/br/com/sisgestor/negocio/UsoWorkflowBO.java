/*
 * Projeto: sisgestor
 * Cria��o: 31/03/2009 por Jo�o L�cio
 */
package br.com.sisgestor.negocio;

import br.com.sisgestor.negocio.exception.NegocioException;
import br.com.sisgestor.entidade.HistoricoUsoWorkflow;
import br.com.sisgestor.entidade.Tarefa;
import br.com.sisgestor.entidade.UsoWorkflow;
import br.com.sisgestor.entidade.Usuario;
import java.util.List;

/**
 * Interface para um objeto de neg�cio de {@link UsoWorkflow}.
 * 
 * @author Jo�o L�cio
 * @since 31/03/2009
 */
public interface UsoWorkflowBO extends BaseBO<UsoWorkflow> {

	/**
	 * Inicia a tarefa atual do {@link UsoWorkflow}.
	 * 
	 * @param usoWorkflow uso a iniciar a tarefa
	 * @throws NegocioException caso seja violada um regra
	 */
	void iniciarTarefa(UsoWorkflow usoWorkflow) throws NegocioException;

	/**
	 * Modifica a {@link Tarefa} atual do {@link UsoWorkflow} para a especificada.
	 * 
	 * @param usoWorkflow {@link UsoWorkflow} a atualizar a tarefa
	 * @param idTarefa identificador da nova tarefa
	 * @throws NegocioException caso regra de neg�cio seja violada
	 */
	void modificarTarefa(UsoWorkflow usoWorkflow, Integer idTarefa) throws NegocioException;

	/**
	 * Recupera se o {@link UsoWorkflow} em quest�o pode mudar de tarefa.
	 * 
	 * @param idUso identificador do uso
	 * @return <code>true</code> caso possa, <code>false</code> caso contr�rio
	 */
	Boolean podeMudarDeTarefa(Integer idUso);

	/**
	 * Recupera a lista de {@link UsoWorkflow} finalizados a partir dos par�metros informados.
	 * 
	 * @param numeroRegistro n�mero do registro do uso
	 * @param idWorkflow c�digo identificador do workflow
	 * @param paginaAtual p�gina atual da pesquisa
	 * @return {@link List} com {@link UsoWorkflow}
	 */
	List<UsoWorkflow> recuperarFinalizados(String numeroRegistro, Integer idWorkflow, Integer paginaAtual);

	/**
	 * Recupera a lista de {@link UsoWorkflow} em uso com {@link Tarefa} pendente do {@link Usuario} atual.
	 * 
	 * @param paginaAtual p�gina atual da pesquisa
	 * @return {@link List} com {@link UsoWorkflow}
	 */
	List<UsoWorkflow> recuperarPendentesUsuarioAtual(Integer paginaAtual);

	/**
	 * Recupera o n�mero total de registros retornado pela consulta
	 * {@link #recuperarFinalizados(String, Integer, Integer)}.
	 * 
	 * @param numeroRegistro n�mero do registro do uso
	 * @param idWorkflow c�digo identificador do workflow
	 * @return n�mero total de registros
	 */
	Integer recuperarTotalFinalizados(String numeroRegistro, Integer idWorkflow);

	/**
	 * Salva a anota��o do uso
	 * 
	 * @param idUsoWorkflow C�digo indentificador do {@link UsoWorkflow}
	 * @param anotacao Anota��o a ser salva
	 * @throws NegocioException caso seja violada um regra
	 */
	void salvarAnotacao(Integer idUsoWorkflow, String anotacao) throws NegocioException;

	/**
	 * Salva um registro de {@link HistoricoUsoWorkflow}.
	 * 
	 * @param historicoUsoWorkflow {@link HistoricoUsoWorkflow} a salvar
	 */
	void salvarHistorico(HistoricoUsoWorkflow historicoUsoWorkflow);

	/**
	 * Salva os valores informados dos campos.
	 * 
	 * @param valores Array de campos com seus valores
	 * @param idUsoWorkflow C�digo identificador do uso workflow
	 * @throws NegocioException caso seja violada um regra
	 */
	void salvarValoresCampos(String[] valores, Integer idUsoWorkflow) throws NegocioException;
}
