Ext.override(Ext.form.Field, {
	hasHelpIcon: false,
	clsHelp: "x-help-field",
	clickIconHelp: '',
	textHelpIcon: '',
	offsetIcon: 2,
	tooltip: '',
	Ayuda: '',
	onRender: function (C, A) {
		Ext.form.Field.superclass.onRender.call(this, C, A);
		if (!this.el) {
			var B = this.getAutoCreate();
			if (!B.name) {
				B.name = this.name || this.id
			}
			if (this.inputType) {
				B.type = this.inputType
			}
			this.el = C.createChild(B, A)
		}
		var D = this.el.dom.type;
		if (D) {
			if (D == "password") {
				D = "text"
			}
			this.el.addClass("x-form-" + D)
		}
		if (this.readOnly) {
			this.el.dom.readOnly = true
		}
		if (this.tabIndex !== undefined) {
			this.el.dom.setAttribute("tabIndex", this.tabIndex)
		}
		this.el.addClass([this.fieldClass, this.cls]);

		if (this.tooltip != "") {
			this.setToolTip();
		}

		if (this.ayuda != undefined && this.ayuda != '') {
			if (this.Ayuda == undefined || this.Ayuda == '') this.Ayuda = this.ayuda;
		}

		/*
			Asigna un ícono de ayuda al componente
		*/
		if (this.xtype != "hidden") {
			if (this.Ayuda != undefined && this.Ayuda != '') {
				/*var B = this.el.findParent(".x-form-element", 5, true);
				this.helpIcon = B.createChild({
					cls: this.clsHelp,
					onclick: this.clickIconHelp 
				});
				this.alignHelpIcon();
				this.helpIcon.dom.qtip = this.getTextHelpIcon();
				this.helpIcon.show();
				this.on("resize", this.alignHelpIcon, this);
				//this.fireEvent("resize", this, C); */
	
			
			          var label = this.el.findParent('.x-form-element', 5, true) || this.el.findParent('.x-form-field-wrap', 5, true);
			          
			          this.helpIcon = label.createChild({
			            cls:(this.helpIconCls || 'ux-helpicon-icon')
			          ,    style:'width:16px; height:18px; position:absolute; left:0; top:0; display:block; background:transparent no-repeat scroll 0 2px;'
			          });
			          
			          this.alignHelpIcon = function(){
			            var el = this.wrap ? this.wrap : this.el; 
			            //alert("control: " + this.name + "\n triggerClass: " + this.triggerClass);
			            if (this.triggerClass) {
			            	this.helpIcon.alignTo(el, 'tl-tr', [2, 0]);
			            } else this.helpIcon.alignTo(el, 'tl-tr', [2, 0]);
			            
			          }
			          //Redefine alignErrorIcon to move the errorIcon (if it exist) to the right of helpIcon
			          if(this.alignErrorIcon) {
			            this.alignErrorIcon = function() {
			              this.errorIcon.alignTo(this.helpIcon, 'tl-tr', [2, 0]);
			            }
			          }
			          
			          this.on('resize', this.alignHelpIcon, this);
			          
			          //Register QuickTip for icon
			          Ext.QuickTips.register({
			            target:this.helpIcon
			          , title:(this.helpTitle || '')
			          , text:(this.Ayuda || '')
			          , enabled:true
			          });
			          this.fireEvent("resize", this, C);
			          
			};
			/*
				Fin Asignación de ícono de ayuda al componente
			*/
		}
		this.initValue();



	},
	
	alignHelpIcon: function () {
		this.helpIcon.alignTo(this.el, "tl-tr", [this.offsetIcon, 0]);
	},
	
	setTextHelpIcon: function (t) {
		this.textHelpIcon = t;
	},
	
	getTextHelpIcon: function () {
		return this.textHelpIcon;
	},
	
	setToolTip: function () {
		if (this.tooltip != undefined) {
			if (typeof this.tooltip == "object") {
				Ext.QuickTips.unregister(this.getEl());
			}else {
				this.getEl().dom[this.tooltipType] = null;
			}
			Ext.QuickTips.register({
				target: this.getEl(),
				text: this.tooltip
			});
		}
	},
	
	setFieldLabel: function(lbl) {
		this.fieldLabel = lbl;
		var _label = Ext.DomQuery.select(String.format('label[for="{0}"]', this.id));
		if (_label) {
			_label[0].childNodes[0].nodeValue = this.fieldLabel;
		}
	}
});

