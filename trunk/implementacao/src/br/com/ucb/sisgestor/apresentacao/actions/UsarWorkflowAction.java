/*
 * Projeto: sisgestor
 * Cria��o: 18/03/2009 por Gustavo
 */
package br.com.ucb.sisgestor.apresentacao.actions;

import br.com.ucb.sisgestor.apresentacao.forms.UsarWorkflowActionForm;
import br.com.ucb.sisgestor.entidade.Workflow;
import br.com.ucb.sisgestor.negocio.UsoWorkflowBO;
import br.com.ucb.sisgestor.negocio.WorkflowBO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Action para ultilizar um {@link Workflow}.
 * 
 * @author Gustavo
 * @since 27/03/2009
 */
public class UsarWorkflowAction extends BaseAction {

	private UsoWorkflowBO	usoWorkflowBO;
	private WorkflowBO		workflowBO;

	/**
	 * Exclui o(s) anexo(s) selecionado(s)
	 * 
	 * @param mapping objeto mapping da action
	 * @param formulario objeto form da action
	 * @param request request atual
	 * @param response response atual
	 * @return forward da atualiza��o
	 * @throws Exception caso exce��o seja lan�ada
	 */
	public ActionForward excluirAnexo(ActionMapping mapping, ActionForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		UsarWorkflowActionForm form = (UsarWorkflowActionForm) formulario;

		Integer[] anexosSelecionados = form.getAnexosSelecionados();
		this.usoWorkflowBO.excluirAnexos(anexosSelecionados, form.getId());

		this.addMessageKey("mensagem.excluir", "Anexo");
		return this.sendAJAXResponse(true);
	}

	/**
	 * Inclui o anexo ao uso workflow
	 * 
	 * @param mapping objeto mapping da action
	 * @param formulario objeto form da action
	 * @param request request atual
	 * @param response response atual
	 * @return forward da atualiza��o
	 * @throws Exception caso exce��o seja lan�ada
	 */
	public ActionForward incluirAnexo(ActionMapping mapping, ActionForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		UsarWorkflowActionForm form = (UsarWorkflowActionForm) formulario;
		FormFile arquivo = form.getArquivo();

		this.usoWorkflowBO.incluirAnexo(arquivo, form.getId());

		this.addMessageKey("mensagem.salvar", "Anexo");
		return this.sendAJAXResponse(true);
	}

	/**
	 * D� o in�cio de uso de um {@link Workflow}.
	 * 
	 * @param mapping objeto mapping da action
	 * @param formulario objeto form da action
	 * @param request request atual
	 * @param response response atual
	 * @return forward da atualiza��o
	 * @throws Exception caso exce��o seja lan�ada
	 */
	public ActionForward iniciarWorkflow(ActionMapping mapping, ActionForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.findForward(FWD_ENTRADA);
	}

	/**
	 * Abre popup para iniciar um {@link Workflow}.
	 * 
	 * @param mapping objeto mapping da action
	 * @param formulario objeto form da action
	 * @param request request atual
	 * @param response response atual
	 * @return forward da atualiza��o
	 * @throws Exception caso exce��o seja lan�ada
	 */
	public ActionForward popupIniciarWorkflow(ActionMapping mapping, ActionForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UsarWorkflowActionForm form = (UsarWorkflowActionForm) formulario;

		form.setListaWorkflows(this.workflowBO.recuperarPendentesIniciar());

		return this.findForward("popupIniciarWorkflow");
	}

	/**
	 * Abre o popup de um uso do {@link Workflow}.
	 * 
	 * @param mapping objeto mapping da action
	 * @param formulario objeto form da action
	 * @param request request atual
	 * @param response response atual
	 * @return forward da atualiza��o
	 * @throws Exception caso exce��o seja lan�ada
	 */
	public ActionForward popupUsoWorkflow(ActionMapping mapping, ActionForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.findForward("popupUsoWorkflow");
	}

	/**
	 * Abre popup para visualizar os anexos
	 * 
	 * @param mapping objeto mapping da action
	 * @param formulario objeto form da action
	 * @param request request atual
	 * @param response response atual
	 * @return forward da atualiza��o
	 * @throws Exception caso exce��o seja lan�ada
	 */
	public ActionForward popupVisualizarAnexos(ActionMapping mapping, ActionForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UsarWorkflowActionForm form = (UsarWorkflowActionForm) formulario;

		form.setListaAnexos(this.usoWorkflowBO.getAnexos(form.getId()));

		return this.findForward("popupVisualizarAnexos");
	}

	/**
	 * Atribui o BO de {@link UsoWorkflowBO}.
	 * 
	 * @param usoWorkflowBO BO de {@link UsoWorkflowBO}
	 */
	@Autowired
	public void setUsoWorkflowBO(UsoWorkflowBO usoWorkflowBO) {
		this.usoWorkflowBO = usoWorkflowBO;
	}

	/**
	 * Atribui o BO de {@link Workflow}.
	 * 
	 * @param workflowBO BO de {@link Workflow}
	 */
	@Autowired
	public void setWorkflowBO(WorkflowBO workflowBO) {
		this.workflowBO = workflowBO;
	}
}
