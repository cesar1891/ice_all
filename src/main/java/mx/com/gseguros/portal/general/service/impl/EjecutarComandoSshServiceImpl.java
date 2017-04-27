package mx.com.gseguros.portal.general.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import mx.com.gseguros.portal.general.service.EjecutarComandoSshService;
import mx.com.gseguros.utils.Utils;

@Service
public class EjecutarComandoSshServiceImpl implements EjecutarComandoSshService{
	
	final static Logger logger = LoggerFactory.getLogger(EjecutarComandoSshServiceImpl.class);

	@Override
	public String ejectutarCmd(String server, String usuario, String pass, List<String> cmd) throws Exception {
		JSch jsch= new JSch();
		Session session=jsch.getSession(usuario, server);
		session.setPassword(pass);
		Properties config=new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		ChannelExec channel=(ChannelExec) session.openChannel("exec");
		
		BufferedReader in=new BufferedReader(new InputStreamReader(channel.getInputStream()));
		String out="";
		String msg=null;
		for(String com:cmd){
			logger.debug("-> Ejecutando: "+com);
			channel.setCommand(cmd+";");
			channel.connect();
			while((msg=in.readLine())!=null){
				  out+=msg+"\n";
				}
			
			logger.debug(Utils.join("Salida de ",com,":",msg));
		}
		channel.disconnect();
		session.disconnect();
		return out;
	}

}
