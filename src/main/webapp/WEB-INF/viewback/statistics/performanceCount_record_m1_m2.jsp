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
        font-size: 16px;padding: 0 15px;
    }
    .huizong-txt label {
        font-size: 16px;
        color: #000;
        font-weight: bold;
    }
</style>
<%-- <script type="text/javascript" src="<%=basePath%>/js/echarts/loadEcharts.js"></script>	 --%>
<form id="pagerForm" onsubmit="return navTabSearch(this);"
      action="performance/toPerformancePage?myId=${params.myId}&groupLevel=5" method="post">
    <div class="pageHeader">
        <div class="searchBar">
            <table class="searchContent">
                <tr>

                    <td>
                        手机号:
                        <input type="text" name="tel_m1_m2" id="tel"
                               value="${params.tel }"/>
                    </td>

                    <td>派单时间：
                        <input type="text" name="begDate" id="begDate_m1_m2" value="${params.begDate}"
                               class="date textInput readonly" datefmt="yyyy-MM-dd" readonly="readonly"/>
                        至 <input type="text" name="endDate" id="endDate_m1_m2" value="${params.endDate}"
                                 class="date textInput readonly" datefmt="yyyy-MM-dd" readonly="readonly"/>
                    </td>

                    <td>
                        排序:
                        <select id="sortBy_m1_m2" name="sortBy">
                            <option value="" <c:if test="${params.sortBy eq ''}">selected="selected"</c:if>>日期
                            </option>
                            <option value="return_principal"
                                    <c:if test="${params.sortBy eq 'return_principal'}">selected="selected"</c:if>>追回本金
                            </option>
                            <option value="renewal_count"
                                    <c:if test="${params.sortBy eq 'renewal_count'}">selected="selected"</c:if>>续期次数
                            </option>
                            <option value="fee"
                                    <c:if test="${params.sortBy eq 'fee'}">selected="selected"</c:if>>违约金
                            </option>
                            <option value="suc_rate"
                                    <c:if test="${params.sortBy eq 'suc_rate'}">selected="selected"</c:if>>历史成功率
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
        <table class="userTable" style="width: 100%;height: " layoutH="112" nowrapTD="false">
            <thead>
            <tr>
                <th align="center" width="50">序号</th>
                <th align="center" width="100">姓名</th>
                <th align="center" width="100">手机号</th>
                <th align="center" width="100">系统进件</th>
                <th align="center" width="100">人工转派</th>
                <th align="center" width="100">成功出件</th>
                <th align="center" width="100">追回本金</th>
                <th align="center" width="100">续期次数</th>
                <th align="center" width="100">违约金</th>
                <th align="center" width="100">日期</th>
                <th align="center" width="100">历史累计进件</th>
                <th align="center" width="100">历史累计成功件</th>
                <th align="center" width="100">历史成功率</th>
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
                            ${oneData.tel}
                    </td>
                    <td>
                            ${oneData.sysOrder}
                    </td>
                    <td>
                            ${oneData.handOrder}

                    </td>
                    <td>
                            ${oneData.outOrder}
                    </td>
                    <td>
                            ${oneData.returnPrincipal}
                    </td>
                    <td>
                            ${oneData.renewalCount}
                    </td>
                    <td>
                            ${oneData.fee}
                    </td>
                    <td>
                            <fmt:formatDate value="${oneData.countDate}" pattern="yyyy-MM-dd"/>
                    </td>
                    <td>
                            ${oneData.totalIntoOrder}
                    </td>
                    <td>
                            ${oneData.totalOutOrder}
                    </td>
                    <td>
                            ${oneData.sucRate}%
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>


        <c:if test="${total != null}">
            <p class="huizong-txt">
                汇总: 总追回本金：<label>${total.totalSucPrinc}</label>&nbsp;&nbsp;总续期次数：<label>${total.totalRenewalCount}</label>&nbsp;&nbsp;
                总违约金：<label>${total.totalFee}</label>&nbsp;&nbsp;累计进件：<label>${total.totalInOrder}</label>&nbsp;&nbsp;成功出件：<label>${total.totalSucOrder}</label>
                &nbsp;&nbsp;成功率：<label>${total.sucRate}%</label>
            </p>
        </c:if>


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

    function reportExcel_m1_m2(obj){
        var href=$(obj).attr("href");
        var begDate=$("#begDate_m1_m2").val();
        var endDate=$("#endDate_m1_m2").val();
        var tel = $("#tel_m1_m2").val();
        var sortBy = $("#sortBy_m1_m2").val();
        var toHref = href;
        if(begDate.length > 0) {
            toHref += "&begDate="+begDate;
        }
        if(endDate.length > 0) {
            toHref += "&endDate="+endDate;
        }
        if(tel.length > 0) {
            toHref += "&tel="+tel;
        }
        if(sortBy.length > 0) {
            toHref += "&sortBy="+sortBy;
        }
        $(obj).attr("href",toHref);
    }
</script>