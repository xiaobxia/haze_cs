<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
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
    <c:choose>
    <c:when test="${SJMHModel != 'null' and !empty SJMHModel}">
        <div layouth="10" class="tdyys-content pageContent" style="overflow: auto;">
            <div class="table-box u-operator">
                <div class="table-title">运营商信息</div>
                <table>
                    <tbody>
                    <tr>
                        <td>姓名：</td>
                        <td>${SJMHModel.name}</td>
                    </tr>
                    <tr>
                        <td>手机号码：</td>
                        <td>${SJMHModel.phoneNumber }</td>
                    </tr>
                    <tr>
                        <td>联系地址：</td>
                        <td>${SJMHModel.local }</td>
                    </tr>
                    <tr>
                        <td>联系电话：</td>
                        <td>${SJMHModel.phoneNumber}</td>
                    </tr>
                    <tr>
                        <td>入网时间：</td>
                        <td>${SJMHModel.netAge } 月</td>
                    </tr>
                    <tr>
                        <td>运营商：</td>
                        <td>${SJMHModel.channel }</td>
                    </tr>
                    <tr>
                        <td>账户状态：</td>
                        <td>${SJMHModel.accountType }</td>
                    </tr>
                    <tr>
                        <td>账户星级：</td>
                        <td>${SJMHModel.accountStart }</td>
                    </tr>
                    <tr>
                        <td>账户余额：</td>
                        <td>
                                ${SJMHModel.accountBalance } 元
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="table-box">
                <div class="table-title">月使用情况</div>
                <table>
                    <thead>
                    <tr>
                        <th>类型</th>
                        <c:forEach items="${SJMHModel.monthList }" var="time">
                            <th>${time }</th>
                        </c:forEach>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>与第一紧急联系人最后一次通话时间</td>
                        <c:forEach items="${SJMHModel.monthList }" var="time">
                            <td>${SJMHModel.lastCallTimeFirst[time]}</td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td>与第二紧急联系人最后一次通话时间</td>
                        <c:forEach items="${SJMHModel.monthList }" var="time">
                            <td>${SJMHModel.lastCallTimeSecond[time]}</td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td>与第一紧急联系人通话次数</td>
                        <c:forEach items="${SJMHModel.monthList }" var="time">
                            <td>${SJMHModel.frequencyFirst[time]}</td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td>与第二紧急联系人通话次数</td>
                        <c:forEach items="${SJMHModel.monthList }" var="time">
                            <td>${SJMHModel.frequencySecond[time]}</td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td>与第一联系人通话次数排名</td>
                        <c:forEach items="${SJMHModel.monthList }" var="time">
                            <td>${SJMHModel.frequencyRankFirst[time]}</td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td>与第二联系人通话次数排名</td>
                        <c:forEach items="${SJMHModel.monthList }" var="time">
                            <td>${SJMHModel.frequencyRankSecond[time]}</td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td>手机关机静默天数</td>
                        <c:forEach items="${SJMHModel.monthList }" var="time">
                            <td>${SJMHModel.silenceDays[time]}</td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td>手机连续关机最大天数</td>
                        <c:forEach items="${SJMHModel.monthList }" var="time">
                            <td>${SJMHModel.maxSilenceDays[time]}</td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td>通话号码数量</td>
                        <c:forEach items="${SJMHModel.monthList }" var="time">
                            <td>${SJMHModel.monthNumberSize[time]}</td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td>手机通话晚间活跃度</td>
                        <c:forEach items="${SJMHModel.monthList }" var="time">
                            <td>${SJMHModel.monthNightFrequency[time]}</td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td>总费用(单位：元)</td>
                        <c:set value="0" var="billNumber"/>
                        <c:forEach items="${SJMHModel.monthList }" var="time">
                            <td>${SJMHModel.billInfoMap[time]}</td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td>通话总时长(单位：分钟)</td>
                        <c:set value="0" var="callCount"/>
                        <c:forEach items="${SJMHModel.monthList }" var="time">
                            <td>${SJMHModel.monthDayDuration[time]}</td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td colspan="8" style="padding-bottom: 0;">
                            <table class="tdTopten">
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
                                <c:forEach items="${SJMHModel.tdTopTenList}" var="in">
                                    <tr>
                                        <td>${in.rank}</td>
                                        <td>${in.callInNumber}</td>
                                        <td>${in.callInFrequency }</td>
                                        <td>${in.callInPerson}</td>
                                        <td>${in.callOutNumber}</td>
                                        <td>${in.callOutFrequency}</td>
                                        <td>${in.callOutPerson}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="table-box table-box-ts">
                <div class="table-title">通话数据分析(通话详情)</div>
                <table>
                    <c:choose>
                    <c:when test="${SJMHModel.numberCallInfoMap != 'null' and !empty SJMHModel.numberCallInfoMap}">
                        <thead>
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

                        <c:forEach items="${SJMHModel.numberCallInfoMap }" var="one">
                            <tr>
                                <td>${one.value.callNumber}</td>
                                <td>${one.value.netMark}</td>
                                <td>${one.value.location}</td>
                                <td>${one.value.frequency}</td>
                                <td>${one.value.frequencyOut}</td>
                                <td>${one.value.frequencyIn}</td>
                                <td>${one.value.duration}</td>
                                <td>${one.value.durationOut}</td>
                                <td>${one.value.durationIn}</td>
                                <td>${one.value.callName}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td>暂无数据</td>
                        </tr>
                    </c:otherwise>
                    </c:choose>
                </table>
            </div>
        </div>
    </c:when>
    <c:otherwise>
    <div class="tdyys-content popContent">
        <a id="tdCloseBtn" href="javascript:;" class="closeBtn"> <span>同盾运营商数据</span> × </a>
        <div class="table-box u-operator">
            <div class="table-title">运营商信息</div>
            <table>
                <tr>
                    <td>暂无数据</td>
                </tr>
            </table>
        </div>
        </c:otherwise>
        </c:choose>
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
