<%@ page language="java" pageEncoding="UTF-8" %>
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

    img {
        border: none;
        display: block;
    }

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
        margin-bottom: 4px;
    }

    .credit-title p {
        font-size: 12px;
        color: #5c5c5c;
    }

    .info-list {

    }

    .info-list dl {
        overflow: hidden;
    }

    .info-list dl dd {
        width: 33.33%;
        float: left;
        font-size: 12px;
        color: #4e4e4e;
        margin-bottom: 10px;
    }

    .table-box {
        margin-top: 12px;
    }

    .table-title {
        height: 26px;
        background-color: #f8bd54;
        text-align: center;
        font-size: 14px;
        color: #666;
        line-height: 26px;
        font-weight: bold;
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

    .table-box table thead tr {
        height: 21px;
        border-top: 2px solid #666;
        border-bottom: 1px solid #888;
        line-height: 21px;
        font-size: 12px;
    }

    .table-box table tbody tr td {
        height: 19px;
        border-bottom: 1px solid #888;
        line-height: 19px;
        font-size: 12px;
        overflow: hidden;
        word-wrap: break-word;
        word-break: break-all;
    }
</style>
<div class="pageContent">
    <table class="" style="width: 100%;" layoutH="10" nowrapTD="false">
        <tbody>
            <td>
                <div class="pageContent">
                    <div class="table-box">
                        <div class="table-title">淘宝收货地址</div>
                        <c:choose>
                            <c:when test="${addrs != 'null' and !empty addrs}">
                                <table style="text-align: center;">
                                    <thead>
                                    <tr>
                                        <th style="width: 10%">收货地址</th>
                                        <th style="width: 10%">收件人</th>
                                        <th style="width: 10%">地区</th>
                                        <th style="width: 10%">手机</th>
                                        <th style="width: 10%">邮编</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${addrs}" var="addr">
                                        <tr>
                                            <td>${addr.address}</td>
                                            <td>${addr.receiveName}</td>
                                            <td>${addr.region}</td>
                                            <td>${addr.telNumber}</td>
                                            <td>${addr.postCode}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <tr>
                                        <td>暂无数据</td>
                                    </tr>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="table-box">
                        <div class="table-title">淘宝购物记录</div>
                        <c:choose>
                            <c:when test="${orders != 'null' and !empty orders}">
                                <table>
                                    <thead>
                                    <tr>
                                        <th>店铺</th>
                                        <th>商品</th>
                                        <th>数量</th>
                                        <th>实付金额</th>
                                        <th>支付时间</th>
                                        <th>交易状态</th>
                                        <th>是否虚拟商品</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${orders}" var="order">
                                        <tr>
                                            <td>${order.shopName }</td>
                                            <td>${order.itemTitle }</td>
                                            <td>${order.totalQuantity }</td>
                                            <td>${order.actual }</td>
                                            <td>${order.endTime }</td>
                                            <td>${order.tradeStatusName }</td>
                                            <td><c:if test="${order.virtualSign eq 'true'}">是</c:if>
                                                <c:if test="${order.virtualSign eq 'false'}">否</c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <tr>
                                        <td>暂无数据</td>
                                    </tr>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </td>
        </tbody>
    </table>
</div>
<c:if test="${not empty message}">
    <script type="text/javascript">
        alertMsg.error("${message}");
    </script>
</c:if>