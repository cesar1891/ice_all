package mx.com.gseguros.portal.consultas.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.consultas.model.DescargaLotePdfVO;
import mx.com.gseguros.portal.consultas.model.ImpresionLayoutVO;
import mx.com.gseguros.portal.cotizacion.model.Item;

public interface ExplotacionDocumentosManager
{
    
    public Map<String,Item> pantallaExplotacionDocumentos(String cdusuari, String cdsisrol) throws Exception;
    
    public String generarLote(
            String cdusuari
            ,String cdsisrol
            ,String cdtipram
            ,String cdtipimp
            ,String tipolote
            ,List<Map<String, String>> movs
            )throws Exception;
    
    
    /**
     * Se imprime un lote de documentos
     * @param lote
     * @param hoja
     * @param peso
     * @param cdtipram
     * @param cdtipimp
     * @param tipolote
     * @param dsimpres
     * @param charola1
     * @param charola2
     * @param cdusuari
     * @param cdsisrol
     * @param test
     * @param esDuplex Indica si la impresora soporta la opcion duplex (doble cara)
     * @return Archivo con la lista de nombres de archivos no encontrados
     * @throws Exception
     */
    public File imprimirLote(
            String lote
            ,String hoja
            ,String peso
            ,String cdtipram
            ,String cdtipimp
            ,String tipolote
            ,String dsimpres
            ,String charola1
            ,String charola2
            ,String cdusuari
            ,String cdsisrol
            ,boolean test
            ,boolean esDuplex
            )throws Exception;

    public Map<String,Item> pantallaExplotacionRecibos(String cdusuari, String cdsisrol) throws Exception;
    
    public Map<String,Item> pantallaPermisosImpresion(String cdusuari, String cdsisrol) throws Exception;
    
    public void movPermisoImpresion(
            String tipo
            ,String cdusuari
            ,String cdunieco
            ,String cdtipram
            ,String clave
            ,String funcion
            ,String accion
            )throws Exception;
    
    public void actualizarStatusRemesa(
            List<String> ntramite
            ,String status
            ,String cdusuari
            ,String cdsisrol
            )throws Exception;
    
    /*
    public Map<String,String> generarRemesaEmisionEndoso(
            String cdusuari
            ,String cdsisrol
            ,String cdtipimp
            ,String cdunieco
            ,String cdramo
            ,String estado
            ,String nmpoliza
            )throws Exception;
    */
    
    public Map<String,String> marcarImpresionOperacion(
            String cdusuari
            ,String cdsisrol
            ,String cdunieco
            ,String cdramo
            ,String estado
            ,String nmpoliza
            ,String marcar
            ,String ntramiteIn
            )throws Exception;
    
    public String recuperarColumnasGridPol(
            String cdsisrol
            ,String cdtipram
            ,String pantalla
            )throws Exception;
    
    public DescargaLotePdfVO descargarLote(
            String lote
            ,String hoja
            ,String peso
            ,String cdtipram
            ,String cdtipimp
            ,String tipolote
            ,String cdusuari
            ,String cdsisrol
            )throws Exception;
    
    public DescargaLotePdfVO descargarLoteDplx(
            String lote
            ,String hoja
            ,String peso
            ,String cdtipram
            ,String cdtipimp
            ,String tipolote
            ,String cdusuari
            ,String cdsisrol
            )throws Exception;
    
    public ImpresionLayoutVO verificaLayout(
            List<Map<String,String>> layout,
            String tpdocum,
            String cdusuari,
            String cdsisrol,
            String usuario,
            String pass,
            String dirLayouts,
            String server1,
            String server2
            )throws Exception;
    
    public DescargaLotePdfVO generaPdfLayout(
            Map<String, String> documentos,
            String cdtipram,
            String hoja,
            boolean duplex
            ) throws Exception;
    
    public String borrarDatosLayout(String pv_idproceso_i) throws Exception;

    public String obtenerCdagente(String pv_cdusuari_i) throws Exception;

    public List<Map<String, String>> polizasImprimirPromotor(
            String promotor, 
            String cdtipram, 
            String cduniecos, 
            String cdramo,
            String ramo, 
            String nmpoliza, 
            String fefecha, 
            String cdusuariLike, 
            String cdagente, 
            String cdusuariSesion,
            String cduniecoSesion, UserVO usuario) throws Exception;

}