Ext.define('MenuPrincipalModelo',
{
    extend  : 'Ext.data.Model'
    ,config :
    {
        fields :
        [
            {  name : 'text' , type : 'string'  }
            ,{ name : 'href' , type : 'string'  }
            ,{ name : 'leaf' , type : 'boolean' } 
        ]
    }
});