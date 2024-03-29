package br.com.sisgestor.apresentacao.servlets;

import br.com.sisgestor.util.constantes.ConstantesRoles;
import br.com.sisgestor.persistencia.AtividadeDAO;
import br.com.sisgestor.persistencia.BaseDAO;
import br.com.sisgestor.persistencia.CampoDAO;
import br.com.sisgestor.persistencia.ProcessoDAO;
import br.com.sisgestor.persistencia.TarefaDAO;
import br.com.sisgestor.persistencia.UsoWorkflowDAO;
import br.com.sisgestor.entidade.TipoCampoEnum;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Constantes para o Javascript Classe de constantes acessada pelo javascript <br />
 * Finalidade: centralizar a utiliza��o de constantes tanto pelo Java quanto pelo Javascript em um lugar
 * somente
 * 
 * A resposta dessa servlet � inclu�da no definicaoPrincipal.jsp
 */
public class ConstantesJSServlet extends HttpServlet {

	/** Mapa de constantes utilizadas nos JavaScripts */
	private static Map<String, Object> constantes = new HashMap<String, Object>();
	private static String javascript;

	static {
		constantes.put("PERMISSAO_MINIMA", ConstantesRoles.PERMISSAO_MINIMA);
		constantes.put("MANTER_DEPARTAMENTO", ConstantesRoles.MANTER_DEPARTAMENTO);
		constantes.put("MANTER_USUARIO", ConstantesRoles.MANTER_USUARIO);
		constantes.put("MANTER_WORKFLOW", ConstantesRoles.MANTER_WORKFLOW);
		constantes.put("USAR_WORKFLOW", ConstantesRoles.USAR_WORKFLOW);
		constantes.put("ANEXAR_ARQUIVO", ConstantesRoles.ANEXAR_ARQUIVO);
		constantes.put("QTD_REGISTROS_PAGINA", BaseDAO.QTD_REGISTROS_PAGINA);
		constantes.put("QTD_REGISTROS_PAGINA_CAMPO", CampoDAO.QTD_REGISTROS_PAGINA);
		constantes.put("QTD_REGISTROS_PAGINA_PROCESSO", ProcessoDAO.QTD_REGISTROS_PAGINA);
		constantes.put("QTD_REGISTROS_PAGINA_ATIVIDADE", AtividadeDAO.QTD_REGISTROS_PAGINA);
		constantes.put("QTD_REGISTROS_PAGINA_TAREFA", TarefaDAO.QTD_REGISTROS_PAGINA);
		constantes.put("QTD_REGISTROS_PAGINA_USO_WORKFLOW", UsoWorkflowDAO.QTD_REGISTROS_PAGINA);
		constantes.put("QTD_REGISTROS_PAGINA_USO_FINALIZADO", UsoWorkflowDAO.QTD_REGISTROS_PAGINA_FINALIZADOS);
		constantes.put("LISTA_DE_OPCOES", TipoCampoEnum.LISTA_DE_OPCOES.getId());
		constantes.put("MULTIPLA_ESCOLHA", TipoCampoEnum.MULTIPLA_ESCOLHA.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		if (javascript == null) {
			StringBuffer script = new StringBuffer("//Constantes utilizadas pelos JS\n");
			for (String key : constantes.keySet()) {
				Object valor = constantes.get(key);
				script.append("var ");
				script.append(key);
				script.append(" = ");
				if (valor instanceof Number) {
					script.append(valor);
				}
				if (valor instanceof String) {
					script.append('"');
					script.append(valor);
					script.append('"');
				}
				if (valor instanceof Character) {
					script.append('\'');
					script.append(valor);
					script.append('\'');
				}
				script.append(";\r\n");
			}
			javascript = script.toString();
		}
		response.setContentType("text/javascript");
		response.getWriter().write(javascript);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		this.doGet(request, response);
	}
}
