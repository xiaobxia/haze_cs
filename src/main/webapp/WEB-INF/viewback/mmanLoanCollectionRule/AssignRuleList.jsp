<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    String path = request.getContextPath();
%>

<%-- 催收管理/规则模版设置/规则分配 --%>
<form id="pagerForm" onsubmit="return navTabSearch(this);"
      action="collectionRule/getMmanLoanCollectionRulePage?myId=${params.myId}" method="post">

    <div class="pageHeader">
        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>
                        催收组
                        <select name="collectionGroup">
                            <option value="">全部</option>
                            <c:forEach items="${groupNameMap}" var="map">
                                <option value="${map.key}"
                                        <c:if test="${params.collectionGroup eq map.key}">selected="selected"</c:if> >${map.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>
                        催收公司
                        <select name="companyId">
                            <option value="">全部</option>
                            <c:forEach var="company" items="${companyList}" varStatus="status">
                                <option value="${company.id}"
                                        <c:if test="${params.companyId eq company.id}">selected="selected"</c:if>>${company.title}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">
                                    查询
                                </button>
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
        <table id="cs_bili" class="table" style="width: 100%;" layoutH="112"
               nowrapTD="false">
            <thead>
            <tr>
                <th align="center" width="50">
                    催收公司名称
                </th>
                <th align="center" width="50">
                    催收组
                </th>
                <th align="center" width="100">
                    规则分配
                </th>
            </tr>
            </thead>
            <tbody id="t_body">
            <c:forEach var="rule" items="${pm.items }" varStatus="status">
                <tr target="id" rel="${rule.id }">
                    <td>
                            ${rule.companyName}
                    </td>
                    <td>
                            ${groupNameMap[rule.collectionGroup]}
                    </td>
                    <td>
                        <label>
                            <input onclick="edit_assign_auto()" type="radio" name="'assign_type'+${status.index}" value="auto"
                                    <c:if test="${rule.assignType == 'auto'}"> checked</c:if>/> 均匀分配
                        </label>
                        <label> <input onclick="editAssign()" type="radio" name="'assign_type'+${status.index}"
                                       value="hand"
                                       <c:if test="${rule.assignType == 'hand'}">checked</c:if> />手动配置</label>

                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:set var="page" value="${pm }"></c:set>
        <!-- 分页 -->
        <%@ include file="../page.jsp" %>
    </div>
</form>
<script type="text/javascript">
    function edit_assign_auto() {
        var my_id = $("#t_body .hover").attr('rel');
        var post_data = {"id":my_id};
        $.post("/back/collectionRule/updateAssignToAuto", post_data, function (result) {
            var result = eval('(' + result + ')');
            alertMsg.correct(result.message);
        })
    }

    function editAssign() {
        $('#dialog').fadeIn();
        $('.layer-popup-bg').fadeIn();
        var my_id = $("#t_body .hover").attr('rel');
        $.ajax({
            url: '/back/collectionRule/assignRateData',
            type: 'get',
            dataType: 'json',
            data: {
//                "id": update_id,
                "id": my_id
            },
            success: function (data) {
                if(data == null) {
                    return;
                }
                var fm_body = $('#fm_body');
                fm_body.empty();
                var form = $("#fm");
                try {
                    fm_body.append($('<tr>' +'<input type="hidden" name="rel_id" value="'+my_id+'"/>'+'</tr>'))
                    for (var i = 0; i < data.length; i++) {
                        fm_body.append
//                        ($('<tr>' +
//                            '<td style="display: none"><input type="hidden" name="id\'' + i + '\'"  value="' + data[i].id + '"></td>' +
//
//                            '<td><input name="odvName\'' + i + '\' "      value="' + data[i].odvName + '"></td>' +
//                            '<td><input name="assignRate\'' + i + '\'" value="' + data[i].assignRate + '"></td>' +
//                            '</tr>'));
//                    }
                        ($('<tr>' +
                            '<td style="display: none"><input type="hidden" name="id"  value="' + data[i].id + '"></td>' +

                            '<td><input readonly="true" name="odvName" value="' + data[i].odvName + '"></td>' +
                            '<td><input name="assignRate" value="' + data[i].assignRate + '"></td>' +
                            '</tr>'));
                    }

                } catch (e) {
                }

            }
        })

    }

    $(function () {
        $('#dialog-close').on('click', function () {
            $('#dialog').fadeOut();
            $('.layer-popup-bg').fadeOut();
//            this._reload(true);
            navTab.reloadFlag('294');
        });


    })

    $(document).on('click', '#dialog-sure', function () {
        var post_data = $('#fm').serializeArray();//获取序列化表单元素
//        将请求提交到一般处理程序
        $.post("/back/collectionRule/updateAssignRate", post_data, function (result) {
            var result = eval('(' + result + ')');
            if (result.statusCode == "200") {
                $('#dialog').hide();
                $('.layer-popup-bg').hide();
                alertMsg.correct(result.message);
                return;
            } else {
                alertMsg.error(result.message);
            }

        })


    });

</script>
<style>
    .popup-dialog {
        width: 600px;
        height: 400px;
        padding: 20px;
        background: #ececec;
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        z-index: 100;
    }
    .popup-dialog .func_btn {
        font-size: 16px;
        color: #333;
    }
    .popup-dialog .func_btn {
        text-align: center;
        margin-top: 20px;
    }
    .popup-dialog .func_btn .popup-dialog-sure,
    .popup-dialog .func_btn .popup-dialog-close {
        margin: 0 50px;
    }
    .popup-dialog .func_btn .popup-dialog-sure {
        color: #000;
    }
    .popup-dialog .popup-dialog-close {
        color: #999;
    }
    .popup-dialog table {
        width: 100%;
        border: 1px solid #ddd;
        background: #fff;
    }

    .popup-dialog table tr th {
        background: #F8F8F8;
        font-weight: bold;
    }

    .popup-dialog table tr td,
    .popup-dialog table tr th {
        border: 1px solid #ddd;
        padding: 5px;
        text-indent: 10px;
    }
    .popup-dialog .table-box {
        max-height: 362px;
        overflow-y: scroll;
    }
    .layer-popup-bg {
        display: none;
        position: fixed;
        top: 0;
        left: 0;
        background: rgba(0,0,0,.6);
        width: 100%;
        height: 100%;
        z-index: 100;
    }
</style>
<div class="layer-popup-bg"></div>
<div id="dialog" class="popup-dialog" style="display: none" closed="true"
     buttons="#dlg-buttons">
    <form id="fm" method="post" novalidate action="#">
        <div class="table-box">
            <table>
                <thead>
                <tr>
                    <th>姓名</th>
                    <th>派单比例</th>
                </tr>
                </thead>
                <tbody id="fm_body">
                </tbody>
            </table>
        </div>


        <div class="func_btn">
            <button id="dialog-sure" type="button" class="popup-dialog-sure">确认</button>
            <button id="dialog-close" type="button" class="popup-dialog-close">取消</button>
        </div>
    </form>
</div>