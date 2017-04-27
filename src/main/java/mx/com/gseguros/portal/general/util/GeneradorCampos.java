/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.gseguros.portal.general.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;

/**
 *
 * @author Jair
 */
public class GeneradorCampos
{
	
	private static       Logger log                   = Logger.getLogger(GeneradorCampos.class);
    public static final  String namePrefix            = "parametros.pv_otvalor";
    public static final  String namePrefixAux         = "aux.otvalor";
    public static final  String namePrefixSimple      = "otvalor";
    private static final String formatoFecha          = "d/m/Y";
    private static final String xtypeDatecolumn       = "datecolumn";
    private static final int    staticFlex            = 1;
    private static final String descExcTipoCampoVacio = "El campo no tiene tipo campo (A,N,T,F,P)";
	private static final String descExcTipoCampoOtro  = "El campo tiene tipo campo incorrecto (A,N,T,F,P)";
	private static final String prefijoRutaIconos     = "/resources/fam3icons/icons/";
    private static final String formatoFechaHora      = "d/m/Y H:i";
    
    public  String idPrefix;
    private Item   items;
    private Item   fields;
    private Item   columns;
    private Item   buttons;
    private String context;
    private String cdgarant;
    private String cdramo;
    private String cdrol;
    private String cdtipsit;
    private String rutaIconos;
    
    private boolean parcial   = false;
    private boolean conEditor = false;
    private boolean conField  = true;
    private boolean conItem   = true;
    private boolean conColumn = true;
    private boolean conButton = false;
    private boolean esMovil   = false;
    private boolean auxiliar  = false; 
    
    private boolean prefixSimple = false;
    
    public GeneradorCampos(String context)
    {
    	this.context="/"+context;
    	this.rutaIconos = this.context+GeneradorCampos.prefijoRutaIconos;
    	log.debug("contexto para el generador de campos: "+this.context);
    }
    
    public GeneradorCampos(String context, boolean prefixSimple)
    {
    	this.context="/"+context;
    	this.rutaIconos = this.context+GeneradorCampos.prefijoRutaIconos;
    	log.debug("contexto para el generador de campos: "+this.context);
    	this.prefixSimple = prefixSimple;
    }
    
    /**
     * Genera componentes con la cadena (fields, columns o items) sin nombre.
     * Los objetos JSON generados van en llaves sin pertenecer a un padre ni corchetes.
     * Completo :
     * "fields : [ {...}, ... {...} ]"
     * Parcial  :
     * "{...}, ... {...}"
     */
    @Deprecated
    public void generaParcial(List<ComponenteVO> listcomp) throws Exception
    {
        this.parcial=true;
        this.genera(listcomp);
        this.parcial=false;
    }
    
    /**
     * Genera componentes
     * @param listcomp  >> la lista de componentes 
     * @param esParcial >> Genera "items : [ {},...{}]" o solo "{},{},...{}"
     * @param conField  >> Genera "{type:'date',name'fenacimi',dateFormat:'d/m/Y'}"
     * @param conItem   >> Genera "Ext.create('Ext.form.field.Textfield..."
     * @param conColumn >> Genera "{header : 'F. Nacimiento',dataIndex:'fenacimi'...}"
     * @param conEditor >> Genera dentro de column "editor : Ext.create('Ext.form.field.DateField..."
     * @param conButton >> Genera "{xtype:'button',text:'Guardar',icon:...}" o "{tooltip:'Enviar',icon....'}"
     */
    public void generaComponentes(
    		List<ComponenteVO> listcomp, boolean esParcial, boolean conField,
    		boolean conItem, boolean conColumn, boolean conEditor, boolean conButton) throws Exception
    {
    	boolean auxParcial   = this.parcial;
    	boolean auxConField  = this.conField;
    	boolean auxconItem   = this.conItem;
    	boolean auxConColumn = this.conColumn;
    	boolean auxConEditor = this.conEditor;
    	boolean auxConButton = this.conButton;
    	
    	this.parcial   = esParcial;
    	this.conField  = conField;
    	this.conItem   = conItem;
    	this.conColumn = conColumn;
    	this.conEditor = conEditor;
    	this.conButton = conButton;
    	
    	this.genera(listcomp);
    	
    	this.parcial   = auxParcial;
    	this.conField  = auxConField;
    	this.conItem   = auxconItem;
    	this.conColumn = auxConColumn;
    	this.conEditor = auxConEditor;
    	this.conButton = auxConButton;
    	
    }
    
    /**
     * Genera componentes cuyas columnas tienes editor.
     * Sirve para grids editables.
     * Genera el padre (columns, fields o items) y los corchetes que encierran los hijos.
     * "columns :
     * [
     *    {
     *        header     : 'columna'
     *        ,dataIndex : 'index'
     *        ,editor    :
     *        {
     *            xtype       : 'textfield'
     *            ,allowBlank : false
     *        }
     *    },
     *    ...
     *    {...}
     * ]"
     */
    @Deprecated
    public void generaConEditor(List<ComponenteVO> listcomp) throws Exception
    {
    	this.conEditor=true;
    	this.genera(listcomp);
    	this.conEditor=false;
    }
    
