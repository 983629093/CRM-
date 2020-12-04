<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){


		$("#btn1").click(function () {
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});
			//发送一个ajax请求获得数据库列表
			$.ajax({
				url:"workbench/activity/getUserList.do",
				dataType:"json",
				type:"get",
				success:function(data){
					var html="";
					$.each(data,function (i,n) {
						html+="<option value="+n.id+">"+n.name+"</option>"
					})
					$("#create-owner").html(html);
					$("#create-owner").val("${user.id}")
				}
			})
			//显示列表
			$("#createActivityModal").modal();
		})

		//保存按钮点击事件
		$("#saveBtn").click(function () {
			$.ajax({
				url:"workbench/activity/save.do",
				dataType:"json",
				type:"post",
				data:{
					"owner":$("#create-owner").val(),
					"name":$("#create-name").val(),
					"startDate":$("#create-startDate").val(),
					"endDate":$("#create-endDate").val(),
					"cost":$("#create-cost").val(),
					"description":$("#create-descripbtion").val()
				},
				success:function (data) {
					if (data.success) {
						alert("添加成功")
						$("#saveFrom")[0].reset();
						pageList(1,2);
					}else{
						alert("添加失败")
					}
				}
			})
		})

		pageList(1,2);

		//搜索框查询事件
		$("#search-Btn1").click(function () {
			//点击查询按钮的时候 保存搜索框中的值
			$("#hidden-name").val($("#search-name").val())
			$("#hidden-owner").val($("#search-owner").val())
			$("#hidden-startDate").val($("#search-startDate").val())
			$("#hidden-endDate").val($("#search-endDate").val())
			pageList(1,2);
		})

		//全选与反选
		$("#TYJ-chekbox").click(function () {
			$("input[name=xz]").prop("checked",this.checked);
		})

		$("#activityBody").on("click",$("input[name=xz]"),function () {
			$("#TYJ-chekbox").prop("checked",$("input[name=xz]").length== $("input[name=xz]:checked").length);
		})

		//删除按钮点击事件
		$("#btn-delete").click(function () {
			//判断是否选中
			var checkedAll=$("input[name=xz]:checked");
			if(checkedAll.length==0){
				alert("请选择删除选项")
			}else{
				var html="";
				 for (var i=0;i<checkedAll.length;i++){
					html+="id="+$(checkedAll[i]).val();
					if (i!=checkedAll.length-1){
						html+="&";
					}
				 }
				$.ajax({
					url:"workbench/activity/delete.do",
					type:"get",
					dataType:"json",
					data:html,
					success:function (data) {
						if (data.success){
							pageList(1,2);
							alert("删除成功");
						}else{
							alert("删除失败");
						}
					}
				})
			}
		})

		//修改按钮
		$("#updateBtn").click(function () {
			//打开窗口前先加载窗口数据
			//判断是否选择正确
			 var selectNum=$("input[name=xz]:checked").length;
			 var id=$("input[name=xz]:checked").val()
				if (selectNum==0){
					alert("请选择修改列表")
				}else if(selectNum!=1){
					alert("只能选择一条")
				}else{
					$.ajax({
						url:"workbench/activity/selectListAndActivity.do",
						data:{
							"id":id
						},
						dataType:"json",
						type:"post",
						success:function (data) {
							$("#edit-marketActivityOwner").html("<option value='"+data.activity.id+"'>"+data.name+"</option>");
							$("#edit-marketActivityName").val(data.activity.name)
							$("#edit-startTime").val(data.activity.startDate)
							$("#edit-endTime").val(data.activity.endDate)
							$("#edit-cost").val(data.activity.cost)
							$("#edit-describe").val(data.activity.description);
						}
					})
					//数据加载完毕 打开窗口
					$("#editActivityModal").modal();
				}

		})

		//修改市场活动更新按钮
		$("#update-btn").click(function () {
			var id=$("#edit-marketActivityOwner option").val();
			var owner=$("#edit-marketActivityOwner option").html();
			var name=$("#edit-marketActivityName").val();
			var startTime=$("#edit-startTime").val()
			var endTime=$("#edit-endTime").val()
			var cost=$("#edit-cost").val()
			var describe=$("#edit-describe").val();

			//发送ajax请求
			$.ajax({
				url:"workbench/activity/updateActivity.do",
				type:"post",
				dataType:"json",
				data:{
					"id":id,
					"owner":owner,
					"name":name,
					"startTime":startTime,
					"endTime":endTime,
					"cost":cost,
					"describe":describe
				},
				success:function (data) {
					if(data.success){
						alert("修改成功")
					}else{
						alert("修改失败")
					}
				}
			})
		})

//加载主入口结束
	});
					//页数    每页展现数
	function pageList(pageNo,pageSize) {
		//在查询前将隐藏域中赋值到搜索框
		$("#search-name").val($("#hidden-name").val());
		$("#search-owner").val($("#hidden-owner").val());
		$("#search-startDate").val($("#hidden-startDate").val());
		$("#search-endDate").val($("#hidden-endDate").val());
		$.ajax({
			url:"workbench/activity/selectActivity.do",
			dataType:"json",
			type:"get",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$("#search-name").val(),
				"owner":$("#search-owner").val(),
				"startTime":$("#search-startDate").val(),
				"endTime":$("#search-endDate").val()
			},
			success:function (data) {
				var html="";
				$.each(data.list,function (i,n) {
					html+='<tr class="active">'
					html+='<td><input type="checkbox" name="xz" value="'+n.id+'"/></td> '
					html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\'">'+n.name+'</a></td>'
					html+='<td>'+n.owner+'</td>'
					html+='<td>'+n.startDate+'</td>'
					html+='<td>'+n.endDate+'</td>'
					html+='</tr>'
				})
				$("#activityBody").html(html);

				//数据处理完毕后 结合分页插件
				//记录总页数
				var totalPage=data.page%pageSize==0?data.page/pageSize:parseInt(data.page/pageSize)+1;
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPage, // 总页数
					totalRows:data.page, // 总记录条数
					visiblePageLinks: 3, // 显示几个卡片
					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}

		})
	}
</script>
</head>
<body>
	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startDate">
	<input type="hidden" id="hidden-endDate">
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="saveFrom">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>

							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-descripbtion"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime" value="">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="update-btn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="search-Btn1">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="btn1"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="updateBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"  id="btn-delete"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="TYJ-chekbox"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">--%>
							<%--<td><input type="checkbox" /></td>--%>
							<%--<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
                            <%--<td>zhangsan</td>--%>
							<%--<td>2020-10-10</td>--%>
							<%--<td>2020-10-20</td>--%>
						<%--</tr>--%>
                        <%--<tr class="active">--%>
                            <%--<td><input type="checkbox" /></td>--%>
                            <%--<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
                            <%--<td>zhangsan</td>--%>
                            <%--<td>2020-10-10</td>--%>
                            <%--<td>2020-10-20</td>--%>
                        <%--</tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>