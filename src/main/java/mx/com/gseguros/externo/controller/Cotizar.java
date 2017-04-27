package mx.com.gseguros.externo.controller;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;

public class Cotizar
{
	private static SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	
    public Object[] cotizar(Map<String,String>smap1,
        List<Map<String,String>>slist1,
        Map<String,String>sesion,
        Object logger,
        Object manager) throws Exception
    {
    	Object[] result            = new Object[2];
        Method infoMethod          = logger.getClass().getMethod("info", Object.class);
        Method errorMethod         = logger.getClass().getMethod("error",new Class<?>[]{Object.class,Throwable.class});
        Method procedureVoidCall   = manager.getClass().getMethod("procedureVoidCall"  ,new Class<?>[]
        		{String.class,LinkedHashMap.class,String[].class});
        Method procedureMapCall    = manager.getClass().getMethod("procedureMapCall"   ,new Class<?>[]
        		{String.class,LinkedHashMap.class,String[].class});
        Method procedureParamsCall = manager.getClass().getMethod("procedureParamsCall",new Class<?>[]
        		{String.class,LinkedHashMap.class,String[].class,String[].class,String[].class});
        Method procedureListCall   = manager.getClass().getMethod("procedureListCall"  ,new Class<?>[]
        		{String.class,LinkedHashMap.class,String[].class});
        Method procedureMixedCall  = manager.getClass().getMethod("procedureMixedCall" ,new Class<?>[]
        		{String.class,LinkedHashMap.class,String[].class});
        infoMethod.invoke(logger,""
        		+ "\n#####################"
        		+ "\n###### externo ######"
        		);
        
        infoMethod.invoke(logger,">DATOS DE ENTRADA");
        infoMethod.invoke(logger,"smap1: "+smap1);
        infoMethod.invoke(logger,"slist1: "+slist1);
        infoMethod.invoke(logger,"sesion: "+sesion);
        infoMethod.invoke(logger,"<DATOS DE ENTRADA");
        
        infoMethod.invoke(logger,">VARIABLES");
        Date fechaHoy     = new Date();
        String cdunieco   = smap1.get("cdunieco");
		String cdramo     = smap1.get("cdramo");
		String cdtipsit   = smap1.get("cdtipsit");
		String nmpoliza   = slist1.get(0).get("nmpoliza");
		String feini      = slist1.get(0).get("feini");
		String fefin      = slist1.get(0).get("fefin");
		String cdelemento = sesion.get("CDELEMEN");
		String user       = sesion.get("CDUSUARI");
		infoMethod.invoke(logger,"cdunieco: "+cdunieco);
		infoMethod.invoke(logger,"cdramo: "+cdramo);
		infoMethod.invoke(logger,"cdtipsit: "+cdtipsit);
		infoMethod.invoke(logger,"nmpoliza: "+nmpoliza);
		infoMethod.invoke(logger,"feini: "+feini);
		infoMethod.invoke(logger,"fefin: "+fefin);
		infoMethod.invoke(logger,"hoy: "+fechaHoy);
		infoMethod.invoke(logger,"cdelemento: "+cdelemento);
		infoMethod.invoke(logger,"user: "+user);
		infoMethod.invoke(logger,"<VARIABLES");
		
		if(nmpoliza==null||nmpoliza.length()==0)
		{
			LinkedHashMap<String,Object>paramsValues=new LinkedHashMap<String,Object>();
			paramsValues.put("param1",cdunieco);
			paramsValues.put("param2",cdramo);
			paramsValues.put("param3","W");
			Map<String,Object>paramsMap=(Map<String,Object>)procedureParamsCall.invoke(
					manager,
					"PKG_SATELITES.P_CALC_NUMPOLIZA",
					paramsValues,
					null,
					new String[]{"pv_nmpoliza_o"},
					null
					);
			nmpoliza=(String)paramsMap.get("pv_nmpoliza_o");
			smap1.put("nmpoliza",nmpoliza);
		}
        
		LinkedHashMap<String,String>mapaMpolizas=new LinkedHashMap<String,String>();
        mapaMpolizas.put("01cdunieco"  , cdunieco);
        mapaMpolizas.put("02cdramo"    , cdramo);
        mapaMpolizas.put("03estado"    , "W");
        mapaMpolizas.put("04nmpoliza"  , nmpoliza);
        mapaMpolizas.put("05nmsuplem"  , "0");
        mapaMpolizas.put("06status"    , "V");
        mapaMpolizas.put("07swestado"  , "0");
        mapaMpolizas.put("08nmsolici"  , null);
        mapaMpolizas.put("09feautori"  , null);
        mapaMpolizas.put("10cdmotanu"  , null);
        mapaMpolizas.put("11feanulac"  , null);
        mapaMpolizas.put("12swautori"  , "N");
        mapaMpolizas.put("13cdmoneda"  , "001");
        mapaMpolizas.put("14feinisus"  , null);
        mapaMpolizas.put("15fefinsus"  , null);
        mapaMpolizas.put("16ottempot"  , "R");
        mapaMpolizas.put("17feefecto"  , feini);
        mapaMpolizas.put("18hhefecto"  , "12:00");
        mapaMpolizas.put("19feproren"  , fefin);
        mapaMpolizas.put("20fevencim"  , null);
        mapaMpolizas.put("21nmrenova"  , "0");
        mapaMpolizas.put("22ferecibo"  , null);
        mapaMpolizas.put("23feultsin"  , null);
        mapaMpolizas.put("24nmnumsin"  , "0");
        mapaMpolizas.put("25cdtipcoa"  , "N");
        mapaMpolizas.put("26swtarifi"  , "A");
        mapaMpolizas.put("27swabrido"  , null);
        mapaMpolizas.put("28feemisio"  , renderFechas.format(fechaHoy));
        mapaMpolizas.put("29cdperpag"  , "12");
        mapaMpolizas.put("30nmpoliex"  , null);
        mapaMpolizas.put("31nmcuadro"  , "P1");
        mapaMpolizas.put("32porredau"  , "100");
        mapaMpolizas.put("33swconsol"  , "S");
        mapaMpolizas.put("34nmpolant"  , null);
        mapaMpolizas.put("35nmpolnva"  , null);
        mapaMpolizas.put("36fesolici"  , renderFechas.format(fechaHoy));
        mapaMpolizas.put("37cdramant"  , null);
        mapaMpolizas.put("38cdmejred"  , null);
        mapaMpolizas.put("39nmpoldoc"  , null);
        mapaMpolizas.put("40nmpoliza2" , null);
        mapaMpolizas.put("41nmrenove"  , null);
        mapaMpolizas.put("42nmsuplee"  , null);
        mapaMpolizas.put("43ttipcamc"  , null);
        mapaMpolizas.put("44ttipcamv"  , null);
        mapaMpolizas.put("45swpatent"  , null);
        mapaMpolizas.put("46accion"    , "U");
        procedureVoidCall.invoke(manager,
        		"PKG_SATELITES.P_MOV_MPOLIZAS",
        		mapaMpolizas,
        		null);
		
        //>llaves
        String llaveRol="";
        String llaveSexo="";
        String llaveFenacimi="";
        String llaveCodPostal="";
        try
        {
        	LinkedHashMap<String,Object>paramsObtAtributos=new LinkedHashMap<String,Object>();
        	paramsObtAtributos.put("cdtipsit",cdtipsit);
        	Map<String,String>atributos=(Map<String,String>)procedureMapCall.invoke(manager,
        			"PKG_SATELITES.P_OBT_ATRIBUTOS",
        			paramsObtAtributos,
        			null);
        	if(atributos.get("PARENTESCO") != null) {
        		llaveRol=atributos.get("PARENTESCO");
            	if(llaveRol.length()==1) {
            		llaveRol="0"+llaveRol;
            	}
            	llaveRol="parametros.pv_otvalor"+llaveRol;
        	}
        	if(atributos.get("SEXO") != null) {
        		llaveSexo=atributos.get("SEXO");
        		if(llaveSexo.length()==1) {
            		llaveSexo="0"+llaveSexo;
            	}
            	llaveSexo="parametros.pv_otvalor"+llaveSexo;
        	}
        	if(atributos.get("FENACIMI") != null) {
        		llaveFenacimi=atributos.get("FENACIMI");
            	if(llaveFenacimi.length()==1) {
            		llaveFenacimi="0"+llaveFenacimi;
            	}
            	llaveFenacimi="parametros.pv_otvalor"+llaveFenacimi;
        	}
        	if(atributos.get("CODPOSTAL") != null) {
        		llaveCodPostal=atributos.get("CODPOSTAL");
            	if(llaveCodPostal.length()==1) {
            		llaveCodPostal="0"+llaveCodPostal;
            	}
            	llaveCodPostal="parametros.pv_otvalor"+llaveCodPostal;
        	}
        }
        catch(Exception ex)
        {
        	errorMethod.invoke(logger,"error al obtener atributos",ex);
        }
        //<llaves
        
        //>valida cos postal
        if(llaveCodPostal!=null&&llaveCodPostal.length()>0&&slist1.get(0).get(llaveCodPostal)!=null&&slist1.get(0).get(llaveCodPostal).length()>0)
        {
        	LinkedHashMap<String,Object>paramsValues=new LinkedHashMap<String,Object>();
        	paramsValues.put("param1",slist1.get(0).get(llaveCodPostal));
        	paramsValues.put("param2",cdtipsit);
        	procedureVoidCall.invoke(manager,
        			"PKG_SATELITES.P_VALIDA_TARIFA",
        			paramsValues,
        			null);
        }
        //<valida cos postal

        //>ordenar
        int indiceTitular=-1;
        for(int i=0;i<slist1.size();i++)
        {
        	if(slist1.get(i).get(llaveRol).equalsIgnoreCase("T"))
        	{
        		indiceTitular=i;
        	}
        }
        List<Map<String,String>> temp    = new ArrayList<Map<String,String>>(0);
        Map<String,String>       titular = slist1.get(indiceTitular);
        temp.add(titular);
        slist1.remove(indiceTitular);
        temp.addAll(slist1);
        slist1=temp;
        //<ordenar
        
        //>mpolisit y tvalosit
        int contador=1;
        for(Map<String,String>inciso:slist1)
        {
        	//////////////////////////////
        	////// mpolisit iterado //////
        	LinkedHashMap<String,Object>iMapaPolisit=new LinkedHashMap<String,Object>(0);
        	iMapaPolisit.put("01_cdunieco"   , cdunieco);
        	iMapaPolisit.put("02_cdramo"     , cdramo);
        	iMapaPolisit.put("03_estado"     , "W");
        	iMapaPolisit.put("04_nmpoliza"   , nmpoliza);
        	iMapaPolisit.put("05_nmsituac"   , contador+"");
        	iMapaPolisit.put("06_nmsuplem"   , "0");
        	iMapaPolisit.put("07_status"     , "V");
        	iMapaPolisit.put("08_cdtipsit"   , cdtipsit);
        	iMapaPolisit.put("09_swreduci"   , null);
        	iMapaPolisit.put("10_cdagrupa"   , "1");
        	iMapaPolisit.put("11_cdestado"   , "0");
        	iMapaPolisit.put("12_fefecsit"   , renderFechas.parse(feini));
        	iMapaPolisit.put("13_fecharef"   , renderFechas.parse(feini));
        	iMapaPolisit.put("14_cdgrupo"    , null);
        	iMapaPolisit.put("15_nmsituaext" , null);
        	iMapaPolisit.put("16_nmsitaux"   , null);
        	iMapaPolisit.put("17_nmsbsitext" , null);
        	iMapaPolisit.put("18_cdplan"     , "1");
        	iMapaPolisit.put("19_cdasegur"   , "30");
        	iMapaPolisit.put("20_accion"     , "I");
            String[] iMapaPolisitTipos=new String[]
            {
            	"VARCHAR",//1
            	"VARCHAR",
            	"VARCHAR",
            	"VARCHAR",
            	"VARCHAR",//5
            	"VARCHAR",
            	"VARCHAR",
            	"VARCHAR",
            	"VARCHAR",
            	"VARCHAR",//10
            	"VARCHAR",
            	"DATE",
            	"DATE",
            	"VARCHAR",
            	"VARCHAR",//15
            	"VARCHAR",
            	"VARCHAR",
            	"VARCHAR",
            	"VARCHAR",
            	"VARCHAR"
            };
            procedureVoidCall.invoke(manager,
            		"PKG_SATELITES.P_MOV_MPOLISIT",
            		iMapaPolisit,
            		iMapaPolisitTipos);
        	////// mpolisit iterado //////
        	//////////////////////////////
            
            //////////////////////////////
            ////// tvalosit iterado //////
            
            ////// 1. tvalosit base //////
            LinkedHashMap<String,String>mapaValositIterado=new LinkedHashMap<String,String>(0);
            mapaValositIterado.put("01_cdunieco" , cdunieco);
            mapaValositIterado.put("02_cdramo"   , cdramo);
            mapaValositIterado.put("03_estado"   , "W");
            mapaValositIterado.put("04_nmpoliza" , nmpoliza);
            mapaValositIterado.put("05_nmsituac" , contador+"");
            mapaValositIterado.put("06_nmsuplem" , "0");
            mapaValositIterado.put("07_status"   , "V");
            mapaValositIterado.put("08_cdtipsit" , cdtipsit);
            //>poner todas las llaves
            for(int i=1;i<=50;i++)
            {
            	String key="pv_otvalor"+i;
            	if(i<10)
            	{
            		key="pv_otvalor0"+i;
            	}
            	if(!mapaValositIterado.containsKey(key))
            	{
            		mapaValositIterado.put(key,null);
            	}
            }
            //<poner todas las llaves
            mapaValositIterado.put("ZZ_accion_i" , "I");
            ////// 1. tvalosit base //////
            
            ////// 2. tvalosit desde form //////
            for(Entry<String,String>en:inciso.entrySet())
            {
            	// p a r a m e t r o s . p v _ o t v a l o r 
            	//0 1 2 3 4 5 6 7 8 9 0 1
            	String key=en.getKey();
            	String value=en.getValue();
            	if(key.length()>11&&key.substring(0,11).equalsIgnoreCase("parametros."))
            	{
            		mapaValositIterado.put(key.substring(11),value);
            	}
            }
            ////// 2. tvalosit desde form //////
            
            ////// 4. custom //////
            if(cdtipsit.equals("SL"))
            {
            	mapaValositIterado.put("pv_otvalor11","S");
            	mapaValositIterado.put("pv_otvalor12","0");
            	mapaValositIterado.put("pv_otvalor13","21000");
            }
            else if(cdtipsit.equals("SN"))
            {
            	mapaValositIterado.put("pv_otvalor09","N");
            	mapaValositIterado.put("pv_otvalor10","N");
            	mapaValositIterado.put("pv_otvalor11","S");
            	mapaValositIterado.put("pv_otvalor12","0");
            	mapaValositIterado.put("pv_otvalor13","21000");
            	mapaValositIterado.put("pv_otvalor15","N");
            }
            else if(cdtipsit.equals("GB"))
            {
            	mapaValositIterado.put("pv_otvalor16",mapaValositIterado.get("pv_otvalor01"));
            }
            ////// 4. custom //////
            procedureVoidCall.invoke(manager,
            		"PKG_SATELITES.P_MOV_TVALOSIT",
            		mapaValositIterado,
            		null);
            ////// tvalosit iterado //////
            //////////////////////////////
            
            contador++;
        }
        //<mpolisit y tvalosit
        
        /////////////////////////////
        ////// clonar personas //////
        contador=1;
        for(Map<String,String> inciso : slist1)
        {
            LinkedHashMap<String,Object> mapaClonPersonaIterado=new LinkedHashMap<String,Object>(0);
            mapaClonPersonaIterado.put("pv_cdelemen_i"  , cdelemento);
            mapaClonPersonaIterado.put("pv_cdunieco_i"  , cdunieco);
            mapaClonPersonaIterado.put("pv_cdramo_i"    , cdramo);
            mapaClonPersonaIterado.put("pv_estado_i"    , "W");
            mapaClonPersonaIterado.put("pv_nmpoliza_i"  , nmpoliza);
            mapaClonPersonaIterado.put("pv_nmsituac_i"  , contador+"");
            mapaClonPersonaIterado.put("pv_cdtipsit_i"  , cdtipsit);
            mapaClonPersonaIterado.put("pv_fecha_i"     , fechaHoy);
            mapaClonPersonaIterado.put("pv_cdusuario_i" , user);
            mapaClonPersonaIterado.put("pv_p_nombre"    , inciso.get("nombre"));
            mapaClonPersonaIterado.put("pv_s_nombre"    , inciso.get("nombre2"));
            mapaClonPersonaIterado.put("pv_apellidop"   , inciso.get("apat"));
            mapaClonPersonaIterado.put("pv_apellidom"   , inciso.get("amat"));
            mapaClonPersonaIterado.put("pv_sexo"        , inciso.containsKey(llaveSexo)?inciso.get(llaveSexo):llaveSexo);
            mapaClonPersonaIterado.put("pv_fenacimi"    , inciso.containsKey(llaveFenacimi)?
            		renderFechas.parse(inciso.get(llaveFenacimi)):(
            				llaveFenacimi.equalsIgnoreCase("DATE")?
            						fechaHoy :
            							renderFechas.parse(llaveFenacimi)));
            mapaClonPersonaIterado.put("pv_parentesco"  , inciso.containsKey(llaveRol)?inciso.get(llaveRol):llaveRol);
            procedureVoidCall.invoke(manager,
            		"PKG_COTIZA.P_CLONAR_PERSONAS",
            		mapaClonPersonaIterado,
            		new String[]{"VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR","DATE",
            		"VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR","DATE","VARCHAR",
            		});
            contador++;
        }
        ////// clonar personas //////
        /////////////////////////////
        
        ////////////////////////
        ////// coberturas //////
        /*////////////////////*/
        LinkedHashMap<String,String> mapCoberturas=new LinkedHashMap<String,String>(0);
        mapCoberturas.put("pv_cdunieco_i" , cdunieco);
        mapCoberturas.put("pv_cdramo_i"   , cdramo);
        mapCoberturas.put("pv_estado_i"   , "W");
        mapCoberturas.put("pv_nmpoliza_i" , nmpoliza);
        mapCoberturas.put("pv_nmsituac_i" , "0");
        mapCoberturas.put("pv_nmsuplem_i" , "0");
        mapCoberturas.put("pv_cdgarant_i" , "TODO");
        mapCoberturas.put("pv_cdtipsup_i" , "1");
        procedureVoidCall.invoke(manager,
        		"P_EXEC_SIGSVDEF",
        		mapCoberturas,
        		null);
        /*////////////////////*/
        ////// coberturas //////
        ////////////////////////
        
        //////////////////////////
        ////// TARIFICACION //////
        /*//////////////////////*/
        LinkedHashMap<String,String> mapaTarificacion=new LinkedHashMap<String,String>(0);
        mapaTarificacion.put("pv_cdusuari_i" , user);
        mapaTarificacion.put("pv_cdelemen_i" , cdelemento);
        mapaTarificacion.put("pv_cdunieco_i" , cdunieco);
        mapaTarificacion.put("pv_cdramo_i"   , cdramo);
        mapaTarificacion.put("pv_estado_i"   , "W");
        mapaTarificacion.put("pv_nmpoliza_i" , nmpoliza);
        mapaTarificacion.put("pv_nmsituac_i" , "0");
        mapaTarificacion.put("pv_nmsuplem_i" , "0");
        mapaTarificacion.put("pv_cdtipsit_i" , cdtipsit);
        procedureVoidCall.invoke(manager,
        		"PKG_COTIZA.P_EJECUTA_SIGSVALIPOL",
        		mapaTarificacion,
        		null);
        /*//////////////////////*/
        ////// TARIFICACION //////
        //////////////////////////
        
        ///////////////////////////////////
        ////// Generacion cotizacion //////
        /*///////////////////////////////*/
        LinkedHashMap<String,String> mapaDuroResultados=new LinkedHashMap<String,String>(0);
        mapaDuroResultados.put("pv_cdusuari_i" , user);
        mapaDuroResultados.put("pv_cdunieco_i" , cdunieco);
        mapaDuroResultados.put("pv_cdramo_i"   , cdramo);
        mapaDuroResultados.put("pv_estado_i"   , "W");
        mapaDuroResultados.put("pv_nmpoliza_i" , nmpoliza);
        mapaDuroResultados.put("pv_cdelemen_i" , cdelemento);
        mapaDuroResultados.put("pv_cdtipsit_i" , cdtipsit);
        List<Map<String,String>> listaResultados=(List<Map<String,String>>)procedureListCall.invoke(manager,
        		"PKG_COTIZA.P_GEN_TARIFICACION",
        		mapaDuroResultados,
        		null);
        /*///////////////////////////////*/
        ////// Generacion cotizacion //////
        ///////////////////////////////////

        ////////////////////////////////
        ////// Agrupar resultados //////
        /*
        NMSUPLEM=0,
		FEFECSIT=13/01/2014,
		NMPOLIZA=3853,
		MNPRIMA=4571.92,           <--2
		CDPERPAG=7,                <--1
		DSPLAN=Plus 500,           <--3
		FEVENCIM=13/01/2015,
		STATUS=V,
		NMSITUAC=3,
		ESTADO=W,
		DSPERPAG=DXN Catorcenal,   <--(1)
		CDCIAASEG=20,
		CDIDENTIFICA=2,
		CDTIPSIT=SL,
		FEEMISIO=13/01/2014,
		CDUNIECO=1,
		CDRAMO=2,
		CDPLAN=M,                  <--(3)
		DSUNIECO=PUEBLA
         */
        
        ////// 1. encontrar planes, formas de pago y algun nmsituac//////
        Map<String,String>formasPago = new LinkedHashMap<String,String>();
        Map<String,String>planes     = new LinkedHashMap<String,String>();
        String nmsituac="";
        for(Map<String,String>res:listaResultados)
        {
        	String cdperpag = res.get("CDPERPAG");
        	String dsperpag = res.get("DSPERPAG");
        	String cdplan   = res.get("CDPLAN");
        	String dsplan   = res.get("DSPLAN");
        	if(!formasPago.containsKey(cdperpag))
        	{
        		formasPago.put(cdperpag,dsperpag);
        	}
        	if(!planes.containsKey(cdplan))
        	{
        		planes.put(cdplan,dsplan);
        	}
        	nmsituac=res.get("NMSITUAC");
        }
        infoMethod.invoke(logger,"formas de pago: "+formasPago);
        infoMethod.invoke(logger,"planes: "+planes);
        ////// 1. encontrar planes y formas de pago //////
        
        ////// 2. crear formas de pago //////
        List<Map<String,String>>tarifas=new ArrayList<Map<String,String>>();
        for(Entry<String,String>formaPago:formasPago.entrySet())
        {
        	Map<String,String>tarifa=new HashMap<String,String>();
        	tarifa.put("CDPERPAG",formaPago.getKey());
        	tarifa.put("DSPERPAG",formaPago.getValue());
        	tarifa.put("NMSITUAC",nmsituac);
        	tarifas.add(tarifa);
        }
        infoMethod.invoke(logger,"tarifas despues de formas de pago: "+tarifas);
        ////// 2. crear formas de pago //////
        
        ////// 3. crear planes //////
        for(Map<String,String>tarifa:tarifas)
        {
        	for(Entry<String,String>plan:planes.entrySet())
            {
            	tarifa.put("CDPLAN"+plan.getKey(),plan.getKey());
            	tarifa.put("DSPLAN"+plan.getKey(),plan.getValue());
            }
        }
        infoMethod.invoke(logger,"tarifas despues de planes: "+tarifas);
        ////// 3. crear planes //////
        
        ////// 4. crear primas //////
        for(Map<String,String>res:listaResultados)
        {
        	String cdperpag = res.get("CDPERPAG");
        	String mnprima  = res.get("MNPRIMA");
        	String cdplan   = res.get("CDPLAN");
        	for(Map<String,String>tarifa:tarifas)
            {
        		if(tarifa.get("CDPERPAG").equals(cdperpag))
        		{
        			if(tarifa.containsKey("MNPRIMA"+cdplan))
        			{
        				infoMethod.invoke(logger,"ya hay prima para "+cdplan+" en "+cdperpag+": "+tarifa.get("MNPRIMA"+cdplan));
        				tarifa.put("MNPRIMA"+cdplan,((Double)Double.parseDouble(tarifa.get("MNPRIMA"+cdplan))+(Double)Double.parseDouble(mnprima))+"");
        				infoMethod.invoke(logger,"nueva: "+tarifa.get("MNPRIMA"+cdplan));
        			}
        			else
        			{
        				infoMethod.invoke(logger,"primer prima para "+cdplan+" en "+cdperpag+": "+mnprima);
        				tarifa.put("MNPRIMA"+cdplan,mnprima);
        			}
        		}
            }
        }
        infoMethod.invoke(logger,"tarifas despues de primas: "+tarifas);
        
        result[0]=planes;
        result[1]=tarifas;
        ////// 4. crear primas //////
        
        ////// Agrupar resultados //////
        ////////////////////////////////
        
        infoMethod.invoke(logger,""
        		+ "\n###### externo ######"
        		+ "\n#####################"
        		);
        return result;
    }
    
    public static void main(String args[])
    {
    	System.out.println("Hola mundo!");
    }
}