    /**
     * Genera componentes parciales cuyas columnas tienen editor.
     * Sirve para grids editables.
     * Los objetos JSON generados no tienen padre (columns, items o fields) ni estan entre corchetes.
     * "{
     *     header     : 'columna'
     *     ,dataIndex : 'index'
     *     ,editor    :
     *     {
     *         xtype       : 'textfield'
     *         ,allowBlank : false
     *     }
     * },
     * ...
     * { ... }"
     */
    @Deprecated
    public void generaParcialConEditor(List<ComponenteVO> listcomp) throws Exception
    {
    	this.parcial=true;
    	this.conEditor=true;
    	this.genera(listcomp);
    	this.parcial=false;
    	this.conEditor=false;
    }
    
    @Deprecated
    public void genera(List<ComponenteVO> listcomp) throws Exception
    {
    	String itemsKey   = null;
    	String fieldsKey  = null;
    	String columnskey = null;
    	String buttonskey = null;
    	
    	if(!this.parcial)
    	{
    		itemsKey   = "items";
    		fieldsKey  = "fields";
    		columnskey = "columns";
    		buttonskey = "buttons";
    	}
    	
    	idPrefix="idAutoGenerado_"+System.currentTimeMillis()+"_"+((long)Math.ceil((Math.random()*10000d)))+"_";
    	
        items  = new Item(itemsKey   , null , Item.ARR);
        fields = new Item(fieldsKey  , null , Item.ARR);
        columns= new Item(columnskey , null , Item.ARR);
        buttons= new Item(buttonskey , null , Item.ARR);
        
        if(listcomp!=null&&!listcomp.isEmpty())
        {
            for(int i=0;i<listcomp.size();i++)
            {
                this.generaCampoYFieldYColumn(listcomp, listcomp.get(i), i);
            }
        }
        
        if(this.conField)
        {
        	//log.debug(fields.toString());
        }
        if(this.conItem)
        {
        	//log.debug(items.toString());
        }
        if(this.conColumn)
        {
        	//log.debug(columns.toString());
        }
        if(this.conButton)
        {
        	//log.debug(buttons.toString());
        }
    }
    
