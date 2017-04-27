Ext.onReady(function(){

    Ext.QuickTips.init();
    Ext.QuickTips.enable();
    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    var bd = Ext.getBody();
    var recordd; 
    var recordClausulas;

    /**********************************************************************
    * Crea la ventana de periodos
    ***********************************************************************/
    function periodoIE() {
        
        var formPanelPeriodos = new Ext.form.FormPanel({
              id:'load-record-period',
            baseCls: 'x-plain',
            url:'definicion/AsociarPeriodo.action',
            defaultType: 'datefield',
            reader: new Ext.data.JsonReader({        //el edit aun no esta probado
                root: '' 
            }, ['inicio','fin']
            ),
            items: [
                {
                   xtype:'hidden',
                   id:'id-hidden-codigo-periodo',
                   name:'codigoPeriodo'
                },{
                    xtype:'datefield',
                    fieldLabel: 'Fecha Inicial*',
                    name: 'inicio',
                    id: 'startdt',
                    vtype: 'daterange',
                    allowBlank:false,
                    blankText : 'Fecha de inicio del per\u00EDodo requerida.',
                    endDateField: 'enddt',
                    format:'d/m/Y'
                },{
                    xtype:'datefield',
                    allowBlank:false,
                    blankText : 'Fecha de final del per\u00EDodo requerida.',
                    fieldLabel: 'Fecha Final*',
                    name: 'fin',
                    id: 'enddt',
                    vtype: 'daterange',
                    startDateField: 'startdt',
                    format:'d/m/Y'
                }
            ]
        });
        
        // define window and show it in desktop
        var windowPeriodos = new Ext.Window({
            title: 'Agregar Per\u00EDodos de Validez del Producto',
            width: 300,
            height:200,
            layout: 'fit',
            plain:true,
            bodyStyle:'padding:5px;',
            buttonAlign:'center',
            items: formPanelPeriodos,
            modal:true,
            buttons: [{
                text: 'Guardar', 
                handler: function() {
                    // check form value 
                    if (formPanelPeriodos.form.isValid()) {
                         //    alert(Ext.getCmp('id-hidden-codigo-periodo').getValue());              
                         formPanelPeriodos.form.submit({    
                            waitTitle:'Espere',
                            waitMsg:'Procesando...',
                            failure: function(form, action) {
                                Ext.MessageBox.alert('Error al Guardar el Periodo', "La fecha inicial de un periodo no puede ser anterior <br />a la fecha final del ultimo periodo agregado");
                            },
                            success: function(form, action) {
                                //Ext.MessageBox.alert('Confirm', action.result.info);
                                windowPeriodos.close();
                                store.load({params:{start:0, limit:10}});
                            }
                        });                   
                    } else{
                        Ext.MessageBox.alert('Errors', 'Favor de llenar todos los datos requeridos');
                    }             
                }
            },{
                text: 'Cancelar',
                handler: function(){windowPeriodos.close();}
            }]
        });
    
        windowPeriodos.show();
    }
    
    function eliminarPeriodo(btn){
        if(btn=='yes'){
            Ext.MessageBox.alert('alert', 'Changes saved successfully.');
            //DeleteDemouser(store);
        }
    };
    
    function modificarPeriodo(btn){
        if(btn=='yes'){
            //alert("El santo contra las momias de guanajuato"+recordd);
            //Ext.MessageBox.alert('alert', 'Changes saved successfully.');
            //Ext.Msg.alert('alert', 'El santo contra las brujas de tolantongo'+recordd);
            PeriodoIE(store, recordd);
        }
    };
    
    function modificarClausula(){
        ClausulaIE(ds, recordClausulas);
    };
    
    function showResultText(btn, text){
        Ext.example.msg('Button Click', 'You clicked the {0} button and entered the text "{1}".', btn, text);
    };
    
    //segunda tabla 
    var store = new Ext.data.Store({
        storeId:'store-periodos-valides',
        proxy: new Ext.data.HttpProxy({
            url: 'definicion/PeriodoLista.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'periodos',
            id: 'periodos'
            },[
           {name: 'codigoPeriodo', type: 'string',mapping:'codigoPeriodo'},
           {name: 'inicio', type: 'string',mapping:'inicioFormato'},
           {name: 'fin', type: 'string',mapping:'finFormato'}    
        ]),
        remoteSort: true
    });

    store.setDefaultSort('periodos', 'desc');
    
    var columnModel = new Ext.grid.ColumnModel([
        {id:'codigoPeriodo',header: "Periodo", width: 175, sortable: true,dataIndex: 'codigoPeriodo'},
        {header: "Fecha Inicial", width: 185, sortable: true, dataIndex: 'inicio'},
        {header: "Fecha Final", width: 185, sortable: true, dataIndex: 'fin'}
    ]);
    
    // Define the Grid data and create the Grid
    var ds = new Ext.data.Store({
        storeId:'store-clausulas-asociadas',
        proxy: new Ext.data.HttpProxy({
            url: 'definicion/ClausulaLista.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'clausulas',
            id: 'clausulas'
            },[
           {name: 'clave', type: 'string',mapping:'codigoClausula'},
           {name: 'clausulaDescripcion', type: 'string',mapping:'descripcionClausula'}    
        ]),
        remoteSort: true
    });
    
    ds.setDefaultSort('clausulas', 'desc'); 

  
    // the DefaultColumnModel expects this blob to define columns. It can be extended to provide
    // custom or reusable ColumnModels
    var colModel = new Ext.grid.ColumnModel([
        {id:'clave',header: "Clave", width: 75, sortable: true,dataIndex: 'clave'},
        {header: "Descripci\u00F3n", width: 260, sortable: true,  locked:false, dataIndex: 'clausulaDescripcion'}
    ]);
    
    var dsComboCatalogo = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: 'definicion/CatalogoClausulas.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'clausulasCatalogo',
            id: 'comboCatalogo'
            },[
           {name: 'key', type: 'string',mapping:'codigoClausula'},
           {name: 'value', type: 'string',mapping:'descripcionClausula'}
                
        ]),
        remoteSort: true
    });
    dsComboCatalogo.load();
    
    //combo declaracion
     
    //Codigo de los Combos
    // simple array store
    var storeC = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: 'definicion/TestCombos.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'combos',
            id: 'combo'
            },[
           {name: 'clave', type: 'string',mapping:'key'},
           {name: 'clausulaDescripcion', type: 'string',mapping:'value'},
           {name: 'nick', type: 'string',mapping:'nick'}    
        ]),
        remoteSort: true
    });

    storeC.setDefaultSort('combo', 'desc'); 

    //ds del los combos
       var dsCatalogoTipoProducto = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: 'definicion/CatalogoTipoProducto.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'catalogoTipoProducto',
            id: 'comboPadre'
            },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}       
        ]),
        remoteSort: true
    });
    dsCatalogoTipoProducto.load(); 
   
       var dsCatalogoTipoRamo = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: 'definicion/CatalogoTipoRamo.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'catalogoTipoRamo',
            id: 'catalogoTipoRamo'
            },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}           
        ]),
        remoteSort: true
    });    
    dsCatalogoTipoRamo.load(); 
       
    var dsCatalogoTipoPoliza = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: 'definicion/CatalogoTipoPoliza.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'catalogoTipoPoliza',
            id: 'catalogoTipoPoliza'
            },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}      
        ]),
        remoteSort: true
    });
    
    dsCatalogoTipoPoliza.load(); 
    
    var dsCatalogoTipoSeguro = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: 'definicion/CatalogoTipoSeguro.action'
        }),
        reader: new Ext.data.JsonReader({
            root: 'catalogoTipoSeguro',
            id: 'catalogoTipoSeguro'
            },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}      
        ]),
        remoteSort: true
    });
    dsCatalogoTipoSeguro.load();
    //definicion de catalogos
    
     var claveCatalogoClausula = new Ext.form.ComboBox({
        id:'id-combo-clausulas-definicion',
        tpl: '<tpl for="."><div ext:qtip="{value}. {key}" class="x-combo-list-item">{value}</div></tpl>',
        store: dsComboCatalogo,
        displayField:'value',
        valueField: 'key',                        
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Seleccione una cl\u00E1usula',
        selectOnFocus:true,
        fieldLabel: 'Clave',
        name:"claveCatalogoClausulas"
    });
    
    var codigoTipoParametro =new Ext.form.ComboBox({
        id:'id-combo-tipo-parametro-definicion',
        tpl: '<tpl for="."><div ext:qtip="{value}. {key}" class="x-combo-list-item">{value}</div></tpl>',
        store: dsCatalogoTipoProducto,
        displayField:'value',
        valueField: 'key',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Seleccione tipo de producto',
        selectOnFocus:true,
        fieldLabel: 'Línea de Negocio*',
        name:"valorTipoProducto",    
        allowBlank:false,                            
        blankText : 'Tipo de producto es requerido',
        focusAndSelect : function(record) {
            var index = typeof record === 'number' ? record : this.store.indexOf(record);
            
            Ext.getCmp('clave-tipo-producto').setValue(record.get('key'));
            this.select(index, this.isExpanded());
            this.onSelect(this.store.getAt(record), index, this.isExpanded());
        },
        onSelect : function(record, index, skipCollapse){
            if(this.fireEvent('beforeselect', this, record, index) !== false){
                this.setValue(record.data[this.valueField || this.displayField]);
                if( !skipCollapse ) {
                    this.collapse();
                }
                this.lastSelectedIndex = index + 1;
                this.fireEvent('select', this, record, index);
            }
        }
    });
    
    var codigoTipoRamo =new Ext.form.ComboBox({
        id:'id-combo-tipo-ramo-definicion',
        tpl: '<tpl for="."><div ext:qtip="{value}. {key}" class="x-combo-list-item">{value}</div></tpl>',
        store: dsCatalogoTipoRamo,
        displayField:'value',
        valueField: 'key',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Seleccione tipo de ramo',
        selectOnFocus:true,
        fieldLabel: 'Tipo de ramo*',
        name:"valorTipoRamo",
        allowBlank:false,
        blankText : 'Tipo de ramo es requerido',
        focusAndSelect : function(record) {
            var index = typeof record === 'number' ? record : this.store.indexOf(record);
            
            Ext.getCmp('clave-tipo-ramo').setValue(record.get('key'));
            this.select(index, this.isExpanded());
            this.onSelect(this.store.getAt(record), index, this.isExpanded());
        },
        onSelect : function(record, index, skipCollapse){
            if(this.fireEvent('beforeselect', this, record, index) !== false){
                this.setValue(record.data[this.valueField || this.displayField]);
                if( !skipCollapse ) {
                    this.collapse();
                }
                this.lastSelectedIndex = index + 1;
                this.fireEvent('select', this, record, index);
            }
        }
    });
    var codigoTipoPoliza =new Ext.form.ComboBox({
        id:'id-combo-tipo-poliza-definicion',
        tpl: '<tpl for="."><div ext:qtip="{value}. {key}" class="x-combo-list-item">{value}</div></tpl>',
        store: dsCatalogoTipoPoliza,
        displayField:'value',
        valueField: 'key',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Seleccione tipo de p\u00F3liza',
        selectOnFocus:true,
        fieldLabel: 'Tipo p\u00F3liza*',
        name:"valorTipoPoliza",
        allowBlank:false,
        blankText : 'Tipo de p\u00F3liza es requerido',
        focusAndSelect : function(record) {
            var index = typeof record === 'number' ? record : this.store.indexOf(record);
            
            Ext.getCmp('clave-tipo-poliza').setValue(record.get('key'));
            this.select(index, this.isExpanded());
            this.onSelect(this.store.getAt(record), index, this.isExpanded());
        },
        onSelect : function(record, index, skipCollapse){
            if(this.fireEvent('beforeselect', this, record, index) !== false){
                this.setValue(record.data[this.valueField || this.displayField]);
                if( !skipCollapse ) {
                    this.collapse();
                }
                this.lastSelectedIndex = index + 1;
                this.fireEvent('select', this, record, index);
            }
        }
    });
    var codigoTipoSeguro =new Ext.form.ComboBox({
        id:'id-combo-tipo-seguro-definicion',
        tpl: '<tpl for="."><div ext:qtip="{value}. {key}" class="x-combo-list-item">{value}</div></tpl>',
        store: dsCatalogoTipoSeguro,
        displayField:'value',
        valueField: 'key',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Seleccione tipo de seguro',
        selectOnFocus:true,
        fieldLabel: 'Tipo de seguro*',
        name:"valorTipoSeguro",
        allowBlank:false,
        blankText : 'Tipo de seguro es requerido',
        focusAndSelect : function(record) {
            var index = typeof record === 'number' ? record : this.store.indexOf(record);
            
            Ext.getCmp('clave-tipo-seguro').setValue(record.get('key'));
            this.select(index, this.isExpanded());
            this.onSelect(this.store.getAt(record), index, this.isExpanded());
        },
        onSelect : function(record, index, skipCollapse){
            if(this.fireEvent('beforeselect', this, record, index) !== false){
                this.setValue(record.data[this.valueField || this.displayField]);
                if( !skipCollapse ) {
                    this.collapse();
                }
                this.lastSelectedIndex = index + 1;
                this.fireEvent('select', this, record, index);
            }
            
            if(this.getValue() == "2"){
                Ext.getCmp('switch-subincisos').setDisabled(false);                                                                            
            }
            if(this.getValue() == "1"){
                Ext.getCmp('switch-subincisos').setDisabled(true);
                Ext.getCmp('switch-subincisos').reset();                                        
            }
            if(this.getValue() == "3"){
                Ext.getCmp('switch-subincisos').setDisabled(true);
                Ext.getCmp('switch-subincisos').reset();                                        
            }                                    
        }
    });

    //primera tabla
    
    //definicion datos principales de producto          
    var codigoRamo= new Ext.form.NumberField({
        id:'codigo-ramo-producto',
        fieldLabel: 'Producto*',
        name: 'codigoRamo',
        allowDecimals : false,
        allowNegative : false,                
        labelSeparator:'',
        maxValue:999,
        maxText:'El valor M\u00E1ximo es 999.',
        width:44,
        //allowBlank:false,
        //value:'',
        disabled:true,
        blankText : 'Clave del producto es num\u00E9rica y requerida.'
     });
     
    var descripcionRamo= new Ext.form.TextField({
        hideLabel: true,                                                                    
        fieldLabel: 'Descripci\u00F3n ramo*',
        name: 'descripcionRamo',
        labelSeparator:'',
        width:150,
        allowBlank:false,
        blankText : 'Nombre del producto es requerido.',
        maxLength:'30'
     });
     
    var descripcionP= new Ext.form.TextArea({
        name: "descripcion",
        fieldLabel: 'Descripci\u00F3n*',
        width: 200,
        labelSeparator:'',
        allowBlank: false,
        blankText : 'Descripci\u00F3n es requerida.'
    });

    //definicion attributos de producto
    var tipoDagnos= new Ext.form.Radio({    
        id:'tipo-dagnos',                     
        name:'switchTipo',
        boxLabel:'',//'<s:text name="def.productos.danios"/>',
        hideLabel:true,
        hideParent:true,
        checked:true,
        hidden:true,
        onClick:function(){
            Ext.getCmp('hidden-codigo-switch-tipo').setValue("1");
            if(this.getValue()){
                Ext.getCmp('switch-reinstalacion-automatica').setDisabled(false);
            }
        }
    });
    
    var tipoVida= new Ext.form.Radio({                         
        id:'tipo-vida', 
        name:'switchTipo',
        boxLabel:'',//'<s:text name="def.productos.vida"/>',
        hideLabel:true,
        hideParent:true,
        hidden:true,
        onClick:function(){
            Ext.getCmp('hidden-codigo-switch-tipo').setValue("3");
            if(this.getValue()){                                    
                Ext.getCmp('switch-reinstalacion-automatica').setDisabled(true);
                Ext.getCmp('switch-reinstalacion-automatica').reset();                                    
            }
        }
    });
    
    var tipoGastosMedicos= new Ext.form.Radio({    
        id:'tipo-gastos-medicos',                     
        name:'switchTipo',
        boxLabel:'',//'<s:text name="def.productos.gastosMedicos"/>',
        hideLabel:true,
        hideParent:true,
        hidden:true,
        onClick:function(){
            Ext.getCmp('hidden-codigo-switch-tipo').setValue("2");
            if(this.getValue()){
                Ext.getCmp('switch-reinstalacion-automatica').setDisabled(false);
            }
        }
    });
    
    var tipoOtros= new Ext.form.Radio({                         
        id:'tipo-otros', 
        name:'switchTipo',
        boxLabel:'',//'<s:text name="def.productos.otros"/>',
        hideLabel:true,
        hideParent:true,
        hidden:true,
        onClick:function(){
            Ext.getCmp('hidden-codigo-switch-tipo').setValue("4");
            if(this.getValue()){
                Ext.getCmp('switch-reinstalacion-automatica').setDisabled(false);                                    
                            }
                        }
    });
     
    var switchPermisoPagosOtrasMonedas= new Ext.form.Checkbox({
        id:'switch-permiso-pagos-otras-monedas',
        name:'switchPermisoPagosOtrasMonedas',
        boxLabel: '',//'<s:text name="def.productos.monedas"/>',
        hideLabel:true,
        hidden:true,
        onClick:function(){
            if(this.getValue()){                                
                this.setRawValue("S");                                
            }else{      
                this.setRawValue("N");                              
            }
        }
    });
    
    var switchReinstalacionAutomatica= new Ext.form.Checkbox({
        id:'switch-reinstalacion-automatica',
        name:'switchReinstalacionAutomatica',
        boxLabel: 'Renovaci\u00F3n',
        hideLabel:true,
        onClick:function(){
            if(this.getValue()){                                
                this.setRawValue("S");                                
            }else{      
                this.setRawValue("N");                              
            }
        }            
    });
    
    var switchEndosos= new Ext.form.Checkbox({
        id:'switch-endosos',
        name:'switchEndosos',
        boxLabel: 'Endosos',
        hideLabel:true,
        onClick:function(){
            if(this.getValue()){                                
                this.setRawValue("S");                                
            }else{      
                this.setRawValue("N");                              
            }
        }            
    });
    
    var switchDistintasPolizasPorAsegurado= new Ext.form.Checkbox({
        id:'switch-distintas-polizas-por-asegurado',
        name:'switchDistintasPolizasPorAsegurado',
        boxLabel: '',//'<s:text name="def.productos.distintasPolReaseguro"/>',
        hideLabel:true,
        hidden:true,
        onClick:function(){
            if(this.getValue()){                                
                this.setRawValue("S");                                
            }else{      
                this.setRawValue("N");                              
            }
        }
    });
    
    var renovable = new Ext.form.Checkbox({
        id:'switch-renovable',
        name:'renovable',
        boxLabel: 'Renovable',
        hideLabel:true,
        onClick:function(){
            if(this.getValue()){                                
                this.setRawValue("S");                                
            }else{      
                this.setRawValue("N");                              
            }
        }            
    });
    
    var temporal = new Ext.form.Checkbox({
        id:'switch-temporal',
        name:'temporal',
        boxLabel: 'Temporal',
        hideLabel:true,
        onClick:function(){
            if(this.getValue()){                                
                this.setRawValue("S");                                
            }else{      
                this.setRawValue("N");                              
            }
        }            
    });
    
    var vidaEntera = new Ext.form.Checkbox({
        id:'switch-vidaEntera',
        name:'vidaEntera',
        boxLabel: 'Vida Entera',
        hideLabel:true,
        onClick:function(){
            if(this.getValue()){                                
                this.setRawValue("S");
                Ext.getCmp('switch-renovable').setDisabled(true);
                Ext.getCmp('switch-temporal').setDisabled(true);
                Ext.getCmp('switch-renovable').reset();
                Ext.getCmp('switch-temporal').reset();
                Ext.getCmp('tipo-dagnos').setDisabled(true);
                Ext.getCmp('tipo-otros').setValue(true);                                
            }else{      
                this.setRawValue("N");                                    
                Ext.getCmp('switch-renovable').setDisabled(false);
                Ext.getCmp('switch-temporal').setDisabled(false); 
                Ext.getCmp('tipo-dagnos').setDisabled(false);                             
            }
        }
    });
       
    var switchSubincisos = new Ext.form.Checkbox({
        id:'switch-subincisos',
        name:'switchSubincisos',
        boxLabel: '',//'<s:text name="def.productos.subIncisos"/>',
        hideLabel:true,
        hidden:true, 
        onClick:function(){
            if(this.getValue()){                                
                this.setRawValue("S");                                
            }else{      
                this.setRawValue("N");                              
            }
        }
    });
    
    var mesesBeneficiosDummy= new Ext.form.NumberField({                            
        fieldLabel: '',//'Antigüedad Mínima <br>(Meses)',
        labelAlign:'left',
        hidden:true
    });
    
    var mesesBeneficios= new Ext.form.NumberField({
        id:'meses-beneficios',
        name:'mesesBeneficios',
        fieldLabel: '',//'<s:text name="def.productos.mesesBeneficios"/>',
        hideParent:true,
        allowDecimals : false,
        allowNegative : false,                
        labelSeparator:'',
        maxValue:500,
        maxText:'Valor m\u00E1ximo 500.',
        width:50,
        hideLabel:true,
        hidden:true         
    });
    
    var tipoCalculoPolizasTemporalesDummy= new Ext.form.NumberField({
        fieldLabel: '',//'Tipo Cálculo Pólizas Temporales',                             
        labelAlign:'left',
        hidden:true
    });
    
    var tipoCalculoPolizasTemporales= new Ext.form.NumberField({
        name:'tipoCalculoPolizasTemporales',
        fieldLabel: '',//'<s:text name="def.productos.tipoCalcPolTemp"/>',
        hideParent:true,
        allowDecimals : false,
        allowNegative : false,                
        labelSeparator:'',
        labelAlign:'top',
        maxValue:500,
        maxText:'Valor m\u00E1ximo 500.',
        width:50,
        hideLabel:true,
        hidden:true    
    });
    
    var switchRehabilitacion= new Ext.form.Checkbox({
        id:'switch-rehabilitacion',
        name:'switchRehabilitacion',
        boxLabel: 'Rehabilitaci\u00F3n',
        hideLabel:true,
        onClick: function(){                                
            if(this.getValue()){    
                 this.setRawValue("S");     
                 //mesesBeneficios.reset();
                 //Ext.getCmp('id4test-hidding').show();                                                               
            }else{      
                this.setRawValue("N");    
                //Ext.getCmp('id4test-hidding').hide();                                         
                //mesesBeneficios.disable();                                    
            }
        }
    });
    
    var switchCancelacion= new Ext.form.Checkbox({
        id:'switch-cancelacion',
        name:'switchCancelacion',
        boxLabel: 'Cancelaci\u00F3n',
        hideLabel:true,
        onClick: function(){                                
            if(this.getValue()){    
                 this.setRawValue("S");
                 Ext.getCmp('id-forma-rehabilitacion').show();
                 //mesesBeneficios.reset();
                 //Ext.getCmp('id4test-hidding').show();                                                               
            }else{      
                this.setRawValue("N");
                Ext.getCmp('id-forma-rehabilitacion').hide();
                switchRehabilitacion.reset();
                
                //Ext.getCmp('id4test-hidding').hide();                                         
                //mesesBeneficios.disable();                                    
            }
        }            
    });
            
    var formaAnidada= new Ext.FormPanel({
        layout:'form',
        border:false, 
        width:800,           
        items:[{
            layout:'column',
            border:false,
            items:[{
                columnWith:.7,
                layout:'form',
                border:false,
                width:200,
                labelAlign:'top',
                items:[
                claveCatalogoClausula]
            },{
                columnWith:.3,
                border:false,
                layout:'form',
                width:130,
                items:[{
                    layout: 'form',
                    border:false,
                    height:18
                },{
                    border:false,
                    layout:'form',
                    width:130,
                    items:[{
                        xtype:'button',
                        text:'Agregar al cat\u00E1logo',
                        handler: function(){
                            ClausulaIE(dsComboCatalogo,selectedId); 
                        }
                    }]
                }]
            }]
        }]
    });
    
    formaAnidada.render('center-anidado');
    
    function reload(){
        ds.load({params:{start:0, limit:10}});
    }
    
    var selectedId;
    
    var tab2 = new Ext.FormPanel({
        labelAlign: 'right',
        id:'reload-products',
        title: 'Definici\u00F3n de Productos',
        bodyStyle:'padding:5px',
        url:"definicion/InsertarProducto.action",
        border:true,
        width: 650,
        height:470,
        autoScroll:true,
        items: [{
            layout:'column',
            border:false,
            bodyStyle:'padding:5px 5px 0',
            items:[{
                columnWidth:.55,
                layout:'form',                                            
                border:false,
                bodyBorder:true,
                labelAlign:"left",
                width: 310,
                items: [{
                    layout:'column',
                    border:false,
                    items:[{
                        columnWidth:.45,
                        layout: 'form',
                        border:false,
                        items: [codigoRamo]
                    },{
                           columnWidth:.55,
                        layout: 'form',
                        border:false,
                        items: [descripcionRamo]
                    }]
                  },
                  descripcionP
                ]
            },{
                columnWidth:.45,
                layout:'form',                                            
                border:false,
                bodyBorder:true,
                labelAlign:"left",
                labelWidth:110,
                width: 310,
                items: [{
                    layout:'column',
                    border:false,
                    items:[{
                        layout:'form',
                        defaultType: 'textfield',
                        border: false,
                        items: [
                          codigoTipoParametro,
                          codigoTipoRamo,
                          codigoTipoPoliza,
                          codigoTipoSeguro
                        ]
                    }]
                }]
            }]
        }, {
            xtype:'tabpanel',
            plain:true,
            deferredRender:false,            
            activeTab: 0,
            height:280,
            defaults:{bodyStyle:'padding:10px'},
            items:[{
                title:'Definici\u00F3n',
                layout:'form',                           
                border:false,                            
                items: [{
                    layout:'column',
                    border:false,
                    items:[{
                        columnWidth:.09,
                        border:false
                    },{                                            
                        columnWidth:.45,
                        layout: 'form',
                        labelAlign : "top" ,
                        border:false,                                           
                        items: [//switchSuscripcion,
                            //switchClausulasNoTipificadas,    
                            switchCancelacion,
                            {
                                layout:'form',
                                border:false,
                                //width:250,
                                id:'id-forma-rehabilitacion',
                                items:[
                                    switchRehabilitacion
                                ]
                            },
                            switchReinstalacionAutomatica,
                            switchEndosos,
                            switchDistintasPolizasPorAsegurado
                         ]
                    },{
                        columnWidth:.35,
                        layout: 'form',
                        border:false,                                   
                        labelAlign : "top" ,
                        items: [{
                              layout:'column',
                            border:false,
                            labelAlign : "left" ,
                            items:[{
                                //width:'70',
                                layout:'form',
                                border:false,
                                items:[{                                                           
                                    layout: 'form',
                                    border:false,                                                           
                                    items: [tipoDagnos]
                                },{
                                    layout: 'form',
                                    border:false,                                                        
                                    items: [tipoVida]
                                }]
                            },{ 
                                layout:'form',
                                border:false,
                                items:[{                                                            
                                    layout: 'form',
                                    border:false,                                                           
                                    items: [tipoGastosMedicos]
                                },{                                                               
                                    layout: 'form',
                                    border:false,                                                        
                                    items: [tipoOtros]
                                }]
                            }]
                        },{
                            layout:'form',
                            heigth:'2',
                            border: false
                        },{
                            layout:'form',                                                
                            border: true,
                            title:'Temporalidad de las P\u00F3lizas',
                            items: [
                                renovable,
                                temporal,
                                vidaEntera
                            ]
                        },{
                            layout:'form',
                            heigth:'2',
                            border: false
                        },
                        switchSubincisos]
                    }]
                    }]
            },{
                layout: 'form',
                title: 'Cl\u00E1usulas',
                labelAlign: 'top',
                frame:false,        
                bodyStyle:'padding:5px 5px 0',
                width: 680,
                border:false,
                items: [{
                    layout: 'form',
                    border:false,
                    contentEl:'center-anidado'
                },{
                    xtype: 'grid',
                    id:'grid-clausulas',
                    ds: ds,
                    cm: colModel,
                    autoExpandColumn: 'clave',
                    height: 180,
                    width:560,
                    border: true,
                    sm: new Ext.grid.RowSelectionModel({
                        singleSelect: true,
                        listeners: {
                            rowselect: function(grid2, row, rec) {
                                //selectedId=row.getSelected();                                                                                                                                 
                                //alert(selectedId);
                                var sel = Ext.getCmp('grid-clausulas').getSelectionModel().getSelected();
                                var selIndex = ds.indexOf(sel);
                                //alert(selIndex);
                                
                                Ext.getCmp('eliminar-clausula').on('click',function(){
                                    if(rec!=null){
                                        urlEliminarClausula='definicion/EliminarClausula.action?claveCatalogoClausulas='+rec.get('clave');
                                        //urlEliminarClausula='definicion/EliminarClausula.action';
                                        Ext.MessageBox.confirm('Mensaje','Realmente deseas eliminar el elemento?', function(btn) {
                                           if(btn == 'yes'){   
                                                formaAnidada.form.submit({    
                                                    url: urlEliminarClausula,
                                                    waitMsg:"Procesando...",
                                                    failure: function(form, action) {
                                                        Ext.MessageBox.alert('Status', 'Error al eliminar');
                                                    },
                                                    success: function(form, action) {
                                                        Ext.MessageBox.alert('Status', 'Elemento eliminado');                            
                                                        ds.load();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }),
                    tbar:[{
                        text:'Agregar',
                        tooltip:'Asocia la clausula seleccionada en el combo',
                        iconCls:'add',
                        handler:function(){
                            if((Ext.getCmp('grid-clausulas').getStore().find('clave',claveCatalogoClausula.getValue())) < 0){
                                formaAnidada.form.submit({
                                    url:'definicion/AsociarClausula.action',
                                    waitTitle:'Espere',
                                    waitMsg:'Procesando...',
                                    failure: function(form, action) {
                                         Ext.MessageBox.alert('Mensaje de Error', Ext.util.JSON.decode(action.response.responseText).mensaje);
                                    },
                                    success:function(a,t){
                                        Ext.MessageBox.alert('Mensaje de Exito', 'Clausula agregada');
                                        ds.reload();
                                        dsComboCatalogo.load();
                                    }
                                });
                            }else { 
                                Ext.MessageBox.alert('Mensaje de Error', 'La Clausula seleccionada ya est&aacute; asociada');
                            }
                        }
                    },'-',{
                        id:'eliminar-clausula',
                        text:'Eliminar',
                        tooltip:'Eliminar la Clausula seleccionada',
                        iconCls:'remove'
                    }]      
                }]
            },{
                layout: 'form',
                frame: false,
                title: 'Per\u00EDodos',
                bodyStyle:'padding:5px',
                buttonAlign:'center',
                width: 680,
                items: [{
                    xtype: 'grid',
                    id:'grid-periodos',
                    ds: store,
                    cm: columnModel,
                    autoExpandColumn: 'codigoPeriodo',
                    height: 210,
                    width:560,                
                    border: true,
                    sm: new Ext.grid.RowSelectionModel({
                        singleSelect: true,
                        listeners: {
                            rowselect: function(grid2, row, rec) {
                                //Ext.MessageBox.confirm('Buu','el santo contra los vampiros'+row, modificarPeriodo);
                                //formaAnidadaPeriodos.form.loadRecord(recordd);                                              
                                //Ext.Msg.alert('alert', 'El santo contra las brujas de tolantongo'+recordd);
                                   
                                Ext.getCmp('eliminar-periodo').on('click',function(){
                                    if(rec!=null){
                                        urlEliminarPeriodo='definicion/EliminarPeriodo.action?codigoPeriodo='+rec.get('codigoPeriodo');
                                        Ext.MessageBox.confirm('Mensaje','Realmente deseas eliminar el elemento?', function(btn) {
                                               if(btn == 'yes'){   
                                                    formaAnidada.form.submit({    
                                                        url: urlEliminarPeriodo,
                                                        waitMsg:"Procesando...",
                                                        failure: function(form, action) {
                                                             Ext.MessageBox.alert('Status', 'Error al eliminar');
                                                        },
                                                        success: function(form, action) {
                                                            Ext.MessageBox.alert('Status', 'Elemento eliminado');                            
                                                            store.load();                            
                                                        }
                                                    });              
                                                }
                                        });
                                        //DeleteInDataStoreFromAction(formaAnidada,urlEliminarPeriodo,store); 
                                    }                                                                         
                                });
                                afuera=row;
                                Ext.getCmp('editar-periodo').on('click',function(){
                                    if(afuera!=temporal){
                                        temporal=afuera;
                                        recorddP=rec;
                                        periodoIE();
                                           Ext.getCmp('load-record-period').getForm().loadRecord(recorddP);                                                                    
                                    }                                                                                                                                             
                                });    
                                temporal=-1;
                            }
                        }
                    }),
                    buttonAlign:'center',
                    tbar:[{
                       text:'Agregar',
                       tooltip:'Agregar un nuevo Periodo',
                       iconCls:'add',
                       handler:function (){                                       
                                  periodoIE();                             
                       }
                    }, '-', {
                        id:'editar-periodo',
                        text:'Editar',
                        tooltip:'Editar el periodo seleccionado',
                        iconCls:'option'
                    },'-',{
                        text:'Eliminar',
                        id:'eliminar-periodo',
                        tooltip:'Eliminar el periodo seleccionado',
                        iconCls:'remove'
                        }]
                }]
            }]
        },
        {xtype:'hidden',id:'hidden-codigo-switch-tipo',name:'codigoSwitchTipo'},
       	{xtype:'hidden',id:'clave-tipo-producto',name:'claveTipoProducto'},
        {xtype:'hidden',id:'clave-tipo-ramo',name:'claveTipoRamo'},
        {xtype:'hidden',id:'clave-tipo-poliza',name:'claveTipoPoliza'},
        {xtype:'hidden',id:'clave-tipo-seguro',name:'claveTipoSeguro'},
        {xtype:'hidden',id:'hidden-bandera-editar',name:'banderaEditar'},
        {xtype:'hidden',id:'hidden-codigo-ramo-editar',name:'codigoRamoEditar'}],
        buttons: [{
            text: 'Guardar', 
            handler: function() {
                // check form value 
                if (tab2.form.isValid()) {
                    //alert(codigoTipoParametro.getValue());
                    Ext.getCmp('clave-tipo-producto').setValue(codigoTipoRamo.getValue());
                    Ext.getCmp('clave-tipo-ramo').setValue(codigoTipoParametro.getValue());
                    Ext.getCmp('clave-tipo-poliza').setValue(codigoTipoPoliza.getValue());
                    Ext.getCmp('clave-tipo-seguro').setValue(codigoTipoSeguro.getValue());                    
                    tab2.form.submit({                  
                        waitTitle:'Espere',
                        waitMsg:'Procesando...',
                        failure: function(form, action) {
                            Ext.MessageBox.alert('Error', "Producto ya existe");
                        },
                        success: function(form, action) {
                            Ext.MessageBox.alert('Status', "Producto guardado correctamente");
                            //Ext.MessageBox.alert('Status', Ext.util.JSON.decode(response.responseText).mensaje);
                            //window.location.replace("<s:url value="/"/>");
                            //window.location = String.format("<s:url value="/"/>");
                            //window.location.href  = "<s:url value='/Redireccionate.action'/>";
                            //window.location.href = 'Redireccionate.action';
                            //window.location.refresh();
                            //recargarPagina();
                            Ext.getCmp('id-generar-producto-button').setDisabled(false);
                            Ext.getCmp('id-clonar-producto-button').setDisabled(false);
                            
                            Ext.getCmp('arbol-productos').getRootNode().reload();
                            //alert(Ext.util.JSON.decode(action.response.responseText).codigoRamo);
                            Ext.getCmp('codigo-ramo-producto').enable(); 
                            Ext.getCmp('codigo-ramo-producto').setValue(Ext.util.JSON.decode(action.response.responseText).codigoRamo);
                            Ext.getCmp('hidden-codigo-ramo-editar').setValue(Ext.util.JSON.decode(action.response.responseText).codigoRamo);
                            Ext.getCmp('hidden-bandera-editar').setValue('1');
                            Ext.getCmp('codigo-ramo-producto').disable(true);
                            //dataStore.load({params:{start:0, limit:10}});
                        }
                    });                   
                } else{
                    Ext.MessageBox.alert('Error', 'Favor de llenar datos requeridos');
                }             
            }
        },{
            id: 'id-clonar-producto-button',
            text: 'Clonar Producto', 
            handler: function() {
                // check form value 
                if (tab2.form.isValid()) {
                    //alert(codigoTipoParametro.getValue());
                    Ext.getCmp('codigo-ramo-producto').enable(); 
                    tab2.form.submit({                  
                        url: 'definicion/ClonarProducto.action',
                        waitTitle:'Espere',
                        waitMsg:'Procesando...',
                        failure: function(form, action) {
                            //Ext.MessageBox.alert('Error', "No se pudo generar el producto debido a errores");
                            Ext.MessageBox.alert('Status', Ext.util.JSON.decode(action.response.responseText).mensaje);
                            Ext.getCmp('codigo-ramo-producto').disable(true);
                        },
                        success: function(form, action) {
                            //Ext.MessageBox.alert('Status', "Producto guardado correctamente");
                            Ext.MessageBox.alert('Status', Ext.util.JSON.decode(action.response.responseText).mensaje);
                            
                            //window.location.replace("<s:url value="/"/>");
                            //window.location = String.format("<s:url value="/"/>");
                            //window.location.href  = "<s:url value='/Redireccionate.action'/>";
                            //window.location.href = 'Redireccionate.action';
                            //window.location.refresh();
                            //recargarPagina();
                            Ext.getCmp('arbol-productos').getRootNode().reload();
                            Ext.getCmp('codigo-ramo-producto').disable(true);
                            //dataStore.load({params:{start:0, limit:10}});
                        }
                    });                   
                } else{
                    Ext.MessageBox.alert('Error', 'Favor de llenar datos requeridos');
                }
            }
        },{
            id: 'id-generar-producto-button',
            text: 'Generar Producto', 
            handler: function() {
                // check form value 
                if (tab2.form.isValid()) {
                    //alert(codigoTipoParametro.getValue());
                    Ext.getCmp('codigo-ramo-producto').enable(); 
                    tab2.form.submit({                  
                        url: 'definicion/GenerarProducto.action',
                        waitTitle:'Espere',
                        waitMsg:'Procesando...',
                        failure: function(form, action) {
                            Ext.MessageBox.alert('Error', Ext.util.JSON.decode(action.response.responseText).mensaje);
                            Ext.getCmp('codigo-ramo-producto').disable(true);
                        },
                        success: function(form, action) {
                            Ext.MessageBox.alert('Status', Ext.util.JSON.decode(action.response.responseText).mensaje);
                            Ext.getCmp('codigo-ramo-producto').disable(true);
                            
                        }
                    });                   
                } else{
                    Ext.MessageBox.alert('Error', 'Favor de llenar datos requeridos');
                }             
            }
        },{
            text: 'Cancelar',
            handler: function() {
                tab2.form.load({
                    waitTitle:'Espere',
                    waitMsg:'Procesando...',
                    url:'definicion/LimpiarSessionProducto.action',
                    success: function(form, action) {
                        ds.load({params:{start:0, limit:25}});
                        store.load({params:{start:0, limit:25}}); 
                        action.form.reset();
                        Ext.getCmp('reload-products').getForm().reset();
                        Ext.getCmp('switch-permiso-pagos-otras-monedas').setValue(false);
                        Ext.getCmp('switch-permiso-pagos-otras-monedas').setRawValue("N");
                        Ext.getCmp('switch-reinstalacion-automatica').setValue(false);
                        Ext.getCmp('switch-reinstalacion-automatica').setRawValue("N");
                        Ext.getCmp('switch-distintas-polizas-por-asegurado').setValue(false);
                        Ext.getCmp('switch-distintas-polizas-por-asegurado').setRawValue("N");
                        Ext.getCmp('switch-rehabilitacion').setValue(false);
                        Ext.getCmp('switch-rehabilitacion').setRawValue("N");
                        Ext.getCmp('id4test-hidding').hide();                                       
                        Ext.getCmp('switch-subincisos').setValue(false);                                                          
                        Ext.getCmp('switch-subincisos').setRawValue("N");                
                        Ext.getCmp('switch-renovable').setValue(false);
                        Ext.getCmp('switch-renovable').setRawValue("N"); 
                        Ext.getCmp('switch-temporal').setValue(false);
                        Ext.getCmp('switch-temporal').setRawValue("N");
                        Ext.getCmp('switch-vidaEntera').setValue(false);
                        Ext.getCmp('switch-vidaEntera').setRawValue("N");
                        //dataStore.load({params:{start:0, limit:10}});
                    }
                });
            }
        }]
    });

    //Ext.getCmp('id-forma-rehabilitacion').hide();    
    //Ext.getCmp('id4test-hidding').hide();  
    function recargarPagina(){
        window.location.reload();
    }
    
    ds.load({params:{start:0, limit:25}});
    store.load({params:{start:0, limit:25}});
    Ext.getCmp('id-generar-producto-button').setDisabled(true);
    Ext.getCmp('id-forma-rehabilitacion').hide();
    tab2.render('center');
    
});
