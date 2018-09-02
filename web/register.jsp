<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- saved from url=(0059)https://adminlte.io/themes/AdminLTE/pages/layout/boxed.html -->
<html class="gr__adminlte_io" style="height: auto; min-height: 100%;">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>AdminLTE 2 | Boxed Layout</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.7 -->
    <link type="text/css" rel="stylesheet" href="css/AdminLTE.min.css">
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="css/general.css">
    <link type="test/css" rel="stylesheet" href="css/index.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <script src="js/sidebarMenuCreator.js"></script>
    <script src="js/index.js"></script>
    <![endif]-->

    <!-- Google Font -->
    <link rel="stylesheet" href="css/css.css">
</head>
<!-- ADD THE CLASS layout-boxed TO GET A BOXED LAYOUT -->
<body class="skin-blue layout-boxed sidebar-mini" data-gr-c-s-loaded="true" style="height: auto; min-height: 100%;">
<div style="height:180px">

</div >
<div class="box box_info" style="width:330px;margin-left:auto;margin-right:auto">
    <div class="col-md-12"  id="register_center">
        <div class="box-header with-border" style="text-align:center">
            <h3>注册</h3>
        </div>
        <form class="form-horizontal" method="post" >
            <div class="box-body" style="text-align:center">
                <div class="form-group" >
                    <label for="register_user" class="col-sm-4 control-label">用户名：</label>
                    <div class="col-sm-8">
                        <input class="form-control" id="register_user" name="register_user" placeholder="user">
                    </div>
                </div>
                <div class="form-group" style="text-align:center">
                    <label for="register_pwd" class="col-sm-4 control-label">密  码：</label>
                    <div class="col-sm-8">
                        <input type="password" class="form-control" id="register_pwd" name="register_pwd" placeholder="password">
                    </div>
                </div>
                <div class="form-group" style="text-align:center">
                    <label for="confirm_pwd" class="col-sm-4 control-label">确认密码：</label>
                    <div class="col-sm-8">
                        <input type="password" class="form-control" id="confirm_pwd" name="confirm_pwd" placeholder="confirm">
                    </div>
                </div>
            </div>
            <div class="box-footer with-border" style="text-align: center">
                <h5><button  onClick="userRegister()" >注册</button><a href="index.jsp">返回登录</a></h5>
            </div>
        </form>
    </div>
</div>

<!-- jQuery 3 -->
<script async="" src="js/analytics.js"></script>
<script src="js/jquery.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<script src="js/bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="js/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="js/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="js/adminlte.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="js/demo.js"></script>
<script src="js/userMainPage.js"></script>
<script src="js/index.js"></script>

</body></html>