    /**
     * Genera un item para formulario o un editor para grid segun parametro de entrada esEditor
     */
    private Item generaItem(List<ComponenteVO> listcomp, ComponenteVO comp, Integer idx, boolean esEditor) throws Exception
    {
    	boolean esCombo   = StringUtils.isNotBlank(comp.getCatalogo());
    	String  tipoCampo = comp.getTipoCampo();
    	
        Item item=new Item();
        item.setType(Item.OBJ);
        
        if(StringUtils.isNotBlank(comp.getDefaultValue()))
        {
        	//tipoCampo = ComponenteVO.TIPOCAMPO_ALFANUMERICO;
        	//esCombo   = false;
        	comp.setSoloLectura(true);
        	comp.setValue("'"+comp.getDefaultValue()+"'");
        }
        
        ////// Ext.create('Ext...',{}) //////
        if(esCombo)
        {
        	if(!esMovil)
        	{
        		item.setComposedName("Ext.create('Ext.form.ComboBox',{");
        	}
        	else
        	{
        		item.setComposedName("Ext.create('MiSelectField',{");
        	}
        }
        else if(tipoCampo.equals(ComponenteVO.TIPOCAMPO_ALFANUMERICO))
        {
        	if(!esMovil)
        	{
        		item.setComposedName("Ext.create('Ext.form.TextField',{");
        	}
        	else
        	{
        		item.setComposedName("Ext.create('Ext.field.Text',{");
        	}
        }
        else if(tipoCampo.equals(ComponenteVO.TIPOCAMPO_TEXTAREA))
        {
        	if(!esMovil)
        	{
        		item.setComposedName("Ext.create('Ext.form.TextArea',{");
        	}
        	else
        	{
        		item.setComposedName("Ext.create('Ext.field.TextArea',{");
        	}
        }
        else if(tipoCampo.equals(ComponenteVO.TIPOCAMPO_NUMERICO)||tipoCampo.equals(ComponenteVO.TIPOCAMPO_PORCENTAJE))
        {
        	if(!esMovil)
        	{
        		item.setComposedName("Ext.create('Ext.form.NumberField',{");
        	}
        	else
        	{
        		item.setComposedName("Ext.create('Ext.field.Number',{");
        	}
        }
        else if(tipoCampo.equals(ComponenteVO.TIPOCAMPO_FECHA)||tipoCampo.equals(ComponenteVO.TIPOCAMPO_FECHA_HORA))
        {
        	if(!esMovil)
        	{
        		item.setComposedName("Ext.create('Ext.form.DateField',{");
        	}
        	else
        	{
        		item.setComposedName("Ext.create('Ext.field.DatePicker',{");
        		item.add(Item.crear("picker",null,Item.OBJ).add("yearFrom",1940));
        	}
        }
        item.setComposedNameClose("})");
        ////// Ext.create('Ext...',{}) //////
        
        if(esMovil||comp.isLabelTop())
        {
        	item.add("labelAlign","top");
        }
        
        ////// id, cdatribu, fieldLabel, allowBlank, name, readOnly, value, hidden, style //////
        String auxIdEditor = "";
        if(esEditor)
        {
        	auxIdEditor = "editor_";
        }
        String compId = this.idPrefix+auxIdEditor+idx; 
        
        String cdatribu = comp.getNameCdatribu();
        if(cdatribu==null)
        {
        	cdatribu = "";
        }
        
        String fieldLabel = comp.getLabel();
        if(fieldLabel==null||esEditor)
        {
        	fieldLabel = "";
        }
        
        String name = comp.getNameCdatribu();
        if(name==null)
        {
        	name = "";
        }
        if(comp.isFlagEsAtribu())
        {
        	int size = 2;
    		if(comp.getType()==ComponenteVO.TIPO_TATRIGAR)
    		{
    			size=3;
    		}
        	name = StringUtils.leftPad(name,size,"0");
        	name = (prefixSimple
        			? GeneradorCampos.namePrefixSimple
        			: auxiliar
        				? GeneradorCampos.namePrefixAux
        				: GeneradorCampos.namePrefix
        	) + name;
        }
        
        String value=comp.getValue();
        
        item.add("id"              , compId);
        item.add("cdatribu"        , cdatribu);
        item.add("fieldLabel"      , fieldLabel);
        item.add("label"           , fieldLabel);
        item.add("orden"           , comp.getOrden());
        item.add("allowBlank"      , !comp.isObligatorio());
        item.add("name"            , name);
        item.add("readOnly"        , comp.isSoloLectura());
        item.add("swobligaflot"    , comp.isObligatorioFlot());
        item.add("swobligaemiflot" , comp.isObligatorioEmiFlot());
        if(StringUtils.isNotBlank(comp.getAuxiliar()))
        {
        	item.add("auxiliar" , comp.getAuxiliar());
        }
        if(StringUtils.isNotBlank(value))
        {
        	item.add(Item.crear("valorInicial" , value).setQuotes(""));
        	item.add(Item.crear("value"        , value).setQuotes(""));
        }
        item.add("hidden"     , comp.isOculto());
        if(!esMovil)
        {
        	item.add("style"      , "margin:5px");
        }
        if(comp.getWidth()>0)
        {
        	item.add("width" , comp.getWidth());
        }
        ////// id, cdatribu, fieldLabel, allowBlank, name, readOnly, value, hidden, style //////
        
        ////// format //////
        if(tipoCampo.equals(ComponenteVO.TIPOCAMPO_FECHA))
        {
        	item.add("format",GeneradorCampos.formatoFecha);
        }
        else if(tipoCampo.equals(ComponenteVO.TIPOCAMPO_FECHA_HORA))
        {
        	item.add("format",GeneradorCampos.formatoFechaHora);
        }
        ////// format //////
        
        ////// decimales //////
        if(tipoCampo.equals(ComponenteVO.TIPOCAMPO_PORCENTAJE))
		{
        	item.add("allowDecimals",true);
        	item.add("decimalSeparator",".");
		}
        ////// decimales //////
        
        ////// numericos no menores a cero //////
        if(tipoCampo.equals(ComponenteVO.TIPOCAMPO_NUMERICO)||tipoCampo.equals(ComponenteVO.TIPOCAMPO_PORCENTAJE))
        {
        	if(StringUtils.isBlank(comp.getMinValue())&&!comp.isMenorCero())
        	{
        		item.add("minValue",0);
        	}
        	else if(StringUtils.isNotBlank(comp.getMinValue()))
        	{
        		item.add("minValue",Double.parseDouble(comp.getMinValue()));
        	}
        	if(comp.getMaxValue()!=null)
        	{
        		item.add("maxValue",comp.getMaxValue());
        	}
        }
        ////// numericos no menores a cero //////
        
        ////// minLength, maxLength //////
        if((tipoCampo.equals(ComponenteVO.TIPOCAMPO_ALFANUMERICO)
        		||tipoCampo.equals(ComponenteVO.TIPOCAMPO_TEXTAREA)
        		||tipoCampo.equals(ComponenteVO.TIPOCAMPO_NUMERICO)
        		||tipoCampo.equals(ComponenteVO.TIPOCAMPO_PORCENTAJE)
        		)&&!esCombo
        		)
        {
	        if(comp.isFlagMinLength())
	        {
	        	item.add("minLength" , comp.getMinLength());
	        }
	        if(comp.isFlagMaxLength())
	        {
	        	item.add("maxLength" , comp.getMaxLength());
	        }
        }
        ////// minLength, maxLength //////
        
        ////// es padre //////
        boolean esPadre = idx<listcomp.size()-1&&listcomp.get(idx+1).isDependiente();
        if(esPadre&&!esCombo)//si es combo no se pone porque se sobreescribe
        {
        	this.agregarHerenciaPadre(listcomp,item,idx,esEditor);
        }
        ////// es padre //////
        
        ////// combo //////
        if(esCombo)
        {
        	boolean esHijo          = comp.isDependiente();
        	boolean esAutocompleter = StringUtils.isNotBlank(comp.getQueryParam());
        	boolean esComboVacio    = comp.isComboVacio();
        	
        	////// typeAhead, displayField, valueField, matchFieldWidth //////
        	item.add("typeAhead"       , true);
        	item.add("anyMatch"        , true);
        	item.add("displayField"    , "value");
            item.add("valueField"      , "key");
            item.add("matchFieldWidth" , false);
            item.add(new Item("listConfig" , "{maxHeight:150,minWidth:120}").setQuotes(""));
            ////// typeAhead, displayField, valueField, matchFieldWidth //////
        	
        	////// forceSelection, editable //////
            boolean editable = false;
            if(esAutocompleter||esComboVacio)
            {
            	editable = true;
            }
            item.add("forceSelection" , editable);
            item.add("editable"       , editable);
        	////// forceSelection, editable //////
        	
        	////// queryMode //////
            String queryMode = "local";
            if(esAutocompleter)
            {
            	queryMode = "remote";
            }
        	item.add("queryMode", queryMode);
        	////// queryMode //////
        	
        	////// herencia //////
        	boolean listeners = false;
        	if(esPadre)
            {
            	if(esHijo)
            	{
            		this.agregarHerenciaPadreHijo(listcomp, item, idx, esEditor);
            	}
            	else
            	{
            		this.agregarHerenciaPadre(listcomp, item, idx, esEditor);
            	}
            	listeners=true;
            }	
            if(esHijo)
            {
            	String auxCompAnteIdEditor = "";
                if(esEditor)
                {
                	auxCompAnteIdEditor = "editor_";
                }
                String compAnteriorId = this.idPrefix+auxCompAnteIdEditor+(idx-1);
                
                item.add("anidado" , true);
                
                //it.add(Item.crear("forceSelection",false));??
                item.add(Item.crear(""
                		+ "heredar",
                		  "function(remoto,icallback)"
                		+ "{"
                		+ "    /*debug('Heredar "+name+"','icallback:',icallback?true:false);*/"
                		+ "    me = this;"
                		+ "    if(!me.noEsPrimera||remoto==true)"
                		+ "    {"
                		+ "        /*debug('Hereda por primera vez o porque la invoca el padre');*/"
                		+ "        me.noEsPrimera=true;"
                		+ "        me.getStore().load("
                		+ "        {"
                		+ "            params    :"
                		+ "            {"
                		+ "                'params.idPadre':Ext.getCmp('"+compAnteriorId+"').getValue()"
                		+ "            }"
                		+ "            ,callback : function()"
                		+ "            {"
                		+ "                var thisCmp=Ext.getCmp('"+compId+"');"
                		+ "                var valorActual=thisCmp.getValue();"
                		+ "                var dentro=false;"
                		+ "                thisCmp.getStore().each(function(record)"
                		+ "                {"
                		+ "                    if(valorActual==record.get('key'))"
                		+ "                    {"
                		+ "                        dentro=true;"
                		+ "                    }"
                		+ "                });"
                		+ "                if(!dentro)"
                		+ "                {"
                		+ "                    thisCmp.clearValue();"
                		+ "                }"
                		+ "                if(icallback)"
                		+ "                {"
                		+ "                    icallback(thisCmp);"
                		+ "                }"
                		+ "            }"
                		+ "        });"
                		+ "    }"
                		+ "    else"
                		+ "    {"
                		+ "        /*debug('No hereda porque es un change repetitivo');*/"
                		+ "    }"
                		+ "}"
                		+ (listeners?
                		  "":
                		  ",listeners:"
                		+ "{"
                		+ "    change       :"
                		+ "    {"
                		+ "        fn:function()"
                		+ "        {"
                		+ "            /*debug('change');*/"
                		+ "            if(this.heredar)"
                		+ "            {"
                		+ "                this.heredar();"
                		+ "            }"
                		+ "            else"
                		+ "            {"
                		+ "                try { _g_heredarCombo(false,'"+compId+"','"+compAnteriorId+"'); }catch(e){ debugError(e); }"
                		+ "            }"
                		+ "        }"
                		+ "    }"
                		+ "}")
                		).setQuotes(""));
            }
            ////// herencia //////
            
            ///////////////////
            ////// store //////
            //////       //////
            if(comp.getCatalogo().indexOf("SINO")>0)
            {
            	item.add(new Item("store","_g_storeSino").setQuotes(""));
            }
            else
            {
	            Item store=new Item(null,null,Item.OBJ,"store:Ext.create('Ext.data.Store',{","})");
	            item.add(store);
	            store.add("model","Generic");
	            
	            ////// autoLoad //////
	            boolean autoLoad = true;
	            if(esHijo||esAutocompleter||comp.isSinCargar())
	            {
	            	autoLoad=false;
	            }
	            store.add("autoLoad",autoLoad);
	            ////// autoLoad //////
	            
	            ///////////////////
	            ////// proxy //////
	            Item proxy=new Item("proxy",null,Item.OBJ);
	            store.add(proxy);
	            proxy.add("type","ajax");
	            proxy.add("url",this.context+"/catalogos/obtieneCatalogo.action?tstamp="+(System.currentTimeMillis())+"_"+((int)(10000*Math.random())));
	            proxy.add(
	            		Item.crear("reader", null, Item.OBJ)
	            		.add("type","json")
	            		.add("root","lista")
	            		.add("rootProperty","lista")
	            		);
	            
	            ////// extraParams //////
	            if(comp.getType()==ComponenteVO.TIPO_TATRISIT)
	            {
	            	proxy.add(
	                        Item.crear("extraParams" , null, Item.OBJ)
	                        .add("'params.cdatribu'" , cdatribu)
	                        .add("'params.cdtipsit'" , cdtipsit)
	                        .add("catalogo"          , Catalogos.TATRISIT.getCdTabla())
	                        );
	            }
	            else if(comp.getType()==ComponenteVO.TIPO_TATRISIN)
	            {
	            	proxy.add(
	                        Item.crear("extraParams" , null, Item.OBJ)
	                        .add("'params.cdatribu'" , cdatribu)
	                        .add("'params.cdtipsit'" , "SL")
	                        .add("catalogo"          , Catalogos.TATRISIN.getCdTabla())
	                        );
	            }
	            else if(comp.getType()==ComponenteVO.TIPO_TATRIPOL)
	            {
	            	proxy.add(
	                        Item.crear("extraParams" , null, Item.OBJ)
	                        .add("'params.cdatribu'" , cdatribu)
	                        .add("'params.cdramo'"   , StringUtils.isBlank(cdramo)?Ramo.SALUD_VITAL.getCdramo():cdramo)
	                        .add("catalogo"          , Catalogos.TATRIPOL.getCdTabla())
	                        );
	            }
	            else if(comp.getType()==ComponenteVO.TIPO_TATRIGAR)
	            {
	            	proxy.add(
	                        Item.crear("extraParams" , null, Item.OBJ)
	                        .add("'params.cdatribu'" , cdatribu)
	                        .add("'params.cdgarant'" , cdgarant)
	                        .add("'params.cdramo'" ,   cdramo==null?Ramo.SALUD_VITAL.getCdramo():cdramo)
	                        .add("'params.cdtipsit'" , cdtipsit==null?TipoSituacion.SALUD_VITAL.getCdtipsit():cdtipsit)
	                        .add("catalogo"          , Catalogos.TATRIGAR.getCdTabla())
	                        );
	            }
	            else if(comp.getType()==ComponenteVO.TIPO_TATRIPER)
	            {
	            	proxy.add(
	                        Item.crear("extraParams", null, Item.OBJ)
	                        .add("'params.cdramo'"  , cdramo)
	                        .add("'params.cdrol'"   , cdrol)
	                        .add("'params.cdatribu'", cdatribu)
	                        .add("'params.cdtipsit'", cdtipsit)
	                        .add("catalogo"         , Catalogos.TATRIPER.getCdTabla())
	                        );
	            }
	            else if(comp.getType()==ComponenteVO.TIPO_GENERICO)
	            {
	            	Item extraParams=Item.crear("extraParams", null, Item.OBJ)
	            			.add("catalogo",comp.getCatalogo());
	            	if(StringUtils.isNotBlank(comp.getParamName1()))
	            	{
	            		extraParams.add(Item.crear(comp.getParamName1(),comp.getParamValue1()).setQuotes(""));
	            	}
	            	if(StringUtils.isNotBlank(comp.getParamName2()))
	            	{
	            		extraParams.add(Item.crear(comp.getParamName2(),comp.getParamValue2()).setQuotes(""));
	            	}
	            	if(StringUtils.isNotBlank(comp.getParamName3()))
	            	{
	            		extraParams.add(Item.crear(comp.getParamName3(),comp.getParamValue3()).setQuotes(""));
	            	}
	            	if(StringUtils.isNotBlank(comp.getParamName4()))
	            	{
	            		extraParams.add(Item.crear(comp.getParamName4(),comp.getParamValue4()).setQuotes(""));
	            	}
	            	if(StringUtils.isNotBlank(comp.getParamName5()))
	            	{
	            		extraParams.add(Item.crear(comp.getParamName5(),comp.getParamValue5()).setQuotes(""));
	            	}
	            	proxy.add(extraParams);
	            }
            }
            ////// extraParams //////
            
            ////// proxy //////
            ///////////////////
            
            //////       //////
            ////// store //////
            ///////////////////
            
            ////// autocompleter //////
            if(esAutocompleter)
            {
            	item.add("hideTrigger"  , true);
            	item.add("minChars"     , 3);
            	item.add("queryParam"   , comp.getQueryParam());
            	item.add("queryCaching" , false);
            }
            ////// autocompleter //////
        }
        ////// combo //////
        
        ////// auxiliares //////
    	if(StringUtils.isNotBlank(comp.getParamName1()))
    	{
    		item.add(Item.crear("param1",comp.getParamName1()).setQuotes(""));
    	}
    	if(StringUtils.isNotBlank(comp.getParamName2()))
    	{
    		item.add(Item.crear("param2",comp.getParamName2()).setQuotes(""));
    	}
    	if(StringUtils.isNotBlank(comp.getParamName3()))
    	{
    		item.add(Item.crear("param3",comp.getParamName3()).setQuotes(""));
    	}
    	if(StringUtils.isNotBlank(comp.getParamName4()))
    	{
    		item.add(Item.crear("param4",comp.getParamName4()).setQuotes(""));
    	}
    	if(StringUtils.isNotBlank(comp.getParamName5()))
    	{
    		item.add(Item.crear("param5",comp.getParamName5()).setQuotes(""));
    	}

    	if(StringUtils.isNotBlank(comp.getParamValue1()))
    	{
    		item.add(Item.crear("value1",comp.getParamValue1()).setQuotes(""));
    	}
    	if(StringUtils.isNotBlank(comp.getParamValue2()))
    	{
    		item.add(Item.crear("value2",comp.getParamValue2()).setQuotes(""));
    	}
    	if(StringUtils.isNotBlank(comp.getParamValue3()))
    	{
    		item.add(Item.crear("value3",comp.getParamValue3()).setQuotes(""));
    	}
    	if(StringUtils.isNotBlank(comp.getParamValue4()))
    	{
    		item.add(Item.crear("value4",comp.getParamValue4()).setQuotes(""));
    	}
    	if(StringUtils.isNotBlank(comp.getParamValue5()))
    	{
    		item.add(Item.crear("value5",comp.getParamValue5()).setQuotes(""));
    	}
        ////// auxiliares //////
        
        if(comp.getType()==ComponenteVO.TIPO_TATRIPER)
        {
        	////// para identificar si pide archivo
        	if(StringUtils.isNotBlank(comp.getCodidocu()))
        	{
        		log.debug("Se agrega codidocu "+comp.getCodidocu()+" a "+comp.getNameCdatribu());
        		item.add("codidocu",comp.getCodidocu());
        	}
        	else
        	{
        		log.debug("No es documento "+comp.getNameCdatribu());
        	}
        	
        	if(StringUtils.isNotBlank(comp.getTieneDocu()))
        	{
        		log.debug("Se agrega getTieneDocu "+comp.getTieneDocu()+" a "+comp.getNameCdatribu());
        		item.add("tieneDocu",comp.getTieneDocu());
        		
        		if(StringUtils.isNotBlank(comp.getIcon())){
        			item.add("icon", Constantes.SI.equalsIgnoreCase(comp.getIcon())? "CARGADO" : "SIN_CARGAR");
        		}
        	}
        	else
        	{
        		log.debug("No es se genera campo para subir documento "+comp.getNameCdatribu());
        	}
        	////// para identificar si pide archivo
        }
        
        return item;
    }
    
