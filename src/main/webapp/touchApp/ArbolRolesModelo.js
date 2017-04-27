Ext.define('ArbolRolesModelo',
{
     extend  : 'Ext.data.Model'
     ,config :
     {
         fields   :
         [
             {name  : 'serialVersionUID' , type: 'int'     }
             ,{name : 'id'               , type: 'string'  }
             ,{name : 'text'             , type: 'string'  }
             ,{name : 'codigoObjeto'     , type: 'string'  }
             ,{name : 'leaf'             , type: 'boolean' }
             ,{name : 'allowDelete'      , type: 'boolean' }
             ,{name : 'expanded'         , type: 'boolean' }
             ,{name : 'nick'             , type: 'string'  }
             ,{name : 'name'             , type: 'string'  }
             ,{name : 'claveRol'         , type: 'string'  }
             ,{name : 'dsRol'            , type: 'string'  }
             ,{name : 'cdElemento'       , type: 'string'  }
         ]
     }
 });