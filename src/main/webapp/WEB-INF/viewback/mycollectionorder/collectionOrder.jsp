<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%-- 我的催收订单 --%>
<form id="pagerForm" onsubmit="return navTabSearch(this);"
      action="collectionOrder/getListCollectionOrder?myId=${params.myId}" method="post">
    <div class="pageHeader">

        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>借 款 编 号: <input type="text" id="loanId" name="loanId" value="${params.loanId}"/></td>
                    <td>借款人姓名: <input type="text" id="loanRealName" name="loanRealName" value="${params.loanRealName}"/>
                    </td>
                    <td>借款人手机: <input type="text" id="loanUserPhone" name="loanUserPhone"
                                      value="${params.loanUserPhone}"/></td>
                </tr>
                <tr>
                    <td>
                        催 收 时 间:
                        <input type="text" id="collectionBeginTime" name="collectionBeginTime"
                               value="${params.collectionBeginTime}" class="date textInput readonly"
                               datefmt="yyyy-MM-dd" readonly="readonly"/>
                        至
                        <input type="text" id="collectionEndTime" name="collectionEndTime"
                               value="${params.collectionEndTime}" class="date textInput readonly" datefmt="yyyy-MM-dd"
                               readonly="readonly"/>
                    </td>
                    <td>
                        派 单 时 间:
                        <input type="text" id="dispatchBeginTime" name="dispatchBeginTime"
                               value="${params.dispatchBeginTime}" class="date textInput readonly" datefmt="yyyy-MM-dd"
                               readonly="readonly"/>
                        至
                        <input type="text" id="dispatchEndTime" name="dispatchEndTime" value="${params.dispatchEndTime}"
                               class="date textInput readonly" datefmt="yyyy-MM-dd" readonly="readonly"/>
                    </td>
                    <td>逾 期 天 数: <input type="text" id="overDueDaysBegin" name="overDueDaysBegin"
                                        value="${params.overDueDaysBegin}"/>至<input type="text" id="overDueDaysEnd"
                                                                                    name="overDueDaysEnd"
                                                                                    value="${params.overDueDaysEnd}"/>
                    </td>
                </tr>
                <tr>
                    <c:if test="${userGropLeval ne '10021'}">
                        <c:choose>
                            <c:when test="${not empty params.CompanyPermissionsList}">
                                <td>催 收 公 司:
                                    <select name="companyId" id="companyId">
                                        <option value="">全部</option>
                                        <c:forEach var="company" items="${ListMmanLoanCollectionCompany }">
                                            <c:forEach var="companyViw" items="${params.CompanyPermissionsList}">
                                                <c:if test="${companyViw.companyId eq company.id}">
                                                    <option value="${company.id }"
                                                            <c:if test="${company.id eq params.companyId}">selected="selected"</c:if>>
                                                            ${company.title}
                                                    </option>
                                                </c:if>
                                            </c:forEach>
                                        </c:forEach>
                                    </select>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>催 收 公 司:
                                    <select name="companyId" id="companyId">
                                        <option value="">全部</option>
                                        <c:forEach var="company" items="${ListMmanLoanCollectionCompany }">
                                            <option value="${company.id }"
                                                    <c:if test="${company.id eq params.companyId}">selected="selected"</c:if>>
                                                    ${company.title}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                    <td>催&nbsp;&nbsp;&nbsp;&nbsp;收&nbsp;&nbsp;&nbsp;组:
                        <select name="collectionGroup" id="collectionGroup">
                            <c:forEach var="group" items="${dictMap }">
                                <option value="${group.key }"
                                        <c:if test="${group.key eq params.collectionGroup}">selected="selected"</c:if>>
                                        ${group.value}
                                </option>
                            </c:forEach>

                        </select>
                    </td>
                    <td>
                        订单排序:
                        <select id="sortBy" name="sortBy">
                            <option value="" <c:if test="${params.sortBy eq ''}">selected="selected"</c:if>>初始排序
                            </option>
                            <option value="overdueDays ASC"
                                    <c:if test="${params.sortBy eq 'overdueDays ASC'}">selected="selected"</c:if>>逾期天数升序
                            </option>
                            <option value="overdueDays DESC"
                                    <c:if test="${params.sortBy eq 'overdueDays DESC'}">selected="selected"</c:if>>
                                逾期天数降序
                            </option>
                            <option value="dispatch_time ASC"
                                    <c:if test="${params.sortBy eq 'dispatch_time ASC'}">selected="selected"</c:if>>
                                派单时间升序
                            </option>
                            <option value="dispatch_time DESC"
                                    <c:if test="${params.sortBy eq 'dispatch_time DESC'}">selected="selected"</c:if>>
                                派单时间降序
                            </option>
                            <option value="last_collection_time ASC"
                                    <c:if test="${params.sortBy eq 'last_collection_time ASC'}">selected="selected"</c:if>>
                                最新催收时间升序
                            </option>
                            <option value="last_collection_time DESC"
                                    <c:if test="${params.sortBy eq 'last_collection_time DESC'}">selected="selected"</c:if>>
                                最新催收时间降序
                            </option>
                            <option value="update_date ASC"
                                    <c:if test="${params.sortBy eq 'update_date ASC'}">selected="selected"</c:if>>
                                最新还款时间升序
                            </option>
                            <option value="update_date DESC"
                                    <c:if test="${params.sortBy eq 'update_date DESC'}">selected="selected"</c:if>>
                                最新还款时间降序
                            </option>

                        </select>
                    </td>
                    <td>
                        催 收 状 态:
                        <select id="status" name="status">
                            <option value="" <c:if test="${params.status eq '0'}">selected="selected"</c:if>>全部</option>
                            <option value="0" <c:if test="${params.status eq '0'}">selected="selected"</c:if>> 待催收
                            </option>
                            <option value="1" <c:if test="${params.status eq '1'}">selected="selected"</c:if>> 催收中
                            </option>
                            <option value="2" <c:if test="${params.status eq '2'}">selected="selected"</c:if>> 承诺还款
                            </option>
                            <option value="3" <c:if test="${params.status eq '3'}">selected="selected"</c:if>> 待催收（委外）
                            </option>
                            <option value="4" <c:if test="${params.status eq '4'}">selected="selected"</c:if>> 催收成功
                            </option>
                            <option value="5" <c:if test="${params.status eq '5'}">selected="selected"</c:if>> 续期
                            </option>
                            <option value="6" <c:if test="${params.status eq '6'}">selected="selected"</c:if>> 减免申请
                            </option>
                            <option value="7" <c:if test="${params.status eq '7'}">selected="selected"</c:if>> 减免审核成功
                            </option>
                            <option value="8" <c:if test="${params.status eq '8'}">selected="selected"</c:if>> 减免审核拒绝
                            </option>
                            <option value="9" <c:if test="${params.status eq '9'}">selected="selected"</c:if>> 留单
                            </option>
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
        <%-- 调用rightSubList功能(增删改……)组件 --%>
        <jsp:include page="${BACK_URL}/rightSubList">
            <jsp:param value="${params.myId}" name="parentId"/>
        </jsp:include>
        <script type="text/javascript">
            $('.edit').parents('li').css('float', 'right');
        </script>
        <table class="table" style="width: 100%;" layoutH="160"
               nowrapTD="false">
            <thead>
            <tr>
                <th align="center" width="10">
                    <input type="checkbox" id="checkAlls" onclick="checkAll(this);"/>
                </th>
                <th align="center" width="50">
                    借款编号
                </th>
                <th align="center" width="50">
                    借款人姓名
                </th>
                <th align="center" width="50">
                    借款人手机号
                </th>
                <th align="center" width="50">
                    催收状态
                </th>
                <th align="center" width="50">
                    借款金额
                </th>
                <th align="center" width="50">
                    滞纳金
                </th>
                <th align="center" width="50">
                    减免滞纳金
                </th>
                <th align="center" width="50">
                    已还金额
                </th>
                <th align="center" width="50">
                    逾期天数
                </th>
                <th align="center" width="50">
                    催收组
                </th>
                <th align="center" width="80">
                    应还时间
                </th>
                <%--<th align="center" width="70">--%>
                    <%--派单时间--%>
                <%--</th>--%>
                <th align="center" width="70">
                    最新催收时间
                </th>
                <th align="center" width="70">
                    承诺还款时间
                </th>
                <th align="center" width="70">
                    最新还款时间
                </th>
                <th align="center" width="50">
                    最近一次催收备注
                </th>
                <c:if test="${showSave eq 'true'}"><th align="center" width="50">
                    留单
                </th></c:if>
            </tr>
            </thead>
            <tbody id="cs_list_box">
            <c:forEach var="order" items="${page.items}" varStatus="status">
                <tr target="id" rel="${order.id }">
                    <td>
                        <input type="checkbox" name="checkItem" value="${order.id}" group="${order.collectionGroup}"/>
                    </td>
                    <td align="center" width="50">
                            ${order.loanId}
                    </td>
                    <td align="center" width="50">
                            ${order.realName}
                    </td>
                    <td align="center" width="100">
                            ${order.phoneNumber}
                        <button type="button" onclick="callPhone(${order.phoneNumber})">
                            拨号<img  src="${pageContext.request.contextPath}/common/IccTel/image/icc_phone.png"/>
                        </button>
                    </td>
                    <td align="center" width="50">
                        <c:choose>
                            <c:when test="${order.collectionStatus eq '0'}">待催收</c:when>
                            <c:when test="${order.collectionStatus eq '1'}"> 催收中</c:when>
                            <c:when test="${order.collectionStatus eq '2'}">承诺还款</c:when>
                            <c:when test="${order.collectionStatus eq '3'}">待催收（委外）</c:when>
                            <c:when test="${order.collectionStatus eq '4'}">催收成功</c:when>
                            <c:when test="${order.collectionStatus eq '5'}">续期</c:when>
                            <c:when test="${order.collectionStatus eq '6'}">减免申请</c:when>
                            <c:when test="${order.collectionStatus eq '7'}">减免审核成功</c:when>
                            <c:when test="${order.collectionStatus eq '8'}">减免审核拒绝</c:when>
                        </c:choose>
                    </td>
                    <td align="center" width="50">
                            ${order.loanMoney}
                    </td>
                    <td align="center" width="50">
                            ${order.loanPenlty}
                    </td>
                    <td align="center" width="50">
                            ${order.reductionMoney}
                    </td>
                    <td align="center" width="50">
                            ${order.returnMoney}
                    </td>
                    <td class="over-due-days" align="center" width="50">
                            ${order.overdueDays}
                    </td>
                    <td align="center" width="50">
                            ${dictMap[order.collectionGroup]}
                    </td>
                    <td align="center" width="50">
                        <fmt:formatDate value="${order.loanEndTime}" pattern="yyyy-MM-dd"/>
                    </td>
                    <%--<td align="center" width="50">--%>
                        <%--<fmt:formatDate value="${order.dispatchTime}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
                    <%--</td>--%>
                    <td align="center" width="50">
                        <fmt:formatDate value="${order.lastCollectionTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                    <td align="center" width="50">
                        <fmt:formatDate value="${order.promiseRepaymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                    <td align="center" width="50">
                        <c:if test="${order.returnMoney>0.0}">
                            <fmt:formatDate value="${order.currentReturnDay}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </c:if>
                    </td>
                    <td align="center" width="50">
                            ${order.csContent}
                    </td>

                    <c:if test="${showSave eq 'true'}">
                        <td align="center" width="50">
                            <label> <input onclick="edit_save_status('${status.index}')" type="radio" name="save_status${status.index}"
                                           value="${order.csstatus}"
                                           <c:if test="${order.csstatus == 'Y'}">checked</c:if> /></label>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:set var="page" value="${page}"></c:set>
        <!-- 分页 -->
        <%@ include file="/WEB-INF/viewback/page.jsp" %>
    </div>

    <script type="text/javascript">
        function edit_save_status(curr_index) {
            var my_id = $("#cs_list_box .hover").attr('rel');
            var current_status = $("input[name='save_status"+curr_index+"']").val();
            if(current_status == '') {
                current_status = "N";
            }
            var post_data = {"id":my_id, "status": current_status};
            $.post("/back/collectionOrder/updateSaveStatus", post_data, function (result) {
                var result = eval('(' + result + ')');
                if(result.statusCode == '200') {
                    if("N" == current_status) {
                        $("input:radio[name="+curr_index+"]").attr('checked','true');
                        $("input:radio[name="+curr_index+"]").val('Y');
                    } else {
                        $("input:radio[name="+curr_index+"]").attr('checked','false');
                        $("input:radio[name="+curr_index+"]").val('N');
                    }
                    navTab.reloadFlag('${params.myId}');
                    alertMsg.correct(result.message);
                } else {
                    navTab.reloadFlag('${params.myId}');
                    alertMsg.error(result.message);
                }
            })
        }

        function getMsg(obj) {
            var tt = $(".pageContent tbody tr").is(':checked').value();
            var bol = $(obj).is(':checked');
            var text = bol.get(1).text();
            console.log(text);
        }

        function add_red_class() {
            var term = '${term}';
            var overDaysArr = $('#cs_list_box .over-due-days');
            var csStatusArr = $('#cs_list_box tr td:nth-child(5)');
            for (var i = 0; i < overDaysArr.length; i++) {
                if (overDaysArr[i].innerText == term && csStatusArr[i].innerText == '催收中') {
                    overDaysArr[i].parentNode.style.background = '#ff8b8b';
                }
            }
        }

        /* $("#pagerForm tbody tr").each(function() {
            $("input [name='checkItem']").live("click",function(){
                var $thisTD=$(this).parents("tr").find("td:eq(2)");
                var tt = $thisTD.val();
                alert(tt);
                }
            );
        });

          $(".pageContent tbody tr").each(function(){
                $("tr",this).click(function(){
                      alert(1);
                 alert($(this).parents("tr").child("td").get(3));
                          });
                      }); */
    </script>

