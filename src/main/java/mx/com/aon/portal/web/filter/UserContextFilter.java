package mx.com.aon.portal.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mx.com.aon.portal.model.BaseObjectVO;
import mx.com.aon.portal.model.IsoVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.service.LoginManager;
import mx.com.aon.portal.util.ConnectionCallInterceptor;
import mx.com.gseguros.exception.ApplicationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class UserContextFilter implements Filter {

    private Logger logger = Logger.getLogger(UserContextFilter.class);
    private LoginManager loginManager;

	@Autowired
	private mx.com.gseguros.portal.general.service.NavigationManager navigationManagerNuevo;

    public void init(FilterConfig config) throws ServletException {
    }
 
    /** Find a cookie by name; return first found */
    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] allCookies;

        if ( name==null ) {
            throw new IllegalArgumentException("cookie name is null");
        }

        allCookies = request.getCookies();
        if (allCookies != null) {
            for (int i=0; i < allCookies.length; i++) {
                Cookie candidate = allCookies[i];
                logger.debug("cookie: "+candidate==null?"NULL":candidate.getName());
                logger.debug("value: "+candidate==null?"NULL":candidate.getValue());
                if (name.equals(candidate.getName()) ) {                    
                	return candidate==null?"NULL":candidate.getValue();
                }
            }
        }
        return null;
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest)request).getSession();
        logger.debug("request: "+((HttpServletRequest)request).getRequestURI());
        logger.debug("Obteniendo el usuario de la session");
        UserVO userVO = (UserVO)session.getAttribute("USUARIO");
        try {
	        if (userVO != null) {
	        	logger.debug("userVO viene no null");
	        	//String user = getCookieValue((HttpServletRequest)request, "SSOAON");
	            String user = ((HttpServletRequest)request).getHeader("ACW_REMOTE_USER");
	            logger.debug("Valor request ACW_REMOTE_USER: "+ user);
	            if (user == null)
	/*  64*/                user = getCookieValue((HttpServletRequest)request, "SSOAON");
	            
	            
	            logger.debug("De sesion userVO.getUser(): "+ userVO.getUser());
	            logger.debug("De request: "+ user);
	        	if (!userVO.getUser().equals(user)) {
	        		logger.debug("valor de user en cookie NO es igual a valor de userVO.getUser()");
	        		session.invalidate();
	        		logger.debug("Destruyendo la session.....");
	        		session = ((HttpServletRequest)request).getSession(true);
	        		userVO = null;
	        		logger.debug("Creando userVO nulo con session nuevo");
	        	}
	        }
	        if (userVO == null) {
	
	        	userVO = new UserVO();
	        	
	            //Agregado para obetner usuario de OID
	            //@author rruiz CIMA
	            logger.debug("Obteniendo el principal del SSO");
	//            String user = ((HttpServletRequest)request).getRemoteUser();
	            String user = getCookieValue((HttpServletRequest)request,"SSOAON");
	            logger.debug("SSO: se obtuvo el usuario "+user);
	
	            if (user == null || user.equals("")) {
	                logger.error("Usuario logueado es Nulo.");
	                //throw new ServletException("No se encontro Remote User(HttpServletRequest)request).getRemoteUser() ");
	                logger.debug("No se encontro Remote User(HttpServletRequest)request).getRemoteUser() ");
	        		((HttpServletResponse)response).sendRedirect("logout.action");
	        		return;
	            }
	
	            logger.debug("Obteniendo LoginManager en el servletContext");
	            ServletContext servletContext  = session.getServletContext();
	            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	            loginManager = (LoginManager) context.getBean("loginManager");
	            //navigationManager = (NavigationManager) context.getBean("navigationManagerJdbcTemplate");
	            try {
	                logger.debug("Obteniendo los datos adicionales del usuario");
	                userVO = loginManager.obtenerDatosUsuario(user);
	                IsoVO isoVO = navigationManagerNuevo.getVariablesIso(userVO.getUser());
		            BaseObjectVO languague = new BaseObjectVO();
					languague.setValue(isoVO.getLanguague()); 
					
					BaseObjectVO pais = new BaseObjectVO();
					pais.setValue(isoVO.getPais());
					
					userVO.setPais(pais);
					userVO.setFormatoFecha(isoVO.getFormatoFecha());
					userVO.setFormatoNumerico(isoVO.getFormatoNumerico());
					userVO.setIdioma(languague);
					//userVO.setClientDateFormat(isoVO.getFormatoFecha());
	
					//userVO.setFormatDate(dateFormat);
	                userVO.setDecimalSeparator(isoVO.getFormatoNumerico());
	            } catch (ApplicationException ex) {
	                logger.error("Fallo al ejecutar el obtenerDatosUsuario.",ex);     
	                throw new ServletException("Error al invocar el filtro contexto de Usuario",ex);
	            }
	            		/**
	             * Esta session debe ser borrada.
	             */
	            session.setAttribute("userVO", userVO);
	            /**/
	
	            session.setAttribute("USUARIO", userVO);
	        }
	        
	        logger.debug("seteando el userVO en el ThreadLocal");
	        ThreadLocal<UserVO> tl = ConnectionCallInterceptor.getLocalUser();
	        tl.set(userVO);
//	        doAudit(userVO,request);

	        chain.doFilter(request, response);
        } catch (Exception e) {
        	logger.debug("Error en UserContextFilter: " + e.getMessage());
        	chain.doFilter(request, response);
        }
    }

    
    public void destroy() {
    }

    public void doAudit(UserVO userVO, ServletRequest request) throws IOException, ServletException {
        try {
            //HttpSession session = ((HttpServletRequest)request).getSession();
            logger.debug("Obteniendo consultaActividadUsuarioManager en el servletContext");
            //ServletContext servletContext  = session.getServletContext();
            //WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            //ConsultaActividadUsuarioManager consultaActividadUsuarioManager = (ConsultaActividadUsuarioManager) context.getBean("consultaActividadUsuarioManager");

            String requestUri = ((HttpServletRequest)request).getRequestURI();
            if (requestUri.indexOf(".action")>0) {

                String method = ((HttpServletRequest)request).getMethod();

                logger.debug("Insertando el url invocado por el usuario");

                String reqid = userVO.getUser() + System.currentTimeMillis();
                logger.debug("reqId : "+ reqid);
                logger.debug("Usuario "+ userVO.getUser());
                logger.debug("url invocado "+ requestUri);
                logger.debug("method "+ method);

                //String rolActivo = (userVO.getRolActivo() != null)?userVO.getRolActivo().getClave():null;
                //consultaActividadUsuarioManager.insertarActividadesUsuario(reqid,requestUri,method,userVO.getUser(), rolActivo);
            }



        } catch (Exception ex) {
            logger.error("Fallo al ejecutar el obtenerDatosUsuario.",ex);
            throw new ServletException("Error al invocar el filtro contexto de Usuario",ex);
        }
    }

}

