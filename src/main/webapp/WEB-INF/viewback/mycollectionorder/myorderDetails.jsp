<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7"/>
    <script type="text/javascript" src="<%=path %>/resources/js/maps.js"></script>
    <script type="text/javascript" src="http://cache.amap.com/lbs/static/addToolbar.js"></script>
    <title>订单详情</title>
    <style type="text/css">
        .userTable td {
            border-bottom: 1px solid #928989;
            border-right: 1px solid #928989;
            line-height: 31px;
            overflow: hidden;
            padding: 0 3px;
            vertical-align: middle;
            font-size: 14px;
        }

        .userTable td a, .detailB td a {
            color: #5dacd0;
        }

        .userTable {
            padding: 0;
            border: solid 1px #928989;
            width: 100%;
            line-height: 21px;
        }

        .tdGround {
            background-color: #ededed;
        }

        .detailB th {
            border: 1px solid darkgray;
            color: #555;
            background: #f5ecec none repeat scroll 0 0;
            font-weight: bold;
            width: 100px;
            line-height: 21px;
        }

        .detailB td {
            border: 1px solid darkgray;
            font-weight: bold;
            width: 100px;
            line-height: 21px;
            text-align: center;
        }

        /*图片资料-查看大图弹窗*/
        .view-larger {
            display: none;
        }

        .view-larger .overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, .6);
            z-index: 999;
        }

        .view-larger .o-header {
            margin: 8px 0;
            background: #000;
            padding: 8px 40px 13px;
            text-align: center;
        }

        .view-larger .o-header p {
            color: #eee;
            font-size: 14px;
            font-weight: bold;
            letter-spacing: 2px;
            margin-top: 5px;
        }

        .view-larger .o-header p span {
            font-size: 14px;
            color: #fff;
        }

        .view-larger .o-header a.close {
            color: #fff;
            font-size: 28px;
            position: absolute;
            right: 40px;
            top: 15px;
        }

        .view-larger .o-middle img {
            position: fixed;
            top: 50%;
            margin-top: -20px;
            z-index: 100000;
            cursor: pointer;
        }

        .view-larger .o-middle img.left {
            left: 10%;
        }

        .view-larger .o-middle img.right {
            right: 10%;
        }

        .view-larger .img-group {
            position: fixed;
            top: 50%;
            left: 50%;
            z-index: 100000;
            display: none;
        }

        .view-larger .img-group img {
            border: 3px solid #DDDDDD;
            float: left;
            width: auto;
            height: auto;
            max-height: 550px;
            max-width: 670px;
        }

        .imgPreview {
            width: 320px;
        }

        .identity td {
            text-align: center;
        }
    </style>
</head>
<body>
<div class="pageHeaderBtn">
    <%-- 调用rightSubList功能(增删改……)组件 --%>
    <jsp:include page="${BACK_URL}/rightSubList">
        <jsp:param value="${params.myId}" name="parentId"/>
    </jsp:include>
    <script type="text/javascript">
        $('.edit').parents('li').css('float', 'right');
    </script>
