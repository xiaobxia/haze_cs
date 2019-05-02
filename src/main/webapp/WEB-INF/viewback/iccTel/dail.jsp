<%--
  Created by IntelliJ IDEA.
  User: tql
  Date: 2018/3/8
  Time: 11:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<script src="${path}/common/IccTel/Tel.js" type="text/javascript"></script>
<script src="${path}/common/IccTel/lang/zh_CN.js" type="text/javascript"></script>
<html>
<head>
    <title>拨号</title>
</head>
<body>
    <div id="Icctel" style="visibility: hidden;"></div>
</body>

<script type="text/javascript">
    $(function(){
        setTimeout(autoCheckIn,400);
    })
    function autoCheckIn(){
        $('#whfckIn').click();
    }
    function telCall(tel) {
        console.log(tel);
    }
    var tel;
    IccPhone.ready(function (P) {
        P.basePath = "${path}/common/IccTel/";
        tel = P.create('div[id="Icctel"]', {
            seatNo: '${seatExt}',//工号
            seatExt: '${seatExt}',//分机号
            seatPassWord: '123456',//密码
            iccSerVicePath: 'ws://192.168.1.254:5061',//服务地址
            isCallinAlert: true,//呼入是否弹屏
            CallinFn: telCall,//呼入弹频的方法(由对接方提供如上【test】参数tel)
            isCalloutAlert: true,//呼出是否弹屏
            CalloutFn: telCall,//同呼入弹屏方法一样
            //            items: ['checkIn', 'checkOut', 'txtTel', 'callTel', 'dropTel', 'threeCall'
            //                , 'dialBack', 'threeTalk', 'killTalk', 'transSeatno', 'freeCall', 'keepCall', 'keepQuiet']//电话条组件(可自己根据权限或是顺序进行相对应的调整)
            items :['checkIn', 'checkOut', 'txtTel']
        });
        $('input[id="Icctxttel"]').attr("value",'${phoneNumber}');
    });
</script>
</html>
