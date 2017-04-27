Ext.Loader.setPath('Ext.org', '../../js/confpantallas');
Ext.Loader.setPath('Ext.ux.js', '../../js/confpantallas');

Ext.require(['Ext.org.ImgViewPaneles','Ext.org.ImgViewControles']);
Ext.require(['*']);

//var inicia = false;
var panelesId = 0;
var columnasId = 0;
var tabsId = 0;
var acordionId = 0;
var tablasId = 0;
var borderId = 0;
var ventanaId = 0;
var textoId = 0;
var numericId = 0; var labelId = 0; var imagenId = 0; var hiddenId = 0; var gridId = 0; var gridIdH = 0;
var pickerId = 0;
var checkId = 0;
var radioId = 0;
var botonId = 0;
var comboId = 0;
var panelTabId =0;
var panelAcordionId = 0;
var treeTabla = '';
var ctrl = '';
var nCtrl = 0;
var cChk = false;
var bChk = false;
var namePnl = '';
var focusPanel='';
var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
var isOpen = true;

Ext.onReady(function() {
    Ext.QuickTips.init();
    Ext.Ajax.timeout = 350000;

    var target = new Ext.Panel({ 
    	region:'center', 
    	layout: {
            type: 'table',
            columns: 1
        },
    	border: false,
    	autoScroll:true,
    	id:'winContenedorF',
    	bodyStyle:'font-size:13px',
    	afterRender:function() { 
    		Ext.Panel.prototype.afterRender.apply(this, arguments); 
    		this.dropTarget = Ext.getCmp('winContenedorF').body;
            var dd = new Ext.dd.DropTarget(this.dropTarget, {
    			ddGroup:'organizerDD',
    			notifyDrop:function(dd, e, node) { 
    				agrega(node.nodes[0].innerText);
    				return true; 
    			}
    		}); 
    	}
    });


var storeD = Ext.create('Ext.data.TreeStore', {
proxy : {
        type : 'ajax',
        url : '../../confpantallas/pintatreepanel.action?nodo=inicio_inicio&tabla=',
        reader : {
             type: 'json'
        }
    }
});
storeD.load();
    var viewport = Ext.create('Ext.Viewport', {
        id: 'border-gral',
        layout: 'border',
        items: [
        Ext.create('Ext.Component', {
            region: 'north',
            height: 32, 
            autoEl: {
                tag: 'div',
                html:'<center><p><b><font size="4" face="Georgia, Arial, Garamond">Generador de Pantallas</font></b></p></center>'
                //html:'<center><img src="../resources/img/tituloConsola.png"></center>'
            }
        }),{
            region: 'west',
            stateId: 'navigation-panel',
            id: 'west-panel', 
            title: 'Panel de Parametrización',
            split: true,
            width: 290,
            minWidth: 200,
            maxWidth: 380,
            collapsible: true,
            animCollapse: true,
            margins: '0 0 5 5',
            layout: 'accordion',
            items: [{
                title: 'Tipo de layout',
                iconCls: 'tipo_panel',
                items: [{
                        xtype: 'panel',
                        padding: '0 0 0 0',
                        border: false,
                        items: {
                            xtype: 'imageview',
                            trackOver: true
                        }
                }]
            }, {
                title: 'Controles',
                iconCls: 'settings',
                id:'idControles',
                items: [{
                        xtype: 'panel',
                        padding: '0 0 0 0',
                        border: false,
                        items: {
                            xtype: 'imageviewctrol',
                            trackOver: true
                        }
                }]               
            }, {
                title: 'Controles Predefinidos',
                id:'idCtrlsPredifinidos',
                iconCls: 'info',
            	autoScroll:true,
                items: [{
                	xtype: 'treepanel',
                	rootVisible: false,
                	useArrows:false,
                	animate:true,
                	border:false,
                	store: storeD,
                	listeners: {
        				itemdblclick: function(s,r) {
        					if(r.data.leaf){
        						if(focusPanel === ''){
									Ext.MessageBox.show({
										title:'Aviso Importante',
										msg: 'Debes seleccionar un panel para incluir el control.',
										buttons: Ext.MessageBox.OK,
										icon: Ext.MessageBox.ERROR
									});
        						}else{
									Ext.Ajax.request({
										url: '../../confpantallas/buscacontrol.action',
										params: {tarea:'getDatosCtl',strRamo:r.data.parentId,campo:r.data.text,tabla:treeTabla},
										success: function(response, opts){          
											var text = trim(response.responseText);
											var jsonResponse = Ext.JSON.decode(text);
											var arryAttr = [];
											var tipo = '';
											var nombre = 'parametros.pv_otvalor';
											var nAttr = jsonResponse[0].cdatribu;
											if(nAttr < 10){
												nAttr = '0'+nAttr;
											}
											nombre = nombre+nAttr;
											if( Ext.isEmpty(jsonResponse[0].ottabval) ){
												if(jsonResponse[0].swformat == 'F'){
													tipo = 'Picker';
												}else if(jsonResponse[0].swformat == 'P' || jsonResponse[0].swformat == 'N'){
													tipo = 'Numeric';
													arryAttr.push('valorMax:'+jsonResponse[0].nmlmax);
													arryAttr.push('valorMin:'+jsonResponse[0].nmlmin);
												}else if(jsonResponse[0].swformat == 'A'){
													tipo = 'Texto';
													arryAttr.push('textoMax:'+jsonResponse[0].nmlmax);
													arryAttr.push('textoMin:'+jsonResponse[0].nmlmin);
												}
											}else{
												tipo = 'Combo';
												arryAttr.push('store:'+jsonResponse[0].ottabval);
											}
											arryAttr.push('etiqueta:'+jsonResponse[0].dsatribu);
											arryAttr.push('nombre:'+nombre);
											agregaCtrl(tipo,focusPanel,arryAttr);
											setAttrCtrl('etiqueta',jsonResponse[0].dsatribu);
										}
									});
        						}
        					}
        				},
        				beforeitemexpand: function ( node, index, item, eOpts ){
        					if(node.data.parentId === 'root'){
        						treeTabla = node.data.id;
        					}
        					storeD.getProxy().url = '../../confpantallas/pintatreepanel.action?nodo=' + node.data.id + '&tabla=' + treeTabla;
						}
    				}

                }]
            }]
        }, 
        
        {
        	region:'center',
            collapsible: false,
            margins: '0 0 5 0',
            layout: 'border',
            id: 'superpanel',
            items: [target
                    /*{
                   	xtype	:	"panel",
                   	id : 'winContenedorF',
                   	region: 'center'
               }*/,{
                // lazily created panel (xtype:'panel' is default)
                region: 'south',
                id: 'pnlSur',
                html: '<p><b>Panel de Parametrización.- </b>Elemento que se utiliza para la definición y construcción de nuevas ventanas de forma dinámica.</p>',
                split: true,
                height: 100,
                minSize: 100,
                maxSize: 200,
                collapsible: true,
                //collapsed: true,
                title: 'Consola de descripciones, definiciones y funcionalidades',
                margins: '0 0 0 0'
            },{
                xtype: 'tabpanel',
                region: 'east',
                id: 'atributosPanel',
                title: 'Objetos, atributos y caracteristicas',
                dockedItems: [{
                    dock: 'top',
                    xtype: 'toolbar',
                    items: [
                    {
                       xtype: 'button',
                       iconCls: 'preview_v',
                       scale:'medium',
                       tooltip: 'Presentación Preliminar',
                       handler: fncPreliminar
                    },{
                       xtype: 'button',
                       iconCls: 'preview_save',
                       scale:'medium',
                       tooltip: 'Grabar Panel',
                       handler: fncAsignaNombrePanel
                    },{
                        xtype: 'button',
                        iconCls: 'preview_open',
                        scale:'medium',
                        tooltip: 'Abrir Panel',
                        handler: fncAbrePanelExistente
                     }]
                }],
                animCollapse: true,
                collapsible: true,
                split: true,
                width: 280, 
            	minWidth: 220,
            	maxWidth: 340,
                margins: '0 5 5 0',
                activeTab: 1,
                tabPosition: 'bottom',
                items: [{
                    title: 'Ejemplos',
                    id:'tabEjemplos'
                }, Ext.create('Ext.grid.PropertyGrid', {
                        title: 'Atributos',
                        id:'propGrid',
                        customEditors: {
                    		etiqueta_aling: Ext.create('Ext.form.ComboBox', {                                         
                        	store: storeListaEtiqueta_Aling,queryMode: 'local',displayField: 'key',valueField: 'value',
                        	editable:false
                    		}),
                    		escala: Ext.create('Ext.form.ComboBox', {                                         
                        	store: storeListaEscala,
                        	queryMode: 'local',
                        	displayField: 'key',
                        	valueField: 'value'
                    		}),
                    		estilo: Ext.create('Ext.form.ComboBox', {                                         
                        	store: storeListaEstilo,
                        	queryMode: 'local',
                        	displayField: 'key',
                        	valueField: 'value'
                    		}),
                    		winEstilo: Ext.create('Ext.form.ComboBox', {                                         
                            store: storeListaWinEstilo,
                            queryMode: 'local',
                            displayField: 'key',
                            valueField: 'value'
                        	}),                    		
                    		imagenCls: Ext.create('Ext.form.ComboBox', {                                         
                        	store: storeListaIconCls,
                        	queryMode: 'local',
                        	displayField: 'key',
                        	valueField: 'value'
                    		}),
                    		imagen_aling: Ext.create('Ext.form.ComboBox',{
                    		store: storeListaIconAling,
                    		queryMode: 'local',
                    		displayField: 'key',
                    		valueField: 'value',
                    		editable:false
                    		}),
                    		titulo_Posicion: Ext.create('Ext.form.ComboBox',{
                    		store: storetitulo_Posicion,
                    		queryMode: 'local',
                    		displayField: 'key',
                    		valueField: 'value',
                    		editable:false
                    		}),
                    		modo: Ext.create('Ext.form.ComboBox',{
                    		store: storeListaModo,
                    		queryMode: 'local',
                    		displayField: 'key',
                    		valueField: 'value',
                    		editable:false
                    		}),
                    		selectAction: Ext.create('Ext.form.ComboBox', {store: storeListaSelectAction,queryMode: 'local',displayField: 'key',valueField: 'value',
                        	editable:false
                    		}),
                    		valorDisplay: Ext.create('Ext.form.ComboBox', {store: storeListaValorDisplay,queryMode: 'local',displayField: 'key',valueField: 'value',
                        	editable:false
                    		}),
                    		valorId: Ext.create('Ext.form.ComboBox', {store: storeListaValorId,queryMode: 'local',displayField: 'key',valueField: 'value',
                        	editable:false
                    		}),
                    		titulo_Aling: Ext.create('Ext.form.ComboBox', {store: storeListaTituloAling,queryMode: 'local',displayField: 'key',valueField: 'value',
                        	editable:false
                    		})
                },
                        source: {
                            "(id)": "superpanel"
                        },
               listeners: {
        			beforecellclick: {
            			fn: function(j, td, cellIndex, record, tr, rowIndex, e, eOpts ){ 
            				if(record.data.name == '(id)'){
            					setSur('<p><b>(id).- </b>Es el identificador único del control [en tiempo de ejecución] será elque designes en el atributo nombre. <b>[ExtJs.-id]</b></p>');
            				}else if(record.data.name == 'height'){
            					setSur('<p><b>Height.- </b>Es el atributo que define el tamaño del [alto] del control. <b>[ExtJs.-height]</b></p>');
            				}else if(record.data.name == 'nombre'){
            					setSur('<p><b>Nombre.- </b>Es el id que tomará el control [en tiempo de ejecución] así como el atributo name del objeto. <b>[ExtJs.-name]</b></p>');
            				}else if(record.data.name == 'tipo'){
            					setSur('<p><b>Tipo.- </b>Es el nombre del tipo de control que utiliza ExtJS.</p>');
            				}else if(record.data.name == 'titulo'){
            					setSur('<p><b>Título.- </b>Es el encabezado que se mostrará en el panel/ventana. <b>[ExtJs.-title]</b></p>');
            				}else if(record.data.name == 'width'){
            					setSur('<p><b>Width.- </b>Es el atributo que define el tamaño del [ancho] del control.  <b>[ExtJs.-width]</b></p>');
            				}else if(record.data.name == 'etiqueta'){
            					setSur('<p><b>Etiqueta.- </b>Es el texto que describirá al campo de texto. <b>[ExtJs.-fieldLabel]</b></p>');
            				}else if(record.data.name == 'isRequerido'){
            					setSur('<p><b>IsRequerido.- </b>[True/False] True.-Si el campo de texto va a ser obligatorio dentro del formulario. <b>[ExtJs.-allowBlank]</b></p>');
            				}else if(record.data.name == 'isAnchor'){
            					setSur('<p><b>IsAnchor.- </b>Define el % de tamaño que va a tener el control respecto a su contenerdor ajustandose cuendo se redimenciona la forma, [en tiempo de ejecución] si el valor es [0] no será conciderada dicha propiedad. <b>[ExtJs.-anchor]</b></p>');
            				}else if(record.data.name == 'etiqueta_width'){
            					setSur('<p><b>Etiqueta Width.- </b>Define el ancho del texto que describirá al campo de texto solo visible [en tiempo de ejecución]. Con valor [0] se omite el atributo. <b>[ExtJs.-labelWidth]</b></p>');
            				}else if(record.data.name == 'isBloqueado'){
            					setSur('<p><b>IsBloqueado.- </b>[True/False] True.-Si el campo de texto se quiere apagar/bloquear. <b>[ExtJs.-disabled]</b></p>');
            				}else if(record.data.name == 'textoSugerido'){
            					setSur('<p><b>TextoSugerido.- </b>Es el atributo que suguiere un texto fantasma en el cuadro de texto. Si se deja vacío se omitirá el atributo [en tiempo de ejecución]. <b>[ExtJs.-emptyText]</b></p>');
            				}else if(record.data.name == 'textoMax'){
            					setSur('<p><b>TextoMax.- </b>Es el atributo que define el número máximo de caracteres que pudiera contener el cuadro de texto. Si esta en [0] [en tiempo de ejecución] el atributo no es considerado. <b>[ExtJs.-maxLength]</b></p>');
            				}else if(record.data.name == 'textoMaxMsg'){
            					setSur('<p><b>TextoMaxMsg.- </b>Es el mensaje que aparece cuendo se sobrepaso el número máximo de caracteres permitidos, definidos en el atributo textoMax. Si esta vacío [en tiempo de ejecución] el atributo no es considerado. <b>[ExtJs.-maxLengthText]</b></p>');
            				}else if(record.data.name == 'textoMin'){
            					setSur('<p><b>TextoMin.- </b>Es el atributo que define el número mínimo de caracteres que pudiera contener el cuadro de texto. Si esta en [0] [en tiempo de ejecución] el atributo no es considerado. <b>[ExtJs.-minLength]</b></p>');
            				}else if(record.data.name == 'textoMinMsg'){
            					setSur('<p><b>TextoMinMsg.- </b>Es el mensaje que aparece cuendo se rebaso el número mínimo de caracteres permitidos, definidos en el atributo textoMin. Si esta vacío [en tiempo de ejecución] el atributo no es considerado. <b>[ExtJs.-minLengthText]</b></p>');
            				}else if(record.data.name == 'soloLectura'){
            					setSur('<p><b>Solo Lectrura.- </b>[True/False] True.-Define si el campo es de solo lectura y no puedes escribir sobre el. <b>[ExtJs.-readOnly]</b></p>');
            				}else if(record.data.name == 'toolTip'){
            					setSur('<p><b>ToolTip.- </b>Es el atributo que define un texto emergente para la descripción del campo. De estar en blanco [en tiempo de ejecución] el atributo no es considerado.</p>');
            				}else if(record.data.name == 'texto'){
            					var ctl = Ext.getCmp(ctrl);
            					if(ctl.getXType() == 'button'){
            						setSur('<p><b>Texto.- </b>Es el valor que contendrá el botón para describir su acción.  <b>[ExtJs.-text]</b></p>');
            					}else if (ctl.getXType() == 'combobox'){
            						setSur('<p><b>Texto.- </b>Es el valor que contendrá el ComboBox por default [en tiempo de ejecución].  <b>[ExtJs.-value]</b></p>');
            					}else if (ctl.getXType() == 'label'){
            						if(ctl.id.indexOf("hidden") > -1){
            							setSur('<p><b>Texto.- </b>Es el valor que contendrá el control Hidden [en tiempo de ejecución] para pasar como parametro.  <b>[ExtJs.-value]</b></p>');
            						}else{
            							setSur('<p><b>Texto.- </b>Es el valor que contendrá el control Label para describirse.  <b>[ExtJs.-text]</b></p>');
            						}
            					}else{
            						setSur('<p><b>Texto.- </b>Es el valor que contendrá el cuadro de texto por default. De estar en blanco [en tiempo de ejecución] el atributo no es considerado.</p>');
            					}
            				}else if(record.data.name == 'valorMax'){
            					setSur('<p><b>ValorMax.- </b>Es el valor númerico máximo que el campo pudierá permitir capturar. De estar en 0 [en tiempo de ejecución] el atributo no es considerado. <b>[ExtJs.-maxValue]</b></p>');
            				}else if(record.data.name == 'valorMin'){
            					setSur('<p><b>ValorMin.- </b>Es el valor númerico mínimo que el campo pudierá permitir capturar. De estar en 0 [en tiempo de ejecución] el atributo no es considerado. <b>[ExtJs.-minValue]</b></p>');
            				}else if(record.data.name == 'valorMaxMsg'){
            					setSur('<p><b>ValorMaxMsg.- </b>Es el atributo que define el mensaje que aparecerá cuando se supere el número máximo configurado en el atributo [valorMax]. Si esta vacío será omitido [en tiempo de ejecución]. <b>[ExtJs.-maxText]</p>');
            				}else if(record.data.name == 'valorMinMsg'){
            					setSur('<p><b>ValorMinMsg.- </b>Es el atributo que define el mensaje que aparecerá cuando se supere el número mínimo configurado en el atributo [valorMin]. Si esta vacío será omitido [en tiempo de ejecución]. <b>[ExtJs.-minText]</p>');
            				}else if(record.data.name == 'isFlechas'){
            					setSur('<p><b>IsFlechas.- </b>[True/False] False.-Define si se pintan las flechas que aumenta y/o diminuye el valor numerico del campo. <b>[ExtJs.-hideTrigger]</b></p>');
            				}else if(record.data.name == 'fecha'){
            					setSur('<p><b>Fecha.- </b>Es el atributo que define el texto que contendra por default el control [en tiempo de ejecución]. <b>[ExtJs.-value]</b></p>');
            				}else if(record.data.name == 'fechaMax'){
            					setSur('<p><b>FechaMax.- </b>Es la fecha máxima que permitirá se capture en el control. Si esta vacío el atributo no se incluirá [en tiempo de ejecución]. <b>[ExtJs.-maxValue]</b></p>');
            				}else if(record.data.name == 'fechaMaxMsg'){
            					setSur('<p><b>FechaMaxMsg.- </b>Es el mensaje que se mostrará al usuario [en tiempo de ejecución] cuando se rebase el valor máximo permtido. <b>[ExtJs.-maxText]</b></p>');
            				}else if(record.data.name == 'fechaMin'){
            					setSur('<p><b>FechaMin.- </b>Es la fecha mínima que permitirá se capture en el control. Si esta vacío el atributo no se incluirá [en tiempo de ejecución]. <b>[ExtJs.-minValue]</b></p>');
            				}else if(record.data.name == 'fechaMinMsg'){
            					setSur('<p><b>FechaMinMsg.- </b>Es el mensaje que se mostrará al usuario [en tiempo de ejecución] cuando se rebase el valor mínimo permtido. <b>[ExtJs.-minText]</b></p>');
            				}else if(record.data.name == 'etiqueta_aling'){
            					setSur('<p><b>Etiqueta_Aling.- </b>Es el atributo que define la aliniación que tendrá la etiqueta respecto al control. <b>left.-</b>Se pega a la izquierda <b>top.-</b>Sobre el control <b>right.-</b>Se pega a la derecha del control. <b>[ExtJs.-labelAlign]</b></p>');
            				}else if(record.data.name == 'isSeleccionado'){
            					setSur('<p><b>IsSeleccionado.- </b>[True/False] True.-Define si el checkbox se selecciona/palomea. <b>[ExtJs.-checked]</b></p>');
            				}else if(record.data.name == 'imagenCls'){
            					setSur('<p><b>ImagenCls.- </b>Define si el boton contendrá una imagen. Es importante mencionar que solo se ingresa el nombre de un estilo previamente cargado en un archivo [CSS]. <b>[ExtJs.-iconCls]</b></p>');
            				}else if(record.data.name == 'margen'){
            					setSur('<p><b>Margen.- </b>Es el atributo que define los margenes del control en el siguiente orden [arriba derecha abajo izquierda]. Si esta vacío no será considerado el atributo [en tiempo de ejecución]. <b>[ExtJs.-margin]</b></p>');
            				}else if(record.data.name == 'padding'){
            					setSur('<p><b>Padding.- </b>Es el atributo que define los margenes del control en el siguiente orden [arriba derecha abajo izquierda]. Si esta vacío no será considerado el atributo [en tiempo de ejecución]. <b>[ExtJs.-padding]</b></p>');
            				}else if(record.data.name == 'imagen_aling'){
            					setSur('<p><b>Imagen_aling.- </b>Es la definición de la orientación que tendrá [en tiempo de ejecución] la imagen del boton en caso de asignarse el atributo de [imagen]. <b>[ExtJs.-iconAling]</b></p>');
            				}else if(record.data.name == 'isRequeridoMsg'){
            					setSur('<p><b>IsRequeridoMsg.- </b>Es el mensaje que aparecerá cuando se marque un campo como obligatorio. Si esta vacío [en tiempo de ejecución] será omitido. <b>[ExtJs.-blankText]</b></p>');
            				}else if(record.data.name == 'isEditable'){
            					setSur('<p><b>IsEditable.- </b>[True/False] False.-Define que no se podrá capturar información en el control, sólo seleccionar el tipo de dato. <b>[ExtJs.-editable]</b></p>');
            				}else if(record.data.name == 'multiSelect'){
            					setSur('<p><b>MultiSelect.- </b>[True/False] True.-Es el atributo que permite seleccionar mas de un elemento en el combobox. <b>[ExtJs.-multiSelect]</b></p>');
            				}else if(record.data.name == 'store'){
            					setSur('<p><b>Store.- </b>Es el atributo que permite la carga de datos del control. <b>[ExtJs.-store]</b></p>');
            				}else if(record.data.name == 'modo'){
            					setSur('<p><b>Modo.- </b>Es el atributo que define el modo de conexión que tendrá el store del combobox.  <b>[ExtJs.-queryMode]</b></p>');
            				}else if(record.data.name == 'selectconFoco'){
            					setSur('<p><b>SelectconFoco.- </b>Define si el contenedor de texto se va a seleccionar y/o contener el foco cuando seleccionemos un elemento de nuestra lista. <b>[ExtJs.-selectOnFocus]</b></p>');
            				}else if(record.data.name == 'selectAction'){
            					setSur('<p><b>SelectAction.- </b>Es el atributo que define como será presentada la lista de elementos del control. <b>All.-</b>Presenta toda la información que contiene el store. <b>Last.-</b>Presenta la ultima seleccionada. <b>Query.-</b>Presenta en la lista unicamente los valores coincidentes con la descripción capturada en el control. [se suguiere utilizar esta cuando la lista de valores va a ser demasiado grande.]  <b>[ExtJs.-triggerAction]</b></p>');
            				}else if(record.data.name == 'valorDisplay'){
            					setSur('<p><b>ValorDisplay.- </b>Es el atributo que define que información mostrará el Combobox  <b>[ExtJs.-displayField]</b></p>');
            				}else if(record.data.name == 'valorId'){
            					setSur('<p><b>(ValorId.- </b>Es el atributo que define cual campo del store utilizar como "value", puede ser un ID numérico.  <b>[ExtJs.-valueField]</b></p>');
            				}else if(record.data.name == 'isFlecha'){
            					setSur('<p><b>IsFlecha.- </b>[True/False] True.-Define si aparece la flecha que despliega la lista que contiene el control. <b>[ExtJs.-hideTrigger]</b></p>');
            				}else if(record.data.name == 'isAutoComp'){
            					setSur('<p><b>IsAutoComp.- </b>[True/False] True.-Realiza el autocompletado de la palabra que se identifica con algun contenido de la lista sugiriendo el resto de la palabra. <b>[ExtJs.-typeAhead]</b></p>');
            				}else if(record.data.name == 'delimitador'){
            					setSur('<p><b>Delimitador.- </b>Es el atributo que concatena cada valor seleccionado en el combo cuando la propiedad multiselect esta en true.  <b>[ExtJs.-delimiter]</b></p>');
            				}else if(record.data.name == 'row'){
            					setSur('<p><b>Row.- </b>Es un agrupador que permitirá contener en una linea el número de botones señalados con ese identificador.</p>');
            				}else if(record.data.name == 'tabs'){
            					setSur('<p><b>Tabs.- </b>Es el número de Tabs que contendra el contenedor [en tiempo de ejecución].</p>');
            				}else if(record.data.name == 'isDesplegable'){
            					setSur('<p><b>IsDesplegable.- </b>[True/False] False.-Es el atributo del Panel que define si aparece en la barra de título un botón que haga que se contraiga y despliegue el Panel. <b>[ExtJs.-collapsible]</b></p>');
            				}else if(record.data.name == 'isFondo'){
            					setSur('<p><b>IsFondo.- </b>[True/False] True.-Atributo que define si se pone fondo dentro del panel. <b>[ExtJs.-frame]</b></p>');
            				}else if(record.data.name == 'isCerrable'){
            					setSur('<p><b>IsCerrable.- </b>[True/False] False.-Atributo que define si se pone en la barra del panel y/o tab un boton para cerrarlo. <b>[ExtJs.-closable]</b></p>');
            				}else if(record.data.name == 'bodyPadding'){
            					setSur('<p><b>BodyPadding.- </b>Es el atributo que define los margenes dentro del contenedor en el siguiente orden [arriba derecha abajo izquierda]. Si esta vacío no será considerado el atributo [en tiempo de ejecución]. <b>[ExtJs.-bodyPadding]</b></p>');
            				}else if(record.data.name == 'isAutoScroll'){
            					setSur('<p><b>IsAutoScroll.- </b>[True/False] False.-Atributo que define si van aparecer las barras desplazadoras cuando se supere el tamaño del contenedor. <b>[ExtJs.-autoScroll]</b></p>');
            				}else if(record.data.name == 'isResizable'){
            					setSur('<p><b>IsResizable.- </b>[True/False] False.-Atributo que define si el contenedor va a permitir cambiar su tamaño por el usuario [en tiempo de ejecución]. <b>[ExtJs.-resizable]</b></p>');
            				}else if(record.data.name == 'isBodyBorder'){
            					setSur('<p><b>IsBodyBorder.- </b>[True/False] True.-Atributo que define si el contenedor va a dibujar un borde. Aplica preferentemente cuando [isFondo] esta en True. <b>[ExtJs.-bodyBorder]</b></p>');
            				}else if(record.data.name == 'titulo_Aling'){
            					setSur('<p><b>Titulo_Aling.- </b>Es el atributo que alinea el titulo de las formas [izquierda derecha y al centro]. <b>[ExtJs.-titleAlign]</b></p>');
            				}else if(record.data.name == 'fechaInvalidTxt'){
            					setSur('<p><b>FechaInvalidTxt.- </b>Es el atributo que define el mensaje que va aparecer cuando se capture mal el dato de la fecha. Si esta en blanco [en tiempo de ejecución] no será considerado. <b>[ExtJs.-invalidText]</b></p>');
            				}else if(record.data.name == 'escala'){
            					setSur('<p><b>Escala.- </b>Es el atributo que define el tamaño la imagen que se pintará en el botón 16px, 24px ó 32px así como el tamaño del mismo. <b>[ExtJs.-scale]</b></p>');
            				}else if(record.data.name == 'columnas'){
            					setSur('<p><b>Columnas.- </b>Es el atributo que define el número de columnas que contendrá el panel. <b>[ExtJs.-columns]</b></p>');
            				}else if(record.data.name == 'isPadre'){
            					setSur('<p><b>IsPadre.- </b>Es el atributo que define si el control se liga a otro; para lo cual hay que definir en el atributo el ID del combobox al que se liga.</p>');
            				}else if(record.data.name == 'url'){
            					setSur('<p><b>Url.- </b>Es el atributo que define a donde se redirige la información del formulario despues del Submit. Si esta en blanco [en tiempo de ejecución] no será considerado. <b>[ExtJs.-url]</b></p>');
            				}else if(record.data.name == 'estilo'){
            					setSur('<p><b>Estilo.- </b>Es el tipo de boton que va a definir el comportamiento del click en el boton. Sólo aplica a paneles tipo formulario.</p>');
            				}else if(record.data.name == 'titulo_Posicion'){
            					setSur('<p><b>Titulo_Posicion.- </b>Es el atributo que define la orientación que va a tener el cabecero de la ventana. [Izquierda, Derecha, Arriba, Abajo]. <b>[ExtJs.-headerPosition]</b></p>');
            				}else if(record.data.name == 'isModal'){
            					setSur('<p><b>IsModal.- </b>Es el atributo que define si la ventana se comportará como tipo Modal. True.- Bloquea toda la pantalla hasta que la ventana sea cerrada. <b>[ExtJs.-modal]</b></p>');
            				}else if(record.data.name == 'isMinimizable'){
            					setSur('<p><b>IsMinimizable.- </b>Es el atributo que define si la ventana tendrá la opción que permitirá minimizarla. Es importante mencionar que no tiene la funcionalidad implementada comomla tiene la opción de cerrar. Habría que implementarla.<b>[ExtJs.-minimizable]</b></p>');
            				}else if(record.data.name == 'cordX'){
            					setSur('<p><b>CordX.- </b>Es el atributo que define la coordenada X de la posición en la que se desea aparezca la ventana. De ser cero su valor [en tiempo de ejecución] no será considerado. <b>[ExtJs.-x]</b></p>');
            				}else if(record.data.name == 'cordY'){
            					setSur('<p><b>CordY.- </b>Es el atributo que define la coordenada Y de la posición en la que se desea aparezca la ventana. De ser cero su valor [en tiempo de ejecución] no será considerado. <b>[ExtJs.-y]</b></p>');
            				}else if(record.data.name == 'html'){
            					setSur('<p><b>html.- </b>Es el atributo que define el texto con formato HTML se desea que aparezca en la etiqueta. Para que tenga efecto este atributo el de Texto deberá estar en blanco. Si esta vacío el atributo no se incluirá [en tiempo de ejecución]. <b>[ExtJs.-html]</b></p>');
            				}else if(record.data.name == 'src'){
            					setSur('<p><b>Src.- </b>Es el atributo que define la url relativa y/o absoluta de donde será cargada la imagen. Es indispensable para que se pinte la imagen de lo contrario se cargará la default. <b>[ExtJs.-src]</b></p>');
            				}else if(record.data.name == 'query'){
            					setSur('<p><b>Query.- </b>Es el atributo que define la consulta a la base de datos de la información que se desea pintar en el Grid. Es importante mencionar que la sentencia SQL debe contener el nombre del dataIndex definido en el control grid.</p>');
            				}else if(record.data.name == 'columna_orden'){
            					setSur('<p><b>Columna_orden.- </b>Su valor es True y permite que dentro del Grid se ordenen los datos de la columna medinte una función que aparece en la cabecera de cada columna. <b>[ExtJs.-sortableColumns]</b></p>');
            				}else if(record.data.name == 'columna_hidden'){
            					setSur('<p><b>Columna_hidden.- </b>Es el atributo que define si en el cabecero de las columnas del Grid se mostrará la funcionalidad de ocultar la columna. <b>[ExtJs.-enableColumnHide]</b></p>');
            				}else if(record.data.name == 'columna_resize'){
            					setSur('<p><b>Columna_resize.- </b>Es el atributo que define si las columnas del Grid se prodrán modificar respecto al ancho, con la finalidad de ocultar o mostrar mayor información del renglon. <b>[ExtJs.-enableColumnResize]</b></p>');
            				}else if(record.data.name == 'columna_move'){
            					setSur('<p><b>Columna_move.- </b>Es el atributo que permite cambiar el orden de presentación de las columnas que se presentan en el Grid. <b>[ExtJs.-enableColumnMove]</b></p>');
							}else if(record.data.name == 'isBorder'){
            					setSur('<p><b>IsBorder.- </b>Es el atributo que define en tiempo se ejecución si el Grid contendrá un borde.</p>');
            					}else if(record.data.name == ''){
            					setSur('<p><b>(id).- </b>Es el id</p>');
            				}else if(record.data.name == ''){
            					setSur('<p><b>(id).- </b>Es el id</p>');
            				}else if(record.data.name == ''){
            					setSur('<p><b>(id).- </b>Es el id</p>');
            				}else if(record.data.name == ''){
            					setSur('<p><b>(id).- </b>Es el id</p>');
            				}else if(record.data.name == ''){
            					setSur('<p><b>(id).- </b>Es el id</p>');
            				}else if(record.data.name == ''){
            					setSur('<p><b>(id).- </b>Es el id</p>');
            				}
            				if(record.data.name == 'tipo' || record.data.name == '(id)'){
            					return false;
            				}
            			}
        			},  
        			propertychange: {
            			fn: function(source, recordId, value, oldValue, eOpts ){ 
            					if(recordId =='zuprimir' && value == true){
            						eliminarCtrl(recordId);
            					}else{
            						setAttrCtrl(recordId,value);
            					}
            			}
        			}
        		}
                    })]
            }]	
        }]
    });

//Aplica atributo al panel
function setAttrCtrl(name, val){
	var ctl = Ext.getCmp(ctrl);
	if(name === 'etiqueta'){
		setDataSP(name,val);
		ctl.setFieldLabel(val);
	}else if (name === 'titulo'){
		setDataSP(name,val);
		ctl.setTitle(val);
	/*}else if (name === 'nombre' || name === 'isRequerido' || name === 'isAnchor' || name === 'etiqueta_width' || name === 'isFondo'
		|| name === 'textoMax' || name === 'textoSugerido' || name === 'textoMaxMsg' || name === 'textoMin'
		|| name === 'row' || name === 'columnas' || name === 'tabs'){
		setDataSP(name,val);*/
	}else if (name === 'height'){
		if(val == null){ val = 0;}
		ctl.setHeight(val);
		setDataSP(name,val);
	}else if (name === 'width'){
		if(val == null){ val = 0;}
		ctl.setWidth(val);
		setDataSP(name,val);
	}else if (name === 'isBloqueado'){
		//ctl.setDisabled(val);
		setDataSP(name,val);
	}else if (name === 'texto'){
		if(ctl.getXType() == 'button'){
			ctl.setText(val);	
		}else if (ctl.getXType() == 'combobox'){
			ctl.setValue(val);
		}else if (ctl.getXType() == 'label'){
			if(ctl.id.indexOf("hidden") == -1){
				ctl.setText(val);
			}
		}else{
			ctl.setValue(val);
		}
		setDataSP(name,val);
	}else if (name === 'valorMax'){
		ctl.setMaxValue(val);
		setDataSP(name,val);
	}else if (name === 'valorMin'){
		ctl.setMinValue(val);
		setDataSP(name,val);
	}else if (name === 'fecha'){
		ctl.setValue(val);
		setDataSP(name,val);
	}else if (name === 'isSeleccionado'){
		ctl.setValue(val);
		setDataSP(name,val);
	}else if (name === 'imagenCls'){
		ctl.setIconCls(val);
		setDataSP(name,val);
	}else{
		setDataSP(name,val);
	}
}
 
function creaDataSP(idP, idH){
	nCtrl ++;
	var rgs = { //spanel.json
    	idPadre: idP,
        idHijo: idH,
        order: nCtrl
	};
	storeSuperPanel.add(rgs);
}
function creaDataSPG(idP, idH, idG,idName){
	nCtrl ++;
	var rgs = { //spanel.json
    	idPadre: idP,
        idHijo: idH,
        idGrid: idG,
        order: nCtrl,
        texto:'New Columna',
        width: 80,
        tipoG:'string',
        dataIndex:'titulo',
        name: idName
	};
	storeSuperPanel.add(rgs);
}

//Funcionciones para crear Paneles Layout//

function CreaPanelFormaPreliminar(idPnl,isBton) {
if (isBton){var frm = {xtype:'form',buttons: []};}else{var frm = {xtype:'form'};}
attrGralPreliminar(frm, idPnl, storeFormulario);
return frm;}

function CreaPanelColumnasPreliminar(idPnl,isBton) {
if (isBton){var frm = {xtype:'form',defaultType: 'container',layout: 'column',buttons: []};}else{var frm = {xtype:'form',defaultType: 'container',layout: 'column'};}
var columnasP = parseInt(getDataSP('columnas',idPnl));
if( columnasP > 0){Ext.apply(frm, {columns:columnasP});}
attrGralPreliminar(frm, idPnl, storeColumnas);
return frm;
}

function CreaCtrlPanelPreliminar(idPnl,isBton) {
var region = '';var stroreB;
if(idPnl.indexOf("sur") > -1){
	region='south';
	stroreB = storeBorderSur;
}else if (idPnl.indexOf("izq") > -1){
	region='west';
	stroreB = storeBorderIzq;
}else if (idPnl.indexOf("centro") > -1){
	region='center';
	stroreB = storeBorderCenter;
}else if (idPnl.indexOf("der") > -1){
	region='east';
	stroreB = storeBorderDer;
}else if (idPnl.indexOf("norte") > -1){
	region='north';
	stroreB = storeBorderNorte;
}
if (isBton){var frm = {xtype:'panel',defaultType: 'container',region:region,layout: 'column',buttons: []};}else{var frm = {xtype:'panel',defaultType: 'container',region:region,layout: 'column'};}	
var columnasP = parseInt(getDataSP('columnas',idPnl));
if( columnasP > 0){Ext.apply(frm, {columns:columnasP});}
attrGralPreliminar(frm, idPnl, stroreB);
return frm;
}

function CreaWinPreliminar(idPnl,isBton) {
if (isBton){
var ppWina =  Ext.create('Ext.Window', {modal:true,buttons: []});
}else{var ppWina =  Ext.create('Ext.Window', {modal:true});}
	//Ext.apply(ppWina, {id:'preliminar_' + idPnl});
attrGralPreliminar(ppWina, idPnl, storeWindow);
Ext.apply(ppWina, {closable:true});
return ppWina;
}
function CreaAcordionColumnasPreliminar(idPnl,isBton) {
	if (isBton){
		var frm = {xtype:'form',defaultType: 'container',layout: 'column',buttons: []};
	}else{
		var frm = {xtype:'form',defaultType: 'container',layout: 'column'};
	}
	var columnasP = parseInt(getDataSP('columnas',idPnl));
	if( columnasP > 0){Ext.apply(frm, {columns:columnasP});}
	(frm, idPnl, storeColumnas);
	return frm;
}

function CreaPanelTabsPreliminar(idPnl,isBton) {
	var frm = {xtype:'tabpanel'};
	attrGralPreliminar(frm, idPnl, storeTabs);
	return frm;
}		
		
function CreaPanelAcordionPreliminar(idPnl,isBton){
	var frm = {xtype:'panel',layout: 'accordion'};
		attrGralPreliminar(frm, idPnl, storeAcordion);
	return frm;
}
function CreaPanelBorderPreliminar(idPnl,isBton){
	var frm = {xtype:'panel',layout: 'border'};
		attrGralPreliminar(frm, idPnl, storeBorder);
	return frm;
}		
function attrGralPreliminar(pCtrl, idCtls, store){
	Ext.apply(pCtrl, {id:'preliminar_' + idCtls});
	if(getExisteStore(store,'etiqueta')){
		var etiquetaP = getDataSP('etiqueta',idCtls);
		if(etiquetaP != '' ){Ext.apply(pCtrl, {fieldLabel:etiquetaP});}
	}
	if(getExisteStore(store,'etiqueta_aling')){
		var etiqueta_alingP = getDataSP('etiqueta_aling',idCtls);
		if( etiqueta_alingP != '' && etiqueta_alingP != 'right'){
			Ext.apply(pCtrl, {labelAlign:etiqueta_alingP});
		}else{
			if(idCtls.indexOf("checkbox") > -1 || idCtls.indexOf("radiofield") > -1){
				Ext.apply(pCtrl, {labelAlign:etiqueta_alingP});
				Ext.apply(pCtrl, {hideLabel:true});
				Ext.apply(pCtrl, {boxLabel:etiquetaP});
			}else{
				Ext.apply(pCtrl, {labelAlign:etiqueta_alingP});
			}
		}
	}
	if(getExisteStore(store,'etiqueta_width')){
		var etiqueta_widthP = parseInt(getDataSP('etiqueta_width',idCtls));
		if( etiqueta_widthP > 0){Ext.apply(pCtrl, {labelWidth:etiqueta_widthP});}
	}
	if(getExisteStore(store,'height')){
		var heightP = parseInt(getDataSP('height',idCtls));
		if( heightP > 0){Ext.apply(pCtrl, {height:heightP});}
	}
	if(getExisteStore(store,'isBloqueado')){Ext.apply(pCtrl, {disabled:parseBol(getDataSP('isBloqueado',idCtls))});}
	if(getExisteStore(store,'isEditable')){Ext.apply(pCtrl, {editable:parseBol(getDataSP('isEditable',idCtls))});}
	if(getExisteStore(store,'soloLectura')){Ext.apply(pCtrl, {readOnly:parseBol(getDataSP('soloLectura',idCtls))});}
	if(getExisteStore(store,'isAnchor')){
		var isAnchorP = parseInt(getDataSP('isAnchor',idCtls));
		var valor = '%';
		if( isAnchorP > 0){valor = isAnchorP + '%';Ext.apply(pCtrl, {anchor:valor});}
	}
	if(getExisteStore(store,'isRequerido')){
		var isRequeridoP = parseBol(getDataSP('isRequerido',idCtls));
		if(isRequeridoP == false){Ext.apply(pCtrl, {allowBlank:true});}else{Ext.apply(pCtrl, {allowBlank:false});}
	}
	if(getExisteStore(store,'isRequeridoMsg')){
		var isRequeridoMsgP = getDataSP('isRequeridoMsg',idCtls);
		if( isRequeridoMsgP != ''){Ext.apply(pCtrl, {blankText:isRequeridoMsgP});}
	}
	if(getExisteStore(store,'margen')){
		var margenP = getDataSP('margen',idCtls);
		if( margenP != ''){Ext.apply(pCtrl, {margin:margenP});}
	}
	if(getExisteStore(store,'padding')){
		var paddingP = getDataSP('padding',idCtls);
		if( paddingP != ''){Ext.apply(pCtrl, {padding:paddingP});}
	}
	if(getExisteStore(store,'textoMax')){
		var textoMaxP = parseInt(getDataSP('textoMax',idCtls));
		if( textoMaxP > 0){Ext.apply(pCtrl, {maxLength:textoMaxP});}
	}
	if(getExisteStore(store,'textoMaxMsg')){
		var textoMaxMsgP = getDataSP('textoMaxMsg',idCtls);
		if( textoMaxMsgP != ''){Ext.apply(pCtrl, {maxLengthText:textoMaxMsgP});}
	}
	if(getExisteStore(store,'textoMin')){
		var textoMinP = parseInt(getDataSP('textoMin',idCtls));
		if( textoMinP > 0){Ext.apply(pCtrl, {minLength:textoMinP});}
	}	
	if(getExisteStore(store,'textoMinMsg')){
		var textoMinMsgP = getDataSP('textoMinMsg',idCtls);
		if( textoMinMsgP != ''){Ext.apply(pCtrl, {minLengthText:textoMinMsgP});}
	}
	if(getExisteStore(store,'toolTip')){
		var toolTipP = getDataSP('toolTip',idCtls);
		if( toolTipP != ''){Ext.apply(pCtrl, {listeners:{afterrender: function(){Ext.QuickTips.register({target: this.id,text: toolTipP,dismissDelay: 2000}) ;}}});}
	}
	if(getExisteStore(store,'width')){
		var widthP = parseInt(getDataSP('width',idCtls));
		if( widthP > 0){Ext.apply(pCtrl, {width:widthP});}
	}
	if(getExisteStore(store,'cordX')){
		var cordX = parseInt(getDataSP('cordX',idCtls));
		if( cordX > 0){Ext.apply(pCtrl, {x:cordX});}
	}
	if(getExisteStore(store,'cordY')){
		var cordY = parseInt(getDataSP('cordY',idCtls));
		if( cordY > 0){Ext.apply(pCtrl, {y:cordY});}
	}
	if(getExisteStore(store,'texto')){
		var textoP = getDataSP('texto',idCtls);
		if( textoP != ''){Ext.apply(pCtrl, {value:textoP});}
	}
	if(getExisteStore(store,'html')){
		var html = getDataSP('html',idCtls);
		if( html != ''){Ext.apply(pCtrl, {html:html});}
	}
	if(getExisteStore(store,'textoSugerido')){
		var textoSugeridoP = getDataSP('textoSugerido',idCtls);
		if( textoSugeridoP != ''){Ext.apply(pCtrl, {emptyText:textoSugeridoP});}
	}
	if(getExisteStore(store,'titulo')){
		var tituloP = getDataSP('titulo',idCtls);
		if( tituloP != ''){Ext.apply(pCtrl, {title:tituloP});}
	}
	if(getExisteStore(store,'titulo_Posicion')){
		var titulo_Posicion = getDataSP('titulo_Posicion',idCtls);
		if( titulo_Posicion != ''){Ext.apply(pCtrl, {headerPosition:titulo_Posicion});}
	}
	if(getExisteStore(store,'bodyPadding')){
		var bodyPaddingP = getDataSP('bodyPadding',idCtls);
		if( bodyPaddingP != ''){Ext.apply(pCtrl, {bodyPadding:bodyPaddingP});}
	}
	if(getExisteStore(store,'isAutoScroll')){Ext.apply(pCtrl, {autoScroll:parseBol(getDataSP('isAutoScroll',idCtls))});}
	if(getExisteStore(store,'isBodyBorder')){Ext.apply(pCtrl, {bodyBorder:parseBol(getDataSP('isBodyBorder',idCtls))});}
	if(getExisteStore(store,'isFondo')){Ext.apply(pCtrl, {frame:parseBol(getDataSP('isFondo',idCtls))});}
	if(getExisteStore(store,'isCerrable')){Ext.apply(pCtrl, {closable:parseBol(getDataSP('isCerrable',idCtls))});}
	if(getExisteStore(store,'isDesplegable')){Ext.apply(pCtrl, {collapsible:parseBol(getDataSP('isDesplegable',idCtls))});}
	if(getExisteStore(store,'isSeleccionado')){Ext.apply(pCtrl, {checked:parseBol(getDataSP('isSeleccionado',idCtls))});}
	if(getExisteStore(store,'isResizable')){Ext.apply(pCtrl, {resizable:parseBol(getDataSP('isResizable',idCtls))});}
	if(getExisteStore(store,'isMinimizable')){Ext.apply(pCtrl, {minimizable:parseBol(getDataSP('isMinimizable',idCtls))});}
	if(getExisteStore(store,'titulo_Aling')){
		var titulo_AlingP = getDataSP('titulo_Aling',idCtls);
		if( titulo_AlingP != ''){Ext.apply(pCtrl, {titleAlign:titulo_AlingP});}
	}
}

function CreaCtrlComboPreliminar(idCtls,col){
	var txt = {
		xtype:'combobox'
	};
	if(col > 0){Ext.apply(txt, {columnWidth:1/col});}
	var delimitadorP = getDataSP('delimitador',idCtls);
	if( delimitadorP != ''){Ext.apply(txt, {delimiter:delimitadorP});}
	Ext.apply(txt, {typeAhead:parseBol(getDataSP('isAutoComp',idCtls))});
	Ext.apply(txt, {hideTrigger:parseBol(getDataSP('isFlecha',idCtls))});
	Ext.apply(txt, {multiSelect:parseBol(getDataSP('multiSelect',idCtls))});
	Ext.apply(txt, {selectOnFocus:parseBol(getDataSP('selectconFoco',idCtls))});
	var selectActionP = getDataSP('selectAction',idCtls);
	if( selectActionP != ''){Ext.apply(txt, {triggerAction:selectActionP});}
	var modoP = getDataSP('modo',idCtls);
	if( modoP != ''){Ext.apply(txt, {queryMode:modoP});}
	//var storeP = getDataSP('store',idCtls);
	//if( storeP != ''){Ext.apply(txt, {store:storeP});}
	var valorDisplayP = getDataSP('valorDisplay',idCtls);
	if( valorDisplayP != ''){Ext.apply(txt, {displayField:valorDisplayP});}
	var valorIdP = getDataSP('valorId',idCtls);
	if( valorIdP != ''){Ext.apply(txt, {valueField:valorIdP});}
	attrGralPreliminar(txt, idCtls, storeComboAttr);
	return txt;

}

function CreaCtrlBotonPreliminar(idCtls,col){
var txt = {
		xtype:'button'
	};
	if(col > 0){Ext.apply(txt, {columnWidth:1/col});}
	var textoP = getDataSP('texto',idCtls);
	if( textoP != ''){Ext.apply(txt, {text:textoP});}
	var imagen_alingP = getDataSP('imagen_aling',idCtls);
	if( imagen_alingP != ''){Ext.apply(txt, {iconAlign:imagen_alingP});}
	var imagenClsP = getDataSP('imagenCls',idCtls);
	if( imagenClsP != ''){Ext.apply(txt, {iconCls:imagenClsP});}
	var escalaP = getDataSP('escala',idCtls);
	if( escalaP != ''){Ext.apply(txt, {scale:escalaP});}
	attrGralPreliminar(txt, idCtls, storeBotonAttr);
	return txt;
}
function CreaCtrlRadioPreliminar(idCtls,col){
	var txt = {
		xtype:'radiofield',
		checked:parseBol(getDataSP('isSeleccionado',idCtls))
	};
	if(col > 0){Ext.apply(txt, {columnWidth:1/col});}
	attrGralPreliminar(txt, idCtls, storeRadioAttr);
	return txt;

}

function CreaCtrlCheckPreliminar(idCtls,col){
	var txt = {
		xtype:'checkboxfield'
	};
	if(col > 0){Ext.apply(txt, {columnWidth:1/col});}
	attrGralPreliminar(txt, idCtls, storeCheckAttr);
	return txt;
}

function CreaCtrlPickerPreliminar(idCtls,col){
	var txt = {
		xtype:'datefield',
		format:'d/m/Y',
		value:getDataSP('fecha',idCtls)
	};
	if(col > 0){Ext.apply(txt, {columnWidth:1/col});}
	var fechaInvalidTxtP = getDataSP('fechaInvalidTxt',idCtls);
	if( fechaInvalidTxtP != ''){Ext.apply(txt, {invalidText:fechaInvalidTxtP});}
	var valorMaxP = getDataSP('fechaMax',idCtls);
	if( valorMaxP != ''){Ext.apply(txt, {maxValue:parseDate(valorMaxP)});}
	var valorMaxMsgP = getDataSP('fechaMaxMsg',idCtls);
	if( valorMaxMsgP != ''){Ext.apply(txt, {maxText:valorMaxMsgP});}
	var valorMinP = getDataSP('fechaMin',idCtls);
	if( valorMinP != ''){Ext.apply(txt, {minValue:parseDate(valorMinP)});}
	var valorMinMsgP = getDataSP('fechaMinMsg',idCtls);
	if( valorMinMsgP != ''){Ext.apply(txt, {minText:valorMinMsgP});}
	

	attrGralPreliminar(txt, idCtls, storePickerAttr);
	return txt;
}

function CreaCtrlNumericPreliminar(idCtls,col){
	var txt = {
		xtype:'numberfield'
	};
	if(col > 0){Ext.apply(txt, {columnWidth:1/col});}
	var isFlechasP = parseBol(getDataSP('isFlechas',idCtls));
	if(isFlechasP == false){
		Ext.apply(txt, {hideTrigger:true});
		Ext.apply(txt, {keyNavEnabled:false});
	}


	var valorMaxP = parseInt(getDataSP('valorMax',idCtls));
	if( valorMaxP > 0){Ext.apply(txt, {maxValue:valorMaxP});}
	var valorMaxMsgP = getDataSP('valorMaxMsg',idCtls);
	if( valorMaxMsgP != ''){Ext.apply(txt, {maxText:valorMaxMsgP});}
	var valorMinP = parseInt(getDataSP('valorMin',idCtls));
	if( valorMinP > 0){Ext.apply(txt, {minValue:valorMinP});}
	var valorMinMsgP = getDataSP('valorMinMsg',idCtls);
	if( valorMinMsgP != ''){Ext.apply(txt, {minText:valorMinMsgP});}
	attrGralPreliminar(txt, idCtls, storeNumericAttr);
	return txt;
}
function CreaCtrlLabelPreliminar(idCtls,col){
	var txt = {
		xtype:'label'
	};
	if(col > 0){Ext.apply(txt, {columnWidth:1/col});}
	attrGralPreliminar(txt, idCtls,storeLabelAttr);
	var texto = getDataSP('texto',idCtls);
	if( texto != ''){Ext.apply(txt, {text:texto});}
	return txt;
}
function CreaCtrlGridPreliminar(idCtls,col){
	var grid = Ext.getCmp(idCtls);
	var arryG =[];
	Ext.each(grid.store.data.items, function(item){
		var attr = {text:'label'};
		Ext.apply(attr,{text:item.data.texto});
		Ext.apply(attr,{width:item.data.width});
		arryG.push(attr);
	});
	var txt = {
		xtype:'gridpanel'
	};
	Ext.apply(txt,{columns:arryG});
	if(col > 0){Ext.apply(txt, {columnWidth:1/col});}
	attrGralPreliminar(txt, idCtls,storeGridAttr);
	return txt;
}
function CreaCtrlHiddenPreliminar(idCtls,col){
	var txt = {
		xtype:'label',margin:'5 5 5 5'
	};
	attrGralPreliminar(txt, idCtls,storeHiddenAttr);
	var texto = getDataSP('nombre',idCtls);
	if( texto != ''){Ext.apply(txt, {text:texto});}
	return txt;
}
function CreaCtrlImagenPreliminar(idCtls,col){
	var txt = {
		xtype:'image'
	};
	if(col > 0){Ext.apply(txt, {columnWidth:1/col});}
	attrGralPreliminar(txt, idCtls,storeImagenAttr);
	var src = getDataSP('src',idCtls);
	if( src != ''){Ext.apply(txt, {src:src});}
	return txt;
}
function CreaCtrlTextoPreliminar(idCtls,col) {
	var txt = {
		xtype:'textfield'
	};
	if(col > 0){Ext.apply(txt, {columnWidth:1/col});}

	attrGralPreliminar(txt, idCtls,storeTextAttr);
	return txt;
}

//Formularios OPEN
function CreaPanelFormaOpen(attrPnl) {var idP = getAttrOpen(attrPnl,'id');var frm = new Ext.form.Panel({title: getAttrOpen(attrPnl,'title'),id: idP,height: getAttrOpen(attrPnl,'height'),width: getAttrOpen(attrPnl,'width'),collapsible: true,closable: true,titleAlign : 'center',frame: true,bodyPadding: '5 5 0',margin: '5,0,0,5',autoScroll:true,resizable:true,tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler: function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeFormulario);}}],listeners: {close: {fn: function(panel, eOpts ){limpiaObjetos(panel.id);}},resize: {fn: function(){cargaAtributosPanel(this.id,storeFormulario);}}},afterRender:function() { Ext.Panel.prototype.afterRender.apply(this, arguments); this.dropTarget = Ext.getCmp(idP).body;var dd = new Ext.dd.DropTarget(this.dropTarget,{ddGroup:'controlesDD',notifyDrop:function(dd, e, node) { agregaCtrl(node.nodes[0].innerText,idP);return true; }}); }});return frm;}
function CreaPanelColumnasOpen(attrPnl) {var id = getAttrOpen(attrPnl,'id');var frm = new Ext.form.Panel({title: getAttrOpen(attrPnl,'title'),id: id, height: getAttrOpen(attrPnl,'height'),width: getAttrOpen(attrPnl,'width'),layout: 'column',collapsible: true,	margin: '5,0,0,5',frame: true, closable: true, columns: getAttrOpen(attrPnl,'columns'),bodyPadding: '5 5 0',autoScroll:true,	resizable:true,tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler: function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeColumnas);}}],listeners: {close: {fn: function(panel, eOpts ){limpiaObjetos(panel.id);}},resize: {fn: function(){cargaAtributosPanel(this.id,storeColumnas);}}},afterRender:function() { Ext.Panel.prototype.afterRender.apply(this, arguments); this.dropTarget = Ext.getCmp(id).body;var dd = new Ext.dd.DropTarget(this.dropTarget, {ddGroup:'controlesDD',notifyDrop:function(dd, e, node) { agregaCtrl(node.nodes[0].innerText,id);return true; }}); }});return frm;}    
function CreaPanelTabsOpen(attrPnl,hijosPnl) {var id = getAttrOpen(attrPnl,'id');var frm = new Ext.TabPanel({title: getAttrOpen(attrPnl,'title'),id: id,collapsible: true,margin: '5',frame: true,closable: true, bodyPadding: '5 5 5 5',width: getAttrOpen(attrPnl,'width'),height: getAttrOpen(attrPnl,'height'),resizable:true,tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler: function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeTabs);}}],listeners: {close: {fn: function(panel, eOpts ){limpiaObjetos(panel.id);}},resize: {fn: function(){cargaAtributosPanel(this.id,storeTabs);}},tabchange:{fn: function( tabPanel, newCard, oldCard, eOpts ){var arryAttr = [];ctrl = newCard.id;arryAttr.push('(id):'+newCard.id);arryAttr.push('width:'+newCard.getWidth());arryAttr.push('height:'+newCard.getHeight());arryAttr.push('titulo:'+newCard.title);arryAttr.push('columnas:'+getValorSP('columnas',2));arryAttr.push('isFondo:'+getValorSP('isFondo',true));arryAttr.push('bodyPadding:'+getValorSP('bodyPadding','5 5 5 5'));arryAttr.push('isAutoScroll:'+getValorSP('isAutoScroll',true));arryAttr.push('isCerrable:'+getValorSP('isCerrable',true));arryAttr.push('isBodyBorder:'+getValorSP('isBodyBorder',false));seteaAtributosGrid(arryAttr,newCard.id,storeTabsIn);}}},afterRender:function() { Ext.Panel.prototype.afterRender.apply(this, arguments); this.dropTarget = Ext.getCmp(id).body;var dd = new Ext.dd.DropTarget(this.dropTarget, {ddGroup:'controlesDD',notifyDrop:function(dd, e, node) { agregaCtrl(node.nodes[0].innerText,id);return true; }});}});for(pl = 0; pl < hijosPnl.length; pl++){var pnl = CreaTabColumnasOpen(hijosPnl[pl].attrs,id);frm.add(pnl);}return frm;}
function CreaTabColumnasOpen(attrPnl,papaId) {var regId = getAttrOpen(attrPnl,'id');panelTabId = parseInt(regId.substring(regId.lastIndexOf("_")+1));var titulo = getAttrOpen(attrPnl,'title');var hWidth = getAttrOpen(attrPnl,'width');var hHeight = getAttrOpen(attrPnl,'height');var frm = new Ext.form.Panel({title: titulo,id: regId,layout: 'column',columns: 2,frame: true,bodyPadding: '5 5 5 5',width: hWidth,height: hHeight,autoScroll:true,closable: true,defaultType: 'container',listeners: {close: {fn: function(c ){ limpiaObjetos(this.id);}}}});nCtrl ++;var rgs = {idPadre: papaId,idHijo: regId,order: nCtrl,columnas: 2,titulo:titulo,bodyPadding: getAttrOpen(attrPnl,'bodyPadding'),isFondo: getAttrOpen(attrPnl,'frame'),width: hWidth,height: hHeight,isAutoScroll:getAttrOpen(attrPnl,'autoScroll'),isCerrable: getAttrOpen(attrPnl,'closable'), isBodyBorder:getAttrOpen(attrPnl,'bodyBorder'),tipo:'form_columns'};storeSuperPanel.add(rgs);return frm;}
function CreaPanelAcordionOpen(attrPnl,hijosPnl) {var id = getAttrOpen(attrPnl,'id');var frm = new Ext.Panel({title: getAttrOpen(attrPnl,'title'),id: id,collapsible: true,margin: '5,0,0,5',frame: true,layout: 'accordion',closable: true,width: getAttrOpen(attrPnl,'width'),height: getAttrOpen(attrPnl,'height'),autoScroll:true,resizable:true,defaults: {bodyPadding: 10},tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler: function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeAcordion);}}],listeners: {close: {fn: function(panel, eOpts ){limpiaObjetos(panel.id);}},resize: {fn: function(){cargaAtributosPanel(this.id,storeAcordion);}}},afterRender:function() { Ext.Panel.prototype.afterRender.apply(this, arguments); this.dropTarget = Ext.getCmp(id).body;var dd = new Ext.dd.DropTarget(this.dropTarget, {ddGroup:'controlesDD',notifyDrop:function(dd, e, node) { agregaCtrl(node.nodes[0].innerText,id);return true; }});}});for(pl = 0; pl < hijosPnl.length; pl++){var pnl = CreaAcordionColumnasOpen(hijosPnl[pl].attrs,id);frm.add(pnl);}return frm;}
function CreaAcordionColumnasOpen(attrPnl,papaId) {var regId = getAttrOpen(attrPnl,'id');panelAcordionId = parseInt(regId.substring(regId.lastIndexOf("_")+1));var titulo = getAttrOpen(attrPnl,'title');var hWidth = getAttrOpen(attrPnl,'width');var hHeight = getAttrOpen(attrPnl,'height');var frm = new Ext.form.Panel({title: titulo,	id: regId,layout: 'column',columns: 2,frame: false,bodyPadding: '5 5 5 5',width: hWidth,height: hHeight,autoScroll:true,closable: false,defaultType: 'container',listeners: {expand: function(pnl) {var arryAttr = [];ctrl = pnl.id;arryAttr.push('(id):'+pnl.id);arryAttr.push('width:'+pnl.getWidth());arryAttr.push('height:'+pnl.getHeight());arryAttr.push('titulo:'+pnl.title);arryAttr.push('columnas:'+getValorSP('columnas',2));arryAttr.push('isFondo:'+getValorSP('isFondo',false));arryAttr.push('bodyPadding:'+getValorSP('bodyPadding','5 5 5 5'));arryAttr.push('isAutoScroll:'+getValorSP('isAutoScroll',true));arryAttr.push('isCerrable:'+getValorSP('isCerrable',false));arryAttr.push('isBodyBorder:'+getValorSP('isBodyBorder',false));seteaAtributosGrid(arryAttr,pnl.id,storeAcordionIn);},close: {fn: function(c ){ limpiaObjetos(this.id);}}}});nCtrl ++;var rgs = {idPadre: papaId,idHijo: regId,order: nCtrl,columnas: 2,titulo:titulo,bodyPadding:getAttrOpen(attrPnl,'bodyPadding'),isFondo: getAttrOpen(attrPnl,'frame'),width: hWidth,height: hHeight,isAutoScroll:getAttrOpen(attrPnl,'autoScroll'),isCerrable: getAttrOpen(attrPnl,'closable'), isBodyBorder:getAttrOpen(attrPnl,'bodyBorder'),tipo:'form_columns'};storeSuperPanel.add(rgs);return frm;}
function CreaPanelBorderOpen(attrPnl,hijosPnl) {var id = getAttrOpen(attrPnl,'id');var frm = new Ext.panel.Panel({title: getAttrOpen(attrPnl,'title'),id: id,collapsible: true,margin: '5,0,0,5',frame: true,layout: 'border',closable: true,width: getAttrOpen(attrPnl,'width'),height: getAttrOpen(attrPnl,'height'),autoScroll:true,resizable:true,listeners: {close: {fn: function(panel, eOpts ){limpiaObjetos(panel.id);}},resize: {fn: function(){cargaAtributosPanel(this.id,storeBorder);}}},tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler: function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeBorder);}}]});for(pl = 0; pl < hijosPnl.length; pl++){var pnl = CreaBorderColumnasOpen(hijosPnl[pl].attrs,id);frm.add(pnl);}return frm;}
function CreaBorderColumnasOpen(attrPnl,papaId) {var hRegion = getAttrOpen(attrPnl,'region');var hStore;var hTipo;var regId = getAttrOpen(attrPnl,'id');panelAcordionId = parseInt(regId.substring(regId.lastIndexOf("_")+1));var titulo = getAttrOpen(attrPnl,'title');var hWidth = getAttrOpen(attrPnl,'width');var hHeight = getAttrOpen(attrPnl,'height');if(hRegion == 'south'){hStore = storeBorderSur;hTipo = 'sur';}else if(hRegion == 'west'){hStore = storeBorderIzq;hTipo = 'izq';}else if(hRegion == 'center'){hStore = storeBorderCenter;hTipo = 'centro';}else if(hRegion == 'east'){hStore = storeBorderDer;hTipo = 'der';}else if(hRegion == 'north'){hStore = storeBorderNorte;hTipo = 'norte';}var frm = new Ext.Panel({title: titulo, region : hRegion, collapsible:true,	split:true,margins:'5 5 5 5',id: regId,layout: 'column',columns: 2,frame: false,bodyPadding: '5 5 5 5',width: hWidth,height: hHeight,autoScroll:true,closable: true,defaultType: 'container',tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler:function(event, toolEl, panelHeader){var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,hStore);}}],afterRender:function(){Ext.Panel.prototype.afterRender.apply(this, arguments);this.dropTarget = Ext.getCmp(regId).body;var dd = new Ext.dd.DropTarget(this.dropTarget,{ddGroup:'controlesDD',notifyDrop:function(dd, e, node){agregaCtrl(node.nodes[0].innerText,regId);return true;}});},listeners: {close:{fn: function(panel, eOpts ){limpiaObjetos(panel.id);}}}});nCtrl ++;var rgs = {idPadre: papaId,idHijo: regId,order: nCtrl,titulo:titulo,columnas:2,bodyPadding:getAttrOpen(attrPnl,'bodyPadding'),isDesplegable:getAttrOpen(attrPnl,'collapsible'),isFondo: getAttrOpen(attrPnl,'frame'),margen:getAttrOpen(attrPnl,'margin'),titulo_Aling:getAttrOpen(attrPnl,'titleAlign'),width: hWidth,height: hHeight,isAutoScroll:getAttrOpen(attrPnl,'autoScroll'),isCerrable: getAttrOpen(attrPnl,'closable'), isBodyBorder:getAttrOpen(attrPnl,'bodyBorder'),tipo:hTipo};storeSuperPanel.add(rgs);return frm;}
function CreateWinOpen(attrPnl) {var idP = getAttrOpen(attrPnl,'id');Ext.create('Ext.Window', {title: getAttrOpen(attrPnl,'title'),id: idP,width: getAttrOpen(attrPnl,'width'),height: getAttrOpen(attrPnl,'height'),headerPosition: 'right',collapsible: true,frame: true,bodyPadding:'5 5 5 5',tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler: function(event, toolEl, panelHeader){var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeWindow);}}],layout: {type: 'column',columns: 1},listeners: {close: {fn: function(panel, eOpts ){limpiaObjetos(panel.id);}},resize: {fn: function(){cargaAtributosPanel(this.id,storeWindow);}}},afterRender:function() { Ext.Panel.prototype.afterRender.apply(this, arguments); this.dropTarget = Ext.getCmp(idP).body;var dd = new Ext.dd.DropTarget(this.dropTarget, {ddGroup:'controlesDD',notifyDrop:function(dd, e, node) { agregaCtrl(node.nodes[0].innerText,idP);return true; }}); }}).show();}
//Formularios NEW
function CreaPanelForma(id,titulo) {var frm = new Ext.form.Panel({title: titulo,id: id,collapsible: true,closable: true,titleAlign : 'center',frame: true,bodyPadding: '5 5 0',width: 350,height: 200,margin: '5,0,0,5',autoScroll:true,resizable:true,tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler: function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeFormulario);}}],listeners: {close: {fn: function(panel, eOpts ){limpiaObjetos(panel.id);}},resize: {fn: function(){cargaAtributosPanel(this.id,storeFormulario);}}/*,render:{fn: function(panel, eOpts ){panel.body.on('click', function() {alert('onclick');});}}*/},afterRender:function() { Ext.Panel.prototype.afterRender.apply(this, arguments); this.dropTarget = Ext.getCmp(id).body;var dd = new Ext.dd.DropTarget(this.dropTarget, {ddGroup:'controlesDD',notifyDrop:function(dd, e, node) {agregaCtrl(node.nodes[0].innerText,id);return true; }}); }});return frm;}
	//****Pendiente agregar por columna
