/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.gseguros.portal.cotizacion.model;

import java.util.ArrayList;

/**
 *
 * @author Jair
 */
public class Item
{
    public static final int OBJ=0;
    public static final int ARR=1;
    public static final int ATR=2;
    
    private String key=null;
    private Object value=null;
    private int type=Item.ATR;
    private Item parent=null;
    private String composedName=null;
    private String composedNameClose=null;
    private String quotes="'";
    
    public Item()
    {
    }
    
    public Item(String key)
    {
        this.key=key;
    }
    
    public Item(String key, Object value)
    {
        this.key=key;
        this.value=value;
    }
    
    public Item(String key, Object value, int type)
    {
        this.key=key;
        this.value=value;
        this.type=type;
    }
    
    public Item(String key, Object value, int type,String composedName,String composedNameClose)
    {
        this.key=key;
        this.value=value;
        this.type=type;
        this.composedName=composedName;
        this.composedNameClose=composedNameClose;
    }
    
    public static Item crear()
    {
        return new Item();
    }
    
    public static Item crear(String key)
    {
        return new Item(key);
    }
    
    public static Item crear(String key, Object value)
    {
        return new Item(key,value);
    }
    
    public static Item crear(String key, Object value, int type)
    {
        return new Item(key,value,type);
    }
    
    public static Item crear(String key, Object value, int type,String composedName,String composedNameClose)
    {
        return new Item(key,value,type,composedName,composedNameClose);
    }
    
    public Item add(String key)throws Exception
    {
        return this.add(new Item(key));
    }
    
    public Item add(String key,Object value)throws Exception
    {
        return this.add(new Item(key,value));
    }
    
    public Item add(String key,Object value,int type)throws Exception
    {
        return this.add(new Item(key,value,type));
    }
    
    public Item add(String key,Object value,int type,String composedName,String composedNameClose)throws Exception
    {
        return this.add(new Item(key,value,type,composedName,composedNameClose));
    }
    
    public Item add(Item item) throws Exception
    {
        if(this.type==Item.ARR)//array
        {
            if(item.getType()!=Item.OBJ)
            {
                throw new Exception("No se puede agregar hijo "+item.getKey()+" en array "+this.key);
            }
            if(this.value==null)
            {
                this.value=new ArrayList<Item>(0);
            }
            ((ArrayList)this.value).add(item);
            item.setParent(this);
        }
        else if(this.type==Item.OBJ)//object
        {
            if(this.value==null)
            {
                this.value=item;
                item.setParent(this);
            }
            else if(
                this.value.getClass().equals(String.class)
                ||this.value.getClass().equals(Integer.class)
                ||this.value.getClass().equals(Float.class)
                ||this.value.getClass().equals(Double.class))
            {
                throw new Exception("No se puede agregar hijo "+item.getKey()+" en objeto "+this.key);
            }
            else if(this.value.getClass().equals(Item.class))
            {
                Item aux=Item.crear(
                		((Item)this.value).getKey(),
                		((Item)this.value).getValue(),
                		((Item)this.value).getType(),
                		((Item)this.value).getComposedName(),
                		((Item)this.value).getComposedNameClose()).setQuotes(((Item)this.value).getQuotes());
                this.value=new ArrayList<Item>(0);
                ((ArrayList)this.value).add(aux);
                ((ArrayList)this.value).add(item);
                item.setParent(this);
            }
            else if(this.value.getClass().equals(ArrayList.class))
            {
                ((ArrayList<Item>)this.value).add(item);
                item.setParent(this);
            }
        }
        else//attribute
        {
            throw new Exception("No se puede agregar hijo "+item.getKey()+" en atributo "+this.key);
        }
        return this;
    }
    
