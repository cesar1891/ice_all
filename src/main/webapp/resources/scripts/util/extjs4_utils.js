/**
 * Funciones comunes
 * @author rbautista
 */


/**
 * Muestra un mensaje emergente
 * @param title   String
 * @param msg     String
 * @param buttons Object/Object[]
 * @param icon    String 
 */
function showMessage(title, msg, buttons, icon){
    Ext.Msg.show({
        title: title,
        msg: msg,
        buttons: buttons,
        icon: icon
    });
}

/**
 * Funcion para implementar Paginacion,
 * carga un Store y pagina sus datos de forma local.
 * Se debe de tener un Store con un proxy 
 * de la siguiente forma:
 *
   var store=Ext.create('Ext.data.Store',
   {
       pageSize : 10,
       autoLoad : true,
       model    : 'modelPersonalizado',
       proxy    :
       {
           enablePaging : true,
           reader       : 'json',
           type         : 'memory',
           data         : []
       }
   });
       
 * @param {Ext.data.Store} _store
 * @param {String} _url
 * @param {String} _root
 * @param {Array} _params
 * @param {Function} _callback Function to be called 
 * @param {Ext.grid.Panel} _grid (Optional)
 * @param {Number} _timeout (Optional)
 */
function cargaStorePaginadoLocal(_store, _url, _root, _params, _callback, _grid, _timeout) {
    
    var timeOut = null;
    if(_timeout) {
        timeOut = _timeout;
    }
    
    if(_grid){
        _grid.setLoading(true);
        if(_grid.down('pagingtoolbar')){
            _grid.down('pagingtoolbar').moveFirst();
        }
    }
    Ext.Ajax.request(
    {
        url       : _url,
        params    : _params,
        callback  : _callback,
        timeout   : timeOut != null ? timeOut : Ext.Ajax.timeout, 
        success   : function(response)
        {
            if(_grid){
                _grid.setLoading(false);
            }
            _store.removeAll();
            var jsonResponse = Ext.decode(response.responseText);
            _store.setProxy({
                type         : 'memory',
                enablePaging : true,
                reader       : 'json',
                data         : jsonResponse[_root]
            });
            _store.load();
        },
        failure   : function()
        {
            if(_grid){
                _grid.setLoading(false);
            }
            _store.removeAll();
            Ext.Msg.show({
                title   : 'Error',
                icon    : Ext.Msg.ERROR,
                //msg     : 'Error cargando los datos de ' + _url,
                msg     : 'Error al obener los datos, intente m\u00E1s tarde',
                buttons : Ext.Msg.OK
            });
        }
    });
}

/**
 * Esta funcion permite que se guarden los datos en
 * store.datos si no viene filtro, y cuando viene
 * filtro los toma de ahí y filtra sin hacer peticion
 * dejando intactos los store.datos y solo filtrando
 * lo del store normal, esa propiedad .datos siempre
 * tiene la coleccion completa
 * NOTA: puede dar error para getSelectionModel
 */
function cargaStorePaginadoLocalFiltro(_store, _url, _root, _params, _callback, _grid, _timeout, filterFn) {
	
	debug('cargaStorePaginadoLocalFiltro filterFn:',filterFn);
	
	var timeOut = null;
	if(_timeout) {
		timeOut = _timeout;
	}
	
	if(_grid){
		_grid.setLoading(true);
		if(_grid.down('pagingtoolbar')){
			_grid.down('pagingtoolbar').moveFirst();
		}
	}
	
	if(Ext.isEmpty(filterFn))
	{
	    Ext.Ajax.request(
	    {
	        url       : _url,
	        params    : _params,
	        callback  : _callback,
	        timeout   : timeOut != null ? timeOut : Ext.Ajax.timeout, 
	        success   : function(response)
	        {
	        	if(_grid){
	        		_grid.setLoading(false);
	        	}
	        	_store.removeAll();
	            var jsonResponse = Ext.decode(response.responseText);
	            
	            /*se crean los datos en otro store, un store temporal INICIO*/
	            debug('_store.proxy.model:',_store.proxy.model);
	            Ext.create('Ext.data.Store',
	            {
	                model     : _store.proxy.model
	                ,autoLoad : true
	                ,proxy    :
	                {
	                    type    : 'memory'
	                    ,reader : 'json'
	                    ,data   : jsonResponse[_root]
	                }
	                ,listeners :
	                {
	                    load : function(me,records)
	                    {
	                        debug('records storeTmp:',records);
	                        _store.datos = records;
	                        _store.setProxy({
	                            type          : 'memory'
	                            ,enablePaging : true
	                            ,reader       : 'json'
	                            ,data         : records
	                        });
	                        _store.load();
	                        _store.commitChanges();
	                    }
	                }
	            })
	            /*se crean los datos en otro store, un store temporal FIN*/
	        },
	        failure   : function()
	        {
	        	if(_grid){
	        		_grid.setLoading(false);
	        	}
	        	_store.removeAll();
	            Ext.Msg.show({
	                title   : 'Error',
	                icon    : Ext.Msg.ERROR,
	                //msg     : 'Error cargando los datos de ' + _url,
	                msg     : 'Error al obener los datos, intente m\u00E1s tarde',
	                buttons : Ext.Msg.OK
	            });
	        }
	    });
	}
	else
	{
	    setTimeout(function()
	    {
	        /* jtezva
	        En esta parte filtramos los records, los records ya están cargados en store.datos, solo los filtramos con
	        la funcion recibida, la funcion recibe el records y regresa true/false, con false se quita, con true se incluye
	        */
	        _store.removeAll();
	        var records   = _store.datos;
	        var incluidos = [];
	        for(var i in records)
	        {
	            var rec = records[i];
     	        if(filterFn(rec))
	            {
	                incluidos.push(rec);
	            }
	        }
	        _store.setProxy({
	            type          : 'memory'
	            ,enablePaging : true
	            ,reader       : 'json'
	            ,data         : incluidos
	        });
	        _store.load();
	        if(_grid)
	        {
	            _grid.setLoading(false);
	        }
	    },300);
	}
}


/**
 * Maneja los errores en un submit de un formulario e invoca la funci�n callback enviada como parametro
 *
 * @param {Ext.form.Basic} form Formulario que solicit� la acci�n
 * @param {Ext.form.action.Action} action El objeto Action que ejecut� la operaci�n
 * @param {Function} clbkFn Funcion callback a ejecutar
 */
function manejaErrorSubmit(form, action, clbkFn) {
    var msgServer = Ext.isEmpty(action.result.errorMessage) ? 'Error interno del servidor, consulte a soporte' : action.result.errorMessage;
    try {
    	debug('action=',action);
    	debug('action.failureType=', action.failureType);
        switch (action.failureType) {
        case Ext.form.action.Action.CONNECT_FAILURE:
            Ext.Msg.show({title: 'Error', msg: 'Error de comunicaci&oacute;n', buttons: Ext.Msg.OK, icon: Ext.Msg.ERROR});
            break;
        case Ext.form.action.Action.SERVER_INVALID:
        case Ext.form.action.Action.LOAD_FAILURE:
            if(clbkFn) {
                clbkFn(form,action);
            } else {
                Ext.Msg.show({title: 'Error', msg: msgServer, buttons: Ext.Msg.OK, icon: Ext.Msg.ERROR});
            }
            break;
        }
    } catch(err) {
        debug(err);
        Ext.Msg.show({title: 'Error', msg: msgServer, buttons: Ext.Msg.OK, icon: Ext.Msg.ERROR});
    }
}
