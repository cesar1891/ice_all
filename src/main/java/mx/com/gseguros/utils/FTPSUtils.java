package mx.com.gseguros.utils;

import java.io.File;

import mx.com.gseguros.portal.catalogos.controller.ClausuladoAction;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.log4j.Logger;

/**
 * Utiler&iacute;as para manejar recursos v&iacute;a el protocolo Secure FTP
 * @author Ricardo
 *
 */
public class FTPSUtils {
	
	private static Logger logger = Logger.getLogger(ClausuladoAction.class);
	
    /**
     * Method to check if the remote file exists in the specified remote location
     * 
     * @param hostName       Nombre del servidor
     * @param username       Nombre de usuario
     * @param password       Password de usuario
     * @param remoteFilePath Ruta absoluta y nombre del archivo remoto (usando / como separador)
     * @return true si el archivo remoto existe, false sino
     */
    public static boolean exist(String hostName, String username, String password, 
            String remoteFilePath) {
        
        boolean success = false;
        
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            manager.init();

            // Create remote object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(hostName, username, password,
                            remoteFilePath), createDefaultOptions());
            
            success = remoteFile.exists();
            logger.info("Archivo " + remoteFilePath + " existe: " + success);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            manager.close();
        }
        return success;
    }
    
    
    /**
     * Sube un archivo en un servidor remoto
     * 
     * @param hostName       Nombre del servidor
     * @param username       Nombre de usuario
     * @param password       Password de usuario
     * @param localFilePath  Ruta absoluta y nombre del archivo local a subir
     * @param remoteFilePath Ruta absoluta y nombre que tendr&aacute; el archivo remoto (usando / como separador)
     *                          <br/> Si no existe la ruta, la crea
     * @return true si el archivo se subi&oacute; correctamente, false sino
     */
    public static boolean upload(String hostName, String username, String password, 
            String localFilePath, String remoteFilePath) {

        boolean success = false;
        
        StandardFileSystemManager manager = new StandardFileSystemManager();
        
        try {
            File file = new File(localFilePath);
            if (!file.exists()) {
                throw new RuntimeException("No se encontro el archivo local: " + localFilePath);
            }
            
            manager.init();
            
            // Create local file object
            FileObject localFile = manager.resolveFile(file.getAbsolutePath());
            
            // Create remote file object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(hostName, username, password,
                            remoteFilePath), createDefaultOptions());
            
            // Copy local file to sftp server
            remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
            
            logger.info("Archivo subido exitosamente: " + remoteFilePath);
            success = true;
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            manager.close();
        }
        return success;
    }
    
    
    /**
     * Descarga un archivo remoto a una ruta local
     * 
     * @param hostName       Nombre del servidor
     * @param username       Nombre de usuario
     * @param password       Password de usuario
     * @param localFilePath  Ruta absoluta Y nombre del archivo local (usando \\ como separador)
     * @param remoteFilePath Ruta absoluta y nombre del archivo remoto (usando / como separador)
     * @return true si el archivo se descarg&oacute; correctamente, false sino
     */
    public static boolean download(String hostName, String username, String password, 
            String localFilePath, String remoteFilePath) {

        boolean success = false;
        
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            // Para evitar que sobreescriba una carpeta conviriendola en un archivo:
            File file = new File(localFilePath);
            logger.debug("existe? " + file.exists());
            logger.debug("es folder? " + file.isDirectory());
            logger.debug("es archivo? " + file.isFile());
            if(file.exists() && file.isDirectory()) {
                throw new RuntimeException("La ruta local debe contiener el nombre del archivo: " + localFilePath);
            }
            
            manager.init();

            // Append _downlaod_from_sftp to the given file name.
            // String downloadFilePath = localFilePath.substring(0,
            // localFilePath.lastIndexOf(".")) + "_downlaod_from_sftp" +
            // localFilePath.substring(localFilePath.lastIndexOf("."),
            // localFilePath.length());

            // Create local file object. Change location if necessary for new
            // downloadFilePath
            FileObject localFile = manager.resolveFile(localFilePath);

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(hostName, username, password,
                            remoteFilePath), createDefaultOptions());
            
            if (!remoteFile.exists()) {
                throw new RuntimeException("No existe el archivo origen: " + remoteFile);
            }
            
            // Copy local file to sftp server
            localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
            
            logger.info("Archivo descargado exitosamente: " + localFilePath);
            success = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            manager.close();
        }
        return success;
    }
    
    
    /**
     * Descarga un los archivos de una carpeta remoto a una ruta local
     * 
     * @param hostName            Nombre del servidor
     * @param username            Nombre de usuario
     * @param password            Password de usuario
     * @param localDirectoryPath  Ruta absoluta Y nombre de la carpeta local (usando \\ como separador)
     * @param remoteDirectoryPath Ruta absoluta y nombre de la carpeta remoto (usando / como separador)
     * @return true si el archivo se descarg&oacute; correctamente, false sino
     */
    public static boolean downloadChildrenFiles(String hostName, String username, String password, 
            String localDirectoryPath, String remoteDirectoryPath) {

        boolean success = false;
        
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            // Para evitar que sobreescriba una carpeta conviriendola en un archivo:
            File directory = new File(localDirectoryPath);
            logger.debug("existe? " + directory.exists());
            logger.debug("es folder? " + directory.isDirectory());
            logger.debug("es archivo? " + directory.isFile());
            if(!directory.exists())
            {
            	throw new RuntimeException("No existe la carpeta local: " + localDirectoryPath);
            }
            if(!directory.isDirectory())
            {
                throw new RuntimeException("La ruta local debe ser carpeta: " + localDirectoryPath);
            }
            
            manager.init();

            // Append _downlaod_from_sftp to the given file name.
            // String downloadFilePath = localFilePath.substring(0,
            // localFilePath.lastIndexOf(".")) + "_downlaod_from_sftp" +
            // localFilePath.substring(localFilePath.lastIndexOf("."),
            // localFilePath.length());

            // Create local file object. Change location if necessary for new
            // downloadFilePath
            FileObject localDirectory = manager.resolveFile(localDirectoryPath);

            // Create remote file object
            FileObject remoteDirectory = manager.resolveFile(
                    createConnectionString(hostName, username, password,
                            remoteDirectoryPath), createDefaultOptions());
            
            if (!remoteDirectory.exists()) {
                throw new RuntimeException("No existe la carpeta origen: " + remoteDirectory);
            }
            
            // Copy local file to sftp server
            localDirectory.copyFrom(remoteDirectory, Selectors.SELECT_CHILDREN);
            
            logger.info("Archivos hijos descargados exitosamente: " + localDirectoryPath);
            success = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            manager.close();
        }
        return success;
    }
    
    
    /**
     * Mueve un archivo entre directorios de un servidor remoto
     * 
     * @param hostName            Nombre del servidor
     * @param username            Nombre de usuario
     * @param password            Password de usuario
     * @param remoteSrcFilePath   
     * @param remoteDestFilePath  
     * @return 
     */
    public static boolean move(String hostName, String username, String password,
            String remoteSrcFilePath, String remoteDestFilePath) {
        
        boolean success = false;
        
        StandardFileSystemManager manager = new StandardFileSystemManager();
        
        try {
            manager.init();
            
            // Create remote objects:
            FileObject remoteSrcFile = manager.resolveFile(
                    createConnectionString(hostName, username, password, remoteSrcFilePath), 
                    createDefaultOptions());
            FileObject remoteDestFile = manager.resolveFile(
                    createConnectionString(hostName, username, password, remoteDestFilePath),
                    createDefaultOptions());
            
            if (!remoteSrcFile.exists()) {
                throw new RuntimeException("No existe el archivo origen: " + remoteSrcFile);
            }
            if (!remoteDestFile.getParent().exists()) {
                throw new RuntimeException("No existe la carpeta destino: " + remoteDestFile.getParent());
            }
            
            remoteSrcFile.moveTo(remoteDestFile);
            logger.info("Archivo movido exitosamente: " + remoteDestFile);
            success = true;
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            manager.close();
        }
        return success;
    }
    
    
    /**
     * Elimina un archivo remoto 
     * @param hostName       Nombre del servidor
     * @param username       Nombre de usuario
     * @param password       Password de usuario
     * @param remoteFilePath Ruta absoluta y nombre del archivo remoto (usando / como separador)
     * @return true si el archivo se elimin&oacute; correctamente, false sino
     */
    public static boolean delete(String hostName, String username, String password, 
            String remoteFilePath) {
        
        boolean success = false;
        
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            manager.init();

            // Create remote object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(hostName, username, password,
                            remoteFilePath), createDefaultOptions());

            if (!remoteFile.exists()) {
                throw new RuntimeException("No existe el archivo origen: " + remoteFile);
            }
            
            remoteFile.delete();
            logger.info("Archivo borrado exitosamente: " + remoteFilePath);
            success = true;
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            manager.close();
        }
        return success;
    }
    
    
    /**
     * Genera la URL o cadena de conexi&oacute;n para SFTP  
     * 
     * @param hostName Nombre del servidor
     * @param username Nombre de usuario
     * @param password Password de usuario
     * @param remoteFilePath Ruta absoluta y nombre del archivo remoto (usando / como separador)
     * @return concatenated SFTP URL string
     */
    public static String createConnectionString(String hostName, String username, String password, 
            String remoteFilePath) {
        
        String connectionString = "sftp://" + username + ":" + password + "@" + hostName + "/" + remoteFilePath;
        logger.info(new StringBuilder("Cadena de conexion: ").append(connectionString));
        return connectionString;
    }
    
    
    /**
     * Crea una configuraci&oacute;n SFTP por defecto
     * 
     * @return Objeto FileSystemOptions con las opciones de configuracion
     * @throws FileSystemException
     */
    private static FileSystemOptions createDefaultOptions() throws FileSystemException {
        
        // Create SFTP options
        FileSystemOptions opts = new FileSystemOptions();

        // SSH Key checking
        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");

        /*
         * Using the following line will cause VFS to choose File System's Root
         * as VFS's root. If I wanted to use User's home as VFS's root then set
         * 2nd method parameter to "true"
         */
        // Root directory set to user home
        SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);

        // Timeout is count by Milliseconds
        SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);
        
        // Setting up Preferred Authentications
        SftpFileSystemConfigBuilder.getInstance().setPreferredAuthentications(opts, "publickey,keyboard-interactive,password");

        return opts;
    }
    
    