function CreaPanelColumnas(id,titulo) {var frm = new Ext.form.Panel({title: titulo,id: id,layout: 'column',collapsible: true,margin: '5,0,0,5',frame: true,closable: true,columns: 2,bodyPadding: '5 5 0',width: 350,height: 200,autoScroll:true,resizable:true,tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler: function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeColumnas);}}],listeners: {close: {fn: function(panel, eOpts ){limpiaObjetos(panel.id);}},resize: {fn: function(){cargaAtributosPanel(this.id,storeColumnas);}}},afterRender:function() { Ext.Panel.prototype.afterRender.apply(this, arguments); this.dropTarget = Ext.getCmp(id).body;var dd = new Ext.dd.DropTarget(this.dropTarget, {ddGroup:'controlesDD',notifyDrop:function(dd, e, node) { agregaCtrl(node.nodes[0].innerText,id);return true; }}); }});return frm;}    
function CreaPanelTabs(id,titulo) {var frm = new Ext.TabPanel({title: titulo,id: id,collapsible: true,margin: '5',frame: true,closable: true,bodyPadding: '5 5 5 5',width: 350,height: 200,resizable:true,items: [CreaTabColumnas(id), CreaTabColumnas(id), CreaTabColumnas(id)],tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler: function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeTabs);}}],listeners: {close: {fn: function(panel, eOpts ){limpiaObjetos(panel.id);}},resize: {fn: function(){cargaAtributosPanel(this.id,storeTabs);}},tabchange:{fn: function( tabPanel, newCard, oldCard, eOpts ){/*Columnas IN*/var arryAttr = [];ctrl = newCard.id;arryAttr.push('(id):'+newCard.id);arryAttr.push('width:'+newCard.getWidth());arryAttr.push('height:'+newCard.getHeight());arryAttr.push('titulo:'+newCard.title);arryAttr.push('columnas:'+getValorSP('columnas',2));arryAttr.push('isFondo:'+getValorSP('isFondo',true));arryAttr.push('bodyPadding:'+getValorSP('bodyPadding','5 5 5 5'));arryAttr.push('isAutoScroll:'+getValorSP('isAutoScroll',true));arryAttr.push('isCerrable:'+getValorSP('isCerrable',true));arryAttr.push('isBodyBorder:'+getValorSP('isBodyBorder',false));seteaAtributosGrid(arryAttr,newCard.id,storeTabsIn);}}},afterRender:function() { Ext.Panel.prototype.afterRender.apply(this, arguments); this.dropTarget = Ext.getCmp(id).body;var dd = new Ext.dd.DropTarget(this.dropTarget, {ddGroup:'controlesDD',notifyDrop:function(dd, e, node) { agregaCtrl(node.nodes[0].innerText,id);return true; }});}});return frm;}
function CreaTabColumnas(id) {panelTabId ++;var frm = new Ext.form.Panel({title: 'Tab ' + panelTabId,id: 'panel_tab_' + panelTabId,layout: 'column',columns: 2,frame: true,bodyPadding: '5 5 5 5',width: 350,height: 200,autoScroll:true,closable: true,defaultType: 'container',listeners: {close: {fn: function(c ){ limpiaObjetos(this.id);}}}});nCtrl ++;var rgs = {idPadre: id,idHijo: 'panel_tab_' + panelTabId,order: nCtrl,columnas: 2,titulo:'Tab ' + panelTabId,bodyPadding: '5 5 5 5',isFondo: true,width: 350,height: 200,isAutoScroll:true,isCerrable: true, isBodyBorder:false,tipo:'form_columns'};storeSuperPanel.add(rgs);return frm;}
function CreaPanelAcordion(id,titulo) {var frm = new Ext.Panel({title: titulo,id: id,collapsible: true,margin: '5,0,0,5',frame: true,layout: 'accordion',closable: true,width: 350,height: 490,autoScroll:true,resizable:true,defaults: {bodyPadding: 10},tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler: function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeAcordion);}}],items: [CreaAcordionColumnas(id), CreaAcordionColumnas(id), CreaAcordionColumnas(id)],listeners: {close: {fn: function(panel, eOpts ){limpiaObjetos(panel.id);}},resize: {fn: function(){cargaAtributosPanel(this.id,storeAcordion);}}},afterRender:function() { Ext.Panel.prototype.afterRender.apply(this, arguments); this.dropTarget = Ext.getCmp(id).body;var dd = new Ext.dd.DropTarget(this.dropTarget, {ddGroup:'controlesDD',notifyDrop:function(dd, e, node) { agregaCtrl(node.nodes[0].innerText,id);return true; }});}});return frm;}
function CreaAcordionColumnas(id) {panelAcordionId ++;var frm = new Ext.form.Panel({title: 'Acordion ' + panelAcordionId,id: 'panel_acordion_' + panelAcordionId,layout: 'column',columns: 2,frame: false,bodyPadding: '5 5 5 5',width: 50,height: 50,autoScroll:true,closable: false,defaultType: 'container',listeners: {expand: function(pnl) {var arryAttr = [];ctrl = pnl.id;arryAttr.push('(id):'+pnl.id);arryAttr.push('width:'+pnl.getWidth());arryAttr.push('height:'+pnl.getHeight());arryAttr.push('titulo:'+pnl.title);arryAttr.push('columnas:'+getValorSP('columnas',2));arryAttr.push('isFondo:'+getValorSP('isFondo',false));arryAttr.push('bodyPadding:'+getValorSP('bodyPadding','5 5 5 5'));arryAttr.push('isAutoScroll:'+getValorSP('isAutoScroll',true));arryAttr.push('isCerrable:'+getValorSP('isCerrable',false));arryAttr.push('isBodyBorder:'+getValorSP('isBodyBorder',false));seteaAtributosGrid(arryAttr,pnl.id,storeAcordionIn);},close: {fn: function(c ){ limpiaObjetos(this.id);}}}});nCtrl ++;var rgs = {idPadre: id,idHijo: 'panel_acordion_' + panelAcordionId,order: nCtrl,columnas: 2,titulo:'Acordion ' + panelAcordionId,bodyPadding: '5 5 5 5',isFondo: false,width: 350,height: 200,isAutoScroll:true,isCerrable: false, isBodyBorder:false,tipo:'form_columns'};storeSuperPanel.add(rgs);return frm;}
function CreaPanelTablas(id) {var nameP = 'tablas' + id;var frm = new Ext.Panel({title: 'Titulo del Panel estilo Tablas ' + id,id: nameP,collapsible: true,margin: '5,0,0,5',frame: true,layout: {type: 'table',columns: 3},defaults: {frame:true, width:200, height: 200},closable: true,width: 350,height: 200,autoScroll:true,resizable:true,afterRender:function() { Ext.Panel.prototype.afterRender.apply(this, arguments); this.dropTarget = Ext.getCmp(nameP).body;var dd = new Ext.dd.DropTarget(this.dropTarget, {ddGroup:'controlesDD',notifyDrop:function(dd, e, node) { agregaCtrl(node.nodes[0].innerText,nameP);return true; }}); }});return frm;}
//****Penidente agregar en cada border agregaCtrl(node.nodes[0].innerText,nameP);
function CreaPanelBorder(id,titulo) {var sur = 'sur_'+ borderId;nCtrl ++;var rgsSur = {idPadre: id,idHijo: sur,order: nCtrl,titulo:'Región Sur ' + borderId,bodyPadding: '5 5 5 5',height: 100,isFondo: false,isAutoScroll:true,isCerrable: false, isBodyBorder:false,isDesplegable:false,nombre:id,margen:'5 5 5 5',titulo_Aling:'left',columnas:2,tipo:'sur'};storeSuperPanel.add(rgsSur);var izq= 'izq_'+ borderId;nCtrl ++;var rgsIzq = {idPadre: id,idHijo: izq,order: nCtrl,titulo:'Región Izquierda ' + borderId,bodyPadding: '5 5 5 5',width: 200,isFondo: false,isAutoScroll:true,isCerrable: false, isBodyBorder:false,isDesplegable:false,nombre:id,margen:'5 5 5 5',titulo_Aling:'left',columnas:2,tipo:'izq'};storeSuperPanel.add(rgsIzq);var centro= 'centro_'+ borderId;nCtrl ++;var rgsCentro = {idPadre: id,idHijo: centro,order: nCtrl,titulo:'Región Centro ' + borderId,bodyPadding: '5 5 5 5',isFondo: false,isAutoScroll:true,isCerrable: false, isBodyBorder:false,isDesplegable:false,nombre:id,margen:'5 5 5 5',titulo_Aling:'left',columnas:2,tipo:'centro'};storeSuperPanel.add(rgsCentro);var der = 'der_'+ borderId;nCtrl ++;var rgsDer = {idPadre: id,idHijo: der,order: nCtrl,titulo:'Región Derecha ' + borderId,bodyPadding: '5 5 5 5',width: 200,isFondo: false,isAutoScroll:true,isCerrable: false, isBodyBorder:false,isDesplegable:false,nombre:id,margen:'5 5 5 5',titulo_Aling:'left',columnas:2,tipo:'der'};storeSuperPanel.add(rgsDer);var norte = 'norte_'+ borderId;nCtrl ++;var rgsNorte = {idPadre: id,idHijo: norte,order: nCtrl,titulo:'Región Norte ' + borderId,bodyPadding: '5 5 5 5',height: 100,isFondo: false,isAutoScroll:true,isCerrable: false, isBodyBorder:false,isDesplegable:false,nombre:id,margen:'5 5 5 5',titulo_Aling:'left',columnas:2, tipo:'norte'};storeSuperPanel.add(rgsNorte);var frm = new Ext.panel.Panel({title: titulo,id: id,collapsible: true,margin: '5,0,0,5',frame: true,layout: 'border',closable: true,width: 700,height: 480,autoScroll:true,resizable:true,items: [{title: 'Región Sur ' + borderId,region:'south',xtype:'panel',height: 100,id: sur,layout: 'column',columns: 2,closable:true,collapsible:true,autoScroll:true,split:true,margins:'5 5 5 5',tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler:function(event, toolEl, panelHeader){var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeBorderSur);}}],afterRender:function(){Ext.Panel.prototype.afterRender.apply(this, arguments);this.dropTarget = Ext.getCmp(sur).body;var dd = new Ext.dd.DropTarget(this.dropTarget,{ddGroup:'controlesDD',notifyDrop:function(dd, e, node){agregaCtrl(node.nodes[0].innerText,sur);return true;}});},listeners: {close:{fn: function(panel, eOpts ){limpiaObjetos(panel.id);}}}},{title: 'Región Izquierda ' + borderId,region:'west',xtype: 'panel',margins: '5 5 5 5',width: 200,closable: true,collapsible: true,id: izq,autoScroll:true,layout: 'column',split:true,columns: 2,afterRender:function(){Ext.Panel.prototype.afterRender.apply(this, arguments);this.dropTarget = Ext.getCmp(izq).body;var dd = new Ext.dd.DropTarget(this.dropTarget,{ddGroup:'controlesDD',notifyDrop:function(dd, e, node){agregaCtrl(node.nodes[0].innerText,izq);return true;}});},listeners:{close:{fn: function(panel, eOpts ){limpiaObjetos(panel.id);}}},tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler:function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeBorderIzq);}}]},{title: 'Región Centro '+ borderId, region: 'center', xtype: 'panel',margins: '5 5 5 5',closable: true,collapsible: true,id: centro,autoScroll:true,layout: 'column',split:true,columns: 2,afterRender:function(){Ext.Panel.prototype.afterRender.apply(this, arguments);this.dropTarget = Ext.getCmp(centro).body;var dd = new Ext.dd.DropTarget(this.dropTarget,{ddGroup:'controlesDD',notifyDrop:function(dd, e, node){agregaCtrl(node.nodes[0].innerText,centro);return true;}});},listeners:{close:{fn:function(panel, eOpts ){limpiaObjetos(panel.id);}}},tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler:function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeBorderCenter);}}]},{title: 'Región Norte ' + borderId,region: 'north',xtype: 'panel',height: 100,closable: true,collapsible: true,id: norte,autoScroll:true,layout: 'column',columns: 2,split:true,margins: '5 5 5 5',tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler:function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeBorderNorte);}}],afterRender:function(){Ext.Panel.prototype.afterRender.apply(this, arguments);this.dropTarget = Ext.getCmp(norte).body;var dd = new Ext.dd.DropTarget(this.dropTarget,{ddGroup:'controlesDD',notifyDrop:function(dd, e, node){agregaCtrl(node.nodes[0].innerText,norte);return true;}});},listeners:{close:{fn:function(panel, eOpts ){limpiaObjetos(panel.id);}}}},{title: 'Región Derecha ' + borderId,region:'east',xtype:'panel',margins:'5 5 5 5',width:200,closable:true,collapsible:true,id:der,layout: 'column',columns: 2,split:true,autoScroll:true,afterRender:function(){Ext.Panel.prototype.afterRender.apply(this, arguments);this.dropTarget = Ext.getCmp(der).body;var dd = new Ext.dd.DropTarget(this.dropTarget,{ddGroup:'controlesDD',notifyDrop:function(dd, e, node){agregaCtrl(node.nodes[0].innerText,der);return true;}});},listeners:{close:{fn: function(panel, eOpts ){limpiaObjetos(panel.id);}}},tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler:function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeBorderDer);}}]}],listeners: {close: {fn: function(panel, eOpts ){limpiaObjetos(panel.id);}},resize: {fn: function(){cargaAtributosPanel(this.id,storeBorder);}}},tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler: function(event, toolEl, panelHeader) {var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeBorder);}}]});return frm;}
function CreateWin(id,titulo) {Ext.create('Ext.Window', {title: titulo,id: id,width: 400,height: 200,x: 450,y: 200,headerPosition: 'right',collapsible: true,frame: true,bodyPadding:'5 5 5 5',tools:[{type:'gear',tooltip: 'Mostrar atributos del panel',handler: function(event, toolEl, panelHeader){var name = panelHeader.id;var id = name.substring(0,name.indexOf("_header"));cargaAtributosPanel(id,storeWindow);}}],layout: {type: 'column',columns: 1},listeners: {close: {fn: function(panel, eOpts ){limpiaObjetos(panel.id);}},resize: {fn: function(){cargaAtributosPanel(this.id,storeWindow);}}},afterRender:function() { Ext.Panel.prototype.afterRender.apply(this, arguments); this.dropTarget = Ext.getCmp(id).body;var dd = new Ext.dd.DropTarget(this.dropTarget, {ddGroup:'controlesDD',notifyDrop:function(dd, e, node) { agregaCtrl(node.nodes[0].innerText,id);return true; }}); }}).show();}

