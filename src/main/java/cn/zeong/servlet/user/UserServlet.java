package cn.zeong.servlet.user;

import cn.zeong.pojo.Role;
import cn.zeong.pojo.User;
import cn.zeong.service.Role.RoleServiceImpl;
import cn.zeong.service.User.UserServiceImpl;
import cn.zeong.util.PageSupport;
import cn.zeong.util.constants;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取前端方法
        System.out.println(req.getSession().getId());
        String method = req.getParameter("method");
        if (method == null){
            method = "query";
        }
        System.out.println(method);
        //判断获取的方法是哪种
        if (method.equals("pwdmodify")) {
            this.checkOldPwd(req, resp);
        }
        if (method.equals("savepwd")) {
            try {
                this.updatePwd(req, resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("query")) {
            try {
                this.query(req, resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("view")) {
            try {
                this.view(req, resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("modify")) {
            try {
                int admin = 1;
                int userRole = 0;
                User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
                userRole = user.getUserRole();
                if (userRole == admin) {
                    this.modify(req,resp);
                }else {
                    req.getRequestDispatcher("/jsp/exceedAthority.jsp").forward(req,resp);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("modifyexe")){
            try {
                this.modifyexe(req, resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("getrolelist")) {
            try {
                this.getRoleList(req, resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("getRoleListById")){
            try {
                this.getRoleListById(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("deluser")){
            try {
                int admin = 1;
                int userRole = 0;
                User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
                userRole = user.getUserRole();
                if (userRole == admin) {
                    this.delUser(req,resp);
                }else {
                    req.getRequestDispatcher("/jsp/exceedAthority.jsp").forward(req,resp);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("ucexist")){
            try {
                this.ucexist(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("add")) {
            try {
                this.add(req, resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    /**
     * 修改密码
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        boolean flag = false;
        //service层
        UserServiceImpl userService = new UserServiceImpl();
        //获取前端数据
        String newpassword = req.getParameter("newpassword");
        //获取user_session中的数据
        User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
        Integer id = user.getId();
        //调用service层
        flag = userService.updatePwd(id, newpassword);
        if (flag) {
            req.getSession().removeAttribute(constants.USER_SESSION);
            req.getSession().setAttribute("massage", "密码修改成功！请用新密码登录！");
        } else {
            req.getSession().setAttribute("massage", "密码修改失败！可能用户不存在！");
        }
        resp.sendRedirect("/login.jsp");
    }

    /**
     * 验证旧密码
     *
     * @param req
     * @param resp
     * @return
     * @throws IOException
     */
    public boolean checkOldPwd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean flag = false;
        //获取前端的旧密码
        String oldpassword = req.getParameter("oldpassword");
        //获取session中的user的password的值
        User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
        String password = user.getUserPassword();
        HashMap<String, String> resultMap = new HashMap<>();
        //判断旧密码和数据库密码是否相等
        if (user == null) {
            resultMap.put("result", "sessionerror");
        } else if (oldpassword == null) {
            resultMap.put("result", "error");
        } else if (oldpassword.equals(password)) {
            resultMap.put("result", "true");
            flag = true;
        } else {
            resultMap.put("result", "false");
        }
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(JSONArray.toJSONString(resultMap));
        out.flush();
        out.close();
        return flag;
    }

    /**
     * 查询用户列表
     *
     * @param req
     * @param resp
     * @throws IOException
     * @throws SQLException
     * @throws ServletException
     */
    public void query(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, ServletException {
        //获取前端数据
        String queryname = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        PageSupport pageSupport = new PageSupport();
        //默认userRole为0
        int queryUserRole = 0;
        //默认页面大小为5
        int pageSize = 5;
        //默认当前页为1
        int currentPageNo = 1;
        //将pageIndex作为当前页转换为整数型
        if (pageIndex != null && !StringUtils.isNullOrEmpty(pageIndex)) {
            currentPageNo = Integer.parseInt(pageIndex);
        }
        //将userRole转换为整数型
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp);
        }
        //调用service层
        UserServiceImpl userService = new UserServiceImpl();
        RoleServiceImpl roleService = new RoleServiceImpl();
        //调用返回总数量的方法
        int totalCount = roleService.getTotalCount(queryname, queryUserRole);
        //设置当前页
        pageSupport.setCurrentPageNo(currentPageNo);
        //设置页面大小
        pageSupport.setPageSize(pageSize);
        //设置总数量
        pageSupport.setTotalCount(totalCount);
        //获取当前页
        currentPageNo = pageSupport.getCurrentPageNo();
        //获取页面大小
        pageSize = pageSupport.getPageSize();
        //获取总数量
        totalCount = pageSupport.getTotalCount();
        //获取页总数
        int totalPageCount = pageSupport.getTotalPageCount();
        //给前端数据
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("queryUserName", queryname);
        req.setAttribute("queryUserRole", queryUserRole);
        //调用返回所有用户的方法
        List<User> allUser = userService.getAllUser(queryname, queryUserRole, currentPageNo, pageSize);
        //返回userList给前端
        req.setAttribute("userList", allUser);
        //调用返回所有角色的方法
        List<Role> allRole = roleService.getAllRole();
        //返回roleList給前端
        req.setAttribute("roleList", allRole);
        //请求转发
        req.getRequestDispatcher("/jsp/userlist.jsp").forward(req, resp);
    }

    /**
     *查看用户数据
     * @param req
     * @param resp
     * @throws SQLException
     * @throws ServletException
     * @throws IOException
     */
    public void view(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int id = 0;
        User user = new User();
        //获取前端数据
        String uid = req.getParameter("uid");
        if (uid != null && !StringUtils.isNullOrEmpty(uid)) {
            id = Integer.parseInt(uid);
        }
        //调用service层
        UserServiceImpl userService = new UserServiceImpl();
        user = userService.getUserById(id);
        //给前端数据
        req.setAttribute("user", user);
        //请求转发
        req.getRequestDispatcher("/jsp/userview.jsp").forward
                (req, resp);
    }

    /**
     * 修改用户数据
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     * @throws SQLException
     */
    public void modify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
            UserServiceImpl userService = new UserServiceImpl();
            User user = new User();
            int id = 0;
            //获取前端数据
            String uid = req.getParameter("uid");
            //转换数据类型
            if (uid != null && !StringUtils.isNullOrEmpty(uid)) {
                id = Integer.parseInt(uid);
                //将id存进session
                req.getSession().setAttribute("uid", id);
            }
            //调用service层获取通过id查询用户数据
            user = userService.getUserById(id);
            //将数据给前端
            req.setAttribute("user", user);
            req.getRequestDispatcher("/jsp/usermodify.jsp").forward(req, resp);
        }

    /**
     * 修改用户数据
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     * @throws SQLException
     */
    public void modifyexe(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        User user = new User();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int id = 0;
        boolean flag = false;
        String userName = null;
        int gender = 0;
        Date birthday = null;
        String phone = null;
        String address = null;
        int userRole = 0;
        int modifyBy = 0;
        //获取前端数据
        id = (int) req.getSession().getAttribute("uid");
        userName = req.getParameter("userName");
        String genderTemp = req.getParameter("gender");
        String birthdayTemp = req.getParameter("birthday");
        phone = req.getParameter("phone");
        address = req.getParameter("address");
        String userRoleTemp = req.getParameter("userRole");
        //转换数据类型
        if (genderTemp != null && !StringUtils.isNullOrEmpty(genderTemp)){
            gender = Integer.parseInt(genderTemp);
        }
        if (birthdayTemp != null && !StringUtils.isNullOrEmpty(birthdayTemp)){
            try {
                birthday = sdf.parse(birthdayTemp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (userRoleTemp != null && !StringUtils.isNullOrEmpty(userRoleTemp)){
            userRole = Integer.parseInt(userRoleTemp);
        }
        User attribute = (User) req.getSession().getAttribute(constants.USER_SESSION);
        modifyBy = attribute.getId();
        //封装user
        user.setId(id);
        user.setUserName(userName);
        user.setGender(gender);
        user.setBirthday(birthday);
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(userRole);
        //调用service层
        UserServiceImpl userService = new UserServiceImpl();
        flag = userService.updateUserById(user,modifyBy);
        if (flag){
            req.getRequestDispatcher("/jsp/user.do?method=query").forward(req, resp);
        }else {
            req.getRequestDispatcher("error.jsp").forward(req,resp);
        }
    }

    /**
     * 获取role表的数据给前端
     * @param req
     * @param resp
     * @throws SQLException
     * @throws IOException
     */
    public void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> allRole = roleService.getAllRole();
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(JSONArray.toJSONString(allRole));
        out.flush();
        out.close();
    }

    /**
     * 通过id删除用户
     * @param req
     * @param resp
     * @throws SQLException
     * @throws IOException
     */
    public void delUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
            int id = 0;
            boolean flag = false;
            UserServiceImpl userService = new UserServiceImpl();
            HashMap<String, String> resultMap = new HashMap<>();
            //获取前端数据
            String uid = req.getParameter("uid");
            if (uid != null && !StringUtils.isNullOrEmpty(uid)) {
                id = Integer.parseInt(uid);
            } else {
                resultMap.put("delResult", "notexist");
            }
            //调用service
            flag = userService.delUserById(id);
            if (flag) {
                resultMap.put("delResult", "true");
            } else {
                resultMap.put("delResult", "false");
            }
            //设置传输类型
            resp.setContentType("application/json");
            //将数据转换为json
            String json = JSONArray.toJSONString(resultMap);
            //输出给前端
            PrintWriter out = resp.getWriter();
            out.write(json);
            out.flush();
            out.close();
        }

    /**
     * 验证用户编码是否存在
     * @param req
     * @param resp
     * @throws SQLException
     * @throws IOException
     */
    public void ucexist(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        boolean flag = false;
        HashMap<String, String> resultMap = new HashMap<>();
        //获取前端数据
        String userCode = req.getParameter("userCode");
        //调用service层
        UserServiceImpl userService = new UserServiceImpl();
        flag = userService.getUserByUserCode(userCode);
        if (userCode.equals("")){
            resultMap.put("userCode","userCodeIsNull");
        }
        if (flag){
            resultMap.put("userCode","exist");
        }
        //设置传输类型
        resp.setContentType("application/json");
        //转换为json
        String json = JSONArray.toJSONString(resultMap);
        //将数据给前端
        PrintWriter out = resp.getWriter();
        out.write(json);
        out.flush();
        out.close();
    }

    /**
     * 添加用户
     * @param req
     * @param resp
     * @throws SQLException
     * @throws ServletException
     * @throws IOException
     */
    public void add(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
            User user = new User();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date creationDate = new Date();
            int pageIndex = 0;
            int gender = 0;
            Date birthday = null;
            int userRole = 0;
            //获取前端数据
            String userCode = req.getParameter("userCode");
            String userName = req.getParameter("userName");
            String userPassword = req.getParameter("ruserPassword");
            String genderTemp = req.getParameter("gender");
            String birthdayTemp = req.getParameter("birthday");
            System.out.println(birthdayTemp);
            String phone = req.getParameter("phone");
            String address = req.getParameter("address");
            String userRoleTemp = req.getParameter("userRole");
            User userSession = (User) req.getSession().getAttribute(constants.USER_SESSION);
            Integer createdBy = userSession.getId();
            if (genderTemp != null && !StringUtils.isNullOrEmpty(genderTemp)) {
                gender = Integer.parseInt(genderTemp);
            }
            if (birthdayTemp != null && !StringUtils.isNullOrEmpty(birthdayTemp)) {
                try {
                    birthday = sdf.parse(birthdayTemp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (userRoleTemp != null && !StringUtils.isNullOrEmpty(userRoleTemp)) {
                userRole = Integer.parseInt(userRoleTemp);
            }
            //封装user
            user.setUserCode(userCode);
            user.setUserName(userName);
            user.setUserPassword(userPassword);
            user.setGender(gender);
            user.setBirthday(birthday);
            user.setPhone(phone);
            user.setAddress(address);
            user.setUserRole(userRole);
            user.setCreateBy(createdBy);
            user.setCreationDate(creationDate);
            //调用service层的插入用户的方法
            UserServiceImpl userService = new UserServiceImpl();
            userService.insertUser(user);
            req.getRequestDispatcher("/jsp/user.do?method=query").forward(req, resp);
        }

    public void getRoleListById(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        int userRole = 0;
        //获取session中的userRole
        User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
        userRole = userRole = user.getUserRole();
        //调用service层
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleListById = roleService.getRoleListById(userRole);
        //给前端数据
        resp.setContentType("application/json");
        //转换成json
        String json = JSONArray.toJSONString(roleListById);
        //输出流
        PrintWriter out = resp.getWriter();
        out.write(json);
        out.flush();
        out.close();
    }
}
