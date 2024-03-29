/*
 * Projeto: sisgestor
 * Cria��o: 18/02/2009 por Thiago
 */
package br.com.sisgestor.entidade;

import br.com.sisgestor.util.constantes.ConstantesDB;
import br.com.sisgestor.util.hibernate.HibernateUserTypeConstants;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

/**
 * Classe para representar um campo utilizado no workflow.
 * 
 * @author Thiago
 * @since 18/02/2009
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "CAM_CAMPO")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@org.hibernate.annotations.Table(appliesTo = "CAM_CAMPO")
@AttributeOverride(name = "id", column = @Column(name = "CAM_ID", nullable = false))
public class Campo extends ObjetoPersistente {

	private String nome;
	private String descricao;
	private TipoCampoEnum tipo;
	private Boolean obrigatorio;
	private Workflow workflow;
	private List<OpcaoCampo> opcoes;
	private List<CampoUsoWorkflow> usos;

	/**
	 * Recupera a descri��o do campo.
	 * 
	 * @return descri��o do campo
	 */
	@Column(name = "CAM_DESCRICAO", nullable = true, length = ConstantesDB.DESCRICAO)
	public String getDescricao() {
		return this.descricao;
	}

	/**
	 * Recupera o nome do campo.
	 * 
	 * @return nome do campo
	 */
	@Column(name = "CAM_NOME", nullable = false, length = ConstantesDB.NOME)
	public String getNome() {
		return this.nome;
	}

	/**
	 * Recupera se o campo � obrigat�rio.
	 * 
	 * @return se o campo � obrigat�rio
	 */
	@Column(name = "CAM_OBRIGATORIO", nullable = false, columnDefinition = ConstantesDB.DEFINICAO_BOOLEAN)
	public Boolean getObrigatorio() {
		return this.obrigatorio;
	}

	/**
	 * Recupera as op��es do campo.
	 * 
	 * @return op��es do campo
	 */
	@OneToMany(targetEntity = OpcaoCampo.class, mappedBy = "campo", fetch = FetchType.LAZY)
	@Cascade( {CascadeType.ALL, CascadeType.DELETE_ORPHAN})
	public List<OpcaoCampo> getOpcoes() {
		return this.opcoes;
	}

	/**
	 * Recupera o tipo do campo.
	 * 
	 * @return tipo do campo
	 */
	@Column(name = "CAM_TIPO", nullable = false)
	@Type(type = HibernateUserTypeConstants.CODIGO_INTEGER, parameters = @Parameter(name = "className", value = HibernateUserTypeConstants.TIPO_CAMPO_ENUM))
	public TipoCampoEnum getTipo() {
		return this.tipo;
	}

	/**
	 * Recupera os usos do campo
	 * 
	 * @return usos do campo
	 */
	@OneToMany(targetEntity = CampoUsoWorkflow.class, mappedBy = "campo")
	public List<CampoUsoWorkflow> getUsos() {
		return this.usos;
	}

	/**
	 * Recupera o workflow do campo.
	 * 
	 * @return workflow do campo
	 */
	@ManyToOne
	@JoinColumn(name = "WOR_ID", nullable = false)
	@ForeignKey(name = "IR_WOR_CAM")
	public Workflow getWorkflow() {
		return this.workflow;
	}

	/**
	 * Atribui a descri��o do campo.
	 * 
	 * @param descricao descri��o do campo
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Atribui o nome do campo.
	 * 
	 * @param nome nome do campo
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Atribui se o campo � obrigat�rio.
	 * 
	 * @param obrigatorio se o campo � obrigat�rio
	 */
	public void setObrigatorio(Boolean obrigatorio) {
		this.obrigatorio = obrigatorio;
	}

	/**
	 * Atribui as op��es do campo.
	 * 
	 * @param opcoes op��es do campo
	 */
	public void setOpcoes(List<OpcaoCampo> opcoes) {
		this.opcoes = opcoes;
	}

	/**
	 * Atribui o tipo do campo.
	 * 
	 * @param tipo tipo do campo
	 */
	public void setTipo(TipoCampoEnum tipo) {
		this.tipo = tipo;
	}

	/**
	 * Atribui os usos do campo
	 * 
	 * @param usos usos do campo
	 */
	public void setUsos(List<CampoUsoWorkflow> usos) {
		this.usos = usos;
	}

	/**
	 * Atribui o workflow do campo.
	 * 
	 * @param workflow workflow do campo
	 */
	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}
}