    /**
     * Genera un field para Ext.data.Model
     * Ext.define('MiModelo',
     * {
     *     extend  : 'Ext.data.Model'
     *     ,fields :
     *     [
     *         <s:property value="itemFields" />
     *     ]
     * });
     * La cadena imprime:
     * {
     *     name        : 'atributo'
     *     ,type       : 'date'
     *     ,dateFormat : 'd/m/Y'
     * }
     */
    private Item generaField(List<ComponenteVO> listcomp, ComponenteVO comp, Integer idx) throws Exception
    {
    	String tipoAlfanum   = "string";
    	String tipoFecha     = "date";
    	String tipoEntero    = "int";
    	String tipoFlotante  = "float";
    	String tipoFechaHora = "date2Aux";
    	
    	String name = comp.getNameCdatribu();
    	if(comp.isFlagEsAtribu())
    	{
    		String cdatribu = comp.getNameCdatribu();
    		int size=2;
    		if(comp.getType()==ComponenteVO.TIPO_TATRIGAR)
    		{
    			size=3;
    		}
    		cdatribu = StringUtils.leftPad(cdatribu,size,"0");
    		name    = GeneradorCampos.namePrefix + cdatribu;
    	}
    	
        String type     = tipoAlfanum;
        boolean esCombo = StringUtils.isNotBlank(comp.getCatalogo()); 
        if(!esCombo)
        {
        	String tipo = comp.getTipoCampo();
        	boolean tieneTipo = StringUtils.isNotBlank(tipo);  
        	
        	if(tieneTipo)
        	{
        		if(tipo.equalsIgnoreCase(ComponenteVO.TIPOCAMPO_ALFANUMERICO))
        		{}
        		else if(tipo.equalsIgnoreCase(ComponenteVO.TIPOCAMPO_FECHA))
        		{
        			type = tipoFecha;
        		}
        		else if(tipo.equalsIgnoreCase(ComponenteVO.TIPOCAMPO_FECHA_HORA))
        		{
        			type = tipoFechaHora;
        		}
        		else if(tipo.equalsIgnoreCase(ComponenteVO.TIPOCAMPO_NUMERICO))
        		{
        			type = tipoEntero;
        		}
        		else if(tipo.equalsIgnoreCase(ComponenteVO.TIPOCAMPO_PORCENTAJE))
        		{
        			type = tipoFlotante;
        		}
        		else if(tipo.equalsIgnoreCase(ComponenteVO.TIPOCAMPO_TEXTAREA))
        		{}
        		else
        		{
        			throw new Exception(Utils.join(descExcTipoCampoOtro, ". Campo: ", comp.getLabel(), ", tipo: ", tipo));
        		}
        	}
        	else
        	{
        		throw new Exception(Utils.join(descExcTipoCampoVacio, ". Campo: ", comp.getLabel()));
        	}
        }

        Item field=new Item();
        field.setType(Item.OBJ);
        field.add("name", name);
        
        if(type.equals(tipoFechaHora))
        {
        	field.add("type", tipoFecha);
        }
        else
        {
        	field.add("type", type);
        }
        
        if(type.equals(tipoFecha))
        {
        	field.add(Item.crear("dateFormat", GeneradorCampos.formatoFecha));
        }
        else if(type.equals(tipoFechaHora))
        {
        	field.add(Item.crear("dateFormat", GeneradorCampos.formatoFechaHora));
        }
        
        return field;
    }
    
