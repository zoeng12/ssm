package cn.zeong.servlet.user;

import cn.zeong.pojo.Session;
import cn.zeong.pojo.User;
import cn.zeong.service.User.UserServiceImpl;
import cn.zeong.util.constants;

import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserServiceImpl userService = new UserServiceImpl();
        User user = new User();
        Session session = new Session();
        Date date = new Date();
        int userId = 0;
        //获取前端输入的userCode和password
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        user = userService.login(userCode, userPassword);
        //向session中存user
        req.getSession().setAttribute(constants.USER_SESSION,user);

        //判断用户名是否为空
        if (user!=null) {
            //获取后端该用户的正确密码
            String password = user.getUserPassword();
            //若存在改用户并且密码正确则跳转到首页
            if (userPassword.equals(password)){
                //获取session里的数据的userid
                user = (User) req.getSession().getAttribute(constants.USER_SESSION);
                userId = user.getId();
                //获取sessionId
                String sessionId = req.getSession().getId();
                System.out.println(sessionId);
                //获取数据库中session的相关数据
                try {
                    Session sessionList = userService.getSessionList(userId);
                    //对比数据
                    if (sessionList.getSessionId().equals("offStatus")){
                        //重定向
                        resp.sendRedirect(getServletContext().getContextPath()+"/jsp/frame.jsp");
                        //存储相关信息给数据库
                        session.setId(userId);
                        session.setSessionId(sessionId);
                        session.setloginDate(date);
                        session.setLogoutDate(null);
                        userService.updateSession(session);
                    }else if (sessionId.equals(sessionList.getSessionId())){
                        //重定向
                        resp.sendRedirect(getServletContext().getContextPath()+"/jsp/frame.jsp");
                    }else {
                        req.getRequestDispatcher("/sessionerror.jsp").forward(req,resp);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }else {
                //返回或密码错误的提示
                String error = "密码输入错误！请确认正确密码！";
                req.getSession().setAttribute("massage",error);
                resp.sendRedirect("/login.jsp");
            }
        }else {
            String error = "用户名不存在！请确认正确用户名！";
            req.getSession().setAttribute("massage",error);
            resp.sendRedirect("/login.jsp");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
