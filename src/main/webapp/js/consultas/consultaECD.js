Ext.onReady(function() {
    


    ////// modelos //////
    Ext.define('_modelo_ECD',
    {
        extend  : 'Ext.data.Model'
        ,fields :   [
        	        	 'CDPERSON'
        	        	,'CDICD'
        	        	,'DSICD'
        	        	,'PERFIL'
        	        ]
    });
    ////// modelos //////
    
    ////// stores //////
    _p34_storePolizas=Ext.create('Ext.data.Store',
    {
        model     : '_modelo_ECD'
        ,autoLoad : true
        ,proxy    :
        {
            type         : 'ajax'
            ,url         : _url_obtiene_perfil_medico
            ,extraParams :
            {
                'params.cdperson' : _parametros.cdperson
            }
            ,reader      :
            {
                type             : 'json'
                ,root            : 'list'
                ,successProperty : 'success'
                ,messageProperty : 'message'
            }
        }
    });
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////


Ext.create('Ext.grid.Panel', {
    title: 'Consulta de ECDs por Asegurado',
    store: _p34_storePolizas,
    columns: [
        { text: 'ICD',  dataIndex: 'CDICD', width: '20%'},
        { text: 'Perfil de ICD', dataIndex: 'PERFIL', width: '20%'},
        { text: 'Diagn\u00f3stico', dataIndex: 'DSICD', width: '60%'}
    ],
    height: 300,
    width: 750,
    //forceFit: true,
    renderTo: "_p999_divpri"
});
    
      
});                