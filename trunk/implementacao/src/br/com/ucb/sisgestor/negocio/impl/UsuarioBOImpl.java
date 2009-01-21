/*
 * Projeto: sisgestor
 * Cria��o: 28/12/2008 por Jo�o L�cio
 */
package br.com.ucb.sisgestor.negocio.impl;

import br.com.ucb.sisgestor.entidade.Usuario;
import br.com.ucb.sisgestor.mail.Email;
import br.com.ucb.sisgestor.mail.EmailSender;
import br.com.ucb.sisgestor.negocio.UsuarioBO;
import br.com.ucb.sisgestor.negocio.exception.NegocioException;
import br.com.ucb.sisgestor.persistencia.UsuarioDAO;
import br.com.ucb.sisgestor.persistencia.impl.UsuarioDAOImpl;
import br.com.ucb.sisgestor.util.hibernate.HibernateUtil;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

/**
 * Objeto de neg�cio para {@link Usuario}.
 * 
 * @author Jo�o L�cio
 * @since 28/12/2008
 */
public class UsuarioBOImpl extends BaseBOImpl<Usuario, Integer> implements UsuarioBO {

	private static final UsuarioBO	instancia	= new UsuarioBOImpl();
	private UsuarioDAO					dao;

	/**
	 * Cria uma nova inst�ncia do tipo {@link UsuarioBOImpl}.
	 */
	private UsuarioBOImpl() {
		this.dao = UsuarioDAOImpl.getInstancia();
	}

	/**
	 * Recupera a inst�ncia de {@link UsuarioBO}. <br />
	 * pattern singleton.
	 * 
	 * @return {@link UsuarioBO}
	 */
	public static UsuarioBO getInstancia() {
		return instancia;
	}

	/**
	 * {@inheritDoc}
	 */
	public void atualizar(Usuario usuario) throws NegocioException {
		this.salvar(usuario);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean enviarLembreteDeSenha(String login) throws NegocioException {
		Usuario usuario = this.dao.recuperarPorLogin(login);
		if ((usuario != null) && StringUtils.isNotBlank(usuario.getEmail())) {
			try {
				Email email = new Email();
				email.setAssunto("SisGestor - Lembrete de senha");
				email.addDestinatariosTO(usuario.getEmail());
				email.setRemetente("sisgestor");
				email.setCorpo(usuario.getSenha());
				EmailSender.getInstancia().send(email);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void excluir(Usuario usuario) throws NegocioException {
		Transaction transaction = this.beginTransaction();
		try {
			this.dao.excluir(usuario);
			HibernateUtil.commit(transaction);
		} catch (Exception e) {
			HibernateUtil.rollback(transaction);
			throw new NegocioException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Usuario> getByLoginNomeDepartamento(String login, String nome, Integer departamento,
			Integer paginaAtual) {
		return this.dao.getByLoginNomeDepartamento(login, nome, departamento, paginaAtual);
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getTotalRegistros(String login, String nome, Integer departamento) {
		return this.dao.getTotalRegistros(login, nome, departamento);
	}

	/**
	 * {@inheritDoc}
	 */
	public Usuario obter(Integer pk) {
		return this.dao.obter(pk);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Usuario> obterTodos() {
		return this.dao.obterTodos();
	}

	/**
	 * {@inheritDoc}
	 */
	public Usuario recuperarPorLogin(String login) throws NegocioException {
		Usuario usuario = this.dao.recuperarPorLogin(login);
		if (usuario == null) {
			throw new NegocioException("erro.usuarioNaoEncontrado");
		} else {
			return usuario;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void salvar(Usuario usuario) throws NegocioException {
		Transaction transaction = this.beginTransaction();
		try {
			if (GenericValidator.isBlankOrNull(usuario.getSenha())) {
				usuario.setSenha("1234");
			}
			this.dao.salvarOuAtualizar(usuario);
			HibernateUtil.commit(transaction);
		} catch (ConstraintViolationException ce) {
			HibernateUtil.rollback(transaction);
			if ("UUR_LOGIN".equals(ce.getConstraintName())) {
				throw new NegocioException("erro.usuario.login");
			} else {
				throw ce;
			}
		} catch (Exception e) {
			HibernateUtil.rollback(transaction);
			throw new NegocioException(e);
		}
	}
}