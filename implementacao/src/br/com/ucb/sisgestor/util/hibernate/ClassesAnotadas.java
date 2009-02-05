/*
 * Projeto: SisGestor
 * Cria��o: 23/10/2008 por Jo�o L�cio
 */
package br.com.ucb.sisgestor.util.hibernate;

import br.com.ucb.sisgestor.entidade.Atividade;
import br.com.ucb.sisgestor.entidade.Departamento;
import br.com.ucb.sisgestor.entidade.Permissao;
import br.com.ucb.sisgestor.entidade.Processo;
import br.com.ucb.sisgestor.entidade.Tarefa;
import br.com.ucb.sisgestor.entidade.Usuario;
import br.com.ucb.sisgestor.entidade.Workflow;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Armazena as classes anotadas para o hibernate
 * 
 * @author Jo�o L�cio
 * @since 23/10/2008
 */
public class ClassesAnotadas {

	private static List<Class<? extends Serializable>>	classes	=
																							new ArrayList<Class<? extends Serializable>>();

	/**
	 * Adiciona as classes anotadas em uma lista
	 */
	static {
		classes.add(Usuario.class);
		classes.add(Departamento.class);
		classes.add(Permissao.class);
		classes.add(Workflow.class);
		classes.add(Processo.class);
		classes.add(Atividade.class);
		classes.add(Tarefa.class);
	}

	/**
	 * Retorna a lista com as classes anotadas
	 * 
	 * @return lista de classes anotadas
	 */
	public static List<Class<? extends Serializable>> getClassesAnotadas() {
		return classes;
	}
}
