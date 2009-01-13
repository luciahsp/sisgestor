/*
 * Projeto: SisGestor
 * Cria��o: 16/10/2008 por Jo�o L�cio
 */
package br.com.ucb.sisgestor.negocio;

import br.com.ucb.sisgestor.entidade.ObjetoPersistente;
import java.io.Serializable;
import java.util.List;

/**
 * Interface que representa um objeto de neg�cio (Business Object).
 * 
 * @param <T> objeto persistente utilizado no DAO
 * @param <PK> chave prim�ria do objeto persistente utilizado
 * 
 * @author Jo�o L�cio
 * @since 16/10/2008
 */
public interface BaseBO<T extends ObjetoPersistente, PK extends Serializable> {

	/**
	 * Atualiza um objeto na base de dados.
	 * 
	 * @param obj objeto persistente a atualizar
	 * @throws Exception
	 */
	public void atualizar(T obj) throws Exception;

	/**
	 * Apaga um objeto na base de dados.
	 * 
	 * @param obj objeto persistente a apagar
	 * @throws Exception
	 */
	public void excluir(T obj) throws Exception;

	/**
	 * Recupera um objeto a partir da sua chave prim�ria.
	 * 
	 * @param pk chave prim�ria do objeto persistente
	 * 
	 * @return objeto recuperado
	 */
	public T obter(PK pk);

	/**
	 * Recupera todos os objetos.
	 * 
	 * @return um {@link List} de objeto
	 */
	public List<T> obterTodos();

	/**
	 * Salva um objeto na base de dados.
	 * 
	 * @param obj objeto persistente a salvar
	 * @throws Exception
	 */
	public void salvar(T obj) throws Exception;
}