    /**
     * Genera una columna para usar en grid.
     * Ext.define('MiGrid',
     * {
     *     extend   : 'Ext.grid.Panel'
     *     ,columns :
     *     [
     *         <s:property value="itemColumn" />
     *     ]
     * });
     */
    private Item generaColumn(List<ComponenteVO> listcomp, ComponenteVO comp, Integer idx) throws Exception
    {
    	Item col = null;
    	
    	String columna = comp.getColumna();
    	boolean hayColumna = StringUtils.isNotBlank(columna)
    			&&(
    				columna.equalsIgnoreCase(Constantes.SI)
    				||columna.equalsIgnoreCase(ComponenteVO.COLUMNA_OCULTA)
    			);
    	
    	if(hayColumna)
    	{
	    	String dataIndex = comp.getNameCdatribu();
	    	if(comp.isFlagEsAtribu())
	    	{
	    		String cdatribu = comp.getNameCdatribu();
	    		int size=2;
	    		if(comp.getType()==ComponenteVO.TIPO_TATRIGAR)
	    		{
	    			size=3;
	    		}
	    		cdatribu=StringUtils.leftPad(cdatribu,size,"0");
	    		dataIndex = GeneradorCampos.namePrefix + cdatribu;
	    	}
	    	
	    	String header = comp.getLabel();
	        
	        boolean hidden  = columna.equalsIgnoreCase(ComponenteVO.COLUMNA_OCULTA);
	        String renderer = comp.getRenderer();
	        if(StringUtils.isBlank(renderer))
	        {
	        	renderer = "";
	        }
	        
	        String tipoCampo = comp.getTipoCampo();
	        boolean esFecha     = StringUtils.isNotBlank(tipoCampo)&&tipoCampo.equalsIgnoreCase(ComponenteVO.TIPOCAMPO_FECHA);
	        boolean esFechaHora = StringUtils.isNotBlank(tipoCampo)&&tipoCampo.equalsIgnoreCase(ComponenteVO.TIPOCAMPO_FECHA_HORA);
	    
		    col=new Item();
		    col.setType(Item.OBJ);
		    col.add("text"      , header);
		    col.add("dataIndex" , dataIndex);
		    col.add("orden"     , comp.getOrden());
		    if(comp.getWidth()==0)
		    {
		    	col.add("flex" , GeneradorCampos.staticFlex);
		    }
		    else
		    {
		    	col.add("width" , comp.getWidth());
		    }
		    if(esMovil)
		    {
		    	col.add("width" , 200);
		    }
		    col.add("hidden"    , hidden);
		    
		    if(StringUtils.isNotBlank(renderer))
		    {
		    	col.add(Item.crear("renderer",renderer).setQuotes(""));
		    }
		    
		    if(esFecha)
		    {
		    	col.add("xtype"  , GeneradorCampos.xtypeDatecolumn);
		    	col.add("format" , GeneradorCampos.formatoFecha);
		    }
		    else if(esFechaHora)
		    {
		    	col.add("xtype"  , GeneradorCampos.xtypeDatecolumn);
		    	col.add("format" , GeneradorCampos.formatoFechaHora);
		    }
    	}
    	
    	return col;
    }

