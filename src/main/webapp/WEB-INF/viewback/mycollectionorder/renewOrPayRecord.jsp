<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div class="pageContent">
    <table class="table" style="width: 100%;" layoutH="115"
           nowrapTD="false">
        <thead>
        <tr>
            <th align="center" width="50">
                序号
            </th>
            <th align="center" width="50">
                还款id
            </th>
            <th align="center" width="60">
                操作时间
            </th>
            <th align="center" width="100">
                操作类型
            </th>
            <th align="center" width="100">
                还款金额
            </th>
            <th align="center" width="100">
                剩余未还
            </th>
            <th align="center" width="100">
                逾期天数
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="record" items="${renewOrPayRecords}" varStatus="status">
            <tr>
                <td>
                        ${status.count}
                </td>
                <td>
                        ${record.repayId}
                </td>
                <td>
                    <fmt:formatDate value="${record.handleTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td>
                        ${record.handleType}
                </td>
                <td>
                        ${record.repayedAmount/100 }
                </td>
                <td>
                        ${record.leftAmount/100 }
                </td>
                <td>
                        ${record.lateDays }
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>