package mx.com.gseguros.portal.cotizacion.model;


/**
 * ESPEJO DE PKG_SATELITES2.P_MOV_TVALOSIT		
 * @author jair
 *
 */
public class PMovTvalositDTO
{
	private String cdunieco
	               ,cdramo
	               ,estado
	               ,nmpoliza
	               ,nmsituac
	               ,nmsuplem
	               ,status
	               ,cdtipsit
	               ,accion;
	
	public String otvalor1  , otvalor2  , otvalor3  , otvalor4  , otvalor5  , otvalor6  , otvalor7  , otvalor8  , otvalor9  , otvalor10
	             ,otvalor11 , otvalor12 , otvalor13 , otvalor14 , otvalor15 , otvalor16 , otvalor17 , otvalor18 , otvalor19 , otvalor20
	             ,otvalor21 , otvalor22 , otvalor23 , otvalor24 , otvalor25 , otvalor26 , otvalor27 , otvalor28 , otvalor29 , otvalor30
	             ,otvalor31 , otvalor32 , otvalor33 , otvalor34 , otvalor35 , otvalor36 , otvalor37 , otvalor38 , otvalor39 , otvalor40
	             ,otvalor41 , otvalor42 , otvalor43 , otvalor44 , otvalor45 , otvalor46 , otvalor47 , otvalor48 , otvalor49 , otvalor50
	             ,otvalor51 , otvalor52 , otvalor53 , otvalor54 , otvalor55 , otvalor56 , otvalor57 , otvalor58 , otvalor59 , otvalor60
	             ,otvalor61 , otvalor62 , otvalor63 , otvalor64 , otvalor65 , otvalor66 , otvalor67 , otvalor68 , otvalor69 , otvalor70
	             ,otvalor71 , otvalor72 , otvalor73 , otvalor74 , otvalor75 , otvalor76 , otvalor77 , otvalor78 , otvalor79 , otvalor80
	             ,otvalor81 , otvalor82 , otvalor83 , otvalor84 , otvalor85 , otvalor86 , otvalor87 , otvalor88 , otvalor89 , otvalor90
	             ,otvalor91 , otvalor92 , otvalor93 , otvalor94 , otvalor95 , otvalor96 , otvalor97 , otvalor98 , otvalor99;
	
	public PMovTvalositDTO(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String status
			,String cdtipsit
			,String accion)
	{
		this.cdunieco = cdunieco;
		this.cdramo   = cdramo;
		this.estado   = estado;
		this.nmpoliza = nmpoliza;
		this.nmsituac = nmsituac;
		this.nmsuplem = nmsuplem;
		this.status   = status;
		this.cdtipsit = cdtipsit;
		this.accion   = accion;
	}
	
	public void setOtvalor(int cdatribu, String valor) throws Exception
	{
		this.getClass().getDeclaredField("otvalor"+cdatribu).set(this,valor);
	}
	
	public String[] indexar()
	{
		return new String[]{
				this.cdunieco
				,this.cdramo
				,this.estado
				,this.nmpoliza
				,this.nmsituac
				,this.nmsuplem
				,this.status
				,this.cdtipsit
				,this.otvalor1
				,this.otvalor2
				,this.otvalor3
				,this.otvalor4
				,this.otvalor5
				,this.otvalor6
				,this.otvalor7
				,this.otvalor8
				,this.otvalor9
				,this.otvalor10
				,this.otvalor11
				,this.otvalor12
				,this.otvalor13
				,this.otvalor14
				,this.otvalor15
				,this.otvalor16
				,this.otvalor17
				,this.otvalor18
				,this.otvalor19
				,this.otvalor20
				,this.otvalor21
				,this.otvalor22
				,this.otvalor23
				,this.otvalor24
				,this.otvalor25
				,this.otvalor26
				,this.otvalor27
				,this.otvalor28
				,this.otvalor29
				,this.otvalor30
				,this.otvalor31
				,this.otvalor32
				,this.otvalor33
				,this.otvalor34
				,this.otvalor35
				,this.otvalor36
				,this.otvalor37
				,this.otvalor38
				,this.otvalor39
				,this.otvalor40
				,this.otvalor41
				,this.otvalor42
				,this.otvalor43
				,this.otvalor44
				,this.otvalor45
				,this.otvalor46
				,this.otvalor47
				,this.otvalor48
				,this.otvalor49
				,this.otvalor50
				,this.otvalor51
				,this.otvalor52
				,this.otvalor53
				,this.otvalor54
				,this.otvalor55
				,this.otvalor56
				,this.otvalor57
				,this.otvalor58
				,this.otvalor59
				,this.otvalor60
				,this.otvalor61
				,this.otvalor62
				,this.otvalor63
				,this.otvalor64
				,this.otvalor65
				,this.otvalor66
				,this.otvalor67
				,this.otvalor68
				,this.otvalor69
				,this.otvalor70
				,this.otvalor71
				,this.otvalor72
				,this.otvalor73
				,this.otvalor74
				,this.otvalor75
				,this.otvalor76
				,this.otvalor77
				,this.otvalor78
				,this.otvalor79
				,this.otvalor80
				,this.otvalor81
				,this.otvalor82
				,this.otvalor83
				,this.otvalor84
				,this.otvalor85
				,this.otvalor86
				,this.otvalor87
				,this.otvalor88
				,this.otvalor89
				,this.otvalor90
				,this.otvalor91
				,this.otvalor92
				,this.otvalor93
				,this.otvalor94
				,this.otvalor95
				,this.otvalor96
				,this.otvalor97
				,this.otvalor98
				,this.otvalor99
				,this.accion
		};
	}
	
}