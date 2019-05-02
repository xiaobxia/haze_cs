<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/back";
%>

<style type="text/css">
    .userTable td {
        border-top: 1px solid #928989;
        border-bottom: 1px solid #928989;
        border-right: 1px solid #928989;
        line-height: 31px;
        overflow: hidden;
        padding: 0 3px;
        vertical-align: middle;
        font-size: 14px;
        text-align: center;
    }

    .userTable td a {
        color: #5dacd0;
    }

    .userTable {
        padding: 0;
        border: solid 1px #928989;
        width: auto;
        line-height: 21px;
    }

    .tdGround {
        background-color: #928989;
    }

    .userTable th {
        border: 1px solid darkgray;
        color: #555;
        font-weight: bold;
        width: 100px;
        line-height: 21px;
    }

    .huizong-txt {
        font-size: 16px;
        padding: 0 15px;
    }

    .huizong-txt label {
        font-size: 16px;
        color: #000;
        font-weight: bold;
    }

</style>
<%-- <script type="text/javascript" src="<%=basePath%>/js/echarts/loadEcharts.js"></script>	 --%>
<form id="pagerForm" onsubmit="return navTabSearch(this);"
      action="performance/repayRenewRecordPage?myId=${params.myId}" method="post">
    <div class="pageHeader">
        <div class="searchBar">
            <table class="searchContent">
                <tr>

                    <td>
                        手机号:
                        <input type="text" name="loanTel" id="loanTel"
                               value="${params.loanTel }"/>
                    </td>

                    <td>
                        催收员:
                        <input type="text" name="collectName" id="collectName"
                               value="${params.collectName }"/>
                    </td>

                    <td>日期：
                        <input type="text" name="startDate" id="startDate" value="${params.startDate}"
                               class="date textInput readonly" datefmt="yyyy-MM-dd" readonly="readonly"/>
                        至 <input type="text" name="endDate" id="endDate" value="${params.endDate}"
                                 class="date textInput readonly" datefmt="yyyy-MM-dd" readonly="readonly"/>
                    </td>
                    <td>
                        催收状态:
                        <select id="rechargeStatus" name="rechargeStatus">
                            <option value=""
                                    <c:if test="${params.rechargeStatus eq ''}">selected="selected"</c:if>>全部
                            </option>
                            <option value="5"
                                    <c:if test="${params.rechargeStatus eq '5'}">selected="selected"</c:if>>续期
                            </option>
                            <option value="4"
                                    <c:if test="${params.rechargeStatus eq '4'}">selected="selected"</c:if>>还款
                            </option>
                        </select>
                    </td>
                    <td>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">查询</button>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="pageContent">
        <jsp:include page="${BACK_URL}/rightSubList">
            <jsp:param value="${params.myId}" name="parentId"/>
        </jsp:include>
        <table class="userTable table" style="width: 100%;" layoutH="112" nowrapTD="false">
            <thead>
            <tr>
                <th align="center" width="50">序号</th>
                <th align="center" width="100">催回日期</th>
                <th align="center" width="100">逾期日期</th>
                <th align="center" width="100">借款编号</th>
                <th align="center" width="100">借款人姓名</th>
                <th align="center" width="100">借款人手机号</th>
                <th align="center" width="100">催收状态</th>
                <th align="center" width="100">借款金额</th>
                <th align="center" width="100">滞纳金</th>
                <th align="center" width="100">减免滞纳金</th>
                <th align="center" width="100">还款总金额</th>
                <th align="center" width="100">逾期天数</th>
                <th align="center" width="100">催收员</th>
                <th align="center" width="100">派单人</th>
            </tr>
            </thead>
            <tbody id="t_body">
            <c:forEach var="oneData" items="${pm.items }" varStatus="status">
                <tr target="id" rel="${oneData.id }">
                    <td>
                            ${status.count}
                    </td>


                    <td>
                        <fmt:formatDate value="${oneData.rechargeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>

                    <td>
                        <fmt:formatDate value="${oneData.lateDate}" pattern="yyyy-MM-dd"/>
                    </td>

                    <td>
                            ${oneData.loanId}
                    </td>
                    <td>
                            ${oneData.loanName}
                    </td>
                    <td>
                            ${oneData.loanTel}

                    </td>
                    <td>
                        <c:if test="${oneData.rechargeStatus eq '4'}">还款</c:if>
                        <c:if test="${oneData.rechargeStatus eq '5'}">续期</c:if>
                    </td>
                    <td>
                            ${oneData.loanMoney}
                    </td>
                    <td>
                            ${oneData.lateFee}
                    </td>
                    <td>
                            ${oneData.reducMoney}
                    </td>

                    <td>
                            ${oneData.repayedAmount/100}
                    </td>
                    <td>
                            ${oneData.lateDay}
                    </td>
                    <td>
                            ${oneData.collectName}
                    </td>
                    <td>
                            ${oneData.dispatchName}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <c:set var="page" value="${pm }"></c:set>
        <!-- 分页 -->
        <%@ include file="../page.jsp" %>
    </div>
    <c:if test="${not empty message}">
        <script type="text/javascript">
            alertMsg.error("${message}");
        </script>
    </c:if>
</form>

<script type="text/javascript">

    function report_excel(obj) {
        var href = $(obj).attr("href");
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        var loanTel = $("#loanTel").val();
        var collectName = $("#collectName").val();
        var rechargeStatus = $("#rechargeStatus").val();
        var toHref = href;
        if (startDate.length > 0 && toHref.indexOf(startDate) == -1) {
            toHref += "&startDate=" + startDate;
        }
        if (endDate.length > 0 && toHref.indexOf(endDate) == -1) {
            toHref += "&endDate=" + endDate;
        }
        if (loanTel.length > 0 && toHref.indexOf(loanTel) == -1) {
            toHref += "&loanTel=" + tel;
        }
        if (collectName.length > 0 && toHref.indexOf(collectName) == -1) {
            toHref += "&collectName=" + collectName;
        }
        if (rechargeStatus.length > 0 && toHref.indexOf(rechargeStatus) == -1) {
            toHref += "&rechargeStatus=" + rechargeStatus;
        }
        $(obj).attr("href", toHref);
    }
</script>