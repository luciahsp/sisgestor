<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<script type="text/javascript" src="dwr/interface/ManterDepartamentoDWR.js"></script>
<script type="text/javascript" src="js/manterDepartamento/manterDepartamento.js"></script>

<html:form action="/manterDepartamento.do?method=salvar" onsubmit="departamento.salvar(this); JanelaFactory.fecharJanelaAtiva(); return false;" styleId="manterDepartamentoForm">

	<fieldset style="margin: 5pt auto; padding: 10px; width: 95%;">
		<div>
			<label>
				<b><bean:message key="label.sigla"/>:</b>
				<html:text property="sigla" size="11" maxlength="10" />
			</label> <br />
			<label>
				<b><bean:message key="label.nome"/>:</b>
				<html:text property="nome" size="51" maxlength="50" />
			</label> <br />
			<label>
				<b><bean:message key="label.email"/>: </b>
				<html:text property="email" size="41" maxlength="40" />
			</label> <br />
			<label>
				<b><bean:message key="label.departamentoSuperior"/>: </b>
				<html:select property="departamentoSuperior" styleId="departamentoSuperior" titleKey="dica.departamento.selecione">
					<html:option value="" />
					<html:optionsCollection name="manterDepartamentoForm" property="listaDepartamentos" label="sigla" value="id" />
				</html:select>
			</label>
		</div>
	</fieldset>
	
	<div style="clear: both; padding: 5px;" align="center">	
		<html:submit titleKey="dica.salvar" styleClass="botaoOkCancelar">
			<bean:message key="label.salvar"/>
		</html:submit>
		
		<html:button property="cancelar" titleKey="dica.cancelar" onclick="JanelaFactory.fecharJanelaAtiva();" styleClass="botaoOkCancelar">
			<bean:message key="botao.cancelar"/>
		</html:button>
	</div>
</html:form>