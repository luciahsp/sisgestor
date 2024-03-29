/*
 * Projeto: SisGestor
 * Cria��o: 16/02/2009 por Thiago
 */
package br.com.sisgestor.negocio;

import br.com.sisgestor.negocio.exception.NegocioException;
import br.com.sisgestor.entidade.Atividade;
import br.com.sisgestor.entidade.Tarefa;
import br.com.sisgestor.entidade.TransacaoTarefa;
import br.com.sisgestor.entidade.UsoWorkflow;
import br.com.sisgestor.entidade.Workflow;
import java.util.List;

/**
 * Interface para um objeto de neg�cio de {@link Tarefa}.
 * 
 * @author Thiago
 * @since 16/02/2009
 */
public interface TarefaBO extends BaseBO<Tarefa> {

	/**
	 * Atualiza as {@link TransacaoTarefa} informadas e as posi��es das tarefas na p�gina.
	 * 
	 * @param idAtividade identificador da {@link Atividade}
	 * @param fluxos Fluxos definidos pelo usu�rio
	 * @param posicoes Posi��es das tarefas na p�gina
	 * @throws NegocioException caso exce��o de neg�cio seja violada
	 */
	void atualizarTransacoes(Integer idAtividade, String[] fluxos, String[] posicoes) throws NegocioException;

	/**
	 * Recupera todas as tarefas referenciadas pela atividade
	 * 
	 * @param atividade Id da atividade
	 * @return Lista de tarefas
	 */
	List<Tarefa> getByAtividade(Integer atividade);

	/**
	 * Retorna um {@link List} de {@link Tarefa} a partir dos par�metros informados.
	 * 
	 * @param nome parte do nome da tarefa
	 * @param descricao parte da descri��o da tarefa
	 * @param usuario Usu�rio respons�vel pela tarefa
	 * @param idAtividade indentifica��o da atividade
	 * @param paginaAtual p�gina atual da pesquisa
	 * @return Retorna as tarefas
	 */
	List<Tarefa> getByNomeDescricaoUsuario(String nome, String descricao, Integer usuario,
			Integer idAtividade, Integer paginaAtual);

	/**
	 * Recupera a primeira tarefa do {@link Workflow}.
	 * 
	 * @param workflow workflow da tarefa
	 * @return {@link Tarefa} inicial do {@link Workflow}
	 */
	Tarefa recuperarPrimeiraTarefa(Workflow workflow);

	/**
	 * Recupera as pr�ximas tarefas dispon�veis para o {@link UsoWorkflow}.
	 * 
	 * @param usoWorkflow uso a ser recuperado as pr�ximas tarefas
	 * @return {@link List} de {@link Tarefa}
	 */
	List<Tarefa> recuperarProximasTarefas(UsoWorkflow usoWorkflow);

	/**
	 * Verifica se h� fluxo definido para as tarefas da {@link Atividade} informada.
	 * 
	 * @param idAtividade c�digo identificador da {@link Atividade}
	 * @return <code>true</code>, se houver fluxo definido;<br>
	 *         <code>false</code>, se n�o houver.
	 */
	boolean temFluxoDefinido(Integer idAtividade);
}