    /**
     * Genera una boton para usar en cualquier componente.
     * Ext.define('MiGrid',
     * {
     *     extend   : 'Ext.grid.Panel'
     *     ,buttons :
     *     [
     *         <s:property value="itemButton" />
     *     ]
     * });
     */
    private Item generaButton(List<ComponenteVO> listcomp, ComponenteVO comp, Integer idx) throws Exception
    {
    	Item button = null;
    	
    	if(StringUtils.isNotBlank(comp.getLabel())){
    		
    		boolean hayColumna = StringUtils.isNotBlank(comp.getColumna()) && (comp.getColumna().equalsIgnoreCase(Constantes.SI) || comp.getColumna().equalsIgnoreCase(ComponenteVO.COLUMNA_OCULTA));
    		button=new Item();
    		button.setType(Item.OBJ);
    		
    		if(!hayColumna)
    		{
    			button.add("xtype", "button");
    		}
    		
    		button.add( hayColumna? "tooltip": "text", comp.getLabel());
    		button.add("icon"                        , this.rutaIconos+comp.getIcon());
    		button.add(Item.crear("handler"          , comp.getHandler()).setQuotes(""));
    		
    	}
    	
    	return button;
    }
    
    /**
     * Genera el field para el modelo, el item para el formulario, la columna en caso de tener S o H, y su editor
     * en caso de haber sido llamado desde generaConEditor() o desde generaParcialConEditor()
     */
    @Deprecated
    public void generaCampoYFieldYColumn(List<ComponenteVO> listcomp, ComponenteVO comp, Integer idx) throws Exception
    {
    	if(this.conField)
    	{
    		Item field  = this.generaField(listcomp, comp, idx);
    		fields.add(field);
    	}
    	
    	if(this.conItem)
    	{
    		Item item   = this.generaItem(listcomp, comp, idx, false);
    		items.add(item);    		
    	}
    	
    	if(this.conColumn)
    	{
    		Item column = this.generaColumn(listcomp, comp, idx);
    		if(column != null)
    		{
	    		if(conEditor)
	    		{
	    			Item editor = this.generaItem(listcomp, comp, idx, true);
	    			column.add("editor",editor);
	    			boolean esCombo = StringUtils.isNotBlank(comp.getCatalogo());
	    			if(esCombo)
	    			{
	    				column.add(Item.crear("renderer","function(value,metadata,record,row,col,store,view){"
	    						+ "return rendererDinamico(value,Ext.getCmp('"+(this.idPrefix+"editor_"+idx)+"'),view);"
	    						+ "}").setQuotes(""));
	    			}
	    		}
	    		columns.add(column);
    		}
    	}
    	
    	if(this.conButton)
    	{
	        Item button = this.generaButton(listcomp, comp, idx);
	        buttons.add(button);
    	}
    }

