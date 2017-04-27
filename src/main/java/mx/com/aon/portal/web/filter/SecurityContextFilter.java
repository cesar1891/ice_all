package mx.com.aon.portal.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.service.LoginManager;

import org.apache.log4j.Logger;

public class SecurityContextFilter implements Filter {

    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(SecurityContextFilter.class);
    private LoginManager loginManager;

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest)request).getSession();
        logger.debug("request: "+((HttpServletRequest)request).getRequestURI());
        logger.debug("Obteniendo el usuario de la session");
        UserVO userVO = (UserVO)session.getAttribute("USUARIO");
        if (userVO != null) {

            //doCheckSecurity(userVO,request);

        } else {
            logger.error("Usuario logueado es Nulo.");
            throw new ServletException("No se encontro Remote User(HttpServletRequest)request).getRemoteUser() ");
        }

        chain.doFilter(request, response);
    }


    public void destroy() {
    }

}
