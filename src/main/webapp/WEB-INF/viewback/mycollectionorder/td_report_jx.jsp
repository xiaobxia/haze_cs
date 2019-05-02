<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.parser.JSONParser" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="org.json.simple.JSONArray" %>
<%@ page import="com.info.back.result.MonthDetailResult" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style type="">
    @charset "utf-8";
    body, h1, h2, h3, h4, h5, h6, hr, p, blockquote, dl, dt, dd, ul, ol, li, pre, form, fieldset, legend, button, input, textarea, th, td {
        margin: 0;
        padding: 0;
    }

    body, button, input, select, textarea {
        font-family: "微软雅黑";, "MicroSoft YaHei", "Arial Narrow", "HELVETICA";
        font-size: 14px;
        padding: 0;
        margin: 0;
        color: #666;
    }

    table {
        border-collapse: collapse;
        border-spacing: 0;
    }

    ul, li {
        margin: 0px;
        padding: 0px;
        list-style: none;
    }

    /*img {*/
    /*border: none;*/
    /*display: block;*/
    /*}*/

    h1, h2, h3, h4, h5, h6, p {
        margin: 0;
        padding: 0;
    }

    .clear {
        clear: both;
        margin: 0;
        padding: 0;
    }

    a {
        text-decoration: none;
        color: #666;
    }

    html {
        -webkit-text-size-adjust: none;
    }

    /* 2016-7-13 修改css */
    input, textarea {
        border: none;
        outline: medium;
    }

    textarea {
        overflow-y: auto;
    }

    .clear {
        clear: both;
        overflow: hidden;
        height: 1px;
        margin-top: -1px;
        font-size: 0;
    }

    /* 用户资信报告 */
    .credit-report {
        width: 800px;
        background-color: #fff;
        margin: 0 auto;
        padding: 38px 66px;
    }

    .credit-report i {
        font-style: normal;
    }

    .credit-header {
        padding-bottom: 6px;
        border-bottom: 2px solid #434343;
    }

    .credit-title {
        text-align: center;
        padding: 26px 0 20px;
    }

    .credit-title h1 {
        font-size: 18px;
        color: #1d1d1d;
        line-height: 18px;
        margin-bottom: 10px;
    }

    .credit-title p {
        font-size: 12px;
        color: #5c5c5c;
    }

    .info-list {
        padding: 10px 20px;
        background: #f9f9f9;
        margin: 0 20px;
    }

    .info-list dl {
        overflow: hidden;
    }

    .info-list dl dd {
        width: 33.33%;
        float: left;
        font-size: 14px;
        color: #4e4e4e;
        margin-bottom: 10px;
    }

    .info-list dl dd span {
        font-size: 14px;
    }

    .m-creditReport {
        margin: 12px 20px;
    }

    .table-box {
        margin-top: 12px;
        border: 1px solid #d0d0d0;
        margin: 20px;
    }

    .table-title {
        background-color: #e4e4e4;
        text-align: center;
        font-size: 15px;
        color: #222;
        line-height: 38px;
    }

    .table-box p {
        font-size: 14px;
        color: #464646;
        margin: 7px 0 2px;
    }

    .table-box table {
        width: 100%;
        text-align: left;
        table-layout: fixed;
    }

    .table-box-ts table {
        table-layout: auto;
    }

    .table-box table thead tr th {
        background: #f6f6f6;
        line-height: 32px;
    }

    .table-box table thead tr {
        height: 21px;
        line-height: 21px;
        font-size: 12px;
        text-align: center;
    }

    .table-box table tbody tr td {
        height: 19px;
        border-bottom: 1px solid #f5f5f5;
        line-height: 28px;
        font-size: 12px;
        overflow: hidden;
        word-wrap: break-word;
        word-break: break-all;
        text-align: center;
    }

    .u-operator table tbody tr td {
        text-align: left;
    }

    .table-box table tbody tr td:first-child,
    .table-box table thead tr th:first-child {
        text-indent: 15px;
    }

    .table-box table tbody tr:nth-child(2n) {
        background-color: rgba(246, 246, 246, 0.6);
    }

    /* tdTopten */
    .table-box > .tdTopten tbody tr td {
        text-align: center;
    }

    .table-box > .tdTopten thead tr:nth-child(2) td {
        background: #eee;
        line-height: 25px;
        border: 1px solid #ddd;
    }

    .table-box > .tdTopten tr td {
        border: 1px solid #ddd;
    }
