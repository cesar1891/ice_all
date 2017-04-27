<%@ page language="java"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">

/* ********** CATALOGOS JS ******************** */

// Catalogo de ACCIONES PERSISTENTES:
var Accion = {
    ObtieneDatosFacturas      : '<s:property value="@mx.com.gseguros.portal.general.util.ObjetoBD@OBTIENE_DATOS_FACTURAS" />',
    ObtieneDatosProveedores   : '<s:property value="@mx.com.gseguros.portal.general.util.ObjetoBD@OBTIENE_DATOS_PROVEEDORES" />',
    ObtieneDatosReexpDoc      : '<s:property value="@mx.com.gseguros.portal.general.util.ObjetoBD@OBTIENE_DATOS_REEXPED_DOC" />',
    ObtieneEmail              : '<s:property value="@mx.com.gseguros.portal.general.util.ObjetoBD@OBTIENE_EMAIL" />',
    ValidaCancelacionProrrata : '<s:property value="@mx.com.gseguros.portal.general.util.ObjetoBD@VALIDA_CANC_A_PRORRATA" />',
    ValidaEdadAsegurados      : '<s:property value="@mx.com.gseguros.portal.general.util.ObjetoBD@VALIDA_EDAD_ASEGURADOS" />'
};

// Nombre de cat�logos:
var Cat = {
    Agentes                   : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@AGENTES" />',
    CausaSiniestro            : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CAUSA_SINIESTRO" />',
    Coberturas                : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@COBERTURAS" />',
    CodigosMedicos            : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CODIGOS_MEDICOS" />',
    Colonias                  : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@COLONIAS" />',
    Endosos                   : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ENDOSOS" />',
    ICD                       : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ICD" />',
    EstatusTramiteMC          : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_ESTATUS_TRAMITE" />',
    SucursalesAdminMC         : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_SUCURSALES_ADMIN" />',
    SucursalesDocumentoMC     : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_SUCURSALES_DOCUMENTO" />',
    TiposTramiteMC            : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_TIPOS_TRAMITE" />',
    Medicos                   : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MEDICOS" />',
    MotivosCancelacion        : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MOTIVOS_CANCELACION" />',
    MotivosRechazoSiniestro   : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MOTIVOS_RECHAZO_SINIESTRO" />',
    MotivosReexpedicion       : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MOTIVOS_REEXPEDICION" />',
    Nacionalidad              : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@NACIONALIDAD" />',
    Penalizaciones            : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PENALIZACIONES" />',
    Planes                    : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PLANES" />',
    Proveedores               : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PROVEEDORES" />',
    Ramos                     : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@RAMOS" />',
    RolesPoliza               : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ROLES_POLIZA" />',
    RolesSistema              : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ROLES_SISTEMA" />',
    Sexo                      : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@SEXO" />',
    Sino                      : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@SINO" />',
    StatusPoliza              : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@STATUS_POLIZA" />',
    Subcoberturas             : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@SUBCOBERTURAS" />',
    SubmotivosRechazoSiniestro: '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@SUBMOTIVOS_RECHAZO_SINIESTRO" />',
    Tatrigar                  : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TATRIGAR" />',
    Tatriper                  : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TATRIPER" />',
    Tatripol                  : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TATRIPOL" />',
    Tatrisit                  : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TATRISIT" />',
    TipoAtencionSiniestros    : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_ATENCION_SINIESTROS" />',
    TipoConceptoSiniestros    : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_CONCEPTO_SINIESTROS" />',
    TipoPagoSiniestros        : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_PAGO_SINIESTROS" />',
    TiposPagoPoliza           : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPOS_PAGO_POLIZA" />',
    TiposPagoPolizaSinDxN     : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPOS_PAGO_POLIZA_SIN_DXN" />',
    TiposPersona              : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPOS_PERSONA" />',
    TiposDomicilio            : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPOS_DOMICILIO" />',
    TiposPoliza               : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPOS_POLIZA" />',
    TipSit                    : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPSIT" />',
    Tratamientos              : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TRATAMIENTOS" />',
    TipoMoneda                : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_MONEDA" />',
    tipoEventoGNP             : '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPOEVENTOGNP" />'
};


// Estatus de tramite:
var EstatusTramite = {
        
    Confirmado             : '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@CONFIRMADO.codigo" />',
    EnCaptura              : '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_CAPTURA.codigo" />',
    EnCapturaCMM           : '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_CAPTURA_CMM.codigo" />',
    EnEsperaDeAsignacion   : '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_ESPERA_DE_ASIGNACION.codigo" />',
    EnEsperaDeAutorizacion : '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_ESPERA_DE_AUTORIZACION.codigo" />',
    EnRevisionMedica       : '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_REVISION_MEDICA.codigo" />',
    EndosoConfirmado       : '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@ENDOSO_CONFIRMADO.codigo" />',
    EndosoEnEspera         : '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@ENDOSO_EN_ESPERA.codigo" />',
    Pendiente              : '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@PENDIENTE.codigo" />',
    Rechazado              : '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@RECHAZADO.codigo" />',
    SolicitudMedica        : '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@SOLICITUD_MEDICA.codigo" />',
    VoBoMedico             : '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@VO_BO_MEDICO.codigo" />'
};