    /**
     * Agrega escuchadores.
     * blur : invoca herencia de su hijo.
     * @param lt
     * @param it
     * @param idx
     * @param editor
     * @throws Exception
     */
    private void agregarHerenciaPadre(List<ComponenteVO> lt, Item it, Integer idx, boolean editor) throws Exception
    {
    	it.add(Item.crear(""
    			+ "listeners",
    			  "{"
    			+ "    blur:"
    			+ "    {"
    			+ "        fn:function()"
    			+ "        {"
    			+ "            /*debug('blur',Ext.getCmp('"+this.idPrefix+(editor?"editor_":"")+(idx+1)+"'));*/"
    		    + "            if(Ext.getCmp('"+this.idPrefix+(editor?"editor_":"")+(idx+1)+"').heredar)"
    		    + "            {"
    			+ "                Ext.getCmp('"+this.idPrefix+(editor?"editor_":"")+(idx+1)+"').heredar(true);"
    			+ "            }"
    			+ "            else"
    			+ "            {"
    			+ "                try{ _g_heredarCombo(true,'"+this.idPrefix+(editor?"editor_":"")+(idx+1)+"','"+this.idPrefix+(editor?"editor_":"")+(idx)+"'); }catch(e){ debugError(e); }"
    			+ "            }"
    			+ "        }"
    			+ "    }"
    			+ "}")
    			.setQuotes(""));
	}
    
