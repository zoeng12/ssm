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
        <p>抱歉！您没有该操作权限！</p>
        <div class="providerAddBtn" style="position: absolute; left: 120px">
            <input type="button" name="confirm" id="confirm" value="确定" />
            <input type="button" id="back" name="back" value="返回"/>
        </div>
    </div>
</div>
</section>
<%@include file="/jsp/common/foot.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/exceedAthority.js"></script>