// Catalogo de cdramo:
var Ramo = {
	AutosFronterizos    : '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@AUTOS_FRONTERIZOS.cdramo"/>',
	AutosResidentes     : '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@AUTOS_RESIDENTES.cdramo"/>',
	GastosMedicosMayores: '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@GASTOS_MEDICOS_MAYORES.cdramo"/>',
    Multisalud          : '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@MULTISALUD.cdramo"/>',
    Recupera            : '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@RECUPERA.cdramo"/>',
    SaludVital          : '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@SALUD_VITAL.cdramo"/>',
    ServicioPublico     : '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@SERVICIO_PUBLICO.cdramo"/>',
    GastosMedicosMayoresPrueba : '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@GASTOS_MEDICOS_MAYORES_PRUEBA.cdramo"/>'
};


//Catalogo de Tipo de pago:
var TipoPago = {
    Directo   : '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@DIRECTO.codigo" />',
    Reembolso : '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@REEMBOLSO.codigo" />'
};


// Catalogo de cdtipsit:
var TipoSituacion = {
    AutosFronterizos      : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@AUTOS_FRONTERIZOS.cdtipsit" />',
    AutosResidentes       : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@AUTOS_RESIDENTES.cdtipsit" />',
    AutosPickUp           : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@AUTOS_PICK_UP.cdtipsit" />',
    CamionesCarga         : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@CAMIONES_CARGA.cdtipsit" />',
    Multisalud            : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@MULTISALUD.cdtipsit" />',
    MultisaludColectivo   : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@MULTISALUD_COLECTIVO.cdtipsit" />',
    Motos                 : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@MOTOS.cdtipsit" />',
    SaludNomina           : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@SALUD_NOMINA.cdtipsit" />',
    SaludVital            : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@SALUD_VITAL.cdtipsit" />',
    PickUpCarga           : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@PICK_UP_CARGA.cdtipsit" />',
    PickUpParticular      : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@PICK_UP_PARTICULAR.cdtipsit" />',
    RecuperaIndividual    : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@RECUPERA_INDIVIDUAL.cdtipsit" />',
    RecuperaColectivo     : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@RECUPERA_COLECTIVO.cdtipsit" />',
    TractoCamionesArmados : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@TRACTOCAMIONES_ARMADOS.cdtipsit" />',
    ServicioPublicoMicro  : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@SERVICIO_PUBLICO_MICRO.cdtipsit" />',
    ServicioPublicoAuto   : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@SERVICIO_PUBLICO_AUTO.cdtipsit" />',
    TuristaLicencia		  : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@TURISTA_LICENCIA.cdtipsit" />',
    TuristaVehiculo 	  : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@TURISTA_VEHICULO.cdtipsit" />' ,
    GastosMedicosMayoresPruebaInd : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@GASTOS_MEDICOS_MAYORES_PRUEBA_IND.cdtipsit" />',
    GastosMedicosMayoresPruebaCol : '<s:property value="@mx.com.gseguros.portal.general.util.TipoSituacion@GASTOS_MEDICOS_MAYORES_PRUEBA_COL.cdtipsit" />'
 };
 
 //Catalogo de formas de pago

 var FormaPago={
	Mensual				:	 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@MENSUAL.codigo" />',
	ANUAL				:	 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@ANUAL.codigo" />',
	TRIMESTRAL			:	 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@TRIMESTRAL.codigo" />',
	SEMESTRAL			:	 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@SEMESTRAL.codigo" />',
	DXNQUINCENAL		:	 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXNQUINCENAL.codigo" />',
	DXNCATORCENAL		:	 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXNCATORCENAL.codigo" />',
	DXNMENSUAL			:	 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXNMENSUAL.codigo" />',
	DXN16DIAS			:	 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXN16DIAS.codigo" />',
	DXNSEMANAL          :    '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXNSEMANAL.codigo" />',
	DXNANUAL            :    '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXNANUAL.codigo" />',
	DXNDOCENAL          :    '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXNDOCENAL.codigo" />',
	CONTADO				:    '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@CONTADO.codigo" />',
	esDxN				:    function(formaPago){
		
		var dxn=[
                     '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXNDOCENAL.codigo" />',
					 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXNQUINCENAL.codigo" />',
					 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXNCATORCENAL.codigo" />',
					 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXNMENSUAL.codigo" />',
					 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXN16DIAS.codigo" />',
					 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXNSEMANAL.codigo" />',
					 '<s:property value="@mx.com.gseguros.portal.general.util.TipoFormaPago@DXNANUAL.codigo" />'

		         ];
		
		if(dxn.indexOf(formaPago+"")>-1){
			return true;
		}
		else{
			return false;
		}
	
	}

 };