    @Override
    public String toString()
    {
        String s=null;
        if(this.type==Item.ATR)//xtype:formfield
        {
	        if(this.value.getClass().equals(String.class))
	        {
	            s=this.key+":"+this.quotes+this.value+this.quotes;
	        }
	        else
	        {
	            s=this.key+":"+this.value;
	        }
        }
        else if(this.type==Item.ARR)//items:[...]
        {
        	s="";
        	/////////////////////////////////////////////////
        	////// habilita que se impriman solo hijos //////
        	//////                                     //////
        	if(this.key!=null)
        	{
                s+=this.key+":[\n";
        	}
        	//////                                     //////
        	////// habilita que se impriman solo hijos //////
        	/////////////////////////////////////////////////
            if(this.value!=null)
            {
                for(int i=0;i<((ArrayList)this.value).size();i++)
                //imprimir objetos hijo
                {
                    s+=((ArrayList)this.value).get(i).toString();
                    if(i!=((ArrayList)this.value).size()-1)
                    {
                        s+=",";
                    }
                    s+="\n";
                }
            }
            /////////////////////////////////////////////////
        	////// habilita que se impriman solo hijos //////
        	//////                                     //////
        	if(this.key!=null)
        	{
        		s+="]";
        	}
        	//////                                     //////
        	////// habilita que se impriman solo hijos //////
        	/////////////////////////////////////////////////
        }
        else if(this.type==Item.OBJ)
        {
            if(this.parent!=null&&this.parent.type==Item.ARR)
            {
                if(this.composedName!=null&&this.composedName.length()>0)
                {
                    s=this.composedName+"\n";//(parent)items:[    store:Ext.create('Ext.data.Store',{...})
                    if(this.value!=null)
                    {
                        if(this.value.getClass().equals(ArrayList.class))
                        {
                            for(int i=0;i<((ArrayList)this.value).size();i++)
                            //imprimir objetos,atributos y arrays hijo
                            {
                                s+=((ArrayList)this.value).get(i).toString();
                                if(i!=((ArrayList)this.value).size()-1)
                                {
                                    s+=",";
                                }
                                s+="\n";
                            }
                        }
                        else//un solo hijo
                        {
                            s+=this.value.toString();
                        }
                    }
                    s+=this.composedNameClose;
                }
                else//(parent)items:[    {...}
                {
                    s="{";
                        if(this.value!=null)
                        {
                            if(this.value.getClass().equals(ArrayList.class))
                            {
                                for(int i=0;i<((ArrayList)this.value).size();i++)
                                //imprimir objetos,atributos y arrays hijo
                                {
                                    s+=((ArrayList)this.value).get(i).toString();
                                    if(i!=((ArrayList)this.value).size()-1)
                                    {
                                        s+=",";
                                    }
                                    s+="\n";
                                }
                            }
                            else//un solo hijo
                            {
                                s+=this.value.toString();
                            }
                        }
                    s+="}";
                }
            }
            else//parent object o no parent
            {
                if(this.composedName!=null&&this.composedName.length()>0)
                {
                    s=this.composedName+"\n";//(parent)form:{    store:Ext.create('Ext.data.Store',{...})
                    if(this.value!=null)
                    {
                        if(this.value.getClass().equals(ArrayList.class))
                        {
                            for(int i=0;i<((ArrayList)this.value).size();i++)
                            //imprimir objetos,atributos y arrays hijo
                            {
                                s+=((ArrayList)this.value).get(i).toString();
                                if(i!=((ArrayList)this.value).size()-1)
                                {
                                    s+=",";
                                }
                                s+="\n";
                            }
                        }
                        else
                        {
                            s+=this.value.toString();
                        }
                    }
                    s+=this.composedNameClose;
                }
                else//(parent)form:{    layout:{...}
                {
                    s=this.key+":{";
                    if(this.value!=null)
                    {
                        if(this.value.getClass().equals(ArrayList.class))
                        {
                            for(int i=0;i<((ArrayList)this.value).size();i++)
                            //imprimir objetos,atributos y arrays hijo
                            {
                                s+=((ArrayList)this.value).get(i).toString();
                                if(i!=((ArrayList)this.value).size()-1)
                                {
                                    s+=",";
                                }
                                s+="\n";
                            }
                        }
                        else
                        {
                            s+=this.value.toString();
                        }
                    }
                    s+="}";
                }
            }
        }
        return s;
    }
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Item getParent() {
        return parent;
    }

    public void setParent(Item parent) {
        this.parent = parent;
    }

    public String getComposedName() {
        return composedName;
    }

    public void setComposedName(String composedName) {
        this.composedName = composedName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getComposedNameClose() {
        return composedNameClose;
    }

    public void setComposedNameClose(String composedNameClose) {
        this.composedNameClose = composedNameClose;
    }

	public String getQuotes() {
		return quotes;
	}

	public Item setQuotes(String quotes) {
		this.quotes = quotes;
		return this;
	}
    
}