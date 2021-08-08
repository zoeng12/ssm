package cn.zeong.servlet.user;

import cn.zeong.pojo.Session;
import cn.zeong.pojo.User;
import cn.zeong.service.User.UserServiceImpl;
import cn.zeong.util.constants;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class LoginOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = 0;
        boolean flag = false;
        User user = new User();
        Session session = new Session();
        Date date = new Date();
        UserServiceImpl userService = new UserServiceImpl();
        //获取用户id
        user = (User) req.getSession().getAttribute(constants.USER_SESSION);
        userId = user.getId();
        //修改数据库中的相关登录数据
        session.setId(userId);
        session.setSessionId("offStatus");
        session.setLogoutDate(date);
        session.setloginDate(null);
        try {
           flag = userService.updateSession(session);
           if (flag){
               //清空保存在session的user_session的数据
               req.getSession().removeAttribute(constants.USER_SESSION);
           }else {
               req.getSession().setAttribute("massage","账号注销失败！");
           }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //跳转到登录页面
        resp.sendRedirect(getServletContext().getContextPath()+"/login.jsp");
        req.getSession().setAttribute("massage","账号注销成功！请重新输入用户名与密码登录！");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Test
    public void test(){
        Session session = new Session();
        Date date = new Date();
        session.setloginDate(date);
        System.out.println(session.getLoginDate());

    }
}
