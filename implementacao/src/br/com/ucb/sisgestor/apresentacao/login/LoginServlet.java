/*
 * Projeto: SisGestor
 * Cria��o: 09/12/2008 por Jo�o L�cio
 */
package br.com.ucb.sisgestor.apresentacao.login;

import br.com.ucb.sisgestor.negocio.exception.NegocioException;
import br.com.ucb.sisgestor.negocio.impl.UsuarioBOImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet de login do usu�rio.
 * 
 * @author Jo�o L�cio
 * @since 09/12/2008
 */
public final class LoginServlet extends HttpServlet {

	private static final String	CHARSET			= "ISO-8859-1";
	private static final String	ERROR				= "error";
	private static final String	HTML_MIME_TYPE	= "text/html";
	private static final String	LOGIN_ERROR		= "loginerror";
	private static final String	LOGOUT			= "logout";
	private static final Log		LOG				= LogFactory.getLog(LoginServlet.class);

	private LoginBundle				loginBundle;
	private LoginHelper				loginHelper;
	private ResourceBundle			properties;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		this.loginBundle = new LoginBundle();
		this.loginHelper = new LoginHelper();
		try {
			this.properties = ResourceBundle.getBundle("sisgestor");
		} catch (Exception e) {
			LOG.warn("Erro ao capturar properties", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String param;
		if (request.getParameter(LOGOUT) == null) {
			param = request.getParameter(ERROR);
			if (param == null) {
				LOG.debug("P�gina de login.");
				this.escrevePagina(response, this.loginBundle.getLoginPage(""));
			} else {
				LOG.debug("P�gina de acesso negado.");
				int erro = "".equals(param) ? 0 : Integer.parseInt(param);
				if ((erro == HttpServletResponse.SC_METHOD_NOT_ALLOWED)
						|| (erro == HttpServletResponse.SC_FORBIDDEN)) {
					this.escrevePagina(response, this.loginBundle.getErroPage(this
							.getMensagem("erro.acessoNegado")));
				} else {
					this.escrevePagina(response, this.loginBundle.getErroPage(this
							.getMensagem("erro.desconhecido")));
				}
			}
		} else {
			LOG.debug("Efetuando logout.");
			this.loginHelper.doLogout(request);
			response.sendRedirect(".");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		if (request.getParameter(LOGIN_ERROR) == null) {
			String operacao = request.getParameter("opr");
			LOG.debug("Opera��o: " + operacao);
			if ("paginaAvisarSenha".equals(operacao)) {
				LOG.debug("P�gina de lembrete de senha.");
				this.escrevePagina(response, this.loginBundle.getSenhaPage(""));
			} else if ("lembrarSenha".equals(operacao)) {
				LOG.debug("P�gina de lembrete de senha, enviando a senha.");
				this.enviarLembreteDeSenha(request, response);
			} else {
				LOG.debug("Opera��o n�o identificada. Redirecionando para a raiz da aplica��o.");
				response.sendRedirect(".");
			}
		} else {
			LOG.debug("P�gina de login negado.");
			this
					.escrevePagina(response, this.loginBundle.getLoginPage(this
							.getMensagem("erro.loginSemSucesso")));
		}
	}

	/**
	 * Envia a senha do usu�rio para o seu email.
	 * 
	 * @param request request atual
	 * @param response response atual
	 * @throws IOException
	 */
	private void enviarLembreteDeSenha(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String login = this.getEncodedParamValue(request, "login", 15);
		try {
			boolean ok = UsuarioBOImpl.getInstancia().enviarLembreteDeSenha(login);
			if (ok) {
				this.escrevePagina(response, this.loginBundle.getSenhaPage(this
						.getMensagem("mensagem.senhaEnviada")));
			} else {
				this.escrevePagina(response, this.loginBundle.getSenhaPage(this
						.getMensagem("erro.senhaNaoEnviada")));
			}
		} catch (NegocioException ne) {
			this
					.escrevePagina(response, this.loginBundle.getSenhaPage(this
							.getMensagem("erro.senhaNaoEnviada")));
		}
	}

	/**
	 * Escreve a p�gina html no browser.
	 * 
	 * @param response response atual
	 * @param pagina html a escrever
	 * @throws IOException
	 */
	private void escrevePagina(HttpServletResponse response, String pagina) throws IOException {
		response.setContentType(HTML_MIME_TYPE);
		PrintWriter writer = response.getWriter();
		writer.print(pagina);
		writer.flush();
	}

	/**
	 * Recupera um par�metro enviado no request feito.
	 * 
	 * @param request request atual
	 * @param paramName nome do par�metro
	 * @param maxLength tamanho m�ximo do valor
	 * @return valor do par�metro
	 */
	private String getEncodedParamValue(HttpServletRequest request, String paramName, int maxLength) {
		String valorParametro = request.getParameter(paramName);
		if (valorParametro == null) {
			valorParametro = "";
		} else {
			try {
				valorParametro = URLEncoder.encode(valorParametro.trim(), CHARSET);
			} catch (UnsupportedEncodingException ex) {
				valorParametro = "";
			}
		}
		if ((maxLength > 0) && (maxLength < valorParametro.length())) {
			valorParametro = valorParametro.substring(0, maxLength);
		}
		return valorParametro;
	}

	/**
	 * Recupera uma mensagem no properties.
	 * 
	 * @param key chave da mensagem
	 * @return mensagem
	 */
	private String getMensagem(String key) {
		return this.properties.getString(key);
	}
}