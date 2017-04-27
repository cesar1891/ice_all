<%@ include file="/taglibs.jsp"%>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
    <style>
    .x-action-col-icon {
        height: 16px;
        width: 16px;
        margin-right: 8px;
    }
    </style>
        <script type="text/javascript">
        
            var _NOMBRE_REPORTE_COTIZACION =            '<s:text name="rdf.cotizacion.nombre"/>';
        
            //URL padre de los catalogos
            var _URL_OBTEN_CATALOGO_GENERICO=           '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
            //URL que obtiene una cotizacion
            var _URL_CARGAR_COTIZACION=                 '<s:url action="dinosaurio2" namespace="/" />';
            
            /////////////////////////////////////
            ////// ini atributos catalogos //////
            /////////////////////////////////////
            var CDATRIBU_SEXO=                          '<s:property value="cdatribuSexo" />';                      //1
            //fecha nacimiento                                                                                        2
            var CDATRIBU_ESTADO=                        '<s:property value="cdatribuEstado" />';                    //3
            var CDATRIBU_CIUDAD=                        '<s:property value="cdatribuCiudad" />';                    //4
            var CDATRIBU_DEDUCIBLE=                     '<s:property value="cdatribuDeducible" />';                 //5
            var CDATRIBU_COPAGO=                        '<s:property value="cdatribuCopago" />';                    //6
            var CDATRIBU_SUMA_ASEGURADA=                '<s:property value="cdatribuSumaAsegurada" />';             //7
            var CDATRIBU_CIRCULO_HOSPITALARIO=          '<s:property value="cdatribuCirculoHospitalario" />';       //8
            var CDATRIBU_COBERTURA_VACUNAS=             '<s:property value="cdatribuCoberturaVacunas" />';          //9
            var CDATRIBU_COBERTURA_PREV_ENF_ADULTOS=    '<s:property value="cdatribuCoberturaPrevEnfAdultos" />';   //10
            var CDATRIBU_MATERNIDAD=                    '<s:property value="cdatribuMaternidad" />';                //11
            var CDATRIBU_SUMA_ASEGUARADA_MATERNIDAD=    '<s:property value="cdatribuSumaAseguradaMatenidad" />';    //12
            var CDATRIBU_BASE_TABULADOR_REEMBOLSO=      '<s:property value="cdatribuBaseTabuladorRembolso" />';     //13
            var CDATRIBU_COSTO_EMERGENCIA_EXTRANJERO=   '<s:property value="cdatribuCostoEmergenciaExtranjero" />'; //14
            var CDATRIBU_COB_ELIM_PEN_CAMBIO_ZONA=      '<s:property value="cdatribuCobEliPenCamZona" />';          //15
            var CDATRIBU_ROL=                           '<s:property value="cdatribuRol" />';                       //16
            var CDATRIBU_MUNICIPIO=                     '<s:property value="cdatribuMunicipio" />';                       //17
            var constanteTatrisitPantallaCotiza       = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TATRISIT"/>';
            /////////////////////////////////////
            ////// fin atributos catalogos //////
            /////////////////////////////////////
            
            //URL destino
            var _URL_COTIZAR=               '<s:url namespace="/flujocotizacion" action="resultadoCotizacionVital" />';
            //URL redireccion despues de cotizar
            var _URL_RESULTADO_COTIZACION=  '<s:url namespace="/flujocotizacion" action="resultCotizacion" />';
            var _URL_RESULTADOS=            '<s:url namespace="/flujocotizacion" action="obtenCotizacion4" />';
            var _URL_COBERTURAS=            '<s:url namespace="/flujocotizacion" action="obtenerCoberturas4" />';
            var _URL_DETALLE_COBERTURA=     '<s:url namespace="/flujocotizacion" action="obtenerAyudaCoberturas4" />';
            var urlComprarCotizacion=       '<s:url namespace="/flujocotizacion" action="comprarCotizacion4" />';
            var urlDatosComplementarios=    '<s:url namespace="/"                action="datosComplementarios" />';
            var urlDetalleCotizacion=       '<s:url namespace="/"                action="detalleCotizacion" />';
            var contexto='${ctx}';
            var urlVentanaDocumentos =      '<s:url namespace="/documentos"      action="ventanaDocumentosPoliza" />';
            var urlImprimirCotiza    = '<s:property value="rutaServidorReports" />';
            var repSrvUsr            = '<s:property value="passServidorReports" />';
            var urlEnviarCorreo      =      '<s:url namespace="/general"         action="enviaCorreo" />';
            var urlDatosComplementarios =   '<s:url namespace="/"                action="datosComplementarios" />';
            var urlDocumentosTramite =      '<s:url namespace="/documentos"      action="ventanaDocumentosPoliza" />';
            var panDocUrlViewDoc     =      '<s:url namespace ="/documentos"     action="descargaDocInline" />';
            var datComUrlMCUpdateStatus=    '<s:url namespace="/mesacontrol"     action="actualizarStatusTramite" />';
            var datComUrlMC            =    '<s:url namespace="/mesacontrol"     action="principal" />';
            <s:if test="ntramite!=null&&ntramite.length()>0">
                var hayTramiteCargado=true;
                var ntramiteCargado='<s:property value="ntramite" />';
            </s:if>
            <s:else>
                var hayTramiteCargado=false;
                var ntramiteCargado='';
            </s:else>
            var cotizacionUserSoloCotiza=false;
            <s:if test='%{user!=null&&user.equalsIgnoreCase(getText("usuario.solo.cotiza"))}'>
                cotizacionUserSoloCotiza=true;
            </s:if>
            var sesionUsuarioUser = '<s:property value='%{session.get("USUARIO").getUser()}' />';
            var inputCdunieco='<s:property value="cdunieco" />';
            var inputCdramo  ='<s:property value="cdramo" />';
            var inputCdtipsit='<s:property value="cdtipsit" />';
            var EDAD_MAXIMA_COTIZACION='<s:property value="edadMaximaCotizacion" />';
            debug('cdunieco,cdramo,cdtipsit',inputCdunieco,inputCdramo,inputCdtipsit);
            debug('hayTramiteCargado '+(hayTramiteCargado?'true':'false'));
            debug('ntramiteCargado: '+ntramiteCargado);
            debug('solo cotiza: '+cotizacionUserSoloCotiza);
            debug('sesionUsuarioUser: '+sesionUsuarioUser);
        </script>
        <script src="${ctx}/resources/jsp-script/extjs4/aseguradosGB.js"></script>
    </head>
    <body>
        <div style="height:900px;">
            <div id="maindiv"></div>
            <div id="divResultados" style="margin-top:10px;"></div>
        </div>
    </body>
</html>