//Funciones para agregar controles
function CreaCtrlTexto(_fieldLabel,id, _texto,_height) {var txt = {xtype:'textfield',fieldLabel: _fieldLabel,id: id,labelWidth : 100,height:_height,value:_texto,allowBlank:true,margin:'5 5 5 5',anchor:'95%',listeners: {focus: {fn: function(c ){ var arryAttr = [];ctrl = c.id;arryAttr.push('(id):'+c.id);arryAttr.push('width:'+getValorSP('width','0'));arryAttr.push('isPadre:'+getValorSP('isPadre',false));arryAttr.push('height:'+ c.getHeight());arryAttr.push('margen:'+getValorSP('margen','5 5 5 5'));arryAttr.push('padding:'+getValorSP('padding','0 0 0 0'));arryAttr.push('etiqueta:'+getValorSP('etiqueta',c.id));arryAttr.push('nombre:'+getValorSP('nombre',c.id));arryAttr.push('texto:'+c.getValue());arryAttr.push('etiqueta_aling:'+getValorSP('etiqueta_aling','left'));arryAttr.push('etiqueta_width:'+getValorSP('etiqueta_width',100));arryAttr.push('isBloqueado:'+getValorSP('isBloqueado',false));arryAttr.push('isRequerido:'+getValorSP('isRequerido',false));arryAttr.push('isRequeridoMsg:'+getValorSP('isRequeridoMsg',''));arryAttr.push('isAnchor:'+getValorSP('isAnchor',0));arryAttr.push('textoMax:'+getValorSP('textoMax',0));arryAttr.push('textoMin:'+getValorSP('textoMin',0));arryAttr.push('textoSugerido:'+getValorSP('textoSugerido',''));arryAttr.push('textoMaxMsg:'+getValorSP('textoMaxMsg',''));arryAttr.push('textoMinMsg:'+getValorSP('textoMinMsg',''));arryAttr.push('toolTip:'+getValorSP('toolTip',''));arryAttr.push('soloLectura:'+getValorSP('soloLectura',false));seteaAtributosGrid(arryAttr,c.id,storeTextAttr);}}}};return txt;}
function CreaCtrlNumeric(_fieldLabel,id, _texto,_height) {var txt = {xtype:'numberfield',fieldLabel: _fieldLabel,id: id,labelWidth : 100,height:_height,value:_texto,allowBlank:true,margin:'5 5 5 5',anchor:'95%',listeners: {focus: {fn: function(c ){ var arryAttr = [];ctrl = c.id;arryAttr.push('(id):'+c.id);arryAttr.push('etiqueta:'+getValorSP('etiqueta',c.id));arryAttr.push('isEditable:'+getValorSP('isEditable',true));arryAttr.push('margen:'+getValorSP('margen','5 5 5 5'));arryAttr.push('padding:'+getValorSP('padding','0 0 0 0'));arryAttr.push('etiqueta_aling:'+getValorSP('etiqueta_aling','left'));arryAttr.push('etiqueta_width:'+getValorSP('etiqueta_width',100));arryAttr.push('height:'+ c.getHeight());arryAttr.push('isAnchor:'+getValorSP('isAnchor',0));arryAttr.push('isBloqueado:'+getValorSP('isBloqueado',false));arryAttr.push('isRequerido:'+getValorSP('isRequerido',false));arryAttr.push('isRequeridoMsg:'+getValorSP('isRequeridoMsg',''));arryAttr.push('nombre:'+getValorSP('nombre',c.id));arryAttr.push('soloLectura:'+getValorSP('soloLectura',false));arryAttr.push('texto:'+c.getRawValue());arryAttr.push('textoMax:'+getValorSP('textoMax',0));arryAttr.push('textoMaxMsg:'+getValorSP('textoMaxMsg',''));arryAttr.push('textoMin:'+getValorSP('textoMin',0));arryAttr.push('textoMinMsg:'+getValorSP('textoMinMsg',''));arryAttr.push('toolTip:'+getValorSP('toolTip',''));arryAttr.push('width:'+getValorSP('width','0'));arryAttr.push('valorMax:'+getValorSP('valorMax',0));arryAttr.push('valorMin:'+getValorSP('valorMin',0));arryAttr.push('valorMaxMsg:'+getValorSP('valorMaxMsg',''));arryAttr.push('valorMinMsg:'+getValorSP('valorMinMsg',''));arryAttr.push('isFlechas:'+getValorSP('isFlechas',true));seteaAtributosGrid(arryAttr,c.id,storeNumericAttr);/*cargaAtributosCtrl(arryAttr,storeNumericAttr,c.id);*/}}}};return txt;}
function CreaCtrlPicker(_fieldLabel,id, _texto,_height) {var txt = {xtype:'datefield',fieldLabel: _fieldLabel,id: id,labelWidth : 100,height:_height,value:_texto,anchor:'95%',margin:'5 5 5 5',format:'d/m/Y',listeners: {focus: {fn: function(c ){ var arryAttr = [];ctrl = c.id;arryAttr.push('(id):'+c.id);arryAttr.push('etiqueta:'+getValorSP('etiqueta',c.id));arryAttr.push('etiqueta_aling:'+getValorSP('etiqueta_aling','left'));arryAttr.push('etiqueta_width:'+getValorSP('etiqueta_width',100));arryAttr.push('isPadre:'+getValorSP('isPadre',''));arryAttr.push('height:'+ c.getHeight());arryAttr.push('isAnchor:'+getValorSP('isAnchor',0));arryAttr.push('isBloqueado:'+getValorSP('isBloqueado',false));arryAttr.push('isRequerido:'+getValorSP('isRequerido',false));arryAttr.push('isRequeridoMsg:'+getValorSP('isRequeridoMsg',''));arryAttr.push('isEditable:'+getValorSP('isEditable',true));arryAttr.push('nombre:'+getValorSP('nombre',c.id));arryAttr.push('soloLectura:'+getValorSP('soloLectura',false));arryAttr.push('fecha:'+getValorSP('fecha',''));arryAttr.push('textoMax:'+getValorSP('textoMax',10));arryAttr.push('textoMaxMsg:'+getValorSP('textoMaxMsg',''));arryAttr.push('textoMin:'+getValorSP('textoMin',10));arryAttr.push('textoMinMsg:'+getValorSP('textoMinMsg',''));arryAttr.push('toolTip:'+getValorSP('toolTip',''));arryAttr.push('width:'+getValorSP('width','0'));arryAttr.push('textoSugerido:'+getValorSP('textoSugerido',''));arryAttr.push('fechaMax:'+getValorSP('fechaMax',''));arryAttr.push('fechaMaxMsg:'+getValorSP('fechaMaxMsg',''));arryAttr.push('fechaMin:'+getValorSP('fechaMin',''));arryAttr.push('fechaMinMsg:'+getValorSP('fechaMinMsg',''));arryAttr.push('margen:'+getValorSP('margen','5 5 5 5'));arryAttr.push('padding:'+getValorSP('padding','0 0 0 0'));arryAttr.push('fechaInvalidTxt:'+getValorSP('fechaInvalidTxt',''));seteaAtributosGrid(arryAttr,c.id,storePickerAttr);}}}};return txt;}
function CreaCtrlCheck(_fieldLabel,id,_height,_isCheck){var myCtrl = {xtype:'checkboxfield',/*hideLabel: true,boxLabel: id,*/fieldLabel: _fieldLabel,height:_height,checked:_isCheck,margin:'5 5 5 5',id: id,anchor:'95%',listeners: {focus: {fn: function(c ){ cChk = false;var arryAttr = [];ctrl = c.id;arryAttr.push('(id):'+c.id);arryAttr.push('etiqueta:'+getValorSP('etiqueta',c.id));arryAttr.push('etiqueta_width:'+getValorSP('etiqueta_width',100));arryAttr.push('etiqueta_aling:'+getValorSP('etiqueta_aling','left'));arryAttr.push('height:'+ c.getHeight());arryAttr.push('isAnchor:'+getValorSP('isAnchor',0));arryAttr.push('isBloqueado:'+getValorSP('isBloqueado',false));arryAttr.push('isSeleccionado:'+getValorSP('isSeleccionado',false));arryAttr.push('nombre:'+getValorSP('nombre',c.id));arryAttr.push('soloLectura:'+getValorSP('soloLectura',false));arryAttr.push('toolTip:'+getValorSP('toolTip',''));arryAttr.push('margen:'+getValorSP('margen','5 5 5 5'));arryAttr.push('padding:'+getValorSP('padding','0 0 0 0'));arryAttr.push('width:'+getValorSP('width','0'));seteaAtributosGrid(arryAttr,c.id,storeCheckAttr);}},click: {element: 'el', fn: function (id) {if(cChk == false){cChk = true;bChk = Ext.getCmp(this.id).getValue();}else{cChk = false;Ext.getCmp(this.id).setValue(bChk);}var arryAttr = [];var s = Ext.getCmp(this.id).getValue();ctrl = this.id;arryAttr.push('(id):'+this.id);arryAttr.push('etiqueta:'+getValorSP('etiqueta',this.id));arryAttr.push('etiqueta_width:'+getValorSP('etiqueta_width',100));arryAttr.push('etiqueta_aling:'+getValorSP('etiqueta_aling','left'));arryAttr.push('height:'+ getValorSP('height',24));arryAttr.push('isAnchor:'+getValorSP('isAnchor',0));arryAttr.push('isBloqueado:'+getValorSP('isBloqueado',false));arryAttr.push('nombre:'+getValorSP('nombre',this.id));arryAttr.push('soloLectura:'+getValorSP('soloLectura',false));arryAttr.push('toolTip:'+getValorSP('toolTip',''));arryAttr.push('margen:'+getValorSP('margen','5 5 5 5'));arryAttr.push('padding:'+getValorSP('padding','0 0 0 0'));arryAttr.push('width:'+getValorSP('width','0'));arryAttr.push('isSeleccionado:'+s);seteaAtributosGrid(arryAttr,this.id,storeCheckAttr);}}}};return myCtrl;}
function CreaCtrlRadio(_fieldLabel,id,_height,_isCheck){var myCtrl = {xtype:'radiofield',/*hideLabel: true,//boxLabel: id,*/fieldLabel: _fieldLabel,id: id,height:_height,checked:_isCheck,anchor:'95%',margin:'5 5 5 5',listeners: {focus: {fn: function(c ){ var arryAttr = [];ctrl = c.id;arryAttr.push('(id):'+c.id);arryAttr.push('etiqueta:'+getValorSP('etiqueta',c.id));arryAttr.push('etiqueta_width:'+getValorSP('etiqueta_width',100));arryAttr.push('etiqueta_aling:'+getValorSP('etiqueta_aling','left'));arryAttr.push('height:'+ c.getHeight());arryAttr.push('isAnchor:'+getValorSP('isAnchor',0));arryAttr.push('isBloqueado:'+getValorSP('isBloqueado',false));arryAttr.push('isSeleccionado:'+getValorSP('isSeleccionado',false));arryAttr.push('nombre:'+getValorSP('nombre',c.id));arryAttr.push('soloLectura:'+getValorSP('soloLectura',false));arryAttr.push('toolTip:'+getValorSP('toolTip',''));arryAttr.push('margen:'+getValorSP('margen','5 5 5 5'));arryAttr.push('padding:'+getValorSP('padding','0 0 0 0'));arryAttr.push('width:'+getValorSP('width','0'));seteaAtributosGrid(arryAttr,c.id,storeRadioAttr);}},click: {element: 'el', fn: function (id) {if(cChk == false){cChk = true;bChk = Ext.getCmp(this.id).getValue();}else{cChk = false;Ext.getCmp(this.id).setValue(false);}var arryAttr = [];var s = Ext.getCmp(this.id).getValue();ctrl = this.id;arryAttr.push('(id):'+this.id);arryAttr.push('etiqueta:'+getValorSP('etiqueta',this.id));arryAttr.push('etiqueta_width:'+getValorSP('etiqueta_width',100));arryAttr.push('etiqueta_aling:'+getValorSP('etiqueta_aling','left'));arryAttr.push('height:'+ getValorSP('height',24));arryAttr.push('isAnchor:'+getValorSP('isAnchor',0));arryAttr.push('isBloqueado:'+getValorSP('isBloqueado',false));arryAttr.push('nombre:'+getValorSP('nombre',this.id));arryAttr.push('soloLectura:'+getValorSP('soloLectura',false));arryAttr.push('toolTip:'+getValorSP('toolTip',''));arryAttr.push('margen:'+getValorSP('margen','5 5 5 5'));arryAttr.push('padding:'+getValorSP('padding','0 0 0 0'));arryAttr.push('width:'+getValorSP('width','0'));arryAttr.push('isSeleccionado:'+s);seteaAtributosGrid(arryAttr,this.id,storeRadioAttr);}}}};return myCtrl;}
function CreaCtrlBoton(_fieldLabel,id,_height,_Icon){var myCtrl = {xtype:'button',id: id,anchor:'95%',margin:'5 5 5 5',text : _fieldLabel,scale: 'small',height:_height,iconCls:_Icon,listeners: {click: {fn: function(c ){var arryAttr = [];ctrl = c.id;arryAttr.push('(id):'+c.id);arryAttr.push('height:'+ c.getHeight());arryAttr.push('texto:'+getValorSP('texto',c.id));arryAttr.push('row:'+getValorSP('row',0));arryAttr.push('isAnchor:'+getValorSP('isAnchor',0));arryAttr.push('isBloqueado:'+getValorSP('isBloqueado',false));arryAttr.push('nombre:'+getValorSP('nombre',c.id));arryAttr.push('toolTip:'+getValorSP('toolTip',''));arryAttr.push('estilo:'+getValorSP('estilo','normal'));arryAttr.push('margen:'+getValorSP('margen','5 5 5 5'));arryAttr.push('padding:'+getValorSP('padding','0 0 0 0'));arryAttr.push('imagenCls:'+getValorSP('imagenCls',''));arryAttr.push('imagen_aling:'+getValorSP('imagen_aling',''));arryAttr.push('escala:'+getValorSP('escala',''));arryAttr.push('width:'+getValorSP('width','0'));seteaAtributosGrid(arryAttr,c.id,storeBotonAttr);}}}};return myCtrl;}
function CreaCtrlCombo(_fieldLabel,id,_texto,_height){var myCtrl = {xtype:'combobox',id: id,fieldLabel: _fieldLabel,height:_height,anchor:'95%',value:_texto,margin:'5 5 5 5',store: [['tr','uno'],['ru','dos'],['en','tres']],mode: 'local',listeners: {focus: {fn: function(c ){cChk = false;var arryAttr = [];ctrl = c.id;arryAttr.push('(id):'+c.id);arryAttr.push('etiqueta:'+getValorSP('etiqueta',c.id));arryAttr.push('height:'+ c.getHeight());arryAttr.push('width:'+getValorSP('width','0'));arryAttr.push('etiqueta_aling:'+getValorSP('etiqueta_aling','left'));arryAttr.push('isAnchor:'+getValorSP('isAnchor',0));arryAttr.push('isBloqueado:'+getValorSP('isBloqueado',false));arryAttr.push('isRequerido:'+getValorSP('isRequerido',false));arryAttr.push('isEditable:'+getValorSP('isEditable',true));arryAttr.push('isRequeridoMsg:'+getValorSP('isRequeridoMsg',''));arryAttr.push('nombre:'+getValorSP('nombre',c.id));arryAttr.push('soloLectura:'+getValorSP('soloLectura',false));arryAttr.push('etiqueta_width:'+getValorSP('etiqueta_width',100));arryAttr.push('margen:'+getValorSP('margen','5 5 5 5'));arryAttr.push('padding:'+getValorSP('padding','0 0 0 0'));arryAttr.push('texto:'+getValorSP('texto',''));arryAttr.push('textoSugerido:'+getValorSP('textoSugerido',''));arryAttr.push('textoMaxMsg:'+getValorSP('textoMaxMsg',''));arryAttr.push('textoMinMsg:'+getValorSP('textoMinMsg',''));arryAttr.push('textoMax:'+getValorSP('textoMax',10));arryAttr.push('textoMin:'+getValorSP('textoMin',10));arryAttr.push('toolTip:'+getValorSP('toolTip',''));arryAttr.push('multiSelect:'+getValorSP('multiSelect',false));arryAttr.push('store:'+getValorSP('store',''));arryAttr.push('modo:'+getValorSP('modo','remote'));arryAttr.push('selectconFoco:'+getValorSP('selectconFoco',false));arryAttr.push('selectAction:'+getValorSP('selectAction','all'));arryAttr.push('valorDisplay:'+getValorSP('valorDisplay','value'));arryAttr.push('valorId:'+getValorSP('valorId','key'));arryAttr.push('isFlecha:'+getValorSP('isFlecha',false));arryAttr.push('isAutoComp:'+getValorSP('isAutoComp',false));arryAttr.push('isPadre:'+getValorSP('isPadre',false));arryAttr.push('delimitador:'+getValorSP('delimitador',''));seteaAtributosGrid(arryAttr,c.id,storeComboAttr);}}}};return myCtrl;}
function CreaCtrlLabel(id,_texto){var myCtrl = {xtype:'label',id: id,margin:'5 5 5 5',text : _texto,listeners: {render: function(obj) {Ext.get(obj.id).on('click',function(e) {var arryAttr = [];ctrl = obj.id;arryAttr.push('(id):'+obj.id);arryAttr.push('nombre:'+getValorSP('nombre',obj.id));arryAttr.push('height:'+ obj.getHeight());arryAttr.push('margen:'+getValorSP('margen','5 5 5 5'));arryAttr.push('texto:'+getValorSP('texto',obj.id));arryAttr.push('width:'+getValorSP('width','0'));arryAttr.push('html:'+getValorSP('html',''));seteaAtributosGrid(arryAttr,obj.id,storeLabelAttr);});}}};return myCtrl;}
function CreaCtrlImagen(id,_Src){var myCtrl = {xtype:'image',id: id,src:'http://www.sencha.com/img/20110215-feat-html5.png',listeners: {render: function(obj) {Ext.get(obj.id).on('click',function(e) {var arryAttr = [];ctrl = obj.id;arryAttr.push('(id):'+obj.id);arryAttr.push('nombre:'+getValorSP('nombre',obj.id));arryAttr.push('height:'+ obj.getHeight());arryAttr.push('width:'+getValorSP('width','0'));arryAttr.push('margen:'+getValorSP('margen','5 5 5 5'));arryAttr.push('src:'+getValorSP('src','http://www.sencha.com/img/20110215-feat-html5.png'));seteaAtributosGrid(arryAttr,obj.id,storeImagenAttr);});}}};return myCtrl;}
function CreaCtrlHidden(id,_texto){var myCtrl = {xtype:'label',id: id,text : _texto,margin:'5 5 5 5',listeners: {render: function(obj) {Ext.get(obj.id).on('click',function(e) {var arryAttr = [];ctrl = obj.id;arryAttr.push('(id):'+obj.id);arryAttr.push('nombre:'+getValorSP('nombre',obj.id));arryAttr.push('texto:'+getValorSP('texto',obj.id));seteaAtributosGrid(arryAttr,obj.id,storeHiddenAttr);});}}};return myCtrl;}


