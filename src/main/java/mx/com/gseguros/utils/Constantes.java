package mx.com.gseguros.utils;

public interface Constantes {
	
	public static final String USER = "USUARIO";
	public static final String CDUNIECO = "1";
	public static final String FORMATO_FECHA = "dd/MM/yyyy";
	public static final String SEPARADOR_ARCHIVO = "/";//File.separator;
	
	public static final String MSG_ID_ERROR      = "100000";
	public static final String MSG_ID_OK         = "200000";
	public static final String MSG_TITLE_ERROR   = "1";
	public static final String MSG_TITLE_OK      = "2";
	public static final String MSG_TITLE_WARNING = "4";
    
    public static final String INSERT_MODE = "I";
    public static final String UPDATE_MODE = "U";
    public static final String DELETE_MODE = "D";
    
    public static final String SI = "S";
    public static final String NO = "N";
    
    public static final String STATUS_VIVO   = "V";
    public static final String STATUS_MUERTO = "M";
    
    public static final String POLIZA_WORKING= "W";
    public static final String POLIZA_MASTER = "M";
    
    public static final String MAUTSINI_AREA_RECLAMACIONES = "RE";
    public static final String MAUTSINI_AREA_MEDICA        = "ME";
    public static final String MAUTSINI_SINIESTRO          = "S";
    public static final String MAUTSINI_FACTURA            = "F";
    public static final String MAUTSINI_DETALLE            = "D";
	
    /**
     * Mensaje Atributo Variable cuando tiene hijos asociados
     */
    public static final String MESSAGE_ATRIBUTO_CON_HIJOS_ASOCIADOS = "El atributo tiene hijos asociados, si se elimina tambi�n se eliminar�n esas asociaciones. �Desea continuar?";
    
    public static final String REGISTRO_DUPLICADO = "100006";
    
    public static final String NMSITUAC_TITULAR = "1";
    
    public static final String USUARIO_SISTEMA = "SISTEMA",
                               ROL_SISTEMA     = "SISTEMA";
    
    public static final String PROCESO_FLAGS = "FLAGS";
    
    public static final String MODULO_FLAGS        = "FLAGS",
                               MODULO_EMISION      = "EMISION",
                               MODULO_MESA_CONTROL = "MESADECONTROL",
                               MODULO_COTIZACION   = "COTIZACION",
                               MODULO_GENERAL      = "GENERAL";
    
    public static final String EVENTO_REGRESAR              = "REGRESAR",
                               EVENTO_FLAG_VERDE            = "VERDE",
                               EVENTO_FLAG_AMARILLA         = "AMARILLA",
                               EVENTO_FLAG_ROJA             = "ROJA",
                               EVENTO_FLAG_VENCIDA          = "VENCIDA",
                               EVENTO_COMPRAR_TRAMITE_MC    = "COMTRAMITMC",
                               EVENTO_EMISION               = "EMISION",
                               EVENTO_APROBACION_COT_COL    = "APROBCOTCOL",
                               EVENTO_COTIZAR               = "COTIZA",
                               EVENTO_GENERAR_TRAMITE_GRUPO = "GENTRAGRUP",
                               EVENTO_TURNAR                = "TURNATRA",
                               EVENTO_NUEVO_TRAMITE         = "NUETRAMITMC";
}
