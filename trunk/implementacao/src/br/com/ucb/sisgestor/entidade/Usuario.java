/*
 * Projeto: SisGestor
 * Cria��o: 24/10/2008 por Jo�o L�cio
 */
package br.com.ucb.sisgestor.entidade;

import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.SequenceGenerator;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;

/**
 * Classe que representa um usu�rio no sistema
 * 
 * @author Jo�o L�cio
 * @since 24/10/2008
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "UUR_USUARIO")
@org.hibernate.annotations.Table(appliesTo = "UUR_USUARIO")
@SequenceGenerator(name = "SEQ_UUR", sequenceName = "SEQ_UUR", allocationSize = 10)
@AttributeOverride(name = "id", column = @Column(name = "UUR_ID", nullable = false))
public class Usuario extends ObjetoPersistente {

	private String				nome;
	private String				login;
	private String				senha;
	private Departamento		departamento;
	private List<Permissao>	permissoes;

	/**
	 * Recupera o departamento do usu�rio
	 * 
	 * @return departamento do usu�rio
	 */
	@ManyToOne
	@JoinColumn(name = "DPR_ID", nullable = false)
	@ForeignKey(name = "IRDPRUUR")
	public Departamento getDepartamento() {
		return this.departamento;
	}

	/**
	 * Recupera o login do usu�rio
	 * 
	 * @return login do usu�rio
	 */
	@Column(name = "UUR_DS_LOGIN", nullable = false, columnDefinition = "CHAR(15)", length = 15)
	@NaturalId
	public String getLogin() {
		return this.login;
	}

	/**
	 * Recupera o nome do usu�rio
	 * 
	 * @return nome do usu�rio
	 */
	@Column(name = "UUR_NM", nullable = false, length = 150)
	public String getNome() {
		return this.nome;
	}

	/**
	 * Recupera as permiss�es do usu�rio no sistema
	 * 
	 * @return permissoes permiss�es do usu�rio no sistema
	 */
	@ManyToMany(targetEntity = Permissao.class)
	@Cascade( {CascadeType.ALL, CascadeType.DELETE_ORPHAN})
	@JoinTable(name = "UPM_USUARIO_PERMISSAO", joinColumns = @JoinColumn(name = "UUR_ID", nullable = false), inverseJoinColumns = @JoinColumn(name = "PRM_ID", nullable = false))
	@PrimaryKeyJoinColumns( {@PrimaryKeyJoinColumn(name = "UUR_ID", referencedColumnName = "UUR_ID"),
			@PrimaryKeyJoinColumn(name = "PRM_ID", referencedColumnName = "PRM_ID")})
	public List<Permissao> getPermissoes() {
		return this.permissoes;
	}

	/**
	 * Recupera a senha do usu�rio
	 * 
	 * @return senha senha do usu�rio
	 */
	@Column(name = "UUR_DS_SENHA", nullable = false)
	public String getSenha() {
		return this.senha;
	}

	/**
	 * Atribui o departamento do usu�rio
	 * 
	 * @param departamento departamento do usu�rio
	 */
	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	/**
	 * Atribui o login do usu�rio
	 * 
	 * @param login login do usu�rio
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Atribui o nome do usu�rio
	 * 
	 * @param nome nome do usu�rio
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Atribui as permiss�es do usu�rio no sistema
	 * 
	 * @param permissoes permiss�es do usu�rio no sistema
	 */
	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	/**
	 * Atribui a senha do usu�rio
	 * 
	 * @param senha senha do usu�rio
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}
}