function CreaCtrlGrid(id,forma,titulo){var myCtrl = {xtype:'gridpanel',id: id,margin:'5 5 5 5',
		title : titulo,
//		store: Ext.create('Ext.data.Store', { 
//			model: 'modelGridDef', 
//			storeId : 'store_'+id,
//			autoLoad: false, 
//			proxy: { 
//				type: 'ajax', 
//				url: '../../confpantallas/cargaColumnas.action',
//				reader: {type: 'json',root: 'data'} 
//			}
//		}), 
		store: Ext.create('Ext.data.SimpleStore', { 
			model: 'modelGridDef', 
			storeId : 'store_'+id,
			autoLoad: false
		}), 
		columns: [ {text: "Titulo", 
					width:180,
					dataIndex: 'texto',
					editor: {
		                allowBlank: false
		            }}, 
		           {text: "Width", width: 60, 
						xtype: 'numbercolumn',
						format: '00000',
						editor: {
			                xtype: 'numberfield',
			                allowBlank: false
			            },
						dataIndex: 'width'},
		           {text: "Tipo", width: 80, dataIndex: 'tipoG',
							editor: {
								xtype: 'combobox',
								store: storeListaTipoDatos,
								queryMode: 'local',
								displayField: 'key',
								valueField: 'value'
							}
		           },
		           {text: "DataIndex", width: 90, dataIndex: 'dataIndex',
						editor: {
			                allowBlank: false
			            }},
			        {text: "Id", width: 70, dataIndex: 'name'
					}
		           ], 
		width: 470, height: 260,border:true,resizable:true,
		tbar: [{
		    text: 'Add Columna',
		    handler : function() {
		    	var plugin = Ext.getCmp(id).getPlugin('cellplugin_'+id);
		    	plugin.cancelEdit();
		    	gridIdH ++;
				var gh = 'colG_' + gridIdH;
				creaDataSPG(forma,gh,id,gh);
                // Create a model instance
                var r = Ext.create('modelGridDef', {
                	texto: 'New Columna',
                    width: 80,
                    tipoG: 'string',
                    dataIndex: 'titulo',
                    name: gh
                });
                var rowG = plugin.grid.store.data.length;
		    	Ext.getCmp(id).store.insert(rowG, r);
                plugin.startEdit(rowG, 0);
            }
		    	
		}, {
			text: 'Eliminar',
			handler: function() {
                var sm = Ext.getCmp(id).getSelectionModel();
                var plugin = Ext.getCmp(id).getPlugin('cellplugin_'+id);
                plugin.cancelEdit();
                var row = sm.selected.items[0].data;
                eliminaGridRow(row.name);
                var gstore = Ext.getCmp(id).store;
                gstore.remove(sm.getSelection());
                if (gstore.getCount() > 0) {
                    sm.select(0);
                }
            }
		}],
		plugins: Ext.create('Ext.grid.plugin.RowEditing', {
		    clicksToMoveEditor: 2,
		    pluginId: 'cellplugin_'+id,
		    autoCancel: false,
		    listeners: {
		    	edit: function(editor,e,opt){
					e.record.commit();
					updateGridRow(e.record.data.texto,e.record.data.width,e.record.data.tipoG,e.record.data.dataIndex,e.record.data.name);
				}
			}
		}),
		listeners: {
		    render: function(obj) {
		        Ext.get(obj.id).on('click',function(e) {
		        	var arryAttr = [];ctrl = obj.id;arryAttr.push('(id):'+obj.id);arryAttr.push('nombre:'+getValorSP('nombre',obj.id));
		        	arryAttr.push('height:'+ obj.getHeight());arryAttr.push('width:'+getValorSP('width','0'));arryAttr.push('margen:'+getValorSP('margen','5 5 5 5'));
		        	arryAttr.push('query:'+getValorSP('query',''));
		        	arryAttr.push('titulo:'+getValorSP('titulo',''));
		        	arryAttr.push('titulo_Aling:'+getValorSP('titulo_Aling','left'));
		        	arryAttr.push('columna_orden:'+getValorSP('columna_orden','true'));
		        	arryAttr.push('isResizable:'+getValorSP('isResizable','false'));
		        	arryAttr.push('isFondo:'+getValorSP('isFondo','false'));
		        	arryAttr.push('columna_hidden:'+getValorSP('columna_hidden','true'));
		        	arryAttr.push('columna_move:'+getValorSP('columna_move','true'));
		        	arryAttr.push('columna_resize:'+getValorSP('columna_resize','true'));
		        	arryAttr.push('isDesplegable:'+getValorSP('isDesplegable','false'));
		        	arryAttr.push('isCerrable:'+getValorSP('isCerrable','false'));
		        	arryAttr.push('isBodyBorder:'+getValorSP('isBodyBorder','false'));
		        	seteaAtributosGrid(arryAttr,obj.id,storeGridAttr);
		        });
		   },close: {
           	fn: function(panel, eOpts ){limpiaObjetos(panel.id);}
       		},resize: {
       		fn: function(){cargaAtributosPanel(this.id,storeGridAttr);}
       		}
		}
		};

return myCtrl;}