// Catalogo de Tipos de tramite (cdtiptra):
var TipoTramite = {
    AltaAfiliado                   : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@ALTA_AFILIADO.cdtiptra" />',
    AutorizacionServicios          : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@AUTORIZACION_SERVICIOS.cdtiptra" />',
    BajaAfiliado                   : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@BAJA_AFILIADO.cdtiptra" />',
    CambioAgente                   : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@CAMBIO_AGENTE.cdtiptra" />',
    CambioContratante              : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@CAMBIO_CONTRATANTE.cdtiptra" />',
    CambioFormaPago                : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@CAMBIO_FORMA_PAGO.cdtiptra" />',
    CambioVigencia                 : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@CAMBIO_VIGENCIA.cdtiptra" />',
    Cancelacion                    : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@CANCELACION.cdtiptra" />',
    CancelacionPorPagoImprocedente : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@CANCELACION_POR_PAGO_IMPROCEDENTE.cdtiptra" />',
    CancelacionProrrata            : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@CANCELACION_PRORRATA.cdtiptra" />',
    EndosoParadoPorAutorizacion    : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@ENDOSO_PARADO_POR_AUTORIZACION.cdtiptra" />',
    EmisionEnEspera                : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@EMISION_EN_ESPERA.cdtiptra" />',
    ModificatorioAlta              : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@MODIFICATORIO_ALTA.cdtiptra" />',
    ModificatorioBaja              : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@MODIFICATORIO_BAJA.cdtiptra" />',
    PolizaNueva                    : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@POLIZA_NUEVA.cdtiptra" />',
    Rehabilitacion                 : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@REHABILITACION.cdtiptra" />',
    Siniestro                      : '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@SINIESTRO.cdtiptra" />'
};


// Catalogo de Roles del sistema (cdsisrol):
var RolSistema = {
    Agente                        : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@AGENTE.cdsisrol"                      />'
    ,PromotorAuto                 : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@PROMOTOR_AUTO.cdsisrol"               />'
    ,SuscriptorAuto               : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@SUSCRIPTOR_AUTO.cdsisrol"             />'
    ,TecnicoSuscripcionDanios     : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@TECNICO_SUSCRI_DANIOS.cdsisrol"       />'
    ,JefeSuscripcionDanios        : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@JEFE_SUSCRI_DANIOS.cdsisrol"          />'
    ,GerenteSuscripcionDanios     : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@GERENTE_SUSCRI_DANIOS.cdsisrol"       />'
    ,EmisorSuscripcionDanios      : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@EMISOR_SUSCRI_DANIOS.cdsisrol"        />'
    ,SubdirectorSuscripcionDanios : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@SUBDIRECTOR_SUSCRI_DANIOS.cdsisrol"   />'
    ,Parametrizador				  : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@PARAMETRIZADOR.cdsisrol"              />'
    ,ParametrizadorAreaTecnica	  : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@PARAMETRIZADOR_AREA_TECNICA.cdsisrol" />'
    ,ParametrizadorSistemas  	  : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@PARAMETRIZADOR_SISTEMAS.cdsisrol"     />'
    ,EjecutivoVenta               : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@EJECUTIVO_INTERNO.cdsisrol"           />'
    ,MesaControl                  : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@MESA_DE_CONTROL.cdsisrol"             />'
    ,SuscriptorTecnico            : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@SUSCRIPTOR_TECNICO.cdsisrol"          />'
    ,SupervisorTecnico            : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@SUPERVISOR_TECNICO_SALUD.cdsisrol"    />'
    ,SubdirectorSalud             : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@SUBDIRECTOR_SALUD.cdsisrol"           />'
    ,DirectorSalud                : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@DIRECTOR_SALUD.cdsisrol"              />'
    ,GerenteOperacionesEmision    : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@GENERENTE_OPERACION_EMISION.cdsisrol" />'
    ,SuscriptorSalud              : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@SUSCRIPTOR.cdsisrol"                  />'  
    ,SupervicionEmisionSalud      : '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@SUPERVISOR.cdsisrol"                  />'
    ,
    
    
    /**
     * Indica si un rol puede suscribir Autos
     * 
     * @param {String} cdsisrol Rol del sistema a verificar
     * @return {Boolean} true si el rol puede suscribir autos, false si no
     */
	puedeSuscribirAutos : function(cdsisrol) {
		
		// Roles que pueden suscribir Autos:
		var roles = [
			this.EmisorSuscripcionDanios,
			this.GerenteSuscripcionDanios,
			this.JefeSuscripcionDanios,
			this.SubdirectorSuscripcionDanios,
			this.SuscriptorAuto,
			this.TecnicoSuscripcionDanios
		];
		
		for (var i in roles) {
			//debug('i=', i, ' val:', roles[i]);
			//debug('roles=', roles);
			if (roles.hasOwnProperty(i) && roles[i] === cdsisrol) {
				return true;
			}
		}
		return false;
	}
};



