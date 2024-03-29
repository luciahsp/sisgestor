/**
 * Comportamentos para o UC Manter Tarefa.
 * 
 * @author Thiago
 * @since 16/02/2009
 */
var ManterTarefa = Class.create();
ManterTarefa.prototype = {
   /**
	 * @constructor
	 */
   initialize : function() {},

   tabelaTelaPrincipal :null,

   /**
	 * Retorna a tabela da tela inicial do caso de uso
	 * 
	 * @return {HTMLTableSectionElement}
	 */
   getTBodyTelaPrincipal : function() {
	   return $("corpoManterTarefa");
   },

   /**
	 * Recupera o form manterTarefaForm.
	 * 
	 * @return form do manter tarefa
	 */
   getForm : function() {
	   return $("manterTarefaForm");
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
	 * Recupera o id da tarefa selecionada.
	 * 
	 * @return id da tarefa selecionada
	 */
   getIdSelecionado : function() {
	   return this.getTR().select("input[type=\"hidden\"]")[0].value;
   },

   /**
	 * Preenche os campos da tarefa selecionada.
	 */
   visualizar : function() {
	   Element.hide("formAtualizarTarefa");
	   var idTarefa = this.getIdSelecionado();
	   if (isNaN(idTarefa)) {
		   return;
	   }
	   ManterTarefaDWR.getById(idTarefa, ( function(tarefa) {
		   Effect.Appear("formAtualizarTarefa");
		   dwr.util.setValue($("formAtualizarTarefa").id, idTarefa);
		   dwr.util.setValue("nomeTarefa", tarefa.nome);
		   dwr.util.setValue("descricaoTarefa", tarefa.descricao);
		   if (tarefa.usuario == null) {
			   dwr.util.setValue("usuario", "");
		   } else {
			   dwr.util.setValue("usuario", tarefa.usuario.id);
		   }

		   this.contaChar(false);
	   }).bind(this));
   },

   /**
	 * Faz a pesquisa dos tarefas pelos par�metros informados.
	 */
   pesquisar : function() {
	   Effect.Fade("formAtualizarTarefa");
	   var dto = {
	      nome :dwr.util.getValue("nomePesquisaTarefa"),
	      descricao :dwr.util.getValue("descricaoPesquisaTarefa"),
	      usuario :dwr.util.getValue("usuarioPesquisa"),
	      idAtividade :dwr.util.getValue("atividade")
	   };

	   if ((this.tabelaTelaPrincipal == null)
	      || (this.tabelaTelaPrincipal.getTabela() != this.getTBodyTelaPrincipal())) {
		   var chamadaRemota = ManterTarefaDWR.pesquisar.bind(ManterTarefaDWR);
		   this.tabelaTelaPrincipal = FactoryTabelas.getNewTabelaPaginada(this
		      .getTBodyTelaPrincipal(), chamadaRemota, this.popularTabela.bind(this));
		   this.tabelaTelaPrincipal.setQtdRegistrosPagina(QTD_REGISTROS_PAGINA_TAREFA);
	   }
	   this.tabelaTelaPrincipal.setParametros(dto);
	   this.tabelaTelaPrincipal.executarChamadaRemota();
   },

   /**
	 * Popula a tabela principal com a lista de tarefas.
	 * 
	 * @param listaTarefa lista de tarefas retornadas
	 */
   popularTabela : function(listaTarefa) {
	   this.tabelaTelaPrincipal.removerResultado();

	   if (listaTarefa.length != 0) {
		   var cellfuncs = new Array();
		   cellfuncs.push( function(tarefa) {
			   return Builder.node("input", {
			      type :"hidden",
			      name :"id",
			      value :tarefa.id
			   });
		   });
		   cellfuncs.push( function(tarefa) {
			   return tarefa.nome;
		   });
		   cellfuncs.push( function(tarefa) {
			   return tarefa.descricao;
		   });
		   cellfuncs.push( function(tarefa) {
			   if (tarefa.usuario == null) {
				   return "";
			   }
			   return tarefa.usuario.nome;
		   });
		   this.tabelaTelaPrincipal.adicionarResultadoTabela(cellfuncs);
		   this.tabelaTelaPrincipal.setOnClick(this.visualizar.bind(this));
	   } else {
		   this.tabelaTelaPrincipal.semRegistros("N�o foram encontradas tarefas");
	   }
   },

   /**
	 * Envia ao action a a��o de atualizar os dados da tarefa selecionada.
	 * 
	 * @param form form submetido
	 */
   atualizar : function(form) {
	   JanelasComuns.showConfirmDialog("Deseja atualizar a tarefa selecionada?", ( function() {
		   requestUtils.submitForm(form, ( function() {
			   if (requestUtils.status) {
				   this.pesquisar();
			   }
		   }).bind(this));
	   }).bind(this));
   },

   /**
	 * Envia ao action a a��o de excluir a tarefa selecionada.
	 * 
	 * @param form form submetido
	 */
   excluir : function() {
	   JanelasComuns.showConfirmDialog("Deseja excluir a tarefa selecionada?", ( function() {
		   var idTarefa = dwr.util.getValue($("formAtualizarTarefa").id);
		   requestUtils.simpleRequest("manterTarefa.do?method=excluir&id=" + idTarefa, ( function() {
			   if (requestUtils.status) {
				   this.pesquisar();
			   }
		   }).bind(this));
	   }).bind(this));
   },

   /**
	 * Abre a janela para nova tarefa.
	 */
   popupNovaTarefa : function() {
	   var idAtividade = dwr.util.getValue($("formAtualizarAtividade").id);
	   var url = "manterTarefa.do?method=popupNovaTarefa&atividade=" + idAtividade;
	   createWindow(285, 330, 280, 40, "Nova Tarefa", "divNovaTarefa", url, ( function() {
		   dwr.util.setValue("atividadeNovaTarefa", $F("atividadeTarefa"));
		   this.contaChar(true);
	   }).bind(this));
   },

   /**
	 * Abre a janela para definir fluxo das atividades.
	 */
   popupDefinirFluxoTarefas : function() {
	   var url = "manterTarefa.do?method=popupDefinirFluxo";
	   var apenasVisualizar = this._isApenasVisualizacao();
	   var tipoTitulo = apenasVisualizar ? "Visualizar " : "Definir ";
	   createWindow(486, 840, 280, 40, tipoTitulo + "Fluxo das Tarefas", "divFluxoTarefa", url,
	      ( function() {
		      var idAtividade = $F("atividadeTarefa");
		      dwr.util.setValue("atividadeFluxo", idAtividade);
		      if (apenasVisualizar) {
			      $("botaoSalvarDefinirFluxoTarefas").disable();
			      $("botaoLimparDefinirFluxoTarefas").disable();
		      }
		      fluxo = new DefinirFluxo();
		      ManterTarefaDWR.getByAtividade(idAtividade, ( function(listaTarefas) {
			      fluxo.defineFluxoDefinido(listaTarefas);
		      }));
	      }));
   },

   /**
	 * Envia ao action a a��o de salvar o fluxo.
	 */
   salvarFluxo : function() {
	   var form = getForm($("definirFluxoManterTarefaForm"));
	   form.fluxos = fluxo.listaFluxos;
	   form.posicoes = fluxo.obterPosicoes();
	   JanelasComuns.showConfirmDialog("Deseja definir o fluxo criado?", ( function() {
		   requestUtils.simpleRequest("manterTarefa.do?method=salvarFluxo&"
		      + Object.toQueryString(form), ( function() {
			   if (requestUtils.status) {
				   JanelaFactory.fecharJanela("divFluxoTarefa");
			   }
		   }), grafico.processarResposta);
	   }));
   },

   /**
	 * Envia ao action a a��o de salvar os dados da tarefa.
	 * 
	 * @param form formul�rio submetido
	 */
   salvar : function(form) {
	   requestUtils.submitForm(form, ( function() {
		   if (requestUtils.status) {
			   JanelaFactory.fecharJanela("divNovaTarefa");
			   this.pesquisar();
		   }
	   }).bind(this));
   },

   /**
	 * Limita a quantidade de caracteres do campo descri��o.
	 */
   contaChar : function(novo) {
	   if (novo) {
		   contaChar($("descricaoNovo"), 200, "contagemNovo");
	   } else {
		   contaChar($("descricaoTarefa"), 200, "contagemTarefa");
	   }
   },

   /**
	 * Executado a cada vez no in�cio do UC.
	 */
   entrada : function() {
	   this.pesquisar();
	   if (this._isApenasVisualizacao()) {
		   UtilDWR.getMessage("dica.visualizarFluxo", null, ( function(mensagem) {
			   $("linkDefinirFluxoTarefa").title = mensagem;
		   }));
		   $("botaoExcluirTarefa").disable();
		   $("linkNovaTarefa").className = "btDesativado";
		   $("linkNovaTarefa").onclick = Prototype.emptyFunction;
	   }
	   if($F("excluidoWorkflow") == "true") {
	   	$("botaoAtualizarTarefa").disable();
	   }
   },

   /**
	 * Recupera caso o usu�rio deva apenas visualizar o fluxo, e n�o definir.
	 * 
	 * @return <code>true</code> caso usu�rio deve apenas visualizar o fluxo.
	 * @type Boolean
	 */
   _isApenasVisualizacao : function() {
	   return ($F("workflowAtivadoOuExcluido") == "true") || !Usuario.temPermissao(MANTER_WORKFLOW);
   }
};

var tarefa = new ManterTarefa();