//Open Paneles
function openPanel(jsonResponse,nombre){
	for(rp = 0; rp < jsonResponse.regreso.length; rp++){
		var attrPnl = jsonResponse.regreso[rp].attrs;
		var hijosPnl = jsonResponse.regreso[rp].attrsHijos;  
		var controles = jsonResponse.regreso[rp].controles;
		var regId;
		var arryAttr = [];
		var store;
		var tipo = getAttrOpen(attrPnl,'xtype');
		var layout = getAttrOpen(attrPnl,'layout');
		if (tipo === 'form' && layout == ''){
			regId = getAttrOpen(attrPnl,'id');
			panelesId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store = storeFormulario;
			target.add(CreaPanelFormaOpen(attrPnl));
			arryAttr.push('tipo:form');
			arryAttr.push('url:'+getAttrOpen(attrPnl,'url'));
		}else if(tipo === 'form' && layout == 'column'){
			regId = getAttrOpen(attrPnl,'id');
			columnasId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store = storeColumnas;
			target.add(CreaPanelColumnasOpen(attrPnl));
			arryAttr.push('tipo:form_columns');
			arryAttr.push('url:'+getAttrOpen(attrPnl,'url'));
			arryAttr.push('columnas:'+getAttrOpen(attrPnl,'columns'));
		}else if(tipo === 'panel' && layout == 'accordion'){
			regId = getAttrOpen(attrPnl,'id');
			acordionId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store = storeAcordion;
			target.add(CreaPanelAcordionOpen(attrPnl,hijosPnl));
			arryAttr.push('tipo:form_acordion');
			arryAttr.push('tabs:'+hijosPnl.length);
		}else if(tipo === 'panel' && layout == 'border'){
			regId = getAttrOpen(attrPnl,'id');
			borderId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store = storeBorder;
			target.add(CreaPanelBorderOpen(attrPnl,hijosPnl));
			arryAttr.push('tipo:border');
		}else if(tipo === 'tabpanel'){
			regId = getAttrOpen(attrPnl,'id');
			tabsId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store = storeTabs;
			target.add(CreaPanelTabsOpen(attrPnl,hijosPnl));
			arryAttr.push('tipo:form_tabs');
			arryAttr.push('tabs:'+hijosPnl.length);
		}else if(tipo === 'window'){
			regId = getAttrOpen(attrPnl,'id');
			ventanaId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store = storeWindow;
			target.add(CreateWinOpen(attrPnl));
			arryAttr.push('(id):'+regId);
			arryAttr.push('columnas:'+getAttrOpen(attrPnl,'columns'));
			arryAttr.push('columnas:'+getAttrOpen(attrPnl,'columns'));
			arryAttr.push('titulo_Posicion:'+getAttrOpen(attrPnl,'headerPosition'));
			arryAttr.push('isModal:'+getAttrOpen(attrPnl,'modal'));
			arryAttr.push('isMinimizable:'+getAttrOpen(attrPnl,'minimizable'));
			arryAttr.push('tipo:'+getAttrOpen(attrPnl,'xtype'));
			arryAttr.push('cordX:0');arryAttr.push('cordY:0');
			arryAttr.push('winEstilo:normal');
		}
		focusPanel=regId;
		creaDataSP('superpanel',regId);
		arryAttr.push('nombre:'+getAttrOpen(attrPnl,'name'));
		arryAttr.push('isDesplegable:'+getAttrOpen(attrPnl,'collapsible'));
		arryAttr.push('bodyPadding:' +getAttrOpen(attrPnl,'bodyPadding'));
		arryAttr.push('isAutoScroll:' +getAttrOpen(attrPnl,'autoScroll'));
		arryAttr.push('isBodyBorder:'+getAttrOpen(attrPnl,'bodyBorder'));
		arryAttr.push('isCerrable:'+getAttrOpen(attrPnl,'closable'));
		arryAttr.push('isFondo:'+getAttrOpen(attrPnl,'frame'));
		arryAttr.push('isResizable:'+getAttrOpen(attrPnl,'resizable'));
		arryAttr.push('height:'+getAttrOpen(attrPnl,'height'));
		arryAttr.push('width:'+getAttrOpen(attrPnl,'width'));
		arryAttr.push('titulo:'+getAttrOpen(attrPnl,'title'));
		if (tipo != 'Ventana'){
			arryAttr.push('margen:'+getAttrOpen(attrPnl,'margin'));
		}
		arryAttr.push('titulo_Aling:'+getAttrOpen(attrPnl,'titleAlign'));
		seteaAtributosGrid(arryAttr,regId,store);	
		agregaControlesOpen(controles,regId,nombre);
		Ext.getCmp('west-panel').items.items[1].expand();
	
	}
}


