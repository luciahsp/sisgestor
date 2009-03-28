/*
 * Projeto: sisgestor
 * Cria��o: 18/03/2009 por Gustavo
 */
package br.com.ucb.sisgestor.apresentacao.actions;

import br.com.ucb.sisgestor.entidade.Workflow;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action para ultilizar um {@link Workflow}.
 * 
 * @author Gustavo
 * @since 27/03/2009
 */
public class UsarWorkflowAction extends BaseAction {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActionForward entrada(ActionMapping mapping, ActionForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		return this.findForward(FWD_ENTRADA);
	}

}
