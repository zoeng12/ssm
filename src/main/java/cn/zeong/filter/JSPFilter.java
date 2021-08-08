package cn.zeong.filter;

import cn.zeong.pojo.Session;
import cn.zeong.pojo.User;
import cn.zeong.service.User.UserServiceImpl;
import cn.zeong.util.constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class JSPFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //将参数转为为HttpServlet类型以获得geSession
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        UserServiceImpl userService = new UserServiceImpl();
        User user = new User();
        int id = 0;
        String sessionId = null;
        //获取sessionId
        sessionId = request.getSession().getId();
        //获取session中到user_session的值
        Object attribute = request.getSession().getAttribute(constants.USER_SESSION);
        //判断user_session是否为空，若为空则跳转到错误页面
        if (attribute == null){
            request.getRequestDispatcher("/error.jsp").forward(request,response);
            request.getSession().setAttribute("massage","请重新输入用户名与密码登录！");
        }else if (attribute != null){
            user = (User) request.getSession().getAttribute(constants.USER_SESSION);
            id = user.getId();
            try {
                Session sessionList = userService.getSessionList(id);
                if (!sessionId.equals(sessionList.getSessionId())){
                    request.getRequestDispatcher("/error.jsp").forward(request,response);
                    request.getSession().setAttribute("massage","请重新输入用户名与密码登录！");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else {
            request.getRequestDispatcher("/error.jsp").forward(request,response);
            request.getSession().setAttribute("massage","注销失败！");
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