// Para agregar los Paneles de trabajo
function agrega(tipo){
	isOpen = false;
	tipo = trim(tipo);
	var id='';
	var store;var titulo='';
	var arryAttr = [];
	if (tipo === 'Formulario'){
		panelesId ++;
		id = 'panel_' + panelesId;
		titulo = 'Titulo del formulario ' + panelesId;
		store = storeFormulario;
		target.add(CreaPanelForma(id,titulo));
		arryAttr.push('height:200');/*valor inicial del objeto*/
		arryAttr.push('width:350');/*valor inicial del objeto*/
		arryAttr.push('tipo:form');
	}else if(tipo === 'Columnas'){
		columnasId ++;
		id = 'columnas_' + columnasId;
		titulo = 'Titulo del Panel con Columnas ' + columnasId;
		store = storeColumnas;
		target.add(CreaPanelColumnas(id,titulo));
		arryAttr.push('height:200');/*valor inicial del objeto*/
		arryAttr.push('width:350');
		arryAttr.push('columnas:2');
		arryAttr.push('tipo:form_columns');
	}else if(tipo === 'Tabs'){
		tabsId ++;
		id = 'tabs_' + tabsId;
		titulo = 'Titulo del Panel con Tabs ' + tabsId;
		store = storeTabs;
		target.add(CreaPanelTabs(id,titulo));
		arryAttr.push('height:200');/*valor inicial del objeto*/
		arryAttr.push('width:350');
		arryAttr.push('tabs:3');
		arryAttr.push('tipo:form_tabs');
	}else if(tipo === 'Acordion'){
		acordionId ++;
		id = 'acordion_' + acordionId;
		titulo = 'Titulo del Panel estilo Acordion ' + acordionId;
		store = storeAcordion;
		target.add(CreaPanelAcordion(id,titulo));
		arryAttr.push('height:490');
		arryAttr.push('width:350');
		arryAttr.push('tabs:3');
		arryAttr.push('tipo:form_acordion');
	}else if(tipo === 'Tablas'){tablasId ++;target.add(CreaPanelTablas(tablasId));
	}else if(tipo === 'Border'){
		borderId ++;
		id = 'border_' + borderId;
		store = storeBorder;
		titulo = 'Titulo del Panel estilo Border ' + borderId;
		target.add(CreaPanelBorder(id,titulo));
		arryAttr.push('height:480');
		arryAttr.push('width:700');
		arryAttr.push('tipo:border');
	}else if(tipo === 'Ventana'){
		ventanaId ++;
		id = 'ventana_' + ventanaId;
		titulo = 'Titulo de la Ventana ' + ventanaId;
		store = storeWindow;
		target.add(CreateWin(id,titulo));
		arryAttr.push('height:200');
		arryAttr.push('width:400');
		arryAttr.push('columnas:1');
		arryAttr.push('titulo_Posicion:right');
		arryAttr.push('isModal:false');
		arryAttr.push('isMinimizable:false');
		arryAttr.push('tipo:window');
		arryAttr.push('cordX:0');arryAttr.push('cordY:0');
		arryAttr.push('winEstilo:normal');
	}
	focusPanel=id;
	creaDataSP('superpanel',id);
	arryAttr.push('titulo:'+titulo);
	arryAttr.push('(id):'+id);
	arryAttr.push('nombre:'+id);
	arryAttr.push('isDesplegable:false');
	if (tipo != 'Ventana'){
		arryAttr.push('margen:5');
	}
	arryAttr.push('isFondo:true');
	arryAttr.push('isCerrable:false');
	arryAttr.push('bodyPadding:5 5 5 5');
	arryAttr.push('isAutoScroll:false');
	arryAttr.push('isResizable:false');
	arryAttr.push('isBodyBorder:true');
	arryAttr.push('titulo_Aling:left');
	seteaAtributosGrid(arryAttr,id,store);	
	Ext.getCmp('west-panel').items.items[1].expand();
	//var f = pnl.items.items[1].expand();
}

function agregaControlesOpen(controles, panelId,nombre){
	Ext.each(controles, function(control){
		var ctrl = control.controlAttr;
		var tipo = getAttrOpen(ctrl,'xtype');
		var store;
		var arryAttr = [];
		var pnl = Ext.getCmp(panelId);
		
		//genericas
		var regId = getAttrOpen(ctrl,'id');
		var fieldLabel = getAttrOpen(ctrl,'fieldLabel');
		var texto = getAttrOpen(ctrl,'value');
		var height = getAttrOpen(ctrl,'height');
		var anchor = parseInt(getAttrOpen(ctrl,'anchor'));
		var toolTips = getAttrOpen(ctrl,'listeners');
		var toolTip = toolTips.substring(toolTips.indexOf('text:')+6,toolTips.lastIndexOf(',d')-1);

		if (tipo === 'textfield'){
			textoId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store=storeTextAttr;
			pnl.add(CreaCtrlTexto(fieldLabel,regId,texto,height));
			arryAttr.push('tipo:textfield');
			arryAttr.push('texto:'+texto);
		}else if(tipo === 'numberfield'){
			numericId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store=storeNumericAttr;
			pnl.add(CreaCtrlNumeric(fieldLabel,regId,texto,height));
			arryAttr.push('tipo:numberfield');arryAttr.push('isFlechas:'+getAttrOpen(ctrl,'hideTrigger'));arryAttr.push('isEditable:'+getAttrOpen(ctrl,'editable'));arryAttr.push('valorMax:'+getAttrOpen(ctrl,'maxValue'));arryAttr.push('valorMin:'+getAttrOpen(ctrl,'minValue'));arryAttr.push('valorMaxMsg:'+getAttrOpen(ctrl,'maxText'));arryAttr.push('valorMinMsg:'+getAttrOpen(ctrl,'minText'));
			if(texto === ''){texto = '0';}
			arryAttr.push('texto:'+texto);
		}else if (tipo === 'datefield'){
			pickerId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store=storePickerAttr;
			pnl.add(CreaCtrlPicker(fieldLabel,regId,texto,height));
			arryAttr.push('isEditable:'+getAttrOpen(ctrl,'editable'));arryAttr.push('fechaInvalidTxt:'+getAttrOpen(ctrl,'invalidText'));arryAttr.push('fechaMaxMsg:'+getAttrOpen(ctrl,'maxText'));arryAttr.push('fechaMax:'+getAttrOpen(ctrl,'maxValue'));arryAttr.push('fechaMinMsg:'+getAttrOpen(ctrl,'minText'));arryAttr.push('fechaMin:'+getAttrOpen(ctrl,'minValue'));arryAttr.push('isPadre:'+getAttrOpen(ctrl,'isPadre'));
			arryAttr.push('tipo:datefield');
			arryAttr.push('fecha:'+texto);
		}else if (tipo === 'checkboxfield'){
			checkId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store=storeCheckAttr;
			var isCheck = getAttrOpen(ctrl,'checked');
			pnl.add(CreaCtrlCheck(fieldLabel,regId,height,isCheck));
			arryAttr.push('isSeleccionado:'+getAttrOpen(ctrl,'checked'));
			arryAttr.push('tipo:checkboxfield');
		}else if (tipo === 'radiofield'){	
			radioId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store=storeRadioAttr;
			var isCheck = getAttrOpen(ctrl,'checked');
			pnl.add(CreaCtrlRadio(fieldLabel,regId,height,isCheck));
			arryAttr.push('isSeleccionado:'+getAttrOpen(ctrl,'checked'));
			arryAttr.push('tipo:radiofield');
		}else if (tipo === 'button'){	
			var textoBtn = getAttrOpen(ctrl,'text');
			var iconCls = getAttrOpen(ctrl,'iconCls');
			botonId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store=storeBotonAttr;
			pnl.add(CreaCtrlBoton(textoBtn,regId,height,iconCls));
			arryAttr.push('texto:'+textoBtn);
			arryAttr.push('row:'+getAttrOpen(ctrl,'row'));
			arryAttr.push('estilo:'+getAttrOpen(ctrl,'estilo'));
			arryAttr.push('escala:'+getAttrOpen(ctrl,'scale'));
			arryAttr.push('imagen_aling:'+getAttrOpen(ctrl,'iconAling'));
			arryAttr.push('imagenCls:'+iconCls);
			arryAttr.push('tipo:button');
		}else if(tipo === 'combobox'){
			comboId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store=storeComboAttr;
			pnl.add(CreaCtrlCombo(fieldLabel,regId,texto,height));
			arryAttr.push('isEditable:'+getAttrOpen(ctrl,'editable'));
			arryAttr.push('multiSelect:'+getAttrOpen(ctrl,'multiSelect'));
			arryAttr.push('modo:'+getAttrOpen(ctrl,'queryMode'));
			arryAttr.push('selectconFoco:'+getAttrOpen(ctrl,'selectOnFocus'));
			arryAttr.push('selectAction:'+getAttrOpen(ctrl,'triggerAction'));
			arryAttr.push('valorDisplay:'+getAttrOpen(ctrl,'displayField'));
			arryAttr.push('valorId:'+getAttrOpen(ctrl,'valueField'));
			arryAttr.push('isFlecha:'+getAttrOpen(ctrl,'hideTrigger'));
			arryAttr.push('isAutoComp:'+getAttrOpen(ctrl,'typeAhead'));
			arryAttr.push('delimitador:'+getAttrOpen(ctrl,'delimiter'));
			arryAttr.push('isPadre:'+getAttrOpen(ctrl,'isPadre'));
			arryAttr.push('store:'+getAttrOpen(ctrl,'store'));
			arryAttr.push('texto:'+getAttrOpen(ctrl,'value'));
			arryAttr.push('tipo:combobox');
		}else if(tipo === 'label'){
			labelId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			var textolbl = getAttrOpen(ctrl,'text');
			store=storeLabelAttr;
			pnl.add(CreaCtrlLabel(regId,textolbl));
			arryAttr.push('texto:'+textolbl);
			arryAttr.push('html:'+getAttrOpen(ctrl,'html'));
			arryAttr.push('tipo:label');
		}else if(tipo === 'image'){
			var src = getAttrOpen(ctrl,'src');
			imagenId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store=storeImagenAttr;
			pnl.add(CreaCtrlImagen(regId,src));
			arryAttr.push('src:'+src);
			arryAttr.push('tipo:image');
		}else if(tipo === 'hidden'){
			hiddenId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			store=storeHiddenAttr;
			var temp = '';
			if(texto === ''){
				temp = regId;
			}else{
				temp = texto;
			}
			pnl.add(CreaCtrlHidden(regId,temp));
			arryAttr.push('texto:'+texto);
			arryAttr.push('tipo:hidden');
		}else if(tipo === 'gridpanel'){
			gridId = parseInt(regId.substring(regId.lastIndexOf("_")+1));
			var titulo = getAttrOpen(ctrl,'title');
			store=storeGridAttr;
			pnl.add(CreaCtrlGrid(regId,panelId,titulo));
			arryAttr.push('tipo:gridpanel');
			arryAttr.push('titulo:'+ titulo);
			arryAttr.push('titulo_Aling:'+getAttrOpen(ctrl,'titleAlign'));
			arryAttr.push('columna_orden:'+getAttrOpen(ctrl,'sortableColumns'));
			arryAttr.push('isResizable:'+getAttrOpen(ctrl,'resizable'));
			arryAttr.push('isFondo:'+getAttrOpen(ctrl,'frame'));
			arryAttr.push('columna_hidden:'+getAttrOpen(ctrl,'enableColumnHide'));
			arryAttr.push('columna_move:'+getAttrOpen(ctrl,'enableColumnMove'));
			arryAttr.push('columna_resize:'+getAttrOpen(ctrl,'enableColumnResize'));
			arryAttr.push('isDesplegable:'+getAttrOpen(ctrl,'collapsible'));
			arryAttr.push('isCerrable:'+getAttrOpen(ctrl,'closable'));
			arryAttr.push('isBodyBorder:'+getAttrOpen(ctrl,'bodyBorder'));
		}
		

		//generico texfield
		arryAttr.push('isRequerido:'+getAttrOpen(ctrl,'allowBlank'));
		arryAttr.push('isAnchor:'+anchor);
		arryAttr.push('isRequeridoMsg:'+getAttrOpen(ctrl,'blankText'));
		arryAttr.push('isBloqueado:'+getAttrOpen(ctrl,'disabled'));
		arryAttr.push('textoSugerido:'+getAttrOpen(ctrl,'emptyText'));
		arryAttr.push('etiqueta:'+fieldLabel);
		arryAttr.push('height:'+height);
		arryAttr.push('(id):'+getAttrOpen(ctrl,'id'));
		arryAttr.push('etiqueta_aling:'+getAttrOpen(ctrl,'labelAlign'));
		arryAttr.push('etiqueta_width:'+getAttrOpen(ctrl,'labelWidth'));
		arryAttr.push('toolTip:'+toolTip);
		arryAttr.push('margen:'+getAttrOpen(ctrl,'margin'));
		arryAttr.push('textoMax:'+getAttrOpen(ctrl,'maxLength'));
		arryAttr.push('textoMaxMsg:'+getAttrOpen(ctrl,'maxLengthText'));
		arryAttr.push('textoMin:'+getAttrOpen(ctrl,'minLength'));
		arryAttr.push('textoMinMsg:'+getAttrOpen(ctrl,'minLengthText'));
		arryAttr.push('nombre:'+getAttrOpen(ctrl,'name'));
		arryAttr.push('padding:'+getAttrOpen(ctrl,'padding'));
		arryAttr.push('soloLectura:'+getAttrOpen(ctrl,'readOnly'));
		arryAttr.push('width:'+getAttrOpen(ctrl,'width'));
		creaDataSP(panelId,regId);
		seteaAtributosGrid(arryAttr,regId,store);
		
//    	gridIdH ++;
//		var gh = 'colG_' + gridIdH;
//		creaDataSPG(forma,gh,id,gh);

		
		if(tipo === 'gridpanel'){
			var plugin = Ext.getCmp(regId).getPlugin('cellplugin_'+regId);
			//Ext.getCmp('grid_1').store.loadData(jsonResponse.data);
			//Ext.getCmp(regId).store.getProxy().url = '../../confpantallas/cargaColumnas.action?panelName=' + nombre;
			//Ext.getCmp(regId).store.load();
			Ext.Ajax.request({
		        url: '../../confpantallas/cargaColumnas.action',
		        params: {panelName:nombre},
		        success: function(response, opts){
		        	var text = trim(response.responseText);
		        	var jsonResponse = Ext.JSON.decode(text);
		        	//Ext.getCmp(regId).store.loadData(jsonResponse.data);
		        	gridIdH = jsonResponse.data.length;
		        	Ext.each(jsonResponse.data, function(item){
		        		nCtrl ++;
		        		var rgs = { //spanel.json
		        		    	idPadre: panelId,
		        		    	idHijo: item.name,
		        		    	idGrid: regId,
		        		    	order: nCtrl,
		        		    	texto:item.texto,
		        		    	width: item.width,
		        		    	tipoG:item.tipoG,
		        		    	dataIndex:item.dataIndex,
		        		    	name: item.name
		        		};
		        		storeSuperPanel.add(rgs);
		        		var rowG = plugin.grid.store.data.length;
		        		Ext.getCmp(regId).store.insert(rowG, item);
		        		//plugin.startEdit(rowG, 0);
		        	});		
		        }
		    });
			
		}

		
	});

}
// Para agregar los Controles de trabajo
function agregaCtrl(tipo,forma,arryAttrCtrl){
	tipo = trim(tipo);
	focusPanel = forma;
	var pnl = Ext.getCmp(forma);
	if(pnl.xtype === 'tabpanel'){
		var tb = pnl.getActiveTab();
		pnl = tb;forma=tb.id;
	}else if (pnl.xtype === 'panel' && forma.indexOf("acordion") > -1){
		Ext.each(pnl.items.items, function(item){
			if(item.collapsed == false){
				pnl = item;
				forma= pnl.id;
			}
	 	});		
	}
	var id;
	var store;
	var widthC = 0;
	var arryAttr = [];
	if (tipo === 'Texto'){
		textoId ++;id = 'texto_' + textoId;store=storeTextAttr;pnl.add(CreaCtrlTexto(id,id,'',24));arryAttr.push('tipo:textfield');
	}else if (tipo === 'Label'){
		labelId ++;id = 'label_' + labelId;store=storeLabelAttr;pnl.add(CreaCtrlLabel(id,id));arryAttr.push('texto:'+id);arryAttr.push('tipo:label');arryAttr.push('html:'+'');
	}else if (tipo === 'Numeric'){
		numericId ++;id = 'numerico_' + numericId;store=storeNumericAttr;pnl.add(CreaCtrlNumeric(id,id,0,24));arryAttr.push('texto:0');arryAttr.push('isFlechas:true');arryAttr.push('isEditable:true');arryAttr.push('tipo:numberfield');
	}else if (tipo === 'Picker'){
		pickerId ++; id = 'datafield_' + pickerId; store=storePickerAttr;pnl.add(CreaCtrlPicker(id,id,'',24));arryAttr.push('isEditable:true');arryAttr.push('textoMax:10');arryAttr.push('textoMaxMsg:Verifica el formato de la fecha');arryAttr.push('textoMin:10');arryAttr.push('textoMinMsg:Verifica el formato de la fecha');arryAttr.push('tipo:datefield');
	}else if (tipo === 'Check'){
		checkId ++;id = 'checkboxfield_' + checkId;store=storeCheckAttr;pnl.add(CreaCtrlCheck(id,id,24,false));arryAttr.push('tipo:checkboxfield');
	}else if (tipo === 'Radio'){	
		radioId ++;	id = 'radiofield_' + radioId;store=storeRadioAttr;pnl.add(CreaCtrlRadio(id,id,24,false));arryAttr.push('tipo:radiofield');
	}else if (tipo === 'Boton'){	
		botonId ++;id = 'button_' + botonId;store=storeBotonAttr;pnl.add(CreaCtrlBoton(id,id,24,''));arryAttr.push('texto:'+id);arryAttr.push('row:1');arryAttr.push('estilo:normal');arryAttr.push('imagen_aling:left');arryAttr.push('tipo:button');
	}else if (tipo === 'Combo'){	
		comboId ++;id = 'combobox_' + comboId;store=storeComboAttr;pnl.add(CreaCtrlCombo(id,id,'',24));arryAttr.push('isEditable:true');arryAttr.push('multiSelect:false');arryAttr.push('modo:remote');arryAttr.push('selectconFoco:false');arryAttr.push('selectAction:all');arryAttr.push('valorDisplay:value');arryAttr.push('valorId:key');arryAttr.push('isFlecha:false');arryAttr.push('isAutoComp:false');arryAttr.push('tipo:combobox');
	}else if (tipo === 'Imagen'){
		imagenId ++;id = 'imagen_' + imagenId;store=storeImagenAttr;pnl.add(CreaCtrlImagen(id,'http://www.sencha.com/img/20110215-feat-html5.png'));arryAttr.push('src:http://www.sencha.com/img/20110215-feat-html5.png');arryAttr.push('tipo:image');
	}else if (tipo === 'Hidden'){
		hiddenId ++;id = 'hidden_' + hiddenId;store=storeHiddenAttr;pnl.add(CreaCtrlHidden(id,id));arryAttr.push('tipo:hidden');
	}else if (tipo === 'Grid'){
		gridId ++;
		id = 'grid_' + gridId;
		store=storeGridAttr;
		pnl.add(CreaCtrlGrid(id,forma,''));
		arryAttr.push('tipo:gridpanel');
		arryAttr.push('titulo:'+'');
		arryAttr.push('titulo_Aling:left');
		arryAttr.push('columna_orden:true');
		arryAttr.push('isResizable:false');
		arryAttr.push('isFondo:false');
		arryAttr.push('columna_hidden:true');
		arryAttr.push('columna_move:true');
		arryAttr.push('columna_resize:true');
		arryAttr.push('isDesplegable:false');
		arryAttr.push('isCerrable:false');
		arryAttr.push('isBodyBorder:false');
	}
	creaDataSP(forma,id);
	arryAttr.push('(id):'+id);
	if (tipo != 'Label' && tipo != 'Imagen' && tipo != 'Hidden' && tipo != 'Grid'){
		arryAttr.push('etiqueta:'+id);
		arryAttr.push('etiqueta_aling:left');
		arryAttr.push('etiqueta_width:100');/*valor inicial del objeto*/
		arryAttr.push('isAnchor:95');/*valor inicial del objeto*/
		arryAttr.push('padding:0 0 0 0');
	}
	if (tipo != 'Hidden'){
		arryAttr.push('height:'+getHeightCtrl(id)); 
		arryAttr.push('margen:5 5 5 5');
		arryAttr.push('width:'+getWidthCtrl(id));
	}
	
	arryAttr.push('nombre:'+id);
	Ext.each(arryAttrCtrl, function(attr){
		arryAttr.push(attr);
 	});
	seteaAtributosGrid(arryAttr,id,store);	
}

function fncAsignaNombrePanel(){
	Ext.MessageBox.prompt('Grabar Panel', 'Ingresa el nombre del panel:',fncGrabaPanel);
}
function fncAbrePanelExistente(){
	if(isOpen){
		Ext.MessageBox.prompt('Panel Existente', 'Ingresa el panel que deseas cargar:',fncAbrePanel);
	}
}

