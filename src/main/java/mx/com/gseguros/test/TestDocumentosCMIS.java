package mx.com.gseguros.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/*import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.client.util.ContentStreamUtils;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;*/
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.utils.Utils;

@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/annotations")
public class TestDocumentosCMIS extends PrincipalCoreAction {
	
	private static final long serialVersionUID = 3417469798703366675L;
	
	private static Logger logger = LoggerFactory.getLogger(TestDocumentosCMIS.class);
	
	private Map<String, String> params;
	
    private boolean success;
	
	@Action(value="invocaCMIS",
			results={@Result(name="success", type="json")}
	)
	public String invocaCMIS() throws Exception {
		/*
		try {
		    
		    // default factory implementation
            SessionFactory factory = SessionFactoryImpl.newInstance();
            Map<String, String> parameters = new HashMap<String, String>();
            
            // user credentials
            parameters.put(SessionParameter.USER, null);
            //parameters.put(SessionParameter.PASSWORD, "****");
            
            // connection settings
            parameters.put(SessionParameter.BROWSER_URL, "http://localhost:8080/chemistry-opencmis-server-inmemory-1.0.0/services11/ObjectService");
            parameters.put(SessionParameter.BINDING_TYPE, BindingType.BROWSER.value());
            parameters.put(SessionParameter.REPOSITORY_ID, "A1");
            
            // create session
            Session session = factory.createSession(parameters);
		    
		    Folder parent = session.getRootFolder();
		    
		    String textFileName = "arquetipos.pdf";
		    String contentType = "application/pdf";
		    
		    // create a ContentStream object from file
		    File file = new File("E:\\"+textFileName);
		    ContentStream contentStream = ContentStreamUtils.createFileContentStream(file);
		    
		    // prepare properties
            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(PropertyIds.NAME, textFileName);
            properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		    
            // create the document
            Document newDoc = parent.createDocument(properties, contentStream, VersioningState.NONE);
            
            logger.debug("Documento creado: {}", newDoc);
            
		} catch (Exception e) {
			Utils.manejaExcepcion(e);
		}
		*/
		return SUCCESS;
		
	}
	
	
	
	public static void main(String[] args) {
	    /*
        try {
            
	        // default factory implementation
            SessionFactory factory = SessionFactoryImpl.newInstance();
            Map<String, String> parameters = new HashMap<String, String>();
            
            // user credentials
            //parameters.put(SessionParameter.USER, "Otto");
            //parameters.put(SessionParameter.PASSWORD, "****");
            
            // connection settings
            parameters.put(SessionParameter.BROWSER_URL, "http://localhost:8080/chemistry-opencmis-server-inmemory-1.0.0/services11/ObjectService/");
            parameters.put(SessionParameter.BINDING_TYPE, BindingType.BROWSER.value());
            parameters.put(SessionParameter.REPOSITORY_ID, "A1");
            
            // create session
            Session session = factory.createSession(parameters);
            
            Folder parent = session.getRootFolder();
            
            String textFileName = "arquetipos.pdf";
            String contentType = "application/pdf";
            
            // create a ContentStream object from file
            File file = new File("E:\\"+textFileName);
            ContentStream contentStream = ContentStreamUtils.createFileContentStream(file);
            
            // prepare properties
            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(PropertyIds.NAME, textFileName);
            properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
            
            // create the document
            Document newDoc = parent.createDocument(properties, contentStream, VersioningState.NONE);
            
            logger.debug("Documento creado: {}", newDoc);
            
        } catch (Exception e) {
            Utils.manejaExcepcion(e);
        }
        
	    */
    }
	
	//Getters and setters:
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}