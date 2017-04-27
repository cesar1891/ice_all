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

import org.apache.log4j.Logger;

public class RedirectFilter implements Filter {
	private static Logger logger = Logger.getLogger(RedirectFilter.class);

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

       	StringBuffer url = ((HttpServletRequest)request).getRequestURL();
       	logger.debug("URL recibida: " + url.toString());
   		logger.debug("En filtro de redireccion Header: "+ ((HttpServletRequest)request).getHeader("x-requested-with"));

   		if (((HttpServletRequest)request).getHeader("x-requested-with") != null && ((HttpServletRequest)request).getHeader("x-requested-with").equals("XMLHttpRequest")) {
    		HttpSession httpSession = ((HttpServletRequest)request).getSession(false);
    		if (httpSession == null) {
    			logger.debug("La url " + url + " no se puede procesar en esta sesion");
        		response.getWriter().write("{\"success\": false, \"actionErrors\": [\"Sesi\u00f3n inv\u00e1lida\"], \"totalCount\": 0, \"errCode\": -999999}");
    		} else {
    			logger.debug("La url " + url + " se procesara en la sesion para la sesin con id: " + httpSession.getId());
        		chain.doFilter(request, response);
    		}
    	}else {
    		logger.debug("La url " + url + " no es ajax");
    		chain.doFilter(request, response);
    	}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
