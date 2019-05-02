<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=7" />
		<title>订单减免</title>

	</head>
	<body>
	
	<div class="pageContent">
	  <form id="frm" method="post" enctype="multipart/form-data" action="collectionOrder/addloanCollPaymoney" onsubmit="return validateCallback(this, dialogAjaxDone);">
	                 <input type="hidden" name="parentId" value="${params.parentId}" />
				     <input type="hidden" name="id" id="id" value="${params.id }">
		<div class="pageFormContent" layoutH="50" style="overflow: auto;">
		<fieldset name="message" style="padding-bottom: 30px;">
			<!-- 借款信息 -->
				<legend>欠款信息</legend>
				<table class="userTable">
					<tbody>						
						<tr>
							
							<td>
								<table class="userTable">
									<tr>										
										<dt style="width: 80px;">
											<label>
												欠款金额:
											</label>
										</dt>
										<dd>
											<label>
												${userLoan.loanMoney+userLoan.loanPenalty}
											</label>
										</dd>
										<dt style="width: 80px;">
											<label>
												欠款本金:
											</label>
										</dt>
										<dd>
											<label>
												${userLoan.loanMoney}
											</label>
										</dd>
										<dt style="width: 80px;">
											<label>
												欠款滞纳金:
											</label>
										</dt>
										<dd>
											<label>
												${userLoan.loanPenalty}
											</label>
										</dd>
										<dt style="width: 80px;">
											<label>
												已还金额:
											</label>
										</dt>
										<dd>
											<label>
												${payMonery}
											</label>
										</dd>										
									</td>
									</tr>
								
								</table>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="pageFormContent" layoutH="50" style="overflow: auto;">
					<dl>
						<dt style="width: 80px;">
							<label>
								减免金额:
							</label>
						</dt>
						<dd>
							<input type="text" id="deductionmoney" name="deductionmoney" minlength="1" class="required digits" >
						</dd>
					</dl>
					<div class="divider"></div>
					<dl>
						<dt style="width: 80px;">
							<label>
								减免备注:
							</label>
						</dt>
						<dd>
							<textarea id="deductionremark" name="deductionremark" rows="5" cols="80" maxlength="100" class="required accept "></textarea>
						</dd>
					</dl>
			</div>
			</fieldset>
		</div>
		
		<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="button" id="money" >
										确认
									</button>
								</div>
							</div>
					</ul>
				</div>
	</form>
	
	</div>

</body>
</html>
<script type="text/javascript">
		$(function(){
			$("#money").bind("click",function(){
				if(confirm("是否确定发起订单减免?") == true){
					$("#money").submit();
				}else{
					return false;
				}
			});
		});
</script>
