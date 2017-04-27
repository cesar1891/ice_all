//***********
//***********
EliminaSumaAsegurada = function(storeSumaAsegurada,rec,row,sumaAseguradaForm) {
//alert("ROW"+row);
if(row!=null){
	
    var rec = storeSumaAsegurada.getAt(row);
    var codigoSumaAsegurada= rec.get('codigoCapital');                          
    var codigoSituacion=rec.get('codigoTipoSituacion');
    var eliminaNivel;
    //alert("codigo"+ rec.get('codigoCapital'));
    if(codigoSituacion != null){
    	eliminaNivel='inciso';
    }else{
    	eliminaNivel='producto';
    }                                               
}
    // get the selected items
   Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este elemento?', function(btn) {
           if(btn == 'yes'){ 
           			     
			     var conn = new Ext.data.Connection();
                 conn.request ({	
				     url:'sumaAsegurada/EliminaSumaAsegurada.action?codigoCapital='+codigoSumaAsegurada+'&nivel='+eliminaNivel,
				     waitTitle:'Espere',
					 waitMsg:'Procesando...',
			     	 callback: function (options, success, response) {			     	 		
                             if (Ext.util.JSON.decode(response.responseText).success == false) {
                               Ext.Msg.alert('Status', 'Error al eliminar el elemento');                               
                             } else {
                                 Ext.Msg.alert('Status', 'Elemento eliminado');
                                 storeSumaAsegurada.load();
                               }
                           }
        	 	}); 
        	 
        	}
       } 
      );
};      
//************
//************