//  public static void main(String[] args) {
//    
//      Prueba de Secure FTP:
// 		FTPSUtils.subeArchivo("10.1.1.133", "oinstall", "j4v4n3s", "software", "E:\\", "CARATULA.pdf");
// 		FTPSUtils.subeArchivo("10.1.1.133", "oinstall", "j4v4n3s", "software", new File("E:\\CARATULA.pdf"));
    
//      String hostName = "10.1.1.133";
//      String username = "oinstall";
//      String password = "j4v4n3s";
//      String localFilePath      = "E:\\tmp\\a";
//      String remoteFilePath     = "/export/home/oinstall/testrbs/tmp";
//      String remoteTempFilePath = "/export/home/oinstall/testrbs/tmp/nuevo2.pdf";
//    
//      boolean exito = exist(hostName, username, password, remoteFilePath);
//      boolean exito = upload(hostName, username, password, localFilePath, remoteFilePath);
//      boolean exito = download(hostName, username, password, localFilePath, remoteFilePath);
//      boolean exito = move(hostName, username, password, remoteFilePath, remoteTempFilePath);
//      boolean exito = delete(hostName, username, password, remoteFilePath);
//      System.out.println("EXITO=" + exito);
//  }
	
	
	/**
	 * Sube un archivo v&iacute;a Secure FTP
	 * @param host
	 * @param user
	 * @param password
	 * @param remoteFilePath
	 * @param localDirectory
	 * @param file
	 * @return true si lo sube correctamente, false si no
	 */
	public static boolean subeArchivo(String host, String user, String password, 
			String remoteFilePath, File file) {
		
		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {
			logger.debug("host=" + host);
			logger.debug("user=" + user);
			logger.debug("password="+password);
			logger.debug("remoteDirectory=" + remoteFilePath);
			logger.debug("file=" + file.getAbsolutePath());
			
			// check if the file exists
			if (!file.exists()) {
				throw new RuntimeException("Error. Local file not found");
			}

			// Initializes the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);
			SftpFileSystemConfigBuilder.getInstance().setPreferredAuthentications(opts, "publickey,keyboard-interactive,password");

			// Create the SFTP URI using the host name, user, password, remote
			// path and file name
			String sftpUri = "sftp://" + user + ":" + password + "@"
					+ host + "/" + remoteFilePath + "/" + file.getName();

			// Create local file object
			FileObject localFile = manager.resolveFile(file.getAbsolutePath());

			logger.debug("localFile=" + localFile);
			
			// Create remote file object
			FileObject remoteFile = manager.resolveFile(sftpUri, opts);
			
			logger.debug("remoteFile=" + remoteFile);

			// Copy local file to sftp server
			remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
			logger.info("File upload successful via Secure FTP");

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return false;
		} finally {
			manager.close();
		}
		return true;
	}

}