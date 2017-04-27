package mx.com.gseguros.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Host {

	public static void executeCommand(String command) {
		
		File archivo = null;
		FileWriter writer = null;
		
		try {
			//archivo = new File("/ice/tmp/hostCommand.log");
			archivo = new File("/u01/wlserver/wlapplogs/gseguros/hostCommand.log");
			writer = new FileWriter(archivo, true); 
			writer.write(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()) + " Iniciando   ejecucion de Host Command..." + "\n");
			
			String[] finalCommand;
			if (isWindows()) {
				
				
				finalCommand = new String[4];
				finalCommand[0] = "C:\\windows\\system32\\cmd.exe";
				finalCommand[1] = "/y";
				finalCommand[2] = "/c";
				finalCommand[3] = command;
			} else {
				finalCommand = new String[3];
				finalCommand[0] = "/bin/sh";
				finalCommand[1] = "-c";
				finalCommand[2] = command;
				//finalCommand = new String[1];
				//finalCommand[0] = command;
			}
			writer.write("Antes de ejecutar comando:      " + finalCommand[0]+ " " + finalCommand[1] + " " + finalCommand[2] + "\n");
//			writer.write("Antes de ejecutar comando: " + finalCommand[0] + "\n");
			final Process pr = Runtime.getRuntime().exec(finalCommand);
			writer.write("Despues de de ejecutar comando: " + finalCommand[0]+ " " + finalCommand[1] + " " + finalCommand[2] + "\n");
//			writer.write("Despues de de ejecutar comando: " + finalCommand[0]+ "\n");
			pr.waitFor();
			writer.write("Lanzando thread 1..." + "\n");
			new Thread(new Runnable() {
				public void run() {
					FileWriter writerOut = null;
					try {
						//File archivo = new File("/ice/tmp/out.log");
						File archivo = new File("/u01/wlserver/wlapplogs/gseguros/cmd_out.log");
						writerOut = new FileWriter(archivo, true);
						writerOut.write(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()) + " Inicio" + "\n");
						
						BufferedReader br_in = new BufferedReader(
								new InputStreamReader(pr.getInputStream()));
						String buff = null;
						while ((buff = br_in.readLine()) != null) {
							System.out.println("Process out :" + buff);
							writerOut.write(buff + "\n");
							try {
								Thread.sleep(1);
							} catch (Exception e) {
							}
						}
						writerOut.write(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()) + " Fin" + "\n");
						writerOut.close();
						br_in.close();
					} catch (IOException ioe) {
						System.out.println("Exception caught printing process output.");
						ioe.printStackTrace();
						try {
							writerOut.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			writer.write("Lanzando thread 2..." + "\n");
			new Thread(new Runnable() {
				public void run() {
					FileWriter writerErr = null;
					try {
						//File archivo = new File("/ice/tmp/err.log");
						File archivo = new File("/u01/wlserver/wlapplogs/gseguros/cmd_err.log");
						writerErr = new FileWriter(archivo, true);
						writerErr.write(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()) + " Inicio" + "\n");
						
						BufferedReader br_err = new BufferedReader(
								new InputStreamReader(pr.getErrorStream()));
						String buff = null;
						while ((buff = br_err.readLine()) != null) {
							System.out.println("Process err :" + buff);
							writerErr.write(buff + "\n");
							try {
								Thread.sleep(1);
							} catch (Exception e) {
							}
						}
						writerErr.write(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()) + " Fin" + "\n");
						writerErr.close();
						br_err.close();
					} catch (IOException ioe) {
						System.out
								.println("Exception caught printing process error.");
						ioe.printStackTrace();
						try {
							writerErr.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			
			writer.write(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()) + " Finalizando ejecucion de Host Command" + "\n");
			
		} catch (Exception ex) {
			System.out.println(ex.getLocalizedMessage());
			try {
				writer.write("ERROR: " + ex.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			if(writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean isWindows() {
		if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1)
			return true;
		else
			return false;
	}
	
	public static void main(String[] args) {
		
		//System.out.println(isWindows());
		System.out.println("ANTES DE EJECUTAR COMANDO");
		executeCommand("DIR");
		System.out.println("DESPUES DE EJECUTAR COMANDO");
		
	}

}
