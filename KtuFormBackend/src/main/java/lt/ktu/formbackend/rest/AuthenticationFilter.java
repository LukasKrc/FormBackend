package lt.ktu.formbackend.rest;

import java.io.IOException;
import java.util.StringTokenizer;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.impl.db.DaoFactory;

/**
 *
 * @author Lukas
 */
public class AuthenticationFilter implements javax.servlet.Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest) {
            DaoFactory.getFormDao().setRequest((HttpServletRequest) request);
            String path = ((HttpServletRequest) request).getRequestURI();
            System.out.println(path);
            StringTokenizer tokenizer = new StringTokenizer(path, "/");
            for (int i = tokenizer.countTokens(); i > 1; i--, tokenizer.nextToken());
            String uriEnd = tokenizer.nextToken();
            if (uriEnd.equals("user") || uriEnd.equals("user/")) {
                chain.doFilter(request, response);
            }
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String authCredentials = httpServletRequest.getHeader("Authorization");
            AuthenticationService authenticationService = new AuthenticationService();
            boolean authenticationStatus = false;
            try {
                authenticationStatus = authenticationService.authenticate(authCredentials, request);
                if (authenticationStatus) {
                    chain.doFilter(request, response);
                } else {
                    if (response instanceof HttpServletResponse) {
                        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                        httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        httpServletResponse.getWriter().print("{\"error\" : \"Wrong username or password.\"}");
                    }
                }
            } catch (Exception e) {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpServletResponse.getWriter().print("{\"error\" : \"" + e.getMessage() + "\"}");
            }
        }
    }

    @Override
    public void destroy() {
    }

}