function fncAbrePanel(text,nombre){
	if(text == 'ok'){
		target.body.mask("Cargando el panel...");
		isOpen = false;
		Ext.Ajax.request({
	        url: '../../confpantallas/openpanel.action',
	        params: {nombrepanel: nombre,tarea:'existe'},
	        success: function(response, opts){
	        	var text = trim(response.responseText);
	        	var jsonResponse = Ext.JSON.decode(text);
		        if(jsonResponse.regreso != '') {
		        	openPanel(jsonResponse,nombre);
		        	target.body.unmask();
		        }else{
		        	target.body.unmask();
		        	isOpen = true;
	            	Ext.MessageBox.show({
				           title:'Carga de Panel',
				           msg: 'El panel que deseas cargar NO existe.',
				           buttons: Ext.MessageBox.OK,
				           icon: Ext.MessageBox.INFO
	 				});
		        }
	        }
	    });
	}
}


function fncGrabaPanel(text,nombre){
	if(text == 'ok'){
		namePnl = nombre;
	 	Ext.Ajax.request({
	        url: '../../confpantallas/grabapanel.action',
	        params: {nombrepanel: nombre,tarea:'existe'},
	        success: function(response, opts){          
	            var text = trim(response.responseText);
	            var jsonResponse = Ext.JSON.decode(text);
	            if(jsonResponse.regreso != '') {
	            	Ext.MessageBox.show({
			           title:'Guardando Formulario?',
			           msg: 'El nombre ['+nombre+'] del panel ya existe. <br />Deseas sobreescribirlo?',
			           buttons: Ext.MessageBox.YESNO,
			           fn: fncRespActualizaPanel,
			           icon: Ext.MessageBox.QUESTION
       				});
	            }else{
	            	fncAltadePanel();
	            }
	        }
		});
	 	
	}
}
function fncRespActualizaPanel(a){if(a == 'yes'){fncAltadePanel();}}
function fncAltadePanel(){
	target.body.mask("Guardando la información...");
	var arryPPanelesT = [];
	Ext.each(storeSuperPanel.data.items, function(item){
			arryPPanelesT.push(item.data);
 	});		

	var jsonG = Ext.JSON.encode(arryPPanelesT);
Ext.Ajax.request({
	        url: '../../confpantallas/grabapanel.action',
	        params: {nombrepanel: namePnl,tarea:'graba',json:jsonG},
	        success: function(response, opts){      
	        	target.body.unmask();    
	            var text = trim(response.responseText);
	            var jsonResponse = Ext.JSON.decode(text);
	            if(jsonResponse.regreso == '') {
            	Ext.MessageBox.show({
			           title:'Información',
			           msg: 'El panel ha sido guardado satisfactoriamente \n ID=' + jsonResponse.panel,
			           buttons: Ext.MessageBox.OK,
			           icon: Ext.MessageBox.WARNING
       				});
	            }else{
            	Ext.MessageBox.show({
			           title:'Aviso Importante',
			           msg: jsonResponse.success,
			           buttons: Ext.MessageBox.OK,
			           icon: Ext.MessageBox.ERROR
       				});
	            }
	        }
		});
}
function fncAjaxGral(url,params){
	Ext.Ajax.request({
	        url: url,
	        params: params,
	        success: function(response, opts){          
	            var text = trim(response.responseText);
	            var jsonResponse = Ext.JSON.decode(text);
	            if(jsonResponse.success != '') {
	            	return jsonResponse.success;
	            }
	        }
		});
}
function fncPreliminar(){
	if(storeSuperPanel.data.items.length > 1){
		Ext.getCmp('border-gral').mask();
		var arryVentanas = [];
		var ppWin =  Ext.create('Ext.Window', {
		title: 'Presentación Preliminar',
		id: 'pp_superpanelp',
		autoScroll:true,
		width: Ext.getCmp('border-gral').getWidth() - 100,
		height: Ext.getCmp('border-gral').getHeight() - 50,
		headerPosition: 'left',
		iconCls: 'preview_v',
    	
    	listeners: {
        			close: {
            			fn: function(c ){ 
            			Ext.getCmp('border-gral').unmask();}}}});
        /*Identifico los paneles a construir*/
        var arryPPaneles = [];
		Ext.each(storeSuperPanel.data.items, function(item){
			if(item.data.idPadre=='superpanel' && item.data.idHijo!='superpanel'){
				arryPPaneles.push(item);
			}
	 	});		
	 	
        Ext.each(arryPPaneles, function(ppln){
        	var pplnctl = Ext.getCmp(ppln.data.idHijo);	
        	var d = pplnctl.getId();	
        	/*Identifico los controles dentro del panel*/
	 		var arryPControles = getHijosPanelCtrl(d);
	 		
	 		var isBton = false;var isWin = false;
	 		var row = 0;
	 		Ext.each(arryPControles, function(pctrl){
	 			var pplnctls = Ext.getCmp(pctrl.data.idHijo);
	 			if(pplnctls.getXType() == 'button'){
	 				if(parseInt(pctrl.data.row)>row){
	 					isBton = true;
	 				}
	 			}
	 		});
	 		
	 		//getHijosBotones(arryPControles,isBton,row);
	 		
	 		var dd = pplnctl.getXType();
	 		if(pplnctl.getXType() == 'form'){
	 			if(pplnctl.getLayout().type=='column'){
	 				row=parseInt(ppln.data.columnas);
	 				Ext.getCmp('pp_superpanelp').add(CreaPanelColumnasPreliminar(pplnctl.getId(),isBton));
	 			}else{
	 				row=0;
	 				Ext.getCmp('pp_superpanelp').add(CreaPanelFormaPreliminar(pplnctl.getId(),isBton));
	 			}
        	}else if(pplnctl.getXType() == 'tabpanel'){
        		row=0;
	 			Ext.getCmp('pp_superpanelp').add(CreaPanelTabsPreliminar(pplnctl.getId(),isBton));
        	}else if(pplnctl.getXType() == 'panel' && d.indexOf("acordion") > -1){
        		row=0;
	 			Ext.getCmp('pp_superpanelp').add(CreaPanelAcordionPreliminar(d,isBton));
        	}else if(pplnctl.getXType() == 'panel' && d.indexOf("border") > -1){
        		row=0;
	 			Ext.getCmp('pp_superpanelp').add(CreaPanelBorderPreliminar(d,isBton));
        	}else if(pplnctl.getXType() == 'window'){
        		row=parseInt(ppln.data.columnas);
	 			//Ext.getCmp('pp_superpanelp').add(CreaWinPreliminar(pplnctl.getId(),isBton));
	 			var ppWinD = CreaWinPreliminar(pplnctl.getId(),isBton);
	 			arryVentanas.push(ppWinD);
	 			isWin = true;
	 			
        	}
        	
        	var arryPControlesIN = [];
        	var ctrW;
        	if(isWin == true){
        		Ext.each(arryVentanas, function(panelW){
        			if(panelW.id === 'preliminar_'+d){
        				ctrW=panelW;
        				return;
        			}
        		});
        	}
        	setControlesPreliminar(arryPControles,arryPControlesIN,row,isBton,d,isWin,ctrW);
        	var arryPControlesIN01 = [];
		 	Ext.each(arryPControlesIN, function(pplnIn){
				var arryPControlesIN = getHijosPanelCtrl(pplnIn);		 
				setControlesPreliminar(arryPControlesIN,arryPControlesIN01,row,isBton,pplnIn,isWin,ctrW);	
		 	});
		 	
        });  	
        ppWin.show();
        Ext.each(arryVentanas, function(panelW){
        	panelW.show();
        });
	}
}

function updateGridRow(titulo,width,tipoG,dataIndex,name){
	Ext.each(storeSuperPanel.data.items, function(item){
		if(item.data.idHijo==name){
			item.data.texto = titulo;
			item.data.width = width;
			item.data.tipoG = tipoG;
			item.data.dataIndex = dataIndex;
			return;
		}
	});
}

function eliminaGridRow(name){
	var arryAttrDeleted = [];
	Ext.each(storeSuperPanel.data.items, function(item){
		if(item.data.idHijo==name){
			arryAttrDeleted.push(item);
			return;
		}	
	 });
	storeSuperPanel.remove(arryAttrDeleted);
}

function getHijosBotones(arryPControles,isBton,row){
	Ext.each(arryPControles, function(pctrl){
		var pplnctls = Ext.getCmp(pctrl.data.idHijo);
		if(pplnctls.getXType() == 'button'){
			if(parseInt(pctrl.data.row)>row){
				isBton = true;
			}
		}
	});
}

function getHijosPanelCtrl(id){
	var arryPControlesH = [];
	Ext.each(storeSuperPanel.data.items, function(item){
				if(item.data.idPadre==id && item.data.idGrid==''){
					arryPControlesH.push(item);
				}
	 		});	
	 return arryPControlesH;
}

function setControlesPreliminar(arryPControles,arryPControlesIN,row,isBton,d, isWin,ctrW){
  			 Ext.each(arryPControles, function(pctrl){
			 	var pplnctls = Ext.getCmp(pctrl.data.idHijo);	
			 	var typo = pplnctls.getXType();
			 	if(pplnctls.getXType() == 'textfield'){
			 		if(isWin == false){
			 			Ext.getCmp('preliminar_'+d).add(CreaCtrlTextoPreliminar(pplnctls.getId(),row));
			 		}else{
			 			ctrW.add(CreaCtrlTextoPreliminar(pplnctls.getId(),row));
			 		}
			 	}else if(pplnctls.getXType() == 'numberfield'){
			 		if(isWin == false){
			 			Ext.getCmp('preliminar_'+d).add(CreaCtrlNumericPreliminar(pplnctls.getId(),row));
			 		}else{
			 			ctrW.add(CreaCtrlNumericPreliminar(pplnctls.getId(),row));
			 		}
			 	}else if(pplnctls.getXType() == 'datefield'){
			 		if(isWin == false){
			 			Ext.getCmp('preliminar_'+d).add(CreaCtrlPickerPreliminar(pplnctls.getId(),row));
			 		}else{
			 			ctrW.add(CreaCtrlPickerPreliminar(pplnctls.getId(),row));
			 		}
			 	}else if(pplnctls.getXType() == 'checkbox'){
			 		if(isWin == false){
			 			Ext.getCmp('preliminar_'+d).add(CreaCtrlCheckPreliminar(pplnctls.getId(),row));
			 		}else{
			 			ctrW.add(CreaCtrlCheckPreliminar(pplnctls.getId(),row));
			 		}
			 	}else if(pplnctls.getXType() == 'radiofield'){
			 		if(isWin == false){
			 			Ext.getCmp('preliminar_'+d).add(CreaCtrlRadioPreliminar(pplnctls.getId(),row));
			 		}else{
			 			ctrW.add(CreaCtrlRadioPreliminar(pplnctls.getId(),row));
			 		}
			 	}else if(pplnctls.getXType() == 'grid'){
			 		if(isWin == false){
			 			Ext.getCmp('preliminar_'+d).add(CreaCtrlGridPreliminar(pplnctls.getId(),row));
			 		}else{
			 			ctrW.add(CreaCtrlGridPreliminar(pplnctls.getId(),row));
			 		}
			 	}else if(pplnctls.getXType() == 'label'){
			 		if(pplnctls.id.indexOf("hidden") > -1){
			 			if(isWin == false){
				 			Ext.getCmp('preliminar_'+d).add(CreaCtrlHiddenPreliminar(pplnctls.getId(),row));
				 		}else{
				 			ctrW.add(CreaCtrlHiddenPreliminar(pplnctls.getId(),row));
				 		}
			 		}else{
			 			if(isWin == false){
				 			Ext.getCmp('preliminar_'+d).add(CreaCtrlLabelPreliminar(pplnctls.getId(),row));
				 		}else{
				 			ctrW.add(CreaCtrlLabelPreliminar(pplnctls.getId(),row));
				 		}
			 		}		
			 	}else if(pplnctls.getXType() == 'image'){
			 		if(isWin == false){
			 			Ext.getCmp('preliminar_'+d).add(CreaCtrlImagenPreliminar(pplnctls.getId(),row));
			 		}else{
			 			ctrW.add(CreaCtrlImagenPreliminar(pplnctls.getId(),row));
			 		}
			 	}else if(pplnctls.getXType() == 'button' & parseInt(pctrl.data.row) == 0){
			 		if(isWin == false){
			 			Ext.getCmp('preliminar_'+d).add(CreaCtrlBotonPreliminar(pplnctls.getId(),row));
			 		}else{
			 			ctrW.add(CreaCtrlBotonPreliminar(pplnctls.getId(),row));
			 		}
			 	}else if(pplnctls.getXType() == 'button' & parseInt(pctrl.data.row) > 0){
			 		if(isWin == false){
			 			Ext.getCmp('preliminar_'+d).down('toolbar').add(CreaCtrlBotonPreliminar(pplnctls.getId(),0));
			 		}else{
			 			ctrW.down('toolbar').add(CreaCtrlBotonPreliminar(pplnctls.getId(),0));
			 		}
			 	}else if(pplnctls.getXType() == 'combobox'){
			 		if(isWin == false){
			 			Ext.getCmp('preliminar_'+d).add(CreaCtrlComboPreliminar(pplnctls.getId(),row));
			 		}else{
			 			ctrW.add(CreaCtrlComboPreliminar(pplnctls.getId(),row));
			 		}
			 	}else if(pplnctls.getXType() == 'panel'){
			 		if(d.indexOf("border") > -1){
			 			row=parseInt(pctrl.data.columnas);
			 			arryPControlesIN.push(pctrl.data.idHijo);
			 			Ext.getCmp('preliminar_'+pctrl.data.idPadre).add(CreaCtrlPanelPreliminar(pctrl.data.idHijo,isBton));
			 		}
			 	}else if(pplnctls.getXType() == 'form'){
			 		if(d.indexOf("acordion") > -1){
	 					row=parseInt(pctrl.data.columnas);
	 					arryPControlesIN.push(pctrl.data.idHijo);
	 					Ext.getCmp('preliminar_'+pctrl.data.idPadre).add(CreaAcordionColumnasPreliminar(pctrl.data.idHijo,isBton));
			 		}else{
				 		if(pplnctls.getLayout().type=='column'){
		 					row=parseInt(pctrl.data.columnas);
		 					arryPControlesIN.push(pctrl.data.idHijo);
		 					Ext.getCmp('preliminar_'+pctrl.data.idPadre).add(CreaPanelColumnasPreliminar(pctrl.data.idHijo,isBton));
		 				}
		 			}
			 	}
			 });
}


    
});///Termina Ext.onReady




function getWidthCtrl(elCtrl){
	var c = Ext.getCmp(elCtrl);
	return c.getWidth();
}
function getHeightCtrl(elCtrl){
	var c = Ext.getCmp(elCtrl);
	return c.getHeight();
}
function getValorSP(attr,def){
	var rgs;
	var isAttr=getDataSP(attr,ctrl);
	if(isAttr == 'undefined'){
		rgs = def;
	}else if(isAttr == true || isAttr == false || isAttr != ''){
		rgs = isAttr;
	}else{
		rgs = def;
	}
	return rgs;
}

function cargaAtributosPanel(panel,store){
	focusPanel = panel;
	ctrl = panel;
	var pnl = Ext.getCmp(panel);
	var id = pnl.id;
	var s = panel.indexOf("_");
	var tipo = panel.substr(0,s);
	var propGrid = Ext.getCmp('propGrid');
	propGrid.setSource(store.getAt(0).data);
	var arryAttr = [];
	arryAttr.push('(id):'+id);
	arryAttr.push('width:'+pnl.width);
	arryAttr.push('height:'+pnl.height);
	arryAttr.push('titulo:'+pnl.title);
	arryAttr.push('isDesplegable:'+getValorSP('isDesplegable',false));
	arryAttr.push('isFondo:'+getValorSP('isFondo',true));
	arryAttr.push('isCerrable:'+getValorSP('isCerrable',false));
	arryAttr.push('bodyPadding:'+getValorSP('bodyPadding','5 5 5 5'));
	arryAttr.push('isAutoScroll:'+getValorSP('isAutoScroll',false));
	arryAttr.push('isResizable:'+getValorSP('isResizable',false));
	arryAttr.push('isBodyBorder:'+getValorSP('isBodyBorder',true));
	arryAttr.push('titulo_Aling:'+getValorSP('titulo_Aling','left'));
	if(tipo === 'columnas'){
		arryAttr.push('columnas:'+getValorSP('columnas',2));
		arryAttr.push('url:'+getValorSP('url',''));
		arryAttr.push('margen:'+getValorSP('margen','5'));
	}else if (tipo === 'tabs'){
		arryAttr.push('tabs:'+getValorSP('tabs',3));
		arryAttr.push('margen:'+getValorSP('margen','5'));
	}else if (tipo === 'sur' || tipo === 'izq' || tipo === 'norte' || tipo === 'der' || tipo === 'centro'){
		arryAttr.push('columnas:'+getValorSP('columnas',2));
		arryAttr.push('margen:'+getValorSP('margen','5 5 5 5'));
	}else if (tipo === 'panel'){
		arryAttr.push('url:'+getValorSP('url',''));
		arryAttr.push('margen:'+getValorSP('margen','5'));
	}else if (tipo === 'border'){
		arryAttr.push('margen:'+getValorSP('margen','5'));
	}else if (tipo === 'ventana'){
		arryAttr.push('columnas:'+getValorSP('columnas',1));
		arryAttr.push('isMinimizable:'+getValorSP('isMinimizable',false));
		arryAttr.push('titulo_Posicion:'+getValorSP('titulo_Posicion','right'));
		arryAttr.push('cordX:'+getValorSP('cordX',0));
		arryAttr.push('cordY:'+getValorSP('cordY',0));
		arryAttr.push('winEstilo:'+getValorSP('winEstilo','normal'));
	}
	
	var nombrel=getDataSP('nombre',ctrl);
	if(nombrel != ''){
		if(nombrel == 'undefined'){
			arryAttr.push('nombre:'+id);
		}else{
			arryAttr.push('nombre:'+nombrel);
		}
	}else{
		arryAttr.push('nombre:'+id);
	}
	seteaAtributosGrid(arryAttr,id,store);	

}

function limpiaObjetos(panel){
	var arryAttrDeleted = [];
	var arryAttrRec = [];
	Ext.each(storeSuperPanel.data.items, function(item){
		if(item.data.idHijo==panel || item.data.idPadre==panel){
			if(item.data.idPadre==panel){
				arryAttrRec.push(item.data.idHijo);
			}
			arryAttrDeleted.push(item);
		}	
	 });
	 Ext.each(arryAttrRec, function(hijo){
	 	limpiaObjetosRecurrente(hijo, arryAttrDeleted);
	 });
	storeSuperPanel.remove(arryAttrDeleted);
	var propGrid = Ext.getCmp('propGrid');
	storeBasico.reload();
	propGrid.setSource(storeBasico.getAt(0).data);
	
}


function limpiaObjetosRecurrente(panel, arryAttrDeleted){
	Ext.each(storeSuperPanel.data.items, function(item){
		if(item.data.idPadre==panel){
			arryAttrDeleted.push(item);
		}
	});
}