    /**
     * Agrega escuchadores.
     * blur        : invoca herencia de su hijo.
     * change      : invoca su propia herencia.
     * afterrender : invoca su propia herencia.
     * @param lt
     * @param it
     * @param idx
     * @param editor
     * @throws Exception
     */
    private void agregarHerenciaPadreHijo(List<ComponenteVO> lt, Item it, Integer idx, boolean editor) throws Exception
    {
    	it.add(Item.crear(""
    			+ "listeners",
    			  "{"
    			+ "    blur         :"
    			+ "    {"
    			+ "        fn:function()"
    			+ "        {"
    			+ "            /*debug('blur');*/"
    			+ "            if(Ext.getCmp('"+this.idPrefix+(editor?"editor_":"")+(idx+1)+"').heredar)"
    			+ "            {"
    			+ "                Ext.getCmp('"+this.idPrefix+(editor?"editor_":"")+(idx+1)+"').heredar(true);"
    			+ "            }"
    			+ "            else"
    			+ "            {"
    			+ "                try{ _g_heredarCombo(true,'"+this.idPrefix+(editor?"editor_":"")+(idx+1)+"','"+this.idPrefix+(editor?"editor_":"")+(idx)+"'); }catch(e){ debugError(e); }"
    			+ "            }"
    			+ "        }"
    			+ "    }"
    			+ "    ,change      :"
    			+ "    {"
    			+ "        fn : function()"
    			+ "        {"
    			+ "            /*debug('change');*/"
    			+ "            if(this.heredar)"
    			+ "            {"
    			+ "                this.heredar();"
    			+ "            }"
    			+ "            else"
    			+ "            {"
    			+ "                try{ _g_heredarCombo(true,'"+this.idPrefix+(editor?"editor_":"")+(idx)+"','"+this.idPrefix+(editor?"editor_":"")+(idx-1)+"'); }catch(e){ debugError(e); }"
    			+ "            }"
    			+ "        }"
    			+ "    }"
    			+ "}")
    			.setQuotes(""));
	}

    /////////////////////////////////
    ////// getters and setters //////
    /*/////////////////////////////*/
	public Item getItems() {
        return items;
    }

    public void setItems(Item items) {
        this.items = items;
    }

    public Item getFields() {
        return fields;
    }

    public void setFields(Item fields) {
        this.fields = fields;
    }

	public String getCdgarant() {
		return cdgarant;
	}

	public void setCdgarant(String cdgarant) {
		this.cdgarant = cdgarant;
	}

	public String getCdramo() {
		return cdramo;
	}

	public void setCdramo(String cdramo) {
		this.cdramo = cdramo;
	}

	public String getCdrol() {
		return cdrol;
	}

	public void setCdrol(String cdrol) {
		this.cdrol = cdrol;
	}

	public String getCdtipsit() {
		return cdtipsit;
	}

	public void setCdtipsit(String cdtipsit) {
		this.cdtipsit = cdtipsit;
	}

	public Item getColumns() {
		return columns;
	}

	public void setColumns(Item columns) {
		this.columns = columns;
	}
	
	public Item getButtons() {
		return buttons;
	}

	public void setButtons(Item buttons) {
		this.buttons = buttons;
	}

	public boolean isEsMovil() {
		return esMovil;
	}

	public void setEsMovil(boolean esMovil) {
		this.esMovil = esMovil;
	}

	public boolean isAuxiliar() {
		return auxiliar;
	}

	public void setAuxiliar(boolean auxiliar) {
		this.auxiliar = auxiliar;
	}
    
}