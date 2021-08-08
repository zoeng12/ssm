package cn.zeong.filter;

import cn.zeong.pojo.User;
import cn.zeong.util.constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AthorityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        int userRole = 0;
        int admin = 1;
        int manager = 2;
        //将参数转为为HttpServlet类型以获得geSession
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取session中到user_session的值
        Object attribute = request.getSession().getAttribute(constants.USER_SESSION);
        if (attribute != null){
            User user = (User) attribute;
            userRole = user.getUserRole();
            if (!(userRole == admin || userRole == manager)){
                request.getRequestDispatcher("/jsp/exceedAthority.jsp").forward(request,response);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
