<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="cn.edu.pku.sei.pageAppend.SidebarAppender"%>


<!-- saved from url=(0059)https://adminlte.io/themes/AdminLTE/pages/layout/boxed.html -->
<html class="gr__adminlte_io" style="height: auto; min-height: 100%;">
<head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>AdminLTE 2 | Boxed Layout</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.7 -->
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link type="text/css" rel="stylesheet" href="css/font-awesome.min.css">
    <!-- Ionicons -->
    <link type="text/css" rel="stylesheet" href="css/ionicons.min.css">
    <!-- Theme style -->
    <link type="text/css" rel="stylesheet" href="css/AdminLTE.min.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <link type="text/css" rel="stylesheet" href="css/_all-skins.min.css">
    <link type="text/css" rel="stylesheet" href="css/test.css">
    <link type="text/css" rel="stylesheet" href="css/userMainPage.css">
    <link type="text/css" rel="stylesheet" href="css/general.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <script type="text/javascript" src="js/userMainPage.js"></script>
    <script src="js/sidebarMenuCreator.js"></script>
    <![endif]-->

    <!-- Google Font -->
    <link rel="stylesheet" href="css/css.css">
</head>
<!-- ADD THE CLASS layout-boxed TO GET A BOXED LAYOUT -->
<body class="skin-blue layout-boxed sidebar-mini" data-gr-c-s-loaded="true" style="height: auto; min-height: 100%;">
<!-- Site wrapper -->
<div class="wrapper" style="overflow: hidden; height: auto; min-height: 100%;">

    <header class="main-header">
        <!-- Logo -->
        <a href="https://adminlte.io/themes/AdminLTE/index2.html" class="logo">
            <span class="logo-lg"><b>标注系统</b></span>
        </a>
        <nav class="navbar navbar-static-top">
            <!-- Sidebar toggle button-->
            <a href="https://adminlte.io/themes/AdminLTE/pages/layout/boxed.html#" class="sidebar-toggle" data-toggle="push-menu" role="button">
                <span class="sr-only">Toggle navigation</span>
            </a>

            <div class="navbar-custom-menu">
                <ul class="nav navbar-nav">
                    <!-- Messages: style can be found in dropdown.less-->

                </ul>
            </div>
        </nav>
    </header>

    <!-- =============================================== -->

    <!-- Left side column. contains the sidebar -->
    <aside class="main-sidebar">
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar" style="height: auto;">
            <!-- Sidebar user panel -->
            <div class="user-panel">
                <div class="pull-left image">
                    <img src="images/user2-160x160.jpg" class="img-circle" alt="User Image">
                </div>
                <div class="pull-left info">
                    <p>Alexander Pierce</p>
                    <a href="https://adminlte.io/themes/AdminLTE/pages/layout/boxed.html#"><i class="fa fa-circle text-success"></i> Online</a>
                </div>
            </div>
            <!-- search form -->
            <form action="https://adminlte.io/themes/AdminLTE/pages/layout/boxed.html#" method="get" class="sidebar-form">
                <div class="input-group">
                    <input type="text" name="q" class="form-control" placeholder="Search...">
                    <span class="input-group-btn">
                <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
                </button>
              </span>
                </div>
            </form>
            <!-- /.search form -->
            <!-- sidebar menu: : style can be found in sidebar.less -->
            <ul class="sidebar-menu tree" data-widget="tree">
                <%
                    out.print(SidebarAppender.sidebarString());
                %>
            </ul>

        </section>
        <!-- /.sidebar -->
    </aside>

    <!-- =============================================== -->

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper" style="min-height: 976px;">
        <section class="content">
            <ul class="timeline" id="timeline">
                <li id="database" >
                    <i class="fa fa-envelope bg-blue"></i>
                    <div class="timeline-item">
                        <h3 class="timeline-header">选择标注数据所在数据库</h3>
                        <div class="timeline-body">
                            <form class="form-horizontal" role="form">
                                <!--  数据库地址  -->
                                <div class="form-group">
                                    <label for="database_url" class="col-md-4 control-label">Database URL</label>
                                    <div class="col-md-6">
                                        <input id="database_url" type="text" class="form-control" name="database_url" placeholder="127.0.0.1" value="127.0.0.1">
                                    </div>
                                </div>
                                <!--  数据库端口  -->
                                <div class="form-group">
                                    <label for="database_port" class="col-md-4 control-label">Database Port</label>
                                    <div class="col-md-6">
                                        <input id="database_port" type="text" class="form-control" name="database_port" placeholder="3306" value="3306">
                                    </div>
                                </div>
                                <!--  数据库名称  -->
                                <div class="form-group">
                                    <label for="database_database" class="col-md-4 control-label">Database</label>
                                    <div class="col-md-6">
                                        <input id="database_database" type="text" class="form-control" name="database_group" placeholder="stackoverflow" value="stackoverflow">
                                    </div>
                                </div>
                                <!--  数据库用户  -->
                                <div class="form-group">
                                    <label for="database_user" class="col-md-4 control-label">Database User</label>
                                    <div class="col-md-6">
                                        <input id="database_user" type="text" class="form-control" name="database_user" placeholder="root" value="root">
                                    </div>
                                </div>
                                <!--  数据库密码  -->
                                <div class="form-group">
                                    <label for="database_pwd" class="col-md-4 control-label">Database Password</label>
                                    <div class="col-md-6">
                                        <input id="database_pwd" type="password" class="form-control" name="database_pwd" placeholder="" >
                                    </div>
                                </div>
                                <div class="form-group" style="text-align:center" align="center">
                                    <a type="btn btn-block btn-info btn-lg" onclick="getTablesInfo();">CONNECT</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </li>


                <li id="tableSelector" style="opacity:0">
                    <i class="fa fa-database bg-blue"></i>
                    <div class="timeline-item">
                        <h3 class="timeline-header">选择数据表格</h3>
                        <div class="timeline-body">
                            <form class="form-horizontal" role="form">
                                <div class="form-group">
                                    <label class="col-md-2 control-label" for="tableOptions" style="heigth:30px">选择表格</label>
                                    <div class="col-md-6">
                                        <select id="tableOptions" name="tableOptions" style="height:30px; width:150px" onchange="getColumnsInfo();">
                                            <option value="default">default</option>
                                        </select>
                                    </div>
                                </div>
                                <!-- 选择要显示的内容-->
                                <div class="form-group">
                                    <label class="col-md-2 control-label">选择要显示的数据</label>
                                    <div class="col-md-6">
                                        <div class="col-md-12">
                                            <table class="table table-bordered" rowNum="">
                                                <tbody id="displayElements">
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="col-md-12">
                                            <table class="table table-bordered">
                                                <tbody id="displayCandidate">
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group" align="center">
                                    <a type="btn btn-block btn-info btn-lg" onclick="createTask();">建立</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </li>
            </ul>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <footer class="main-footer">
        <div class="pull-right hidden-xs">
            <b>Version</b> 2.4.0
        </div>
        <strong>Copyright © 2014-2016 <a href="https://adminlte.io/">Almsaeed Studio</a>.</strong> All rights
        reserved.
    </footer>
</div>
<!-- ./wrapper -->

<!-- jQuery 3 -->
<script async="" src="js/analytics.js"></script><script src="js/jquery.min.js"></script>
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
</body></html>