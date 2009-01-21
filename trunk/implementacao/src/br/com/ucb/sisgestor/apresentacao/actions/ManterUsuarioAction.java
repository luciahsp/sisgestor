/*
 * Projeto: sisgestor
 * Cria��o: 03/01/2009 por Jo�o L�cio
 */
package br.com.ucb.sisgestor.apresentacao.actions;

import br.com.ucb.sisgestor.apresentacao.forms.ManterUsuarioActionForm;
import br.com.ucb.sisgestor.entidade.Permissao;
import br.com.ucb.sisgestor.entidade.Usuario;
import br.com.ucb.sisgestor.negocio.PermissaoBO;
import br.com.ucb.sisgestor.negocio.UsuarioBO;
import br.com.ucb.sisgestor.negocio.impl.DepartamentoBOImpl;
import br.com.ucb.sisgestor.negocio.impl.PermissaoBOImpl;
import br.com.ucb.sisgestor.negocio.impl.UsuarioBOImpl;
import br.com.ucb.sisgestor.util.Utils;
import br.com.ucb.sisgestor.util.constantes.ConstantesRoles;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action para manuten��es em {@link Usuario}.
 * 
 * @author Jo�o L�cio
 * @since 03/01/2009
 */
public class ManterUsuarioAction extends BaseAction {

	private static UsuarioBO	usuarioBO;

	static {
		usuarioBO = UsuarioBOImpl.getInstancia();
	}

	/**
	 * Atualiza um usu�rio.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return forward da atualiza��o
	 * @throws Exception
	 */
	public ActionForward atualizar(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ManterUsuarioActionForm form = (ManterUsuarioActionForm) actionForm;
		Usuario usuario = new Usuario();
		Utils.copyProperties(usuario, form);

		//Usu�rio pode atualizar os seus dados
		if (!this.getUser().getId().equals(usuario.getId())
				&& !request.isUserInRole(ConstantesRoles.MANTER_USUARIO)) {
			this.addMessageKey("erro.acessoNegado");
			return this.sendAJAXResponse(false);
		}
		usuarioBO.atualizar(usuario);

		this.addMessageKey("mensagem.usuario.alterar");
		return this.sendAJAXResponse(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActionForward entrada(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ManterUsuarioActionForm form = (ManterUsuarioActionForm) actionForm;

		form.setListaDepartamentos(DepartamentoBOImpl.getInstancia().obterTodos());

		return this.findForward(FWD_ENTRADA);
	}

	/**
	 * Exibe a lista de permiss�es que existem para o usu�rio
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return forward do popup
	 * @throws Exception
	 */
	public ActionForward popupEditarPermissoes(ActionMapping mapping, ActionForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ManterUsuarioActionForm form = (ManterUsuarioActionForm) formulario;
		Usuario usuario = usuarioBO.obter(form.getId());

		List<Permissao> todasPermissoes = PermissaoBOImpl.getInstancia().obterTodos();
		List<Permissao> permissoesUsuario = usuario.getPermissoes();

		//TODO Erro de programa��o, n�o est� removendo 
		form.setRoles(usuario.getPermissoes());
		form.setPermissoesDisponiveis(Utils.subtrair(todasPermissoes, permissoesUsuario, Permissao.class));

		return this.findForward("popupEditarPermissoes");
	}

	/**
	 * Tela de popup para incluir um novo usu�rio.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return forward do popup
	 */
	public ActionForward popupNovoUsuario(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ManterUsuarioActionForm frm = (ManterUsuarioActionForm) form;

		frm.setListaDepartamentos(DepartamentoBOImpl.getInstancia().obterTodos());
		frm.setPermissoesDisponiveis(PermissaoBOImpl.getInstancia().obterTodos());

		return this.findForward("popupNovoUsuario");
	}

	/**
	 * Salva um usu�rio.
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return forward da inclus�o
	 * @throws Exception
	 */
	public ActionForward salvar(ActionMapping mapping, ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ManterUsuarioActionForm form = (ManterUsuarioActionForm) formulario;
		Usuario usuario = new Usuario();
		Utils.copyProperties(usuario, form);

		//TODO Verificar porque nao copiou do form
		usuario.setPermissoes(this.getPermissoes(form.getPermissoes()));

		usuarioBO.salvar(usuario);

		this.addMessageKey("mensagem.usuario.salvar");
		return this.sendAJAXResponse(true);
	}

	/**
	 * 
	 * M�todo tempor�rio para recuperar as permiss�es informadas pelo form mas que n�o foram copiadas pro
	 * objeto persistente.
	 * 
	 * @param permissoes
	 * @return Lista de permissoes
	 */
	private List<Permissao> getPermissoes(Integer[] permissoes) {
		List<Permissao> list = new ArrayList<Permissao>();
		PermissaoBO permissaoBO = PermissaoBOImpl.getInstancia();
		for (Integer key : permissoes) {
			list.add(permissaoBO.obter(key));
		}
		return list;
	}
}
