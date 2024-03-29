/**
 * A��o a ser realizada ao iniciar a p�gina
 */
Event.observe(window, "load", function() {
	workflow.pesquisar();
});

/**
 * Comportamentos para o UC Manter Workflow.
 * 
 * @author Thiago
 * @since 09/02/2009
 */
var ManterWorkflow = Class.create();
ManterWorkflow.prototype = {

   /**
	 * Tabela com os dados da pesquisa.
	 */
   tabelaTelaPrincipal :null,

   /**
	 * Se deve mostrar os workflows exclu�dos na pesquisa.
	 */
   excluidosPesquisa :false,

   /**
	 * @constructor
	 */
   initialize : function() {},

   /**
	 * Retorna a tabela da tela inicial do caso de uso.
	 * 
	 * @return {HTMLTableSectionElement}
	 */
   getTBodyTelaPrincipal : function() {
	   return $("corpoManterWorkflow");
   },

   /**
	 * Recupera a linha selecionada.
	 * 
	 * @return linha selecionada
	 */
   getTR : function() {
	   return FactoryTabelas.getTabelaById(this.getTBodyTelaPrincipal()).getSelectedTR();
   },

   /**
	 * Recupera o id do workflow selecionado.
	 * 
	 * @return id do workflow selecionado
	 */
   getIdSelecionado : function() {
	   return this.getTR().select("input[type=\"hidden\"]")[0].value;
   },

   /**
	 * Preenche os campos do workflow selecionado.
	 */
   visualizar : function() {
	   Element.hide("formAtualizarWorkflow");
	   var idWorkflow = this.getIdSelecionado();
	   if (isNaN(idWorkflow)) {
		   return;
	   }
	   ManterWorkflowDWR.getById(idWorkflow, ( function(workflow) {
		   Effect.Appear("formAtualizarWorkflow");
		   dwr.util.setValue($("formAtualizarWorkflow").id, idWorkflow);
		   dwr.util.setValue("nomeWorkflow", workflow.nome);
		   dwr.util.setValue("descricaoWorkflow", workflow.descricao);
		   dwr.util.setValue("ativoWorkflow", workflow.ativo);
		   dwr.util.setValue("excluidoWorkflow", (workflow.dataHoraExclusao != null));
		   this.habilitarLinks(true);
		   this.contaChar(false);
	   }).bind(this));
   },

   /**
	 * Faz a pesquisa dos workflows pelos par�metros informados.
	 */
   pesquisar : function() {
	   Effect.Fade("formAtualizarWorkflow");
	   this.habilitarLinks(false);
	   var dto = {
	      nome :dwr.util.getValue("nomePesquisaWorkflow"),
	      descricao :dwr.util.getValue("descricaoPesquisaWorkflow"),
	      ativo :dwr.util.getValue("ativoPesquisaWorkflow"),
	      excluidos :this.excluidosPesquisa
	   };
	   this.thDataExclusao();
	   if (this.tabelaTelaPrincipal == null) {
		   var chamadaRemota = ManterWorkflowDWR.pesquisar.bind(ManterWorkflowDWR);
		   this.tabelaTelaPrincipal = FactoryTabelas.getNewTabelaPaginada(this
		      .getTBodyTelaPrincipal(), chamadaRemota, this.popularTabela.bind(this));
		   this.tabelaTelaPrincipal.setQtdRegistrosPagina(QTD_REGISTROS_PAGINA);
	   }
	   this.tabelaTelaPrincipal.setParametros(dto);
	   this.tabelaTelaPrincipal.executarChamadaRemota();
   },

   /**
	 * Popula a tabela principal com a lista de workflows.
	 * 
	 * @param listaWorkflow lista de workflows retornados
	 */
   popularTabela : function(listaWorkflow) {
	   this.tabelaTelaPrincipal.removerResultado();

	   if (listaWorkflow.length != 0) {
		   var cellfuncs = new Array();
		   cellfuncs.push( function(workflow) {
			   return Builder.node("input", {
			      type :"hidden",
			      name :"id",
			      value :workflow.id
			   });
		   });
		   cellfuncs.push( function(workflow) {
			   return workflow.nome;
		   });
		   cellfuncs.push( function(workflow) {
			   return workflow.descricao;
		   });
		   cellfuncs.push( function(workflow) {
			   if (workflow.ativo) {
				   return "Sim";
			   }
			   return "N�o";
		   });
		   if (this.excluidosPesquisa) {
			   cellfuncs.push( function(workflow) {
				   if (workflow.dataHoraExclusao != null) {
					   return getStringTimestamp(workflow.dataHoraExclusao);
				   }
				   return "&nbsp;";
			   });
		   }
		   this.tabelaTelaPrincipal.adicionarResultadoTabela(cellfuncs);
		   this.tabelaTelaPrincipal.setOnClick(this.visualizar.bind(this));
		   this.tabelaTelaPrincipal.setOnDblClick(this.popupGerenciarProcessos.bind(this));
	   } else {
		   this.tabelaTelaPrincipal.semRegistros("N�o foram encontrados workflows");
	   }
   },

   /**
	 * Envia ao action a a��o de atualizar os dados do workflow selecionado.
	 * 
	 * @param form form submetido
	 */
   atualizar : function(form) {
	   JanelasComuns.showConfirmDialog("Deseja atualizar o workflow selecionado?", ( function() {
		   requestUtils.submitForm(form, ( function() {
			   if (requestUtils.status) {
				   this.pesquisar();
			   }
		   }).bind(this));
	   }).bind(this));
   },

   /**
	 * Envia ao action a a��o de excluir o workflow selecionado.
	 * 
	 * @param form form submetido
	 */
   excluir : function() {
	   JanelasComuns.showConfirmDialog("Deseja excluir o workflow selecionado?", ( function() {
		   var idWorkflow = dwr.util.getValue($("formAtualizarWorkflow").id);
		   requestUtils.simpleRequest("manterWorkflow.do?method=excluir&id=" + idWorkflow,
		      ( function() {
			      if (requestUtils.status) {
				      this.pesquisar();
			      }
		      }).bind(this));
	   }).bind(this));
   },

   /**
	 * Abre a janela para novo workflow.
	 */
   popupNovoWorkflow : function() {
	   var url = "manterWorkflow.do?method=popupNovoWorkflow";
	   createWindow(255, 321, 280, 40, "Novo Workflow", "divNovoWorkflow", url, ( function() {
		   this.contaChar(true);
	   }).bind(this));
   },

   /**
	 * Abre janela para gerenciar os processos.
	 */
   popupGerenciarProcessos : function() {
	   var idWorkflow = dwr.util.getValue($("formAtualizarWorkflow").id);
	   var nomeWorkflow = dwr.util.getValue($("formAtualizarWorkflow").nomeWorkflow);
	   var url = "manterProcesso.do?method=entrada&workflow=" + idWorkflow;
	   createWindow(536, 985, 280, 10, "Gerenciar Processos - " + nomeWorkflow,
	      "divGerenciarProcessos", url, ( function() {
		      processo.entrada();
	      }));
   },

   /**
	 * Abre janela para gerenciar os campos.
	 */
   popupGerenciarCampos : function() {
	   var idWorkflow = dwr.util.getValue($("formAtualizarWorkflow").id);
	   var nomeWorkflow = dwr.util.getValue($("formAtualizarWorkflow").nomeWorkflow);
	   var url = "manterCampo.do?method=entrada&workflow=" + idWorkflow;
	   createWindow(536, 985, 280, 10, "Gerenciar Campos - " + nomeWorkflow, "divGerenciarCampos",
	      url, ( function() {
		      campo.pesquisar();
	      }));
   },

   /**
	 * Envia ao action a a��o de salvar os dados do workflow.
	 * 
	 * @param form formul�rio submetido
	 */
   salvar : function(form) {
	   requestUtils.submitForm(form, ( function() {
		   if (requestUtils.status) {
			   JanelaFactory.fecharJanela("divNovoWorkflow");
			   this.pesquisar();
		   }
	   }).bind(this));
   },

   /**
	 * Limita a quantidade de caracteres do campo descri��o.
	 * 
	 * @param (Boolean) novo se for novo workflow
	 */
   contaChar : function(novo) {
	   if (novo) {
		   contaChar($("descricaoNovoWorkflow"), 200, "contagemNovoWorkflow");
	   } else {
		   contaChar($("descricaoWorkflow"), 200, "contagemWorkflow");
	   }
   },

   /**
	 * Habilita/desabilita os links, para quando um workflow seja selecionado.
	 * 
	 * @param (Boolean) caso seja para habilitar ou desabilitar
	 */
   habilitarLinks : function(habilita) {
	   if (habilita) {
		   $("linkGerenciarProcessos").className = "";
		   $("linkGerenciarProcessos").onclick = this.popupGerenciarProcessos;
		   $("linkGerenciarCampos").className = "";
		   $("linkGerenciarCampos").onclick = this.popupGerenciarCampos;
		   if (Usuario.temPermissao(MANTER_WORKFLOW)) {
			   $("linkDuplicarWorkflow").className = "";
			   $("linkDuplicarWorkflow").onclick = this.duplicar;
		   }
	   } else {
		   $("linkGerenciarProcessos").className = "btDesativado";
		   $("linkGerenciarProcessos").onclick = Prototype.emptyFunction;
		   $("linkGerenciarCampos").className = "btDesativado";
		   $("linkGerenciarCampos").onclick = Prototype.emptyFunction;
		   $("linkDuplicarWorkflow").className = "btDesativado";
		   $("linkDuplicarWorkflow").onclick = Prototype.emptyFunction;
	   }
   },

   /**
	 * Filtra os workflows excluidos.
	 * 
	 * @param {Boolean} excluido se deve mostrar os workflows excluidos
	 */
   filtrarAtivos : function(excluido) {
	   selecionarImagem("imagemAtivo", "imagemInativo", excluido);
	   this.excluidosPesquisa = excluido;
	   this.pesquisar();
   },

   /**
	 * Exibe/esconde a th da data de exclus�o do workflow.
	 */
   thDataExclusao : function() {
	   if (this.excluidosPesquisa) {
		   $("thExcluidos").show();
	   } else {
		   $("thExcluidos").hide();
	   }
   },

   /**
	 * Faz a duplica��o de um workflow.
	 */
   duplicar : function() {
	   JanelasComuns.showConfirmDialog("Deseja duplicar o workflow selecionado?", ( function() {
		   requestUtils.simpleRequest("manterWorkflow.do?method=copiar&id=" + $F("id"), ( function() {
			   if (requestUtils.status) {
				   workflow.pesquisar();
			   }
		   }));
	   }));
   }
};

var workflow = new ManterWorkflow();