</form>
<form action="" method="post" id="detail_form">
    <input type="hidden" name="_this_id" value=""/>
</form>

<script type="text/javascript">
    // function testAlert(phoneNumber){
    //     $('#Icctxttel').val('18814871578');
    //     callTel();
    // }
    function closeFunc() {
        checkOut();
        onClose();
        return true;
    }

    function checkAll(obj) {
        var bol = $(obj).is(':checked');
        $("input[name='checkItem']").attr("checked", bol);
    }

    function sel(obj) {
        var bol = true;
        var check = $(obj).find("input[name='checkItem']");
        if ($(check).is(':checked')) {
            bol = false;
        }
        $(check).attr("checked", bol);
    }

    function getOrderIds(obj) {
        var href = $(obj).attr("href").split("&");
        href = href[0] + "&" + href[1];
        var hasDifferentGroup = 0;
        var selectedGroup = '';
        try {
            $("input[name='checkItem']:checked").each(function () {
                var group = $(this).attr("group");
                if (group != undefined && group != '') {
                    if (selectedGroup == '') {
                        selectedGroup = group;//第一次赋值
                    } else if (selectedGroup != group) {// 之后和第一次的值比较，有不同就GG
                        foreach.break = new Error("StopIteration");
                    }
                }
            })
        } catch (e) {
            hasDifferentGroup = 1;
        }
        var ids = "";
        $("input[name='checkItem']:checked").each(function () {
            ids = ids + "," + $(this).val();
        });
        if (ids != "") {
            var toHref = href + "&groupStatus=" + hasDifferentGroup;
            $(obj).attr("href", toHref);
            if (hasDifferentGroup == 0) {
                $.ajax({
                    type: "POST",
                    url: "collectionOrder/getOrderIds",
                    // contentType: "application/json",
                    data: {ids: ids.substring(1)}//参数列表
                });
            }
        } else {
            $(obj).attr("href", href);
        }
    }


    function addIds(obj) {
        var myId = '${myId}';
        var parentId = '${parentId}';
        var id_val = $("input[name='_this_id']").val();
        var btn_url = $(obj).attr('href').split("?")[0];
        btn_url += '?id=' + id_val;
        var paramsMap = '${paramsJson}';
        var jsonObj = eval('(' + paramsMap + ')');
        $.each(jsonObj, function (key, value) {
            if (key != "myId") {
                btn_url += "&" + key + "=" + value;
            }
        });
        btn_url += "&myId=" + "233" + "&parentId=" + "166";
        $(obj).attr("href", btn_url);
    }

    $(function () {
        // add_red_class();
        $(document).on('click', '#cs_list_box .selected', function () {
            // add_red_class();
            $(this).attr("style", "background: '#000000'");
            var _this_id = $(this).attr('rel');
            $("input[name='_this_id']").val(_this_id);
        })
    })

</script>
<c:if test="${not empty message}">
    <script type="text/javascript">
        if ('${navTab}' == 'true') {
            navTab.closeCurrentTab();
        } else {
            $('.dialog').hide();
            $('#dialogBackground').hide();
            $('.shadow').hide();
        }
        alertMsg.error("${message}");
    </script>
</c:if>