// Catalogo de tipos de ramo (salud/autos)
var TipoRamo = {
    Salud  : '<s:property value="@mx.com.gseguros.portal.general.util.TipoRamo@SALUD.cdtipram" />'
    ,Autos : '<s:property value="@mx.com.gseguros.portal.general.util.TipoRamo@AUTOS.cdtipram" />'
};

// Catalogo Tipo Flotilla TIPOFLOT
var TipoFlotilla = {
	Flotilla : '<s:property value="@mx.com.gseguros.portal.general.util.TipoFlotilla@Tipo_Flotilla.cdtipsit" />'
	        
}

// Catalogo de Tipos de Unidad:
var TipoUnidad = {
    Fronterizo  : '<s:property value="@mx.com.gseguros.portal.general.util.TipoUnidad@FRONTERIZO.clave" />'
};

var EstadoRecibo = {
        Pendiente   : '<s:property value="@mx.com.gseguros.portal.general.util.ReciboEstado@PENDIENTE.dsestado"  />',
        Cancelado   : '<s:property value="@mx.com.gseguros.portal.general.util.ReciboEstado@CANCELADO.dsestado"  />',
        Pagado      : '<s:property value="@mx.com.gseguros.portal.general.util.ReciboEstado@PAGADO.dsestado"     />',
        Devuelto    : '<s:property value="@mx.com.gseguros.portal.general.util.ReciboEstado@DEVUELTO.dsestado"   />'
};

//Catalogo de Tipos de Persona:
var TipoPersona = {
    Fisica  : '<s:property value="@mx.com.gseguros.portal.general.util.TipoPersona@FISICA.tipoPersona" />',
    Moral  : '<s:property value="@mx.com.gseguros.portal.general.util.TipoPersona@MORAL.tipoPersona" />',
    RegimenSimplificado  : '<s:property value="@mx.com.gseguros.portal.general.util.TipoPersona@REGIMEN_SIMPLIFICADO.tipoPersona" />'
};

// Catalogo de Tipo de Endoso
var TipoEndoso = {
		SumaAseguradaIncremento   	               : '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@SUMA_ASEGURADA_INCREMENTO.cdTipSup"        />',
		SumaAseguradaDecremento    	               : '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@SUMA_ASEGURADA_DECREMENTO.cdTipSup"        />',
		DeducibleMenos			   				   : '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@DEDUCIBLE_MENOS.cdTipSup"                  />',
		DeducibleMas			   				   : '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@DEDUCIBLE_MAS.cdTipSup"                    />',
		BajaCoberturas							   : '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@BAJA_COBERTURAS.cdTipSup"                  />',
		AltaCoberturas			 		   		   : '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@ALTA_COBERTURAS.cdTipSup"                  />',
		DevolucionDePrimasNoDevengadas			   : '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@DEVOLUCION_PRIMAS_NO_DEVENGADAS.cdTipSup"  />',
		CancelacionAutomaticaEndodo                : '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@CANCELACION_AUTOMATICA_ENDOSO.cdTipSup"    />',
        RecuperarEndososRehabilitables             : '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@REHABILITACION_ENDOSO.cdTipSup"            />',
        EndosoCambioModelo                         : '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@ENDOSO_CAMBIO_MODELO.cdTipSup"             />',
        EndosoCambioDescripcion                    : '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@ENDOSO_CAMBIO_DESCRIPCION.cdTipSup"        />',
        EndosoCambioTipoCarga                      : '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@ENDOSO_CAMBIO_TIPO_CARGA.cdTipSup"         />'
        
}
// catalogo de banderas ECD
var FlagsECD = {
	    PerfilCero  : '<s:property value="@mx.com.gseguros.portal.general.util.EcdFlags@PERFIL_0.perfil" />',
	    PerfilUno   : '<s:property value="@mx.com.gseguros.portal.general.util.EcdFlags@PERFIL_1.perfil" />',
	    PerfilDos   : '<s:property value="@mx.com.gseguros.portal.general.util.EcdFlags@PERFIL_2.perfil" />',
	    PerfilTres  : '<s:property value="@mx.com.gseguros.portal.general.util.EcdFlags@PERFIL_3.perfil" />'
};

</script>