Ext.override(Ext.grid.GridView, {
	sortAscText : "Orden Ascendente",
	sortDescText : "Orden Descendente",
	columnsText : "Columnas"
});

Ext.override(Ext.PagingToolbar, {
	displayMsg : "Mostrando registros {0} - {1} de {2}",
	emptyMsg : "No hay registros para mostrar",
	beforePageText : "P&aacute;gina",
	afterPageText : "de {0}",
	firstText : "Primera P&aacute;gina",
	prevText : "P&aacute;gina Anterior",
	nextText : "Siguiente P&aacute;gina",
	lastText : "Ultima P&aacute;gina",
	refreshText : "Actualizar",
	overrideOnClick: false,

	onLoad : function(A, C, F) {
		if (!this.rendered) {
			this.dsLoaded = [A, C, F];
			//alert(0);
			return
		}
		this.cursor = F.params ? F.params[this.paramNames.start] : 0;
		var E = this.getPageData(), B = E.activePage, D = E.pages;
		this.afterTextEl.el.innerHTML = String.format(this.afterPageText,
				E.pages);
		this.field.dom.value = B;
		this.first.setDisabled(B == 1);
		this.prev.setDisabled(B == 1);
		this.next.setDisabled(B == D);
		this.last.setDisabled(B == D);
		this.loading.enable();
		this.updateInfo()
	},

	updateInfo : function() {
		if (this.displayEl) {
			var A = this.store.getCount();
			var B ;
			if (A == 0) {
				B = String.format(this.displayMsg,
						0/*this.cursor + 1*/, 0/*this.cursor + A*/, this.store.getCount());
				this.displayEl.update(B);
				this.afterTextEl.el.innerHTML = String.format(this.afterPageText, 1);
				this.field.dom.value = 1;
				this.next.setDisabled(true);
				this.last.setDisabled(true);
				this.first.setDisabled(true);
				this.prev.setDisabled(true);
			} else {
				var B = A == 0 ? this.emptyMsg : String.format(this.displayMsg,
						this.cursor + 1, this.cursor + A, this.store
								.getTotalCount());
				this.displayEl.update(B)
			}
		}
	},

	onLoadError : function() {
		if (!this.rendered) {
			return
		}
		this.store.removeAll();
		this.loading.enable();
		this.updateInfo ();
	},

	onClick : function(E) {
		if (!this.overrideOnClick) {
			var B = this.store;
			switch (E) {
				case "first" :
					this.doLoad(0);
					break;
				case "prev" :
					this.doLoad(Math.max(0, this.cursor - this.pageSize));
					break;
				case "next" :
					this.doLoad(this.cursor + this.pageSize);
					break;
				case "last" :
					var D = B.getTotalCount();
					var A = D % this.pageSize;
					var C = A ? (D - A) : D - this.pageSize;
					this.doLoad(C);
					break;
				case "refresh" :
					this.doLoad(this.cursor);
					break
			}
		}
	}
});

Ext.override(Ext.form.TextField, {
	blankText: 'Este campo es requerido',
	minLengthText : "El tama&ntilde;o m&iacute;nimo para este campo es {0}",
	maxLengthText : "El tama&ntilde;o m&aacute;ximo para este campo es {0}"
});

