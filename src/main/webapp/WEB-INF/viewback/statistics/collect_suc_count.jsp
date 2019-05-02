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
<form id="pagerForm" onsubmit="return formCheck();"
      action="performance/toCollectionCountPage?myId=${params.myId}" method="post">
    <div class="pageHeader">
        <div class="searchBar">
            <table class="searchContent">
                <tr>

                    <td>日期：
                        <input type="text" name="startDate" id="startDate" value="${params.startDate}"
                               class="date textInput readonly" datefmt="yyyy-MM-dd" readonly="readonly"/>
                        <span id="starttips" style="color:#F00"></span>
                        至 <input type="text" name="endDate" id="endDate" value="${params.endDate}"
                                 class="date textInput readonly" datefmt="yyyy-MM-dd" readonly="readonly"/>
                    </td>

                    <td>
                        催收员:
                        <input type="text" name="collectName" id="collectName"
                               value="${params.collectName }"/>
                    </td>

                    <td>
                        催收组:
                        <select id="groupLevel" name="groupLevel">
                            <option value=""
                                    <c:if test="${params.groupLevel eq ''}">selected="selected"</c:if>>全部
                            </option>
                            <option value="3"
                                    <c:if test="${params.groupLevel eq '3'}">selected="selected"</c:if>>S1
                            </option>
                            <option value="4"
                                    <c:if test="${params.groupLevel eq '4'}">selected="selected"</c:if>>S2
                            </option>
                            <option value="8"
                                    <c:if test="${params.groupLevel eq '8'}">selected="selected"</c:if>>S3
                            </option>
                            <option value="5"
                                    <c:if test="${params.groupLevel eq '5'}">selected="selected"</c:if>>M1_M2
                            </option>
                        </select>
                    </td>

                    <td>
                        排序:
                        <select id="sortBy" name="sortBy">

                            <option value="" <c:if test="${params.sortBy eq ''}">selected="selected"</c:if>>进单量
                            </option>
                            <option value="suc_num"
                                    <c:if test="${params.sortBy eq 'suc_num'}">selected="selected"</c:if>>已催回订单量
                            </option>
                            <option value="renew_num"
                                    <c:if test="${params.sortBy eq 'renew_num'}">selected="selected"</c:if>>续期处理订单量
                            </option>
                            <option value="repay_num"
                                    <c:if test="${params.sortBy eq 'repay_num'}">selected="selected"</c:if>>全款还款订单量
                            </option>
                            <option value="suc_rate"
                                    <c:if test="${params.sortBy eq 'suc_rate'}">selected="selected"</c:if>>回款率
                            </option>
                        </select>

                    </td>

                    <td>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="button" onclick="formCheck()">查询</button>
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
                <th align="center" width="100">催收员</th>
                <th align="center" width="100">进单量</th>
                <th align="center" width="100">已催回订单量</th>
                <th align="center" width="100">续期处理订单量</th>
                <th align="center" width="100">全款还款订单量</th>
                <th align="center" width="100">回款率</th>
            </tr>
            </thead>
            <tbody id="t_body">
            <c:forEach var="oneData" items="${pm.items }" varStatus="status">
                <tr target="id" rel="${oneData.id }">
                    <td>
                            ${status.count}
                    </td>
                    <td>
                            ${oneData.userName}
                    </td>
                    <td>
                            ${oneData.intoNum}
                    </td>
                    <td>
                            ${oneData.sucNum}
                    </td>
                    <td>
                            ${oneData.renewNum}
                    </td>
                    <td>
                            ${oneData.repayNum}
                    </td>
                    <td>
                            <fmt:formatNumber type="number" value="${oneData.sucRate * 100}" maxFractionDigits="2"/>%
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
    
    function formCheck() {
        var startValue = $("#startDate").val();
        var starttips = document.getElementById("starttips");
        if(startValue.length < 1) {
            starttips.innerHTML="请选择起始日期!";
            return;
        } else {
            return navTabSearch(this);
        }
    }

    function report_excel(obj) {
        var href = $(obj).attr("href");
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        var groupLevel = $("#groupLevel").val();
        var collectName = $("#collectName").val();
        var sortBy = $("#sortBy").val();
        var toHref = href;
        if (startDate.length > 0 && toHref.indexOf(startDate) == -1) {
            toHref += "&startDate=" + startDate;
        }
        if (endDate.length > 0 && toHref.indexOf(endDate) == -1) {
            toHref += "&endDate=" + endDate;
        }
        if (groupLevel.length > 0 && toHref.indexOf(groupLevel) == -1) {
            toHref += "&groupLevel=" + groupLevel;
        }
        if (collectName.length > 0 && toHref.indexOf(collectName) == -1) {
            toHref += "&collectName=" + collectName;
        }
        if (sortBy.length > 0 && toHref.indexOf(sortBy) == -1) {
            toHref += "&sortBy=" + sortBy;
        }
        $(obj).attr("href", toHref);
    }
</script>