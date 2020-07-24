package com.multiple_language_menu.filters;

import com.google.gson.Gson;
import com.multiple_language_menu.models.responses.httpResponse.HttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTFilter extends GenericFilterBean {//Bắt JWT trong header và thực hiện xác thực để cho phép request đi qua
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            TokenJwtUtil tokenJwtUtil = new TokenJwtUtil();
            Authentication authentication = tokenJwtUtil.getAuthentication((HttpServletRequest) servletRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(servletRequest, servletResponse);
        }catch(Exception e){
            String mess = e.toString();
            if (mess.matches("(?i)(.*)jwt(.*)")){//Bắn ra HTTP status 401 khi xác thực JWT gặp lỗi
                ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                servletResponse.setContentType("application/json");
                servletResponse.setCharacterEncoding("UTF-8");
                HttpResponse<Object> response = new HttpResponse();
                response.setStatusCode("401");
                response.setMessage("Unauthorized");
                response.setData(null);
                Gson gson = new Gson();
                String json = gson.toJson(response);
                servletResponse.getWriter().write(json);
            }else {
                throw e;
            }
        }
    }

}
