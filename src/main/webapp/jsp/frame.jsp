<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.zeong.util.constants" %>
<%@ page import="cn.zeong.pojo.User" %>
<%@include file="/jsp/common/head.jsp"%>

<%
    User user = new User();
    if (request.getSession().getAttribute(constants.USER_SESSION)!=null){
        user = (User) request.getSession().getAttribute(constants.USER_SESSION);
    }
%>
<div class="right">
    <img class="wColck" src="../images/clock.jpg" alt=""/>
    <div class="wFont">
        <h2><%=user.getUserName()%></h2>
        <p>欢迎来到超市订单管理系统!</p>
    </div>
</div>
</section>
<%@include file="/jsp/common/foot.jsp" %>
