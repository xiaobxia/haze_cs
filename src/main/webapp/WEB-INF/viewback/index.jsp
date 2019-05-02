<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.info.config.PayContents" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String path = request.getContextPath();
	String basePath = path + "/common/back";
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
	"yyyy-MM-dd");
	java.util.Date currentTime = new java.util.Date();
	String date = formatter.format(currentTime);
	String appName = PayContents.APP_NAME;
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<!DOCTYPE html >
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <!-- <meta http-equiv="X-UA-Compatible" content="IE=7" /> -->
    <!-- <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" /> -->
    <title><%=appName%>-贷后管理系统 </title>
    <link href="${basePath }/themes/default/style.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="${basePath }/themes/css/core.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="${basePath }/themes/css/print.css" rel="stylesheet" type="text/css" media="print" />

    <link href="${basePath }/uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen" />
    <link rel='icon' href='<%=path %>/admin-favicon.ico' type=‘image/x-ico’ />
    <!--[if IE]>
    <link href="themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
    <![endif]-->
    <style type="text/css">
        #header {
        height: 85px
        }

        #leftside,
        #container,
        #splitBar,
        #splitBarProxy {
        top: 90px
        }
        </style>
        <script src="${basePath }/js/dwz.ui.js" type="text/javascript"></script>
        <script src="${basePath }/js/speedup.js" type="text/javascript"></script>
        <script src="${basePath }/js/jquery-1.7.2.js" type="text/javascript"></script>
        <script src="${basePath }/js/jquery.cookie.js" type="text/javascript"></script>
        <script src="${basePath }/js/jquery.validate.js" type="text/javascript"></script>
        <script src="${basePath }/js/jquery.bgiframe.js" type="text/javascript"></script>
        <script src="${basePath }/js/crm.upload.js" type="text/javascript"></script>
        <script src="${basePath }/js/dwz.min.js" type="text/javascript"></script>
        <script src="${basePath }/js/dwz.regional.zh.js" type="text/javascript"></script>
        <script type="text/javascript" src="${basePath }/uploadify/scripts/swfobject.js"></script>
        <script type="text/javascript" src="${basePath }/uploadify/scripts/jquery.uploadify.min.js"></script>
        <script type="text/javascript" src="${basePath }/uploadify/scripts/jquery.uploadify.v2.1.0.js"></script>
        <script src="${basePath }/xheditor/xheditor-1.1.14-zh-cn.min.js" type="text/javascript"></script>
        <script src="${basePath }/js/user/dwzUtil.js" type="text/javascript"></script>
        <script src="/common/IccTel/Tel.js" type="text/javascript"></script>
        <script src="/common/IccTel/lang/zh_CN.js"></script>
        <script type="text/javascript">
        $(function() {

        //用户进入首页，初始化登录状态
        DWZ.init("<%=path%>/resources/dwz.frag.xml", {
                loginUrl: "login_dialog.html",
                loginTitle: "登录", // 弹出登录对话框
                //		loginUrl:"login.html",	// 跳到登录页面
                statusCode: {
                    ok: 200,
                    error: 300,
                    timeout: 301
                }, //【可选】
                pageInfo: {
                    pageNum: "pageNum",
                    numPerPage: "numPerPage",
                    orderField: "orderField",
                    orderDirection: "orderDirection"
                }, //【可选】
                debug: false, // 调试模式 【true|false】
                callback: function() {
                    initEnv();
                    $("#themeList").theme({
                    themeBase: "${basePath }/themes"
                    });
                }
        });

        <%-- var url_summary = "<%=path %>
        $ {
            ADMIN_URL
        }
        /summary/$ {
            PRE_PATH_VIEW
        }
        ";
        $.getJSON(url_summary + "message.do", function(data) {
            var parent = $("fieldset[name='message']");
            $.each(data, function(val, item) {
                parent.find("." + val).html(item);
            });
        });

        $.getJSON(url_summary + "backlog.do", function(data) {
            var parent = $("fieldset[name='backlog']");
            $.each(data, function(val, item) {
                parent.find("." + val).html(item);
            });
        });
        --%>
            function clearPhoneNum(tel) {
                //自己系统
                $('#Icctxttel').val('');
            }
            function test(tel){
                console.log(tel);
            }
            var tel;
            IccPhone.ready(function (P) {
                tel = P.create('div[id="Icctel"]', {
                    seatNo: '${seatExt}',//工号
                    seatExt: '${seatExt}',//分机号
                    seatPassWord: '123456',//密码
                    iccSerVicePath: 'ws://192.168.1.254:5061',//服务地址
                    isCallinAlert: true,//呼入是否弹屏
                    CallinFn: test,//呼入弹频的方法(由对接方提供如上【test】参数tel)
                    isCalloutAlert: true,//呼出是否弹屏
                    CalloutFn: clearPhoneNum,//同呼入弹屏方法一样
                    items: ['checkIn', 'checkOut', 'txtTel', 'callTel', 'dropTel']//电话条组件(可自己根据权限或是顺序进行相对应的调整)
                });
            })
    });