</div>
<div class="pageContent">
    <div class="pageFormContent" layoutH="100" style="overflow: auto;">
        <!-- 借款信息 -->
        <fieldset>
            <legend>个人信息</legend>
            <table class="userTable userTableId">
                <tbody>
                <tr target="id" rel="${params.id }">
                <tr>
                    <td style="font-weight:bold">用户基本信息</td>
                    <td>
                        <table class="userTable">
                            <tr>
                                <td class="tdGround" style="width:180px;">借款人姓名:</td>
                                <td>${userInfo.realname}</td>
                                <td class="tdGround">借款人手机号码:</td>
                                <td>${userInfo.userPhone}</td>
                                <td class="tdGround">身份证号码:</td>
                                <td>${userInfo.idNumber}</td>
                            </tr>
                            <tr>
                                <td class="tdGround">银行名称:</td>
                                <td>${userCar.depositBank}</td>
                                <td class="tdGround">银行卡号:</td>
                                <td>${userCar.bankCard}</td>
                                <td class="tdGround">预留手机号码:</td>
                                <td>${userCar.mobile}</td>
                            </tr>

                            <tr>
                                <td class="tdGround">性别:</td>
                                <td colspan="2">${userInfo.userSex}</td>
                                <td class="tdGround">现居时长</td>
                                <td colspan="2">
                                    <c:choose>
                                        <c:when test="${userInfo.presentPeriod eq 1}">1~6个月</c:when>
                                        <c:when test="${userInfo.presentPeriod eq 2}">6~12个月</c:when>
                                        <c:when test="${userInfo.presentPeriod eq 3}">1年以上</c:when>
                                        <c:otherwise>--</c:otherwise>
                                    </c:choose>

                                </td>
                            </tr>
                            <tr>
                                <td class="tdGround">婚姻:</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${userInfo.maritalStatus eq 1}">未婚</c:when>
                                        <c:when test="${userInfo.maritalStatus eq 2}">已婚未育</c:when>
                                        <c:when test="${userInfo.maritalStatus eq 3}">未婚已育</c:when>
                                        <c:when test="${userInfo.maritalStatus eq 4}">离异</c:when>
                                        <c:when test="${userInfo.maritalStatus eq 5}">其他</c:when>
                                        <c:otherwise>--</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="tdGround">学历</td>

                                <td>
                                    <c:choose>
                                        <c:when test="${userInfo.education eq 1}">博士</c:when>
                                        <c:when test="${userInfo.education eq 2}">硕士</c:when>
                                        <c:when test="${userInfo.education eq 3}">本科</c:when>
                                        <c:when test="${userInfo.education eq 4}">大专</c:when>
                                        <c:when test="${userInfo.education eq 5}">中专</c:when>
                                        <c:when test="${userInfo.education eq 6}">高中</c:when>
                                        <c:when test="${userInfo.education eq 7}">初中及以下</c:when>
                                        <c:otherwise>--</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="tdGround">邮箱</td>

                                <td>
                                    ${userMap.email}
                                </td>
                            </tr>

                            <tr>
                                <td class="tdGround">QQ:</td>
                                <td>
                                    ${userMap.qq}
                                </td>

                                <td class="tdGround">行业</td>

                                <td>
                                    ${userMap.work_industry}
                                </td>
                                <td class="tdGround">公司</td>

                                <td>
                                    ${userMap.company_name}
                                </td>
                            </tr>

                            <%--2017.11.13 删除名片一栏--%>
                            <tr class="identity">
                                <td class="tdGround" style="height: 100px;">身份证图片:</td>
                                <td colspan="2">
                                    <c:if test="${userInfo.idcardImgZ!=null}">
                                        <img class="imgPreview"
                                             src="${domaiName}${needSeparator}${userInfo.idcardImgZ}"/>
                                    </c:if>
                                </td>
                                <td colspan="2">
                                    <c:if test="${userInfo.idcardImgF!=null}">
                                        <img class="imgPreview"
                                             src="${domaiName}${needSeparator}${userInfo.idcardImgF}"/>
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${userInfo.idcardImgF!=null}">
                                        <img class="imgPreview"
                                             src="${domaiName}${needSeparator}${userInfo.headPortrait}"
                                             style="width: 200px;"/>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td class="tdGround">身份证地址:</td>
                                <td colspan="5">${userInfo.presentAddress}</td>
                            </tr>
                            <tr>
                                <td class="tdGround">现居住地址:</td>
                                <td colspan="5">${userInfo.presentAddress}${userInfo.presentAddressDistinct}</td>
                            </tr>
                            <tr>
                                <td class="tdGround">公司地址:</td>
                                <td colspan="5">${userMap.company_address}-${userMap.company_address_distinct}</td>
                            </tr>

                        </table>
                    </td>
                </tr>
                <tr>
                    <td style="font-weight:bold">借款信息</td>
                    <td>
                        <table class="userTable">
                            <tr>
                                <td class="tdGround" style="width:180px;">欠款金额:</td>
                                <td>${userLoan.loanMoney+userLoan.loanPenalty}</td>
                                <td class="tdGround">欠款本金:</td>
                                <td>${userLoan.loanMoney}</td>
                            </tr>
                            <tr>
                                <td class="tdGround">欠款滞纳金:</td>
                                <td>${userLoan.loanPenalty}</td>
                                <td class="tdGround">已还金额:</td>
                                <td>${payMonery}</td>
                            </tr>
                            <tr>
                                <td class="tdGround">借款时间:</td>
                                <td><fmt:formatDate value="${userLoan.loanStartTime}"
                                                    pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td class="tdGround">应还时间:</td>
                                <td><fmt:formatDate value="${userLoan.loanEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            </tr>
                            <tr>
                                <td class="tdGround">逾期天数:</td>
                                <td>${collectionOrder.overdueDays}</td>
                                <td class="tdGround">承诺还款时间</td>
                                <td><fmt:formatDate value="${collectionOrder.promiseRepaymentTime}"
                                                    pattern="yyyy-MM-dd"/></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                <tr>
                    <td style="font-weight: bold">最后登录时间</td>
                    <td>
                        <table>
                            <tr>
                                <td style="text-indent: 10px;line-height: 38px;width: 50%">
                                    ${lastLoginTime}
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td style="font-weight: bold">用户来源</td>
                    <td>
                        <table>
                            <tr>
                                <td style="text-indent: 10px;line-height: 38px;width: 50%">认证时地理位置：
                                    <span id="gaode_address"></span>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                </tr>
                </tbody>
            </table>
        </fieldset>
        <!-- 个人信息 -->
        <fieldset>
            <legend>还款详情</legend>
            <table class="detailB" width="100%">
                <tr>
                    <th align="center">序号</th>
                    <th align="left">实还本金</th>
                    <th align="center">实还罚息</th>
                    <th align="center">剩余应还本金</th>
                    <th align="center">剩余应还罚息</th>
                    <th align="center">还款方式</th>
                    <th align="center">还款时间</th>
                    <th align="center">催收记录</th>
                </tr>
                <c:forEach var="pay" items="${detailList}" varStatus="status">
                    <tr>
                        <td>
                                ${status.count}
                        </td>
                        <td>
                                ${pay.realMoney}
                        </td>
                        <td>
                            <fmt:formatNumber type="number" value="${pay.realPenlty}" pattern="0.00"
                                              maxFractionDigits="2"/>
                        </td>
                        <td>
                            <fmt:formatNumber type="number" value="${pay.realPrinciple}" pattern="0.00"
                                              maxFractionDigits="2"/>
                        </td>
                        <td>
                            <fmt:formatNumber type="number" value="${pay.realInterest}" pattern="0.00"
                                              maxFractionDigits="2"/>
                        </td>
                        <td>
                            <c:if test="${pay.returnType eq '1' }">支付宝</c:if>
                            <c:if test="${pay.returnType eq '2' }">银行卡主动还款</c:if>
                            <c:if test="${pay.returnType eq '3' }">代扣</c:if>
                            <c:if test="${pay.returnType eq '4' }">对公银行卡转账</c:if>
                            <c:if test="${pay.returnType eq '5' }">线下还款</c:if>
                            <c:if test="${pay.returnType eq '6' }">减免</c:if>
                        </td>
                        <td>
                            <fmt:formatDate value="${pay.updateDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                            <a href="collectionOrder/collectionRecordList?id=${params.id }&amp;myId=237&amp;parentId=166"
                               class="icon" target="dialog" rel="ids" width="1100" height="600"
                               posttype="string">
                                <span>⇢</span> </a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </fieldset>
        <fieldset>
            <legend>代扣记录</legend>
            <table class="detailB" width="100%">
                <tr>
                    <th align="center">创建时间</th>
                    <th align="left">借款人姓名</th>
                    <th align="center">借款人电话</th>
                    <th align="center">催收状态</th>
                    <th align="center">欠款金额</th>
                    <th align="center">已还金额</th>
                    <th align="center">扣款金额</th>
                    <th align="center">扣款状态</th>
                    <th align="center">更新时间</th>
                </tr>
                <c:forEach var="withhold" items="${withholdList}" varStatus="status">
                    <tr>
                        <td>
                            <fmt:formatDate value="${withhold.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                                ${withhold.loanUserName }
                        </td>
                        <td>
                                ${withhold.loanUserPhone }
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${withhold.orderStatus eq 1 }">催收中</c:when>
                                <c:when test="${withhold.orderStatus eq 2}">承诺还款</c:when>
                                <c:when test="${withhold.orderStatus eq 3}">待催收（委外）</c:when>
                                <c:when test="${withhold.orderStatus eq 4}">催收成功</c:when>
                                <c:when test="${withhold.orderStatus eq 5}">续期</c:when>
                                <c:otherwise>待催收</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                                ${withhold.arrearsMoney}
                        </td>
                        <td>
                                ${withhold.hasalsoMoney }
                        </td>
                        <td>
                                ${withhold.deductionsMoney }
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${withhold.status eq 0}">申请中</c:when>
                                <c:when test="${withhold.status eq 1}">成功</c:when>
                                <c:when test="${withhold.status eq 2}">失败</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <fmt:formatDate value="${withhold.updateDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </fieldset>
        <fieldset>
            <legend>客服备注</legend>
            <table class="detailB" width="100%">
                <tr>
                    <th align="center">时间</th>
                    <th align="center">备注标签</th>
                    <th align="center">备注内容</th>
                </tr>
                <c:forEach var="remark" items="${remarks}" varStatus="status">
                    <tr>
                        <td>
                            <fmt:formatDate value="${remark.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                                <%--<c:choose>--%>
                                <%--<c:when test="${remark.remarkFlag == -1}">--%>
                                <%--已关机--%>
                                <%--</c:when>--%>
                                <%--<c:when test="${remark.remarkFlag == -2}">--%>
                                <%--用户不接电话--%>
                                <%--</c:when>--%>
                                <%--<c:when test="${remark.remarkFlag == 1}">--%>
                                <%--在通话中--%>
                                <%--</c:when>--%>
                                <%--<c:when test="${remark.remarkFlag == 2}">--%>
                                <%--立即处理还款--%>
                                <%--</c:when>--%>
                                <%--<c:when test="${remark.remarkFlag == 3}">--%>
                                <%--明天还款--%>
                                <%--</c:when>--%>
                                <%--<c:when test="${remark.remarkFlag == 4}">--%>
                                <%--晚上12点前处理还款--%>
                                <%--</c:when>--%>
                                <%--</c:choose>--%>
                                ${remarkMap.get(remark.remarkFlag.toString())}
                        </td>
                        <td>
                                ${remark.remarkContent}
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </fieldset>


        <fieldset>
            <legend>借款记录</legend>
            <table class="detailB" width="100%">
                <tr>
                    <th align="center">序号</th>
                    <th align="center">借款编号</th>
                    <th align="center">姓名</th>
                    <th align="center">借款时间</th>
                    <th align="center">最后还款时间</th>
                    <th align="center">已还金额</th>
                    <th align="center">应还金额</th>
                    <th align="center">累计逾期天数</th>
                    <th align="center">续还记录</th>
                    <th align="center">催收记录</th>
                </tr>
                <c:forEach var="record" items="${loanRecords}" varStatus="status">
                    <tr>
                        <td>
                                ${status.count}
                        </td>
                        <td>
                                ${record.loanId}
                        </td>
                        <td>
                                ${record.name}
                        </td>
                        <td>
                            <fmt:formatDate value="${record.loanTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                            <fmt:formatDate value="${record.lastRepayTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                                ${record.repayedAmount/100}
                        </td>
                        <td>
                                ${record.total/100}
                        </td>
                        <td>
                                ${record.lateDays}
                        </td>
                        <td>
                            <a href="collectionOrder/getRenewOrPayRecord?assetOrderId=${record.loanId }&amp;myId=237&amp;parentId=166"
                               class="icon" target="dialog" rel="ids" width="1100" height="600"
                               posttype="string">
                                <span>⇢</span> </a>
                        </td>
                        <td>
                            <a href="collectionOrder/collectionRecordList?id=${record.mmanloanOrderId }&amp;myId=237&amp;parentId=166"
                               class="icon" target="dialog" rel="ids" width="1100" height="600"
                               posttype="string">
                                <span>⇢</span> </a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </fieldset>
    </div>
</div>
<%--身份证正反面大图预览弹窗--%>
<div class="view-larger pre-view-l">
    <div class="overlay">
        <div class="o-header">
            <p class="fl count-status">0/0</p>
            <a href="javascript:void(0);" class="close fr">×</a>
            <div class="clear"></div>
        </div>
        <div class="o-middle">
            <img src="http://finance.tan66.com/static/default/img/jiaxi/tu_bg_l.png" class="left" alt="上一张"/>
            <img src="http://finance.tan66.com/static/default/img/jiaxi/tu_bg_r.png" class="right" alt="下一张"/>
        </div>
        <div class="img-group">
            <img src="http://finance.tan66.com/static/default/temp/crowd-product.jpg" alt="">
        </div>
    </div>
</div>
<script type="text/javascript">
    function btn_func(obj) {
        var id_val = '${idVal}';
        if (id_val.length < 0) {
            id_val = $('.userTableId tr:first-child').attr('rel');
        }
        var btn_url = $(obj).attr('href').split("?")[0] + '?id=' + id_val;
        $(obj).attr('href', btn_url);

    }

    function change_url(obj) {
        var btn_url = $(obj).attr('href').split("?")[0];
        var is_next_id = '${this_id}';
        var next_str = "next";
        if (btn_url.indexOf(next_str) < 0) {
            btn_url += '?id=' + is_next_id + "&next=true";
        }
        var paramsMap = '${paramsJson}';
        var paramsMap = '${paramsJson}';
        var jsonObj = eval('(' + paramsMap + ')');
        $.each(jsonObj, function (key, value) {
            if (key != "id" && key != "next") {
                btn_url += "&" + key + "=" + value;
            }
        });
        $(obj).attr('href', btn_url);
    }

    //大图预览
    $(function () {
        $.fn.imgshow = function () {
            this.each(function () {
                var l = $(this).find("img");
                var j = 0;
                var m = 0;
                var n = "";
                var i = "";
                l.click(function () {
                    var k = "";
                    l.each(function (e, d) {
                        $(d).attr("eq", e);
                        var c = $(this).attr("data-original");
                        if (!c) {
                            k += "<img src='" + $(this).attr("src") + "' />"
                        } else {
                            k += "<img src='" + c + "' />"
                        }
                    });
                    $(".img-group").html("");
                    $(".img-group").html(k);
                    i = ".img-group";
                    $(".view-larger").show();
                    n = $(this);
                    m = parseInt(n.attr("eq"));
                    j = l.length;
                    $(i + " img")[m].onload = function () {
                        a(i, m, n.attr("title"))
                    }
                });

                function a(v, h, c) {
                    c = c || "";
                    h += 1;
                    var d = $(v + " img");
                    $(v).show();
                    //$(".title-status").html(c);
                    $(".count-status").html(h + "/" + j);
                    d.css({
                        display: "none",
                        width: "auto",
                        height: "auto"
                    });
                    d.eq(m).show();
                    var u = d.eq(m).height();
                    var f = d.eq(m).width();
                    var e = $(window).height() - 100;
                    var w = $(window).width() - 350;
                    if (u > e) {
                        d.eq(m).height(e)
                    } else {
                        if (f > w) {
                            d.eq(m).width(w)
                        }
                    }
                    var t = d.eq(m).height() / 2;
                    var g = d.eq(m).width() / 2;
                    $(v).stop(true, true).animate({
                        "margin-top": "-" + t + "px",
                        "margin-left": "-" + g + "px"
                    }, 0)
                }

                $(".o-middle .left").click(function (e) {
                    e.stopPropagation();
                    if (m <= 0) {
                        return
                    }
                    m -= 1;
                    a(i, m, n.attr("title"))
                });
                $(".o-middle .right").click(function (e) {
                    e.stopPropagation();
                    if (m >= j - 1) {
                        return
                    }
                    m += 1;
                    a(i, m, n.attr("title"))
                });
                $(".view-larger .lager-colse").click(function (e) {
                    e.stopPropagation();
                    $("body").css("overflow", "auto");
                    $(".view-larger").hide();
                    $(".img-group").hide()
                })

                $(".overlay").on("mouseover", function () {
                }).on("click", function () {
                    $("body").css("overflow", "auto");
                    $(".view-larger").hide();
                    $(".img-group").hide()
                });
            })
        }
        $(".identity").imgshow();
//        $("body").delegate('.pageHeaderBtn .toolBar li',"click",function(){
    });
</script>
<%@include file="popUpWindow.jsp" %>
</body>
</html>
<<script type="text/javascript">
var a = 'asdfsdfsdfsadf';
a=a.split(''); //将a字符串转换成数组
a.splice(1,1,'xxxxx'); //将1这个位置的字符，替换成'xxxxx'. 用的是原生js的splice方法。
console.log(a); //结果是：
["a", "xxxxx", "d", "f", "s", "d", "f", "s", "d", "f", "s", "a", "d", "f"]
a.join('');
</script>