</style>
<div class="pageContent">
    <table class="" style="width: 100%;" layoutH="10" nowrapTD="false">
        <%--<div>--%>
        <tr target="id" rel="${order.id }" onclick="sel(this);">
            <td>
                <div class="pageContent">
                        <%
						 JSONParser parser = new JSONParser();
						 String	data;
						 JSONObject obj = null;
						 JSONObject baseInfo = null;
						 JSONObject yysInfo = null;
						 Object objData = request.getAttribute("resultTdYunYingShang");
						 if(objData != null){
                            data  =(String) objData;
                            obj  = (JSONObject) parser.parse(data);
                            baseInfo = (JSONObject)obj.get("baseInfo");
                            yysInfo = (JSONObject)obj.get("yysInfo");
						%>
                    <div class="m-creditReport">
                        <div class="credit-title">
                            <h1>互联网用户资信报告</h1>
                        </div>
                        <div class="info-list clearfix">
                            <dl>
                                <dd><span>报告编号：</span><i><%=baseInfo.get("taskId")%>
                                </i></dd>
                                <dd><span>报告生成时间：</span><i><%=baseInfo.get("taskTime")%>
                                </i></dd>
                            </dl>
                            <dl>
                                <dd><span>姓名：</span><i><%=baseInfo.get("name") %>
                                </i></dd>
                                <dd><span>性别：</span><i><%=baseInfo.get("gender") %>
                                </i></dd>
                                <dd><span>年龄：</span><i><%=baseInfo.get("age") %>
                                </i></dd>
                            </dl>
                            <dl>
                                <dd><span>身份证号：</span><i><%=baseInfo.get("idNumber") %>
                                </i></dd>
                            </dl>
                        </div>
                        <%--运营商信息--%>
                        <div class="table-box u-operator">
                            <div class="table-title">运营商信息</div>
                            <table>
                                <tr>
                                    <td>姓名：</td>
                                    <td><%=yysInfo.get("mobileName") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td>手机号码：</td>
                                    <td><%=yysInfo.get("mobile") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td>联系地址：</td>
                                    <td><%=yysInfo.get("address") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td>联系电话：</td>
                                    <td><%=yysInfo.get("concatNo") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td>邮箱地址：</td>
                                    <td><%=yysInfo.get("email") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td>入网时间：</td>
                                    <td><%=yysInfo.get("netTime") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td>本人注册：</td>
                                    <td><%=yysInfo.get("isSelf") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td>账户状态：</td>
                                    <td><%=yysInfo.get("acountStatus") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td>账户星级：</td>
                                    <td><%=yysInfo.get("acountLevel") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td>账户余额：</td>
                                    <td><%=yysInfo.get("acountBalance") %> 元</td>
                                </tr>
                            </table>
                        </div>
                        <%--月使用情况--%>
                        <%--<div class="table-box">--%>
                        <%--<div class="table-title">月使用情况</div>--%>
                        <%--<table>--%>
                        <%--<thead>--%>
                        <%--<c:forEach items="${monthDetails}" begin="0" end="0" var="one" varStatus="index">--%>
                        <%--<th>${one.type}</th>--%>
                        <%--<c:if test="${one.month1 != null}">--%>
                        <%--<th>${one.month1}</th></c:if>--%>
                        <%--<c:if test="${one.month2 != null}">--%>
                        <%--<th>${one.month2}</th></c:if>--%>
                        <%--<c:if test="${one.month3 != null}">--%>
                        <%--<th>${one.month3}</th></c:if>--%>
                        <%--<c:if test="${one.month4 != null}">--%>
                        <%--<th>${one.month4}</th></c:if>--%>
                        <%--<c:if test="${one.month5 != null}">--%>
                        <%--<th>${one.month5}</th></c:if>--%>
                        <%--<c:if test="${one.month6 != null}">--%>
                        <%--<th>${one.month6}</th></c:if>--%>
                        <%--<c:if test="${one.month7 != null}">--%>
                        <%--<th>${one.month7}</th></c:if>--%>
                        <%--<c:if test="${one.month8 != null}">--%>
                        <%--<th>${one.month8}</th></c:if>--%>
                        <%--<c:if test="${one.month9 != null}">--%>
                        <%--<th>${one.month9}</th></c:if>--%>
                        <%--<c:if test="${one.month10 != null}">--%>
                        <%--<th>${one.month10}</th></c:if>--%>
                        <%--<c:if test="${one.month11 != null}">--%>
                        <%--<th>${one.month11}</th></c:if>--%>
                        <%--<c:if test="${one.month12 != null}">--%>
                        <%--<th>${one.month12}</th>--%>
                        <%--</c:if><c:if test="${one.allCount != null}">--%>
                        <%--<th>${one.allCount}</th>--%>
                        <%--</c:if>--%>

                        <%--</c:forEach>--%>
                        <%--</thead>--%>
                        <%--<tbody>--%>

                        <%--<c:forEach items="${monthDetails}" begin="1" var="one" varStatus="index">--%>
                        <%--<tr>--%>
                        <%--<td>${one.type}</td>--%>
                        <%--<c:if test="${one.month1 != null}">--%>
                        <%--<td>${one.month1}</td></c:if>--%>
                        <%--<c:if test="${one.month2 != null}">--%>
                        <%--<td>${one.month2}</td></c:if>--%>
                        <%--<c:if test="${one.month3 != null}">--%>
                        <%--<td>${one.month3}</td></c:if>--%>
                        <%--<c:if test="${one.month4 != null}">--%>
                        <%--<td>${one.month4}</td></c:if>--%>
                        <%--<c:if test="${one.month5 != null}">--%>
                        <%--<td>${one.month5}</td></c:if>--%>
                        <%--<c:if test="${one.month6 != null}">--%>
                        <%--<td>${one.month6}</td></c:if>--%>
                        <%--<c:if test="${one.month7 != null}">--%>
                        <%--<td>${one.month7}</td></c:if>--%>
                        <%--<c:if test="${one.month8 != null}">--%>
                        <%--<td>${one.month8}</td></c:if>--%>
                        <%--<c:if test="${one.month9 != null}">--%>
                        <%--<td>${one.month9}</td></c:if>--%>
                        <%--<c:if test="${one.month10 != null}">--%>
                        <%--<td>${one.month10}</td></c:if>--%>
                        <%--<c:if test="${one.month11 != null}">--%>
                        <%--<td>${one.month11}</td></c:if>--%>
                        <%--<c:if test="${one.month12 != null}">--%>
                        <%--<td>${one.month12}</td>--%>
                        <%--</c:if><c:if test="${one.allCount != null}">--%>
                        <%--<td>${one.allCount}</td>--%>
                        <%--</c:if>--%>
                        <%--</tr>--%>
                        <%--</c:forEach>--%>


                        <%--</tbody>--%>
                        <%--</table>--%>
                        <%--</div>--%>
                        <div class="table-box u-operator">
                            <%--<div class="table-title">TOP &nbsp;10</div>--%>
                            <%--<td colspan="8" style="padding-bottom: 0;">--%>
                            <table class="tdTopten">
                                <thead>
                                <tr>
                                    <th colspan="7"
                                        style="text-align: center;font-weight: bold;font-size: 20px;line-height: 50px;">
                                        TOP &nbsp;10
                                    </th>
                                </tr>
                                <tr>
                                    <td colspan="1">碰撞数:${SJMHModel.tdTopTenSame }</td>
                                    <td colspan="3">呼入</td>
                                    <td colspan="3">呼出</td>
                                </tr>
                                <tr>
                                    <td>排名</td>
                                    <td>电话号码</td>
                                    <td>通话次数</td>
                                    <td>通讯备注</td>
                                    <td>电话号码</td>
                                    <td>通话次数</td>
                                    <td>通讯录备注</td>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${tdTopTenList}" var="in">
                                    <tr>
                                        <td>${in.rank}</td>
                                        <td>
                                                ${in.callInNumber}
                                            <button type="button" onclick="callPhone(${in.callInNumber})">
                                                拨号<img  src="${pageContext.request.contextPath}/common/IccTel/image/icc_phone.png"/>
                                            </button>
                                        </td>
                                        <td>${in.callInFrequency }</td>
                                        <td>${in.callInPerson}</td>
                                        <td>${in.callOutNumber}</td>
                                        <td>${in.callOutFrequency}</td>
                                        <td>${in.callOutPerson}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <%--</td>--%>
                        </div>
                        <%--通话数据分析--%>
                        <div class="table-box table-box-ts">
                            <div class="table-title">通话数据分析(通话详情)</div>
                            <table>
                                <thead>
                                <%
                                    JSONArray callArray = (JSONArray) obj.get("callInfoDetails");
                                %>
                                <tr>
                                    <th>号码</th>
                                    <th>互联网标识</th>
                                    <th>归属地</th>
                                    <th>联系次数</th>
                                    <th>主叫次数</th>
                                    <th>被叫次数</th>
                                    <th>联系时间（分）</th>
                                    <th>主叫时间（分）</th>
                                    <th>被叫时间（分）</th>
                                    <th>通讯录备注</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:if test=""></c:if>
                                <%
                                    for (int i = 0; i < callArray.size(); i++) {
                                        JSONObject objBill = (JSONObject) callArray.get(i);
                                %>

                                <tr>
                                    <td>
                                        <%=objBill.get("tel") %>
                                        <button type="button" onclick="callPhone(${in.callInNumber})">
                                            拨号<img  src="${pageContext.request.contextPath}/common/IccTel/image/icc_phone.png"/>
                                        </button>
                                    </td>
                                    <td>无</td>
                                    <td>无</td>
                                    <td><%=objBill.get("concatCount") %>
                                    </td>
                                    <td><%=objBill.get("originatingCount") %>
                                    </td>
                                    <td><%=objBill.get("terminatingCount") %>
                                    </td>
                                    <td><%=objBill.get("concatTime") %>
                                    </td>
                                    <td><%=objBill.get("originatingTime") %>
                                    </td>
                                    <td><%=objBill.get("terminatingTime") %>
                                    </td>
                                    <td><%=objBill.get("telRemark") %>
                                    </td>
                                </tr>
                                <% }%>
                                <% }%>

                                </tbody>
                            </table>
                        </div>
                        <%--2017-11-2--%>

                    </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<c:if test="${not empty message}">
    <script type="text/javascript">
        alertMsg.error("${message}");
    </script>
</c:if>
<script type="text/javascript">
    function closeFunc(){
        checkOut();
        onClose();
        return true;
    }
</script>