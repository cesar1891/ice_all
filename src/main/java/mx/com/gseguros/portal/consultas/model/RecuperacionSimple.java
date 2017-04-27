package mx.com.gseguros.portal.consultas.model;

public enum RecuperacionSimple
{
	//NOMBRE                                               L=LISTA/M=MAPA
	 RECUPERAR_CLAUSULAS_POLIZA                              ("L")
	,RECUPERAR_COBERTURAS_ENDOSO_DEVOLUCION_PRIMAS           ("L")
	,VERIFICAR_CODIGO_POSTAL_FRONTERIZO                      ("M")
	,RECUPERAR_CONFIGURACION_VALOSIT_FLOTILLAS               ("L")
	,RECUPERAR_CONTEO_BLOQUEO                                ("M")
	,RECUPERAR_DATOS_VEHICULO_RAMO_5                         ("M")
	,RECUPERAR_DERECHOS_POLIZA_POR_PAQUETE_RAMO_1            ("M")
	,RECUPERAR_DESCUENTO_RECARGO_RAMO_5                      ("M")
	,RECUPERAR_DETALLE_IMPRESION_LOTE                        ("M")
	,RECUPERAR_DETALLE_REMESA                                ("L")
	,RECUPERAR_DETALLES_COBERTURAS_COTIZACION_AUTOS_FLOTILLA ("L")
	,RECUPERAR_DETALLES_COTIZACION_AUTOS_FLOTILLA            ("L")
	,RECUPERAR_DSATRIBUS_TATRISIT                            ("M")
	,RECUPERAR_DSATRIBUS_TATRIPOL                            ("M")
	,RECUPERAR_ENDOSOS_CANCELABLES                           ("L")
	,RECUPERAR_ENDOSOS_REHABILITABLES                        ("L")
	,RECUPERAR_FAMILIAS_POLIZA                               ("L")
	,RECUPERAR_FECHAS_LIMITE_ENDOSO                          ("M")
	,RECUPERAR_GRUPOS_POLIZA                                 ("L")
	,RECUPERAR_HISTORICO_POLIZA                              ("L")
	,RECUPERAR_IMPRESIONES_DISPONIBLES                       ("M")
	,RECUPERAR_IMPRESORAS                                    ("L")
	,RECUPERAR_INCISOS_POLIZA_GRUPO_FAMILIA                  ("L")
	,RECUPERAR_INCISOS_POLIZA_GRUPO_FAMILIAENDOSO            ("L")
	,RECUPERAR_MOVIMIENTOS_ENDOSO_ALTA_BAJA_ASEGURADO        ("L")
	,RECUPERAR_MPOLIPER_OTROS_ROLES_POR_NMSITUAC             ("L")
	,RECUPERAR_PERMISO_USUARIO_DEVOLUCION_PRIMAS             ("M")
	,RECUPERAR_PERMISOS_IMPRESION                            ("L")
	,RECUPERAR_POLIZAS_ENDOSABLES                            ("L")
	,RECUPERAR_POLIZAS_PARA_EXPLOTAR_DOCS                    ("L")
	,RECUPERAR_PORCENTAJE_RECARGO_POR_PRODUCTO               ("M")
	,RECUPERAR_RECIBOS_PARA_EXPLOTAR_DOCS                    ("L")
	,RECUPERAR_REVISION_COLECTIVOS                           ("L")
	,RECUPERAR_REVISION_COLECTIVOS_FINAL                     ("L")
	,RECUPERAR_REVISION_COLECTIVOS_ENDOSOS                   ("L")
	,RECUPERAR_TATRISIT_AMPARADO                             ("M")
	,RECUPERAR_TEXTO_CLAUSULA_POLIZA                         ("M")
	,RECUPERAR_TVALOSIT                                      ("L")
	,RECUPERAR_ULTIMO_NMSUPLEM                               ("M")
	,RECUPERAR_USUARIOS_REASIGNACION_TRAMITE                 ("L")
	,RECUPERAR_VALOR_ATRIBUTO_UNICO                          ("M")
	,RECUPERAR_VALOR_MAXIMO_SITUACION_POR_ROL                ("M")
	,RECUPERAR_VALORES_ATRIBUTOS_FACTORES                    ("L")
	,RECUPERAR_VALORES_PANTALLA                              ("L")
	,RECUPERAR_TTIPTRAMC                                     ("L")  // MC
	,RECUPERAR_TTIPFLUMC 									 ("L")  // MC
	,RECUPERAR_TESTADOMC									 ("L")  // MC
	,RECUPERAR_TPANTMC										 ("L")  // MC
	,RECUPERAR_TCOMPMC										 ("L")  // MC
	,RECUPERAR_TPROCMC 									 	 ("L")  // MC
	,RECUPERAR_TDOCUME                                       ("L")  // MC
	,RECUPERAR_TREQUISI                                      ("L")   // MC
	,RECUPERAR_TICONOS                                       ("L")  // MC
	,RECUPERAR_TFLUJOMC                                      ("L")  // MC
	,RECUPERAR_TFLUEST 									 	 ("L")  // MC
	,RECUPERAR_TFLUESTROL									 ("L")  // MC
	,RECUPERAR_TFLUESTAVI									 ("L")  // MC
    ,RECUPERAR_TFLUPANT                                      ("L")  // MC
	,RECUPERAR_TFLUCOMP                                      ("L")  // MC
	,RECUPERAR_TFLUPROC                                      ("L")  // MC
	,RECUPERAR_TFLUVAL 									 	 ("L")  // MC
	,RECUPERAR_TFLUREV										 ("L")  // MC
	,RECUPERAR_TFLUREVDOC									 ("L")  // MC
	,RECUPERAR_TFLUACC                                       ("L")  // MC
	,RECUPERAR_TFLUACCROL                                    ("L")  // MC
	,RECUPERAR_ROLES                                         ("L")
	,RECUPERAR_SWVISPRE_TRAMITE                              ("M")
	,RECUPERAR_DIAS_FECHA_FACTURACION						 ("M")
	,RECUPERAR_PERMISO_BOTON_GENERAR_COLECTIVO               ("M")
	,RECUPERAR_EXCLUSION_TURNADOS							 ("L")
	,RECUPERAR_ESTADO_BOTON_EMITIR							 ("M")
	,RECUPERAR_CLAVES_PLAN_RAMO4							 ("L")
	,RECUPERAR_LISTA_TATRISIT_SIN_PADRE						 ("L")
	,RECUPERAR_LISTA_FILTRO_PROPIEDADDES_INCISO              ("L")
	,RECUPERAR_CDUNIEXT_POR_LLAVE_POLIZA                     ("M")
	,RECUPERAR_TTIPFLUROL                                    ("L")
	,RECUPERAR_TFLUJOROL                                     ("L")
	,RECUPERAR_FLUJO_POR_DESCRIPCION                         ("M")
	,RECUPERAR_TFLUMAIL										 ("L")
	,RECUPERAR_TVARMAIL										 ("L")
	,RECUPERAR_MPOLIZAS_POR_PARAMETROS_VARIABLES             ("L")
	,RECUPERAR_SI_ES_CDRAMO_DE_SALUD                         ("M")
	,RECUPERAR_REQUISITOS_DATOS_TRAMITE                      ("L")
	,RECUPERAR_TABLERO_INDICADORES                           ("M")
	,RECUPERAR_TRAMITES_POR_LINEA_NEGOCIO                    ("L")
	,RECUPERAR_TRAMITES_LINEANEGOCIO_POR_RAMO                ("L")
	,RECUPERAR_DETALLE_LINEA_NEGOCIO                         ("L")
	,RECUPERAR_DETALLE_LINEA_NEGOCIO_LP                      ("LP")
	,RECUPERAR_LINEANEGOCIO_POR_SUCURSAL                     ("L")
	,RECUPERAR_LINEANEGOCIO_POR_USUARIO                      ("L")
	,RECUPERAR_LINEANEGOCIO_POR_USUARIO_LP                   ("LP")
	,RECUPERAR_TRAMITES_POR_TIPO                             ("L")
	//,RECUPERAR_TRAMITES_PENDIENTES_POR_DIAS                  ("L")
	,RECUPERAR_TRAMITES_PENDIENTES_POR_HORAS                  ("L")
	//,RECUPERAR_TRAMITES_PENDIENTES_POR_DIAS_LP               ("LP")
	,RECUPERAR_TRAMITES_PENDIENTES_POR_HORAS_LP               ("LP")
	,RECUPERAR_CORREO_EMISION_TRAMITE                        ("M")
	,RECUPERAR_TODAS_SUCURSALES                              ("L")
	,RECUPERAR_VALIDACION_POR_CDVALIDAFK                     ("L")
	,RECUPERAR_COTIZACIONES_COLECTIVAS_APROBADAS             ("L")
	,RECUPERAR_ASEGURADOS_ENDOSO_ALTA                        ("L")
	,RECUPERAR_ASEGURADO_COMPLETO_ENDOSO_ALTA                ("M")
	,RECUPERAR_PERSONAS_FISICAS_POR_RFC_MULTIPLE_DOMICILIO   ("L")
	,RECUPERAR_PERSONA_ENDOSO_ALTA                           ("M")
	,RECUPERAR_BUSQUEDA_ASEGURADOS_END_COBERTURAS            ("L")
	,RECUPERAR_COBERTURAS_AMPARADAS_CONFIRMADAS              ("L")
	,RECUPERAR_COBERTURAS_DISPONIBLES                        ("L")
	,RECUPERAR_COBERTURAS_AGREGADAS                          ("L")
	,RECUPERAR_ASEGURADOS_AFECTADOS_END_COBERTURAS           ("L")
	,RECUPERAR_COBERTURAS_BORRADAS                           ("L")
	,RECUPERAR_COBERTURAS_PRIMA_NETA                         ("L")
	,RECUPERAR_DESPACHADOR_DATOS_SUCURSALES                  ("L")
	,RECUPERAR_DESPACHADOR_DATOS_USUARIOS                    ("L")
    ,RECUPERAR_DESPACHADOR_DATOS_USER_ALL_X_ROL              ("L")
    ,RECUPERAR_DESPACHADOR_DATOS_ZONA                        ("L")
    ,RECUPERAR_ENDOSOS_SINIESTRALIDAD                        ("L")
    ,RECUPERAR_ENDOSOS_SINIESTRALIDAD_REHA                   ("L")
    ,RECUPERAR_CORREO_AGENTE_TRAMITE                         ("M")
    ,RECUPERAR_HISTORIAL_TRAMITE                             ("L")
    ,RECUPERAR_DETALLES_TRAMITE                              ("L")
    ,VALIDA_RENOVACION_COLECTIVO                             ("L")
    ,RENOVAR_X_FECHAS_COLECTIVOS                             ("L")
    ,RECUPERAR_RANGO_DESCUENTO_RECARGO                       ("M")
	;
	 
	private String tipo;

	private RecuperacionSimple(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}
	
}
