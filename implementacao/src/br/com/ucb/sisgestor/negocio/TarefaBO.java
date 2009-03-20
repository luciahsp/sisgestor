/*
 * Projeto: SisGestor
 * Cria��o: 16/02/2009 por Thiago
 */
package br.com.ucb.sisgestor.negocio;

import br.com.ucb.sisgestor.entidade.Atividade;
import br.com.ucb.sisgestor.entidade.Tarefa;
import br.com.ucb.sisgestor.entidade.TransacaoTarefa;
import br.com.ucb.sisgestor.negocio.exception.NegocioException;
import java.util.List;

/**
 * Interface para um objeto de neg�cio de {@link Tarefa}.
 * 
 * @author Thiago
 * @since 16/02/2009
 */
public interface TarefaBO extends BaseBO<Tarefa, Integer> {

	/**
	 * Atualiza as {@link TransacaoTarefa} informadas.
	 * 
	 * @param idAtividade identificador do {@link Atividade}
	 * @param transacoes transa��es a armazenar
	 * @throws NegocioException caso exce��o de neg�cio seja violada
	 */
	void atualizarTransacoes(Integer idAtividade, List<TransacaoTarefa> transacoes) throws NegocioException;

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
}