Ext.form.VTypes = function() {
	var C = /^[a-zA-Z_]+$/;
	var D = /^[a-zA-Z0-9_]+$/;
	var B = /^([\w]+)(.[\w]+)*@([\w-]+\.){1,5}([A-Za-z]){2,4}$/;
	var A = /(((https?)|(ftp)):\/\/([\-\w]+\.)+\w{2,3}(\/[%\-\w]+(\.\w{2,})?)*(([\w\-\.\?\\\/+@&#;`~=%!]*)(\.\w{2,})?)*\/?)/i;
	return {
		"email" : function(E) {
			return B.test(E)
		},
		"emailText" : "Este campo debe ser una direcci&oacute;n de e-mail con el formato \"usuario@dominio.com\"",
		"emailMask" : /[a-z0-9_\.\-@]/i,
		"url" : function(E) {
			return A.test(E)
		},
		"urlText" : "Este campo debe ser una URL en el formato \"http:/"
				+ "/www.domain.com\"",
		"alpha" : function(E) {
			return C.test(E)
		},
		"alphaText" : "Este campo solo debe contener letras y _",
		"alphaMask" : /[a-z_]/i,
		"alphanum" : function(E) {
			return D.test(E)
		},
		"alphanumText" : "Este campo debe contener letras, n&uacute;meros y _",
		"alphanumMask" : /[a-z0-9_]/i
	}
}();

Ext.override(Ext.form.ComboBox, {
	offsetIcon: 18,
	blankText: 'Este campo es requerido',
	setStore : function(A, B) {

		if (!B && this.store && this.store.un) {
			this.store.un("beforeload", this.onBeforeLoad, this);
			this.store.un("datachanged", this.refresh, this);
			this.store.un("add", this.onAdd, this);
			this.store.un("remove", this.onRemove, this);
			this.store.un("update", this.onUpdate, this);
			this.store.un("clear", this.refresh, this)
		}
		if (A) {
			A = Ext.StoreMgr.lookup(A);
			A.on("beforeload", this.onBeforeLoad, this);
			A.on("datachanged", this.refresh, this);
			A.on("add", this.onAdd, this);
			A.on("remove", this.onRemove, this);
			A.on("update", this.onUpdate, this);
			A.on("clear", this.refresh, this)
		}
		this.store = A;
		if (A) {
			this.refresh()
		}
	},
	findRecord : function(C, B) {
		var A;
		if (this.store && this.store.getCount) {
			if (this.store.getCount() > 0) {
				this.store.each(function(D) {
					if (D.data[C] == B) {
						A = D;
						return false
					}
				})
			}
			return A
		}
	},
	destroy : function() {
		if (this.store && this.store.un) {
			this.store.un("beforeload", this.onBeforeLoad, this);
			this.store.un("load", this.onLoad, this);
			this.store.un("loadexception", this.onLoad, this)
		} else {
			var A = this.el.getUpdater();
			A.un("beforeupdate", this.onBeforeLoad, this);
			A.un("update", this.onLoad, this);
			A.un("failure", this.onLoad, this)
		}
	}
	
});	

Ext.override(Ext.Button, {
	setToolTip: function (tpl) {
		if (tpl != "") {
			this.tooltip = tpl;
		}
		if (typeof this.tooltip == "object") {
			Ext.QuickTip.register(Ext.apply({target: this.id}, this.tooltip));
		} else {
			this.getEl().child(this.buttonSelector).dom[this.tooltipType] = this.tooltip;
		}
	}
});

/**
*	Se sobrescribe esta clase por el issue ACW-1376
*
*/
Ext.override(Ext.form.HtmlEditor, {
	enableToolbar: true,
	createFontOptions : function() {
		var D = [], B = this.fontFamilies, C, F;
		for (var E = 0, A = B.length; E < A; E++) {
			C = B[E];
			F = C.toLowerCase();
			D.push("<option value=\"", F, "\" style=\"font-family:", C, ";color: black\"",
					(this.defaultFont == F ? " selected=\"true\">" : ">"), C,
					"</option>")
		}
		return D.join("")
	},

	onRender : function(C, A) {
		Ext.form.HtmlEditor.superclass.onRender.call(this, C, A);
		this.el.dom.style.border = "0 none";
		this.el.dom.setAttribute("tabIndex", -1);
		this.el.addClass("x-hidden");
		if (Ext.isIE) {
			this.el.applyStyles("margin-top:-1px;margin-bottom:-1px;")
		}
		this.wrap = this.el.wrap({
			cls : "x-html-editor-wrap",
			cn : {
				cls : "x-html-editor-tb"
			}
		});
		if (this.enableToolbar) {
			this.createToolbar(this);
			this.tb.items.each(function(E) {
				if (E.itemId != "sourceedit") {
					E.disable()
				}
			});
		}
		var D = document.createElement("iframe");
		D.name = Ext.id();
		D.frameBorder = "no";
		D.src = (Ext.SSL_SECURE_URL || "javascript:false");
		this.wrap.dom.appendChild(D);
		this.iframe = D;
		if (Ext.isIE) {
			D.contentWindow.document.designMode = "on";
			this.doc = D.contentWindow.document;
			this.win = D.contentWindow
		} else {
			this.doc = (D.contentDocument || window.frames[D.name].document);
			this.win = window.frames[D.name];
			this.doc.designMode = "on"
		}
		this.doc.open();
		this.doc.write(this.getDocMarkup());
		this.doc.close();
		var B = {
			run : function() {
				if (this.doc.body || this.doc.readyState == "complete") {
					Ext.TaskMgr.stop(B);
					this.doc.designMode = "on";
					this.initEditor.defer(10, this)
				}
			},
			interval : 10,
			duration : 10000,
			scope : this
		};
		Ext.TaskMgr.start(B);
		if (!this.width) {
			this.setSize(this.el.getSize())
		}
	}
});
/**
*	Override para proveer soporte para timeout. Si al ejecutar una petición Ajax se produce un
*	timeout, se devuelve una respuesta JSON que contiene un atributo llamado errCode con un valor
*	igual a -999999 que indica que la sesión ha expirado. Cuando se encuentra ese atributo en la
*	respuesta JSON, se hace un redireccionamiento a la página de login.
*/
Ext.override(Ext.data.Connection, {

	handleResponse: function (A) {
		if (A.responseText) {
			var _response;
			try {
				_response = Ext.util.JSON.decode(A.responseText);
			}catch (e) {}
			if (_response && _response.errCode && _response.errCode == '-999999') {
				location.href = "http://acw.biosnettcs.com:7778/login/login.html"; 
			}else {
				this.transId = false;
				var B = A.argument.options;
				A.argument = B ? B.argument : null;
				this.fireEvent("requestcomplete", this, A, B);
				Ext.callback(B.success, B.scope, [A, B]);
				Ext.callback(B.callback, B.scope, [B, true, A])
			}
		}else {
			this.transId = false;
			var B = A.argument.options;
			A.argument = B ? B.argument : null;
			this.fireEvent("requestcomplete", this, A, B);
			Ext.callback(B.success, B.scope, [A, B]);
			Ext.callback(B.callback, B.scope, [B, true, A])
		}
	}
});
/**
*	Override que permite asociar un ícono de ayuda al form de una pantalla.
*	Cuando se hace click sobre dicho ícono, se abre un popup con la ayuda
*	en línea asociada a esta pantalla. La función que abre el popup se
*	encuentra en el decorator default2.jsp
*
*/
Ext.override(Ext.FormPanel, {
	hasHelp: true,

	afterRender : function() {
		if (this.fromMarkup && this.height === undefined && !this.autoHeight) {
			this.height = this.el.getHeight()
		}
		if (this.floating && !this.hidden && !this.initHidden) {
			this.el.show()
		}
		if (this.title) {
			this.setTitle(this.title)
		}
		if (this.autoScroll) {
			this.body.dom.style.overflow = "auto"
		}
		if (this.html) {
			this.body.update(typeof this.html == "object" ? Ext.DomHelper
					.markup(this.html) : this.html);
			delete this.html
		}
		if (this.contentEl) {
			var A = Ext.getDom(this.contentEl);
			Ext.fly(A).removeClass(["x-hidden", "x-hide-display"]);
			this.body.dom.appendChild(A)
		}
		if (this.collapsed) {
			this.collapsed = false;
			this.collapse(false)
		}
		Ext.Panel.superclass.afterRender.call(this);
		this.initEvents();

		if (this.header && this.hasHelp) {
		var template = '<div class=\"ux-helpicon-form\" onclick=\"mostrarAyuda();return false;\">&#160;</div>';
		this.header.insertHtml("beforeBegin", template, "ux-helpicon-form");
		}
		//Ext.DomHelper.insertHtml("beforebegin", this.header, template);
	}
});