<%@ page import="com.info.config.PayContents" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
String path = request.getContextPath()+"";
String appName = PayContents.APP_NAME;
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><%=appName%>-贷后管理系统</title>
<link rel='icon' href='<%=path %>/admin-favicon.ico' type=‘image/x-ico’ />
<script type="text/javascript" src="<%=path %>/resources/js/DD_belatedPNG.js"></script>

<script>
DD_belatedPNG.fix('.container,.sub,background');
</script>

<style type="text/css">
body {
	margin: 0;
	padding: 0;
	font-size: 12px;
	background:#070f29 url(<%=path %>/common/back/images/bg.jpg) top center no-repeat;
	color:#fff;
	background-size: cover;
}
.input {
	width: 250px;
	height: 35px;
	line-height: 35px;
	padding: 0 6px;
	margin-bottom: 8px;
	background: #fff;
	outline: medium none;
	padding-left: 15px;
	color: #333;
	border: 1px solid #e0e0e0;
	border-radius: 3px;
	outline: medium none;
}
.m-formTable{
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%,-50%);
}
.u-title {
	font-size: 26px;
	font-weight: normal;
	border-bottom: 1px solid #eee;
	margin: 25px 80px;
	padding-bottom: 25px;
	margin-bottom: 25px;
}
.u-login {
	width: 275px;
	line-height: 50px;
	height: 50px;
	background: #0c1131;
	border: none;
	color: #fff;
	font-size: 19px;
	letter-spacing: 26px;
	text-align: center;
	margin-left: 14px;
	border-radius: 6px;
	text-indent: 22px;
}
</style>
</head>
<body>
<div class="container">
	<form id="jvForm" name="jvForm" action="login" method="post" onsubmit="return login();">
		<table width="550" border="0" align="center" cellpadding="0" class="m-formTable" cellspacing="0">
			<tr>
				<td><table width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td valign="top" style="width:550px;heigth:340px;" >
								<table width="100%" border="0" cellspacing="0" cellpadding="0" style="background: rgba(250,250,250,.15); margin: 8px;">
									<tr>
										<td height="80" align="center" >
											<h4 class="u-title">贷后管理系统</h4>
										</td>
									</tr>
									<tr style="text-align: center;">
										<td>
											
											<table width="100%" border="0" align="center" cellpadding="0"
												cellspacing="5">
												<tr>
													<td width="211"><input type="text" id="userAccount" placeholder="用户名" name="userAccount"  maxlength="100" class="input" />
													</td>
												</tr>
												<tr>
													<td><input name="userPassword" type="password" placeholder="密 码" id="userPassword" maxlength="32"  class="input" /></td>
												</tr>
												<tr>
													<%--<td><input maxlength="4" style="width:12px; margin-bottom: 10px;" name="captcha"  placeholder="验证码" type="text" id="captcha" class="input"  />--%>
													<%--<img style="width: 90px; vertical-align: -12px; margin-left: 26px;" src="<%=path %>/captcha.svl" onclick="this.src='<%=request.getContextPath() %>/captcha.svl?d='+new Date()*1" valign="bottom" alt="点击更新" title="点击更新" />--%>
													<%--</td>--%>
													<td>
														<input maxlength="4" style="width:130px; " name="captcha" type="text" placeholder="验证码" id="captcha" class="input"  />
														<img id="imgCap" style="width: 90px; vertical-align: -12px; margin-left: 26px;" src="<%=path %>/captcha.svl" onclick="this.src='<%=request.getContextPath() %>/captcha.svl?d='+new Date()*1" valign="bottom" alt="点击更新" title="点击更新" />
														<%-- <span style="width:100px; color:red; display:none;">${msg}</span> --%>
													</td>
												</tr>
												<tr>
													<td height="120" colspan="2" align="center" class="sub">
														<input type="submit" name="submit" value="登 录" class="u-login"> &nbsp;&nbsp;
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table></td>
						</tr>
					</table></td>
			</tr>
		</table>
	</form>
</div>
</body>
<script>
function login(){
    if (jvForm.userAccount.value==""){	//jvForm.userAccount.value ？？？
        alert("用户名不能为空！");
        return false;
    }else if (jvForm.userPassword.value==""){
        alert("密码不能为空！");
        return false;
    }else if (jvForm.captcha.value==""){
        alert("验证码不能为空！");
        return false;
    }
<!--    jvForm.password.value=encryptByDES(jvForm.password.value, jvForm.publickey.value);-->
}
var msg="${message}";
if(msg){
	alert(msg);
}
</script>
</html>
