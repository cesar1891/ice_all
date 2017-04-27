package mx.com.aon.portal.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import mx.com.aon.portal.model.UserVO;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;


/**
 * @author dacevedo
 *
 */
public class ConnectionCallInterceptor implements MethodInterceptor {
	protected Logger logger = Logger.getLogger(ConnectionCallInterceptor.class);

	protected static ThreadLocal<UserVO> localUser = new ThreadLocal<UserVO>();

    public Object invoke(final MethodInvocation methodInvocation)
            throws Throwable {

        String methodName = methodInvocation.getMethod().getName();

		ThreadLocal<UserVO> tl = localUser;
		UserVO userVo = tl.get();
		if (userVo == null) //Si el dato en el thread loca null no se setea el contexto de seguridad
			return methodInvocation.proceed();

		 if (userVo.getCodigoPersona() == null || userVo.getRolActivo() == null || userVo.getEmpresa().getElementoId() == null) //Si el dato en el thread loca null no se setea el contexto de seguridad
	            return methodInvocation.proceed();
		String usr = userVo.getUser();
    	String rol = userVo.getRolActivo().getClave();
        String cdElemento = userVo.getEmpresa().getElementoId();
        String region = userVo.getRegion().getValue();
        String idioma = userVo.getIdioma().getValue();

        String reqid = cdElemento + System.currentTimeMillis();
        logger.debug("reqId : "+ reqid);


        if ("getConnection".equals(methodName)) {
        	logger.info("detectada invocacion a metodo dataSource.getConnection");
        	Connection connection = (Connection) methodInvocation.proceed();
        	callSetContextProcedure(connection,usr,rol,cdElemento,reqid, region, idioma);
            return connection;
        }
        if ("close".equals(methodName)) {
        	logger.info("detectada invocacion a metodo dataSource.close invocado ");
        	Connection connection = (Connection) methodInvocation.proceed();
        	callUnSetContextProcedure(connection);
            return connection;

        }
        return methodInvocation.proceed();
    }

    public void callSetContextProcedure(Connection conn , String usr, String rol, String cdElemento, String reqid, String regionId, String langId) throws Exception{

		CallableStatement call = null;

        try {

			call = conn.prepareCall("{call P_CONTEXT.SET_CONTEXT(?,?,?,?,?,?)}");
			call.setString(1, usr);
            call.setString(2, rol);
            call.setInt(3, Integer.parseInt(cdElemento));
            call.setString(4, reqid);
            call.setString(5, regionId);
            call.setString(6, langId);

			call.execute();

			logger.debug("Ejecutado ok:  {  call p_context.set_context( ["+usr+"],["+rol+"],["+cdElemento+"],["+reqid+"] ) }");

		} catch(SQLException e) {
			logger.error("Error al invocar procedimiento para setear el contexto de seguridad",e);
			//throw new Exception(e.getMessage(), e); // no lanzar el error si falla para no detener el resto de la ejecucion
		}

    }

    public void callUnSetContextProcedure(Connection conn ) throws Exception{

		CallableStatement call = null;

        try {

			call = conn.prepareCall("{call P_CONTEXT.unset_context()");

			call.execute();

			logger.debug("Ejecutado ok:  {  call p_context.unset_context() }");

		} catch(SQLException e) {
			logger.error("Error al invocar procedimiento para setear el contexto de seguridad",e);
			//throw new Exception(e.getMessage(), e); // no lanzar el error si falla para no detener el resto de la ejecucion
		}

    }


    public static ThreadLocal<UserVO> getLocalUser() {
		return localUser;
	}

}
