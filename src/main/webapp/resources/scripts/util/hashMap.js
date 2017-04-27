/**
*	Implementación javascript de un Map similar al Map en Java
*
*	@return 
*
*/
function Map()
{
    this.keyArray = new Array(); // Array de Claves
    this.valArray = new Array(); // Array de Valores
        
    Array.prototype.removeAt = this.removeAt;
}
Map.prototype = {
	put: function ( key, val ) 
	{
	    var elementIndex = this.findIt( key );
	    
	    if( elementIndex == (-1) )
	    {
	        this.keyArray.push( key );
	        this.valArray.push( val );
	    }
	    else
	    {
	        this.valArray[ elementIndex ] = val;
	    }
	},
	
	get: function ( key )
	{
	    var result = null;
	    var elementIndex = this.findIt( key );
	
	    if( elementIndex != (-1) )
	    {   
	        result = this.valArray[ elementIndex ];
	    }  
	    
	    return result;
	},
	
	remove: function ( key )
	{
	    var result = null;
	    var elementIndex = this.findIt( key );
	
	    if( elementIndex != (-1) )
	    {
	        this.keyArray = this.keyArray.removeAt(elementIndex);
	        this.valArray = this.valArray.removeAt(elementIndex);
	    }  
	    
	    return ;
	},
	
	size: function ()
	{
	    return (this.keyArray.length);  
	},
	
	clear: function ()
	{
	    for( var i = 0; i < this.keyArray.length; i++ )
	    {
	        this.keyArray.pop(); this.valArray.pop();   
	    }
	},
	
	keySet: function ()
	{
	    return (this.keyArray);
	},
	
	valSet: function ()
	{
	    return (this.valArray);   
	},
	
	showMap: function ()
	{
	    var result = "";
	    
	    for( var i = 0; i < this.keyArray.length; i++ )
	    {
	        result += "Clave: " + this.keyArray[ i ] + "\tValores: " + this.valArray[ i ] + "\n";
	    }
	    return result;
	},
	
	findIt: function ( key )
	{
	    var result = (-1);
	
	    for( var i = 0; i < this.keyArray.length; i++ )
	    {
	        if( this.keyArray[ i ] == key )
	        {
	            result = i;
	            break;
	        }
	    }
	    return result;
	},
	
	removeAt: function ( index )
	{
	  var part1 = this.slice( 0, index);
	  var part2 = this.slice( index+1 );
	
	  return( part1.concat( part2 ) );
	}
}