function seteaAtributosGrid(arryAttr,id,store){
	var propGrid = Ext.getCmp('propGrid');
	store.reload();
	ctrl = id;
	propGrid.setSource(store.getAt(0).data);
	var daty = Ext.decode(Ext.encode(store.data.items[0].data));
	Ext.each(arryAttr, function(item){
		var s = item.indexOf(":");
		var name = item.substr(0,s);
		var value = item.substr(s+1);
		if(name === 'titulo'){Ext.apply(daty, {titulo:value});}else if(name==='nombre'){Ext.apply(daty,{nombre:value});}else if(name ==='(id)'){Ext.apply(daty, {"(id)":value}); }else if(name === 'width'){Ext.apply(daty, {width:value});}else if(name === 'height'){Ext.apply(daty, {height:value});}else if(name === 'etiqueta'){Ext.apply(daty, {etiqueta:value});}else if(name === 'isRequerido'){Ext.apply(daty, {isRequerido:value});}else if(name === 'isAnchor'){Ext.apply(daty, {isAnchor:value});}else if(name === 'etiqueta_width'){Ext.apply(daty, {etiqueta_width:value});}else if(name === 'isBloqueado'){Ext.apply(daty, {isBloqueado:value});}else if(name === 'textoSugerido'){Ext.apply(daty, {textoSugerido:value});}else if(name === 'textoMax'){Ext.apply(daty, {textoMax:value});}else if(name === 'textoMaxMsg'){Ext.apply(daty, {textoMaxMsg:value});}else if(name === 'textoMin'){Ext.apply(daty, {textoMin:value});}else if(name === 'textoMinMsg'){Ext.apply(daty, {textoMinMsg:value});}else if(name === 'soloLectura'){Ext.apply(daty, {soloLectura:value});}else if(name === 'toolTip'){Ext.apply(daty, {toolTip:value});}else if(name === 'texto'){Ext.apply(daty, {texto:value});}else if(name === 'valorMax'){Ext.apply(daty, {valorMax:value});}else if(name === 'valorMin'){Ext.apply(daty, {valorMin:value});}else if(name === 'valorMaxMsg'){Ext.apply(daty, {valorMaxMsg:value});}else if(name === 'valorMinMsg'){Ext.apply(daty, {valorMinMsg:value});}else if(name === 'isFlechas'){Ext.apply(daty, {isFlechas:value});}else if(name === 'fecha'){Ext.apply(daty, {fecha:value});}else if(name === 'fechaMax'){Ext.apply(daty, {fechaMax:value});}else if(name === 'fechaMin'){Ext.apply(daty, {fechaMin:value});}else if(name === 'fechaMinMsg'){Ext.apply(daty, {fechaMinMsg:value});}else if(name === 'etiqueta_aling'){Ext.apply(daty, {etiqueta_aling:value});}else if(name === 'isSeleccionado'){Ext.apply(daty, {isSeleccionado:value});}else if(name === 'texto'){Ext.apply(daty, {texto:value});}else if(name === 'imagenCls'){Ext.apply(daty, {imagenCls:value});}else if(name === 'margen'){Ext.apply(daty, {margen:value});}else if(name === 'padding'){Ext.apply(daty, {padding:value});}else if(name === 'imagen_aling'){Ext.apply(daty, {imagen_aling:value});}else if(name === 'isRequeridoMsg'){Ext.apply(daty, {isRequeridoMsg:value});}else if(name === 'isEditable'){Ext.apply(daty, {isEditable:value});}else if(name === 'multiSelect'){Ext.apply(daty, {multiSelect:value});}else if(name === 'store'){Ext.apply(daty, {store:value});}else if(name === 'modo'){Ext.apply(daty, {modo:value});}else if(name === 'selectconFoco'){Ext.apply(daty, {selectconFoco:value});}else if(name === 'selectAction'){Ext.apply(daty, {selectAction:value});}else if(name === 'valorDisplay'){Ext.apply(daty, {valorDisplay:value});}else if(name === 'valorId'){Ext.apply(daty, {valorId:value});}else if(name === 'isFlecha'){Ext.apply(daty, {isFlecha:value});}else if(name === 'isAutoComp'){Ext.apply(daty, {isAutoComp:value});}else if(name === 'delimitador'){Ext.apply(daty, {delimitador:value});}else if(name === 'row'){Ext.apply(daty, {row:value});}else if(name === 'columnas'){Ext.apply(daty, {columnas:value});}else if(name === 'tabs'){Ext.apply(daty, {tabs:value});}else if(name === 'isDesplegable'){Ext.apply(daty, {isDesplegable:value});}else if(name === 'isFondo'){Ext.apply(daty, {isFondo:value});}else if(name === 'isCerrable'){Ext.apply(daty, {isCerrable:value});}else if(name === 'bodyPadding'){Ext.apply(daty, {bodyPadding:value});}else if(name === 'isAutoScroll'){Ext.apply(daty, {isAutoScroll:value});}else if(name === 'isResizable'){Ext.apply(daty, {isResizable:value});}else if(name === 'isBodyBorder'){Ext.apply(daty, {isBodyBorder:value});}else if(name === 'titulo_Aling'){Ext.apply(daty, {titulo_Aling:value});}else if(name === 'titulo'){Ext.apply(daty, {titulo:value});}else if(name === 'etiqueta'){Ext.apply(daty, {etiqueta:value});}else if(name === 'fechaInvalidTxt'){Ext.apply(daty, {fechaInvalidTxt:value});}else if(name === 'escala'){Ext.apply(daty, {escala:value});}else if(name === 'tipo'){Ext.apply(daty, {tipo:value});}else if(name === 'isPadre'){Ext.apply(daty, {isPadre:value});}else if(name === 'estilo'){Ext.apply(daty, {estilo:value});}else if(name === 'url'){Ext.apply(daty, {url:value});}else if(name === 'titulo_Posicion'){Ext.apply(daty, {titulo_Posicion:value});}else if(name === 'isModal'){Ext.apply(daty, {isModal:value});}else if(name === 'isMinimizable'){Ext.apply(daty, {isMinimizable:value});}else if(name === 'cordX'){Ext.apply(daty, {cordX:value});}else if(name === 'cordY'){Ext.apply(daty, {cordY:value});}else if(name === 'html'){Ext.apply(daty, {html:value});}else if(name === 'src'){Ext.apply(daty, {src:value});}else if(name === 'query'){Ext.apply(daty, {query:value});}else if(name === 'columna_orden'){Ext.apply(daty, {columna_orden:value});}else if(name === 'columna_hidden'){Ext.apply(daty, {columna_hidden:value});}else if(name === 'columna_move'){Ext.apply(daty, {columna_move:value});}else if(name === 'columna_resize'){Ext.apply(daty, {columna_resize:value});}else if(name === 'isBorder'){Ext.apply(daty, {isBorder:value});}else if(name==='fechaMaxMsg'){Ext.apply(daty,{fechaMaxMsg:value});
		}else if(name === 'winEstilo'){
			Ext.apply(daty, {winEstilo:value});
		}
		setDataSP(name,value);
    });
	store.removeAll();
	store.add(daty);
	propGrid.setSource(store.getAt(0).data);
	cambiaTab(1);
}
function getDataSP(attr,ctrlp){
	var rgs = 'undefined';
	Ext.each(storeSuperPanel.data.items, function(item){
		if(item.data.idHijo==ctrlp){
			if(attr==='nombre'){rgs = item.data.name;}else if(attr==='isRequerido'){rgs = item.data.isRequerido;}else if(attr==='isAnchor'){rgs = item.data.isAnchor;}else if(attr==='width'){rgs = item.data.width;}else if(attr==='height'){rgs = item.data.height;}else if(attr==='etiqueta_width'){rgs = item.data.etiqueta_width;}else if(attr==='isBloqueado'){rgs = item.data.isBloqueado;}else if(attr==='textoSugerido'){rgs = item.data.textoSugerido;}else if(attr==='textoMax'){rgs = item.data.textoMax;}else if(attr==='textoMaxMsg'){rgs = item.data.textoMaxMsg;}else if(attr==='textoMin'){rgs = item.data.textoMin;}else if(attr==='textoMinMsg'){rgs = item.data.textoMinMsg;}else if(attr==='soloLectura'){rgs = item.data.soloLectura;}else if(attr==='toolTip'){rgs = item.data.toolTip;}else if(attr==='valorMin'){rgs = item.data.valorMin;}else if(attr==='valorMax'){rgs = item.data.valorMax;}else if(attr==='valorMaxMsg'){rgs = item.data.valorMaxMsg;}else if(attr==='valorMinMsg'){rgs = item.data.valorMinMsg;}else if(attr==='isFlechas'){rgs = item.data.isFlechas;}else if(attr==='fecha'){rgs = item.data.fecha;}else if(attr==='fechaMax'){rgs = item.data.fechaMax;}else if(attr==='fechaMaxMsg'){rgs = item.data.fechaMaxMsg;}else if(attr==='fechaMin'){rgs = item.data.fechaMin;}else if(attr==='fechaMinMsg'){rgs = item.data.fechaMinMsg;}else if(attr==='etiqueta_aling'){rgs = item.data.etiqueta_aling;}else if(attr==='isSeleccionado'){rgs = item.data.isSeleccionado;}else if(attr==='texto'){rgs = item.data.texto;}else if(attr==='imagenCls'){rgs = item.data.imagenCls;}else if(attr==='margen'){rgs = item.data.margen;}else if(attr==='padding'){rgs = item.data.padding;}else if(attr==='imagen_aling'){rgs = item.data.imagen_aling;}else if(attr==='isRequeridoMsg'){rgs = item.data.isRequeridoMsg;}else if(attr==='isEditable'){rgs = item.data.isEditable;}else if(attr==='multiSelect'){rgs = item.data.multiSelect;}else if(attr==='store'){rgs = item.data.store;}else if(attr==='modo'){rgs = item.data.modo;}else if(attr==='selectconFoco'){rgs = item.data.selectconFoco;}else if(attr==='selectAction'){rgs = item.data.selectAction;}else if(attr==='valorDisplay'){rgs = item.data.valorDisplay;}else if(attr==='valorId'){rgs = item.data.valorId;}else if(attr==='isFlecha'){rgs = item.data.isFlecha;}else if(attr==='isAutoComp'){rgs = item.data.isAutoComp;}else if(attr==='delimitador'){rgs = item.data.delimitador;}else if(attr==='row'){rgs = item.data.row;}else if(attr==='columnas'){rgs = item.data.columnas;}else if(attr==='tabs'){rgs = item.data.tabs;}else if(attr==='isDesplegable'){rgs = item.data.isDesplegable;}else if(attr==='isFondo'){rgs = item.data.isFondo;}else if(attr==='isCerrable'){rgs = item.data.isCerrable;	}else if(attr==='bodyPadding'){rgs = item.data.bodyPadding;}else if(attr==='isAutoScroll'){rgs = item.data.isAutoScroll;}else if(attr==='isResizable'){rgs = item.data.isResizable;}else if(attr==='isBodyBorder'){rgs = item.data.isBodyBorder;}else if(attr==='titulo_Aling'){rgs = item.data.titulo_Aling;}else if(attr==='titulo'){rgs = item.data.titulo;}else if(attr==='etiqueta'){rgs = item.data.etiqueta;}else if(attr==='fechaInvalidTxt'){rgs = item.data.fechaInvalidTxt;}else if(attr==='escala'){rgs = item.data.escala;}else if(attr==='tipo'){rgs = item.data.tipo;}else if(attr==='isPadre'){rgs = item.data.isPadre;}else if(attr==='estilo'){rgs = item.data.estilo;}else if(attr==='url'){rgs = item.data.url;}else if(attr==='titulo_Posicion'){rgs = item.data.titulo_Posicion;}else if(attr==='isModal'){rgs = item.data.isModal;}else if(attr==='isMinimizable'){rgs = item.data.isMinimizable;}else if(attr==='cordX'){rgs = item.data.cordX;}else if(attr==='cordY'){rgs = item.data.cordY;}else if(attr==='html'){rgs = item.data.html;}else if(attr==='src'){rgs = item.data.src;}else if(attr==='query'){rgs = item.data.query;}else if(attr==='columna_orden'){rgs = item.data.columna_orden;}else if(attr==='columna_hidden'){rgs = item.data.columna_hidden;}else if(attr==='columna_move'){rgs = item.data.columna_move;}else if(attr==='columna_resize'){rgs = item.data.columna_resize;}else if(attr==='isBorder'){rgs = item.data.isBorder;
			}else if(attr==='winEstilo'){
				rgs = item.data.winEstilo;
			}
			return;
		}	
	 });
	 return rgs;
}
function setDataSP(attr,valor){
	Ext.each(storeSuperPanel.data.items, function(item){
		if(item.data.idHijo==ctrl){
			if(attr==='nombre'){item.data.name=valor;}else if(attr==='isRequerido'){item.data.isRequerido=valor;}else if(attr==='isAnchor'){item.data.isAnchor=valor;}else if(attr==='width'){item.data.width=valor;}else if(attr==='height'){item.data.height=valor;}else if(attr==='etiqueta_width'){item.data.etiqueta_width=valor;}else if(attr==='isBloqueado'){item.data.isBloqueado=valor;}else if(attr==='textoSugerido'){item.data.textoSugerido=valor;}else if(attr==='textoMax'){item.data.textoMax=valor;}else if(attr==='textoMaxMsg'){item.data.textoMaxMsg=valor;}else if(attr==='textoMinMsg'){item.data.textoMinMsg=valor;}else if(attr==='textoMin'){item.data.textoMin=valor;}else if(attr==='soloLectura'){item.data.soloLectura=valor;}else if(attr==='toolTip'){item.data.toolTip=valor;}else if(attr==='valorMax'){item.data.valorMax=valor;}else if(attr==='valorMin'){item.data.valorMin=valor;}else if(attr==='valorMaxMsg'){item.data.valorMaxMsg=valor;}else if(attr==='valorMinMsg'){item.data.valorMinMsg=valor;}else if(attr==='isFlechas'){item.data.isFlechas=valor;}else if(attr==='fecha'){item.data.fecha=valor;}else if(attr==='fechaMax'){item.data.fechaMax=valor;}else if(attr==='fechaMaxMsg'){item.data.fechaMaxMsg=valor;}else if(attr==='fechaMin'){item.data.fechaMin=valor;}else if(attr==='fechaMinMsg'){item.data.fechaMinMsg=valor;}else if(attr==='etiqueta_aling'){item.data.etiqueta_aling=valor;}else if(attr==='isSeleccionado'){item.data.isSeleccionado=valor;}else if(attr==='texto'){item.data.texto=valor;}else if(attr==='imagenCls'){item.data.imagenCls=valor;}else if(attr==='margen'){item.data.margen=valor;}else if(attr==='padding'){item.data.padding=valor;}else if(attr==='imagen_aling'){item.data.imagen_aling=valor;}else if(attr==='isRequeridoMsg'){item.data.isRequeridoMsg=valor;}else if(attr==='isEditable'){item.data.isEditable=valor;}else if(attr==='multiSelect'){item.data.multiSelect=valor;}else if(attr==='store'){item.data.store=valor;}else if(attr==='modo'){item.data.modo=valor;}else if(attr==='selectconFoco'){item.data.selectconFoco=valor;}else if(attr==='selectAction'){item.data.selectAction=valor;}else if(attr==='valorDisplay'){item.data.valorDisplay=valor;}else if(attr==='valorId'){item.data.valorId=valor;}else if(attr==='isFlecha'){item.data.isFlecha=valor;}else if(attr==='isAutoComp'){item.data.isAutoComp=valor;}else if(attr==='delimitador'){item.data.delimitador=valor;}else if(attr==='row'){item.data.row=valor;}else if(attr==='columnas'){item.data.columnas=valor;}else if(attr==='tabs'){item.data.tabs=valor;}else if(attr==='isDesplegable'){item.data.isDesplegable=valor;}else if(attr==='isFondo'){item.data.isFondo=valor;}else if(attr==='isCerrable'){item.data.isCerrable=valor;}else if(attr==='bodyPadding'){item.data.bodyPadding=valor;}else if(attr==='isAutoScroll'){item.data.isAutoScroll=valor;}else if(attr==='isResizable'){item.data.isResizable=valor;}else if(attr==='isBodyBorder'){item.data.isBodyBorder=valor;}else if(attr==='titulo_Aling'){item.data.titulo_Aling=valor;}else if(attr==='titulo'){item.data.titulo=valor;}else if(attr==='etiqueta'){item.data.etiqueta=valor;}else if(attr==='fechaInvalidTxt'){item.data.fechaInvalidTxt=valor;}else if(attr==='escala'){item.data.escala=valor;}else if(attr==='tipo'){item.data.tipo=valor;}else if(attr==='isPadre'){item.data.isPadre=valor;}else if(attr==='estilo'){item.data.estilo=valor;}else if(attr==='url'){item.data.url=valor;}else if(attr==='titulo_Posicion'){item.data.titulo_Posicion=valor;}else if(attr==='isModal'){item.data.isModal=valor;}else if(attr==='isMinimizable'){item.data.isMinimizable=valor;}else if(attr==='cordX'){item.data.cordX=valor;}else if(attr==='cordY'){item.data.cordY=valor;}else if(attr==='html'){item.data.html=valor;}else if(attr==='src'){item.data.src=valor;}else if(attr==='query'){item.data.query=valor;}else if(attr==='columna_orden'){item.data.columna_orden=valor;}else if(attr==='columna_hidden'){item.data.columna_hidden=valor;}else if(attr==='columna_move'){item.data.columna_move=valor;}else if(attr==='columna_resize'){item.data.columna_resize=valor;}else if(attr==='isBorder'){item.data.isBorder=valor;
			}else if(attr==='winEstilo'){
				item.data.winEstilo=valor;
			}
			return false;
		}	
	 });
}
//Set focus de la ventana Objetos y atributos
function cambiaTab(tab){var pnl = Ext.getCmp('atributosPanel');pnl.setActiveTab(tab);}
//Creacion de formas de Ejemplo
function getDummy(tipo){var dummy;if (tipo === 'Formulario'){dummy = new Ext.form.FormPanel({collapsible: true,frame: true,id: tipo,title: 'Ejemplo de formulario',bodyPadding: '5 5 5',width: 200,height: 260,margin: '20,0,0,0',fieldDefaults: {msgTarget: 'side',labelWidth: 70, width: 175},defaultType: 'textfield',items: [{fieldLabel: 'Nombre',afterLabelTextTpl: required},{fieldLabel: 'Apellidos',afterLabelTextTpl: required},{fieldLabel: 'Empresa'},{fieldLabel: 'Correo',afterLabelTextTpl: required},{fieldLabel: 'Fecha Nac', xtype: 'datefield'},{fieldLabel: 'Edad', xtype: 'numberfield', minValue: 18, maxValue: 60}],buttons: [{text: 'Enviar',handler: function() {this.up('form').getForm().isValid();}},{text: 'Cancelar',handler: function() {this.up('form').getForm().reset();}}]});setSur('<p><b>Panel estilo Formulario.- </b>Es el contenedor que sirve para envio de información <b>a una sola columna</b> al servidor.</p>');}else if(tipo === 'Columnas'){dummy = new Ext.form.FormPanel({collapsible: true,frame: true,id: 'Columnas',title: 'Panel con Columnas',width: 200,height: 260,margin: '20,0,0,0',layout: 'column',defaultType: 'container',items: [{columnWidth: 1/3,padding: '5 0 5 5',items:[{title: 'Col A',html: 'AAAA'}]},{columnWidth: 1/3,padding: '5 0 5 5',items:[{title: 'Col B',html: 'BBBB'}]},{columnWidth: 1/3,padding: 5,items:[{title: 'Col',html: 'CCCC'},{margin: '5 0 0 0',title: 'Col',html: 'CCCC'}]}]});setSur('<p><b>Panel con Columnas.- </b>Es el panel que sirve para contener otros paneles en N columnas. El panel se ajusta a las dimenciones del texto.</p>');}else if(tipo === 'Tabs'){dummy = new Ext.TabPanel({title: 'Panel con Tabs',id: 'Tabs',collapsible: true,margin: '20,0,0,0',width: 200,height: 260,frame: true,resizable:true,items: [{title: 'Tab1',html: 'Contenido 1'}, {title: 'Tab2',html: 'Contenido 2'}, {title: 'Tab3',html: 'Contenido 3',closable: true}]});setSur('<p><b>Panel con Tabs.- </b>Es el panel que sirve para contener controles en diferfentes pestañas.</p>');}else if(tipo === 'Acordion'){dummy = new Ext.Panel({title: 'Panel estilo Acordion',id: 'Acordion',collapsible: true,margin: '20,0,0,0',frame: true,layout: 'accordion',width: 200,height: 260,autoScroll:true,resizable:true,defaults: {bodyPadding: 10},items: [{title: 'Acordion 1',html: 'Contenido 1'}, {title: 'Acordion 2',html: 'Contenido 2'}, {title: 'Acordion 3',html: 'Contenido 3'}]});setSur('<p><b>Panel estilo Acordion.- </b>Es el panel que sirve para contener controles en diferfentes pestañas tipo Acordion.</p>');}else if(tipo === 'Tablas'){dummy = new Ext.Panel({title: 'Panel estilo Tablas',id: 'Tablas',collapsible: true,margin: '20,0,0,0',width: 200,height: 260,frame: true,layout: {type: 'table',columns: 3},defaults: {frame:true, width:70, height: 110},items:[{title:'1'},{title:'2'},{title:'3',rowspan: 2,height: 220},{title:'4',width:140,colspan:2}]});setSur('<p><b>Panel estilo Tablas.- </b>Es el panel que sirve para contener controles en diferfentes contenedores permitiendo aplicar <b>colspan y rowspan</b> para obtener la vista deseada.</p>');}else if(tipo === 'Border'){dummy = new Ext.Panel({title: 'Panel estilo Border',id: 'Border',collapsible: true,margin: '20,0,0,0',frame: true,layout: 'border',width: 200,height: 260,autoScroll:true,resizable:true,items:[{region:'west',title:'Izquierda',split: true,layout:'fit',collapsible: true,collapsed: true,animCollapse: true,margins: '0 0 5 5',width: 50,height: 260,html: '<p style="padding:10px;color:#556677;">Select a configuration below:</p>'},{region:'center',title:'Centro',split: true,layout:'fit',collapsible: true,animCollapse: true,margins: '0 0 5 5'}]});setSur('<p><b>Panel estilo Border.- </b>Panel que sirve para contener controles y/o paneles en diferfentes contenedores permitiendo colocar controles en el <b>norte, sur, izquierda, derecha</b> para obtener la vista deseada.</p>');}else if(tipo === 'Ventana'){dummy = new Ext.Panel({title: 'Titulo de la Ventana',id: 'Ventana',margin: '20,0,0,0',frame: true,width: 200,height: 260});setSur('<p><b>Ventana.- </b>Es un contenedor como cualquier panel que pudiera contener cualquier Layout solo que de forma flotante.</p>');}return dummy;}
//Escribe descripciones en el Sur
function setSur(texto){var pnlS = Ext.getCmp('pnlSur');pnlS.update(texto);}
//Limpia las formas de ejemplo
function clearDummy(){Ext.each(storeIconos.data.items, function(record){var pnl = Ext.getCmp(record.data.name);if(pnl != undefined){pnl.close();}});}
//Elimina control del Panel Activo
function eliminarCtrl(name){
	var panel;
	var arryAttrDeleted = [];
	Ext.each(storeSuperPanel.data.items, function(item){
		if(item.data.idHijo==ctrl || item.data.idGrid==ctrl){
			arryAttrDeleted.push(item);
			panel = item.data.idPadre;return;
		}
	});
	var cPanel = Ext.getCmp(panel);
	cPanel.remove(ctrl);
	ctrl = cPanel.id;
	var t = cPanel.getXType();
	var store;
	if(cPanel.getXType() == 'form'){
		if(cPanel.getLayout().type=='column'){
			store = storeColumnas;
		}else{
			store = storeFormulario;
		}
	}else if(cPanel.getXType() == 'tabpanel'){
		store = storeTabs;
	}else if(cPanel.getXType() == 'window'){
		store = storeWindow;
	}
	storeSuperPanel.remove(arryAttrDeleted);
	cargaAtributosPanel(cPanel.id,store);
}
//Filtra atributos del modelo
function getExisteStore(store, attr){var rgs = false;Ext.each(store.model.getFields(), function(attrs){if(attrs.name==attr){rgs = true;return;}});return rgs;}
//Quita espacios
function  trim (myString){return myString.replace(/^\s+/g,'').replace(/\s+$/g,'');}
function parseBol(str){if (str == false){return false;}else if (str == true){return true;}else if (str.toLowerCase()=='false'){return false;} else if (str.toLowerCase()=='true'){return true;} else {return undefined;}}
function parseWidth(str){
	if(str == ''){
		return '100%';
	}else if(str == null){
		return '100%';
	}else{
		return parseInt(str);
	}
}

function parseDate(str){var n = str.indexOf("/");var dia =  str.substring(0,n);var resto = str.substring(n+1);n = resto.indexOf("/");var mes = resto.substring(0,n);var año = resto.substring(n+1);if (isNaN(dia)==true || isNaN(mes)==true || isNaN(año)==true){return new Date();}else{return new Date(año,mes-1,dia);}}
function getAttrOpen(lista,valor){
	for(i = 0; i < lista.length; i++){
		if(lista[i].attr == valor){
			if(lista[i].tipo == 'N'){
				
				return parseInt(lista[i].valor);
			}else if(lista[i].tipo == 'B'){
				return parseBol(lista[i].valor);
			}else{
				return lista[i].valor;
			}
		}
	}
	if(valor == 'height' || valor == 'width'){
		return 0;
	}else{
		return '';	
	}
}

