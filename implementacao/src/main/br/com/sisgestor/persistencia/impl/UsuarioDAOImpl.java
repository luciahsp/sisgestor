/*
 * Projeto: sisgestor
 * Cria��o: 01/01/2009 por Jo�o L�cio
 */
package br.com.sisgestor.persistencia.impl;

import br.com.sisgestor.util.GenericsUtil;
import br.com.sisgestor.persistencia.UsuarioDAO;
import br.com.sisgestor.entidade.Usuario;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Implementa��o da interface de acesso a dados de {@link Usuario}.
 * 
 * @author Jo�o L�cio
 * @since 01/01/2009
 */
@Repository("usuarioDAO")
public class UsuarioDAOImpl extends BaseDAOImpl<Usuario> implements UsuarioDAO {

	/**
	 * Cria uma nova inst�ncia do tipo {@link UsuarioDAOImpl}.
	 */
	public UsuarioDAOImpl() {
		super(Usuario.class);
	}

	/**
	 * {@inheritDoc}
	 * */
	public List<Usuario> getByDepartamento(Integer departamento) {
		Criteria criteria = this.getCriteria();
		criteria.createAlias("this.departamento", "departamento");
		criteria.add(Restrictions.eq("departamento.id", departamento));
		return GenericsUtil.checkedList(criteria.list(), Usuario.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Usuario getByLogin(String login) {
		Criteria criteria = this.getCriteria();
		criteria.add(Restrictions.eq("login", login).ignoreCase());
		return (Usuario) criteria.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Usuario> getByLoginNomeDepartamento(String login, String nome, Integer departamento,
			Integer paginaAtual) {
		Criteria criteria = this.montarCriteriosPaginacao(login, nome, departamento);
		this.adicionarPaginacao(criteria, paginaAtual, QTD_REGISTROS_PAGINA);
		criteria.addOrder(Order.asc("nome"));
		return GenericsUtil.checkedList(criteria.list(), Usuario.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getTotalRegistros(String login, String nome, Integer departamento) {
		Criteria criteria = this.montarCriteriosPaginacao(login, nome, departamento);
		criteria.setProjection(Projections.rowCount());
		return (Integer) criteria.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLoginUtilizado(Usuario usuario) {
		Criteria criteria = this.getCriteria();
		criteria.setProjection(Projections.count("this.id"));

		criteria.add(Restrictions.eq("this.login", usuario.getLogin()));
		if (usuario.getId() != null) {
			criteria.add(Restrictions.ne("this.id", usuario.getId()));
		}

		Integer total = (Integer) criteria.uniqueResult();
		return !(Integer.valueOf(0).equals(total));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Order getOrdemLista() {
		return Order.asc("this.nome").ignoreCase();
	}

	/**
	 * Recupera um {@link Criteria} padr�o.
	 * 
	 * @return criteria {@link Criteria} criado
	 */
	private Criteria getCriteria() {
		Criteria criteria = this.createCriteria(Usuario.class);
		criteria.add(Restrictions.isNull("dataHoraExclusao"));
		return criteria;
	}

	/**
	 * Monta os crit�rios para a pagina��o dos usu�rios.
	 * 
	 * @param login parte do login do usu�rio
	 * @param nome parte do nome do usu�rio
	 * @param departamento identificador do departamento do usu�rio
	 * @return {@link Criteria}
	 */
	private Criteria montarCriteriosPaginacao(String login, String nome, Integer departamento) {
		Criteria criteria = this.getCriteria();
		if (StringUtils.isNotBlank(login)) {
			criteria.add(Restrictions.like("login", login, MatchMode.ANYWHERE).ignoreCase());
		}
		if (StringUtils.isNotBlank(nome)) {
			criteria.add(Restrictions.like("nome", nome, MatchMode.ANYWHERE).ignoreCase());
		}
		if (departamento != null) {
			criteria.createAlias("this.departamento", "departamento");
			criteria.add(Restrictions.eq("departamento.id", departamento));
		}
		return criteria;
	}
}
