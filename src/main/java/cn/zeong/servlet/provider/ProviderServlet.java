package cn.zeong.servlet.provider;

import cn.zeong.pojo.Provider;
import cn.zeong.pojo.User;
import cn.zeong.service.Provider.ProviderServiceImpl;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         //获取前端数据
        String method = req.getParameter("method");
        if (method == null){
            method ="query";
        }
        System.out.println(method);
        if (method.equals("query")){
            try {
                this.query(req,resp);
                req.getRequestDispatcher("/jsp/providerlist.jsp").forward(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("view")){
            try {
                this.view(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("modify")){
            try {
                int admin = 1;
                int manager = 2;
                int userRole = 0;
                User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
                userRole = user.getUserRole();
                if (userRole == admin || userRole == manager) {
                    this.modify(req, resp);
                }else {
                    req.getRequestDispatcher("/jsp/exceedAthority.jsp").forward(req,resp);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("modifyexe")){
            try {
                this.modifyexe(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("delprovider")){
            try {
                int admin = 1;
                int manager = 2;
                int userRole = 0;
                User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
                userRole = user.getUserRole();
                if (userRole == admin || userRole == manager) {
                    this.delProvider(req,resp);
                }else {
                    req.getRequestDispatcher("/jsp/exceedAthority.jsp").forward(req,resp);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("add")){
            try {
                this.add(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("pcexist")){
            try {
                this.pcExist(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
       /* if (method.equals("pcexist")){
            try {
                this.pcExist(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }*/
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void query(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        //页面大小
        int pageSize = 5;
        int currentPageNo = 1;
        int totalCount = 0;
        //获取前端数据
        String queryProCode = req.getParameter("queryProCode");
        String queryProName = req.getParameter("queryProName");
        String pageIndex = req.getParameter("pageIndex");
        //转换数据类型
        if (pageIndex != null && !StringUtils.isNullOrEmpty(pageIndex)){
            //当前页
            currentPageNo = Integer.parseInt(pageIndex);
        }
        //调用service层
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        //获取供应商列表
        List<Provider> providerList = providerService.getProviderList(queryProCode,queryProName,currentPageNo,pageSize);
        //给前端数据
        req.setAttribute("providerList",providerList);
        //获取供应商总数量
        totalCount = providerService.totalCount(queryProCode,queryProName);
        //封装分页
        PageSupport pageSupport = new PageSupport();
        //设置当前页
        pageSupport.setCurrentPageNo(currentPageNo);
        //设置页面大小
        pageSupport.setPageSize(pageSize);
        //设置总数量
        pageSupport.setTotalCount(totalCount);
        //给前端数据
        req.setAttribute("totalCount",pageSupport.getTotalCount());
        req.setAttribute("currentPageNo",pageSupport.getCurrentPageNo());
        req.setAttribute("totalPageCount",pageSupport.getTotalPageCount());
        //回填
        req.setAttribute("queryProCode",queryProCode);
        req.setAttribute("queryProName",queryProName);
    }

    public void view(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int id = 0;
        //获取前端数据
        String proid = req.getParameter("proid");
        //转换数据类型
        if (proid != null && !StringUtils.isNullOrEmpty(proid)){
            id = Integer.parseInt(proid);
        }
        //调用service层获取订单信息
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        Provider provider = providerService.getProviderById(id);
        //给前端数据
        req.setAttribute("provider",provider);
        //请求转发
        req.getRequestDispatcher("/jsp/providerview.jsp").forward(req,resp);
    }

    public void modify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        int id = 0;
        //获取前端数据
        String proid = req.getParameter("proid");
        //转换数据类型
        if (proid != null && !StringUtils.isNullOrEmpty(proid)){
            id = Integer.parseInt(proid);
        }
        //将供应商id存进session
        req.getSession().setAttribute("proId",id);
        System.out.println(proid);
        //调用service层
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        Provider provider = providerService.getProviderById(id);
        //回填
        req.setAttribute("provider",provider);
        req.getRequestDispatcher("/jsp/providermodify.jsp").forward(req,resp);
    }

    public void modifyexe(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int id = 0;
        int modifyBy = 0;
        //更新时间
        Date modifyDate = new Date();
        User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
        //更新者
        modifyBy = user.getId();
        //获取前端数据
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");
        id = (int) req.getSession().getAttribute("proId");
        //封装供应商
        Provider provider = new Provider();
        provider.setId(id);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);
        provider.setModifyBy(modifyBy);
        provider.setModifyDate(modifyDate);
        System.out.println(provider.getId());
        //调用service层
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        providerService.updateProvider(provider);
        //请求转发
        req.getRequestDispatcher("/jsp/provider.do?method=query").forward(req,resp);
    }

    public void delProvider(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        int id = 0;
        boolean flag = false;
        HashMap<String,String> resultMap = new HashMap<>();
        //获取前端数据
        String proid = req.getParameter("proid");
        //转换数据类型
        if (proid != null && !StringUtils.isNullOrEmpty(proid)){
           id = Integer.parseInt(proid);
        }
        //调用service层
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        flag = providerService.delProvider(id);
        //设置发送数据类型
        resp.setContentType("application/json");
        if (flag){
            resultMap.put("delResult","true");
        }else if(proid == null){
            resultMap.put("delResult","notexist");
        }else {
            resultMap.put("delResult","false");
        }
        //转换为json
        String json = JSONArray.toJSONString(resultMap);
        //获取输出流
        PrintWriter out = resp.getWriter();
        out.write(json);
        out.flush();
        out.close();;
    }

    public void add(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int createdBy = 0;
        //创建时间
        Date creationDate = new Date();
        User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
        //创建者
        createdBy = user.getId();
        //获取前端数据
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");
        //封装供应商
        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);
        provider.setCreateBy(createdBy);
        provider.setCreationDate(creationDate);
        //调用service层
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        providerService.addProvider(provider);
        //请求转发
        req.getRequestDispatcher("/jsp/provider.do?method=query").forward(req,resp);
    }

    public void pcExist(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        System.out.println("进入pcexist");
        boolean flag = false;
        HashMap<String, String> resultMap = new HashMap<>();
        //获取前端数据
        String proCode = req.getParameter("proCode");
        //调用service层
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        flag = providerService.pcExist(proCode);
        //设置数据类型
        resp.setContentType("application/json");
        System.out.println(proCode);
        //判断结果
        if (flag){
            resultMap.put("proCode","exist");
        }else if (proCode.equals("")){
            resultMap.put("proCode","proCodeIsNull");
        }
        //转换为json
        String json = JSONArray.toJSONString(resultMap);
        //获取输出流
        PrintWriter out = resp.getWriter();
        out.write(json);
        out.flush();
        out.close();
    }

}
