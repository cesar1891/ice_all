package mx.com.gseguros.portal.documentos.model;

public enum Documento
{
	REMESA_IMPRESION_LOTE              ("59")
	,RECIBO                            ("60")
	,EXTERNO_CARATULA                  ("61")
	,EXTERNO_AP                        ("62")
	,EXTERNO_CAIC                      ("63")
	,EXTERNO_INCISOS_FLOTILLAS         ("64")
	,EXTERNO_TARJETA_IDENTIFICACION    ("65")
	,EXTERNO_REDUCE_GS                 ("66")
	,EXTERNO_GESTORIA_GS               ("67")
	,EXTERNO_ESPECIF_SEGURO_VIDA       ("68")
	,EXTERNO_CONDIC_GRALES_SEGURO_VIDA ("69")
	,EXTERNO_CARATULA_B                ("71")
	,EXTERNO_AEUA                      ("75")
	,EXTERNO_DOCUMENTO_EXTRA           ("76")
	;
	
	private String cdmoddoc;

	private Documento(String cdmoddoc)
	{
		this.cdmoddoc = cdmoddoc;
	}

	public String getCdmoddoc()
	{
		return cdmoddoc;
	}
}