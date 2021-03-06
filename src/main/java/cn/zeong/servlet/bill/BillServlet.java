package cn.zeong.servlet.bill;

import cn.zeong.pojo.Bill;
import cn.zeong.pojo.Provider;
import cn.zeong.pojo.Role;
import cn.zeong.pojo.User;
import cn.zeong.service.Bill.BillServiceImpl;
import cn.zeong.service.Bill.IBillService;
import cn.zeong.service.Role.RoleServiceImpl;
import cn.zeong.util.PageSupport;
import cn.zeong.util.constants;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       String method =  req.getParameter("method");
       if (method == null || StringUtils.isNullOrEmpty(method)){
           method = "query";
       }
        System.out.println(method);
        if (method.equals("query")) {
            try {
                this.query(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            req.getRequestDispatcher("/jsp/billlist.jsp").forward(req, resp);
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
        if (method.equals("modifysave")){
            try {
                this.modifysave(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("delbill")){
            try {
                int admin = 1;
                int manager = 2;
                User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
                int userRole = user.getUserRole();
                if (userRole == admin || userRole == manager) {
                    this.delBill(req, resp);
                }else {
                    req.getRequestDispatcher("/jsp/exceedAthority.jsp").forward(req,resp);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("getproviderlist")){
            try {
                this.getProviderList(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("bcexist")){
            try {
                this.bcexist(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (method.equals("add")){
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

    public void query(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        int providerId = 0;
        int isPayment = 0;
        //?????????????????????5
        int pageSize = 5;
        //??????????????????1
        int currentPageNo = 1;
        //??????????????????
        String queryProductName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
        String pageIndex = req.getParameter("pageIndex");
        //????????????
        if (queryProviderId != null && !StringUtils.isNullOrEmpty(queryProviderId)){
            providerId = Integer.parseInt(queryProviderId);
        }
        if (queryIsPayment != null && !StringUtils.isNullOrEmpty(queryIsPayment)){
            isPayment = Integer.parseInt(queryIsPayment);
        }
        if (pageIndex != null && !StringUtils.isNullOrEmpty(pageIndex)){
            currentPageNo = Integer.parseInt(pageIndex);
        }

        //??????service???
        BillServiceImpl billService = new BillServiceImpl();
        //??????????????????
        List<Bill> billList = billService.getBillList(queryProductName, providerId, isPayment,currentPageNo,pageSize);
        req.setAttribute("billList",billList);
        //???????????????
        int totalCount =  billService.getTotalCount(queryProductName,providerId,isPayment);
        //????????????
        PageSupport pageSupport = new PageSupport();
        //???????????????
        pageSupport.setCurrentPageNo(currentPageNo);
        //??????????????????
        pageSupport.setPageSize(pageSize);
        //???????????????
        pageSupport.setTotalCount(totalCount);
        //???????????????
        req.setAttribute("totalCount",pageSupport.getTotalCount());
        req.setAttribute("currentPageNo",pageSupport.getCurrentPageNo());
        req.setAttribute("totalPageCount",pageSupport.getTotalPageCount());
        //??????
        req.setAttribute("queryProductName",queryProductName);
        req.setAttribute("queryProviderId",providerId);
        req.setAttribute("queryIsPayment",isPayment);
        //?????????????????????
        List<Provider> providerList = billService.getProviderList();
        req.setAttribute("providerList",providerList);
    }

    public void view(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int billId = 0;
        //??????????????????
        String billid = req.getParameter("billid");
        //??????????????????
        if (billid != null && !StringUtils.isNullOrEmpty(billid)){
            billId = Integer.parseInt(billid);
        }
        //??????service???
        BillServiceImpl billService = new BillServiceImpl();
        Bill billById = billService.getBillById(billId);
        //???????????????
        req.setAttribute("bill",billById);
        //????????????
        req.getRequestDispatcher("/jsp/billview.jsp").forward(req,resp);
    }

    public void modify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        int id = 0;
        //??????????????????
        String billid = req.getParameter("billid");
        if (billid != null && !StringUtils.isNullOrEmpty(billid)){
            id = Integer.parseInt(billid);
        }
        req.getSession().setAttribute("uid",id);
        //??????service???
        BillServiceImpl billService = new BillServiceImpl();
        //??????????????????
        Bill billById = billService.getBillById(id);
        //???????????????
        req.setAttribute("bill",billById);
        req.getRequestDispatcher("/jsp/billmodify.jsp").forward(req,resp);
    }

    public void getProviderList(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        BillServiceImpl billService = new BillServiceImpl();
        List<Provider> allProvider = billService.getProviderList();
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(JSONArray.toJSONString(allProvider));
        out.flush();
        out.close();

    }

    public void modifysave(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        Date modifyDate = new Date();
        BigDecimal productCount = null;
        BigDecimal totalPrice = null;
        int providerId = 0;
        int isPayment = 0;
        int id = 0;
        //??????????????????
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productUnit = req.getParameter("productUnit");
        String productCountTemp = req.getParameter("productCount");
        String totalPriceTemp = req.getParameter("totalPrice");
        String providerIdTemp = req.getParameter("providerId");
        String isPaymentTemp = req.getParameter("isPayment");
        if (productCountTemp != null && !StringUtils.isNullOrEmpty(productCountTemp)){
            productCount = new BigDecimal(productCountTemp);
        }
        if (totalPriceTemp != null && !StringUtils.isNullOrEmpty(totalPriceTemp)){
            totalPrice = new BigDecimal(totalPriceTemp);
        }
        if (providerIdTemp != null && !StringUtils.isNullOrEmpty(providerIdTemp)){
            providerId = Integer.parseInt(providerIdTemp);
        }
        if (isPaymentTemp != null && !StringUtils.isNullOrEmpty(isPaymentTemp)){
            isPayment = Integer.parseInt(isPaymentTemp);
        }
        //????????????id
        User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
        int userId = user.getId();
        //????????????id
        id = (int) req.getSession().getAttribute("uid");
//      //??????
        Bill bill = new Bill();
        bill.setId(id);
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductUnit(productUnit);
        bill.setProductCount(productCount);
        bill.setTotalPrice(totalPrice);
        bill.setProviderId(providerId);
        bill.setIsPayment(isPayment);
        bill.setModifyBy(userId);
        bill.setModifyDate(modifyDate);
        //??????service???
        BillServiceImpl billService = new BillServiceImpl();
        //????????????
        billService.updateBillById(bill);
        //????????????
        req.getRequestDispatcher("/jsp/bill.do?method=query").forward(req,resp);
    }

    public void delBill(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        int id = 0;
        boolean flag = false;
         //??????????????????
        String billid = req.getParameter("billid");
        System.out.println(billid);
        //????????????
        if (billid != null && !StringUtils.isNullOrEmpty(billid)){
            id = Integer.parseInt(billid);
        }
        //??????service???
        BillServiceImpl billService = new BillServiceImpl();
        flag = billService.delBill(id);
        HashMap<String,String> resultMap = new HashMap<>();
        resp.setContentType("application/json");
        if (flag){
            resultMap.put("delResult","true");
        }else if (billid == null){
            resultMap.put("delResult","notexist");
        }else {
            resultMap.put("delResult","false");
        }
        //?????????json
        String json = JSONArray.toJSONString(resultMap);
        //?????????
        PrintWriter out = resp.getWriter();
        out.write(json);
        out.flush();
        out.close();
    }

    public void add(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        BigDecimal productCount = null;
        BigDecimal totalPrice = null;
        int providerId = 0;
        int isPayment = 0;
        int userId = 0;
        //??????????????????
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productUnit = req.getParameter("productUnit");
        String productCountTemp = req.getParameter("productCount");
        String totalPriceTemp = req.getParameter("totalPrice");
        String providerIdTemp = req.getParameter("providerId");
        String isPaymentTemp = req.getParameter("isPayment");
        //??????????????????
        if (productCountTemp != null && !StringUtils.isNullOrEmpty(productCountTemp)){
            productCount = new BigDecimal(productCountTemp);
        }
        if (totalPriceTemp != null && !StringUtils.isNullOrEmpty(totalPriceTemp)){
            totalPrice = new BigDecimal(totalPriceTemp);
        }
        if (providerIdTemp != null && !StringUtils.isNullOrEmpty(providerIdTemp)){
            providerId = Integer.parseInt(providerIdTemp);
        }
        if (isPaymentTemp != null && !StringUtils.isNullOrEmpty(isPaymentTemp)){
            isPayment = Integer.parseInt(isPaymentTemp);
        }
        //?????????????????????id
        User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
        userId = user.getId();
        //??????????????????
        Date createDate = new Date();
        //??????
        Bill bill = new Bill();
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductUnit(productUnit);
        bill.setProductCount(productCount);
        bill.setTotalPrice(totalPrice);
        bill.setProviderId(providerId);
        bill.setIsPayment(isPayment);
        bill.setCreatedBy(userId);
        bill.setCreationDate(createDate);
        //??????service???
        BillServiceImpl billService = new BillServiceImpl();
        billService.add(bill);
        //????????????
        req.getRequestDispatcher("/jsp/bill.do?method=query").forward(req,resp);
    }

    public void bcexist(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        boolean flag = false;
        //??????????????????
        String billCode = req.getParameter("billCode");
        //??????service???
        BillServiceImpl billService = new BillServiceImpl();
        //??????????????????????????????
        flag = billService.getBillByBillCode(billCode);
        HashMap<String,String> resultMap = new HashMap<>();
        resp.setContentType("application/json");
        if (flag){
            resultMap.put("billCode","exist");
        }else if (billCode.equals("")){
            resultMap.put("billCode","billCodeIsNull");
        }
        //?????????json
        String json = JSONArray.toJSONString(resultMap);
        //?????????
        PrintWriter out = resp.getWriter();
        out.write(json);
        out.flush();
        out.close();
    }

    @Test
    public void test() throws SQLException {
        BillServiceImpl billService = new BillServiceImpl();
        Bill billById = billService.getBillById(13);
        System.out.println(billById.getProductCount());
    }
}