</script>
</head>

<body scroll="no">
<div id="layout">
    <div id="header">
        <div class="headerNav">
            <a class="logo" href="javascript:void(0)"></a>
            <div style="margin:0 250px"><div id="Icctel" style="visibility: hidden;"></div></div>

            <ul class="nav">
                <li id="switchEnvBox">
                    <a href="javascript:" style="color: black;">${BACK_USER.userAccount},您好! 欢迎登录后台管理平台
                    </a>
                </li>
                <li>
                    <a style="color: black;" href="updateCache" target="ajaxTodo">刷新系统缓存</a>
                </li>
                <li>
                    <a style="color: black;" href="user/updateUserPassword?type=toJsp" target="dialog" mask="true" width="600">修改密码</a>
                </li>
                <li>
                    <a style="color: black;" href="logout">退出</a>
                </li>

            </ul>
            <ul class="themeList" id="themeList">
                <li theme="default">
                    <div class="selected">蓝色</div>
                </li>
                <li theme="green">
                    <div>绿色</div>
                </li>
                <li theme="purple">
                    <div>紫色</div>
                </li>
                <li theme="silver">
                    <div>银色</div>
                </li>
                <li theme="azure">
                    <div>天蓝</div>
                </li>
            </ul>
        </div>
        <%-- 蓝色导航 --%>
        <div id="navMenu" style="overflow: unset">
            <ul>
                <c:forEach items="${menuModuleList}" var="item" varStatus="count">
                    <li <c:if test="${count.count==1}"> class="selected" </c:if> >
                        <a href="${item.moduleUrl}?myId=${item.id}">
                            <span>${item.moduleName}</span>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>

    <div id="leftside">
        <div id="sidebar_s">
            <div class="collapse">
                <div class="toggleCollapse">
                    <div></div>
                </div>
            </div>
        </div>

        <div id="sidebar">
            <div class="accordion" fillSpace="sideBar">
                <c:forEach items="${subMenu}" var="item" varStatus="count">
                    <%-- 左侧目录主导航标题 --%>
                    <div class="accordionHeader">
                        <c:if test="${ count.index==0}">
                            <h2 class="collapsable">
                                <span>icon</span>${item.moduleName }
                            </h2>
                        </c:if>
                        <c:if test="${ count.index>0}">
                            <h2 class="">
                                <span>icon</span>${item.moduleName }
                            </h2>
                        </c:if>
                    </div>
                    <%-- 左侧目录子导航标题 --%>
                    <div class="accordionContent">
                        <ul class="tree treeFolder">
                            <c:forEach items="${item.moduleList}" var="rightSubList">
                                <c:if test="${fn:contains(rightSubList.moduleUrl,'?')}">
                                    <c:if test="${fn:contains(rightSubList.moduleUrl,'myId')}">
                                        <c:set var="startIndx" value="${fn:indexOf(rightSubList.moduleUrl,'myId')}"></c:set>
                                        <c:set var="urlLength" value="${fn:length(rightSubList.moduleUrl)}"></c:set>
                                        <li>
                                            <a href="${rightSubList.moduleUrl}" target="navTab" rel="${fn:substring(rightSubList.moduleUrl,startIndx+5,urlLength)}">${rightSubList.moduleName}</a>
                                        </li>
                                    </c:if>
                                    <c:if test="${!fn:contains(rightSubList.moduleUrl,'myId')}">
                                        <li>
                                            <a href="${rightSubList.moduleUrl}&myId=${rightSubList.id}" target="navTab" rel="${rightSubList.id}">${rightSubList.moduleName}</a>
                                        </li>
                                    </c:if>
                                </c:if>
                                <c:if test="${!fn:contains(rightSubList.moduleUrl,'?')}">
                                    <li>
                                        <a href="${rightSubList.moduleUrl}?myId=${rightSubList.id}" target="navTab" rel="${rightSubList.id}">${rightSubList.moduleName}</a>
                                    </li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    <div id="container">
        <div id="navTab" class="tabsPage">
            <div class="tabsPageHeader">
                <div class="tabsPageHeaderContent">
                    <!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
                    <ul class="navTab-tab">
                        <li tabid="main" class="main">
                            <a href="javascript:;"><span><span class="home_icon">我的主页</span></span>
                            </a>
                        </li>
                    </ul>
                </div>
                <div class="tabsLeft">left</div>
                <!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
                <div class="tabsRight">right</div>
                <!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
                <div class="tabsMore">more</div>
            </div>
            <ul class="tabsMoreList">
                <li>
                    <a href="javascript:;">我的主页</a>
                </li>
            </ul>
            <div style="height: 767px;" class="navTab-panel tabsPageContent layoutBox">
                <div style="display: block;" class="page unitBox">
                    <div class="accountInfo" style="height: 35px;">
                        <div class="right">
                            <p>
                                <%=date%>
                            </p>
                        </div>
                        <p>
                            <span>欢迎,${BACK_USER.userAccount}</span>
                        </p>
                    </div>

    <%-------------  信息统计，待办事项 没有传参数 -----------------%>
                    <div style="height: 747px; overflow: auto;" class="pageFormContent" layoutH="20">
                        <fieldset name="message" style="padding-bottom: 30px;">
                            <legend>信息统计</legend>
                            <dl>
                                <dt class="pfc">当前在线用户数：</dt>
                                <dd class="pfc">
                                    <span class="unit">${SESSION_COUNT }</span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">人</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">今日登陆用户数：</dt>
                                <dd class="pfc">
                                    <span class="unit loginCount"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">人</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">今日注册用户数：</dt>
                                <dd class="pfc">
                                    <span class="unit registerCount"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">人</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">累计注册用户数：</dt>
                                <dd class="pfc">
                                    <span class="unit registerAllCount"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">人</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">今日用户总充值：</dt>
                                <dd class="pfc">
                                    <span class="unit recharge"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">元</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">累计用户总充值：</dt>
                                <dd class="pfc">
                                    <span class="unit rechargeAll"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">元</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">今日用户总提现：</dt>
                                <dd class="pfc">
                                    <span class="unit cash"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">元</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">累计用户总提现：</dt>
                                <dd class="pfc">
                                    <span class="unit cashAll"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">元</span>
                                </dd>
                            </dl>
                            <!-- <dl>
                            <dt>今日平台总收益：</dt>
                            <dd>
                            （<span class="unit"></span>）元
                            </dd>
                            </dl>
                            -->
                            <dl>
                                <dt class="pfc">累计平台总收益：</dt>
                                <dd class="pfc">
                                    <span class="unit platformEarnings"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">元</span>
                                </dd>
                            </dl>
                            <!-- <dl>
                            <dt>今日用户总收益：</dt>
                            <dd>
                            （<span class="unit"></span>）元
                            </dd>
                            </dl> -->
                            <dl>
                                <dt class="pfc">累计用户总收益：</dt>
                                <dd class="pfc">
                                    <span class="unit userEarnings"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">元</span>
                                </dd>
                            </dl>
                        </fieldset>
                        <fieldset name="backlog" style="padding-bottom: 30px;">
                            <legend>待办事项</legend>
                            <dl>
                                <dt class="pfc">待审核的借款项目：</dt>
                                <dd class="pfc">
                                    <span class="unit check"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">个</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">待处理的个人借款：</dt>
                                <dd class="pfc">
                                    <span class="unit dispose"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">个</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">待发布的借款项目：</dt>
                                <dd class="pfc">
                                    <span class="unit publish"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">个</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">待放款的借款项目：</dt>
                                <dd class="pfc">
                                    <span class="unit credit"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">个</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">待审核的认证信息：</dt>
                                <dd class="pfc">
                                    <span class="unit userCheck"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">个</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">提现待审：</dt>
                                <dd class="pfc">
                                    <span class="unit cashCheck"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">笔</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">提现待审金额：</dt>
                                <dd class="pfc">
                                    <span class="unit cashChechMoney"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">元</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt class="pfc">线下充值待审：</dt>
                                <dd class="pfc">
                                    <span class="unit rechargeCheck"></span>
                                </dd>
                                <dd id="xxtj">
                                    <span class="unit1">笔</span>
                                </dd>
                            </dl>
                        </fieldset>

                        <!-- <fieldset>
                        <legend>使用帮助</legend>
                        <dl style="width:100%;">
                        <dt>官方交流网站：</dt>
                        <dd style="width:80%;">
                        <span class="unit"><a style="color:#008bed;" href="http://www.irongbao.com"
                        target="_blank"><u>http://www.irongbao.com</u></a></span>
                        </dd>
                        </dl>
                        </fieldset> -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="footer">
    Copyright &copy; 2013-2014 技术支持：小鱼儿
    <a href="#" target="_blank"></a>
</div>

</body>
</html>