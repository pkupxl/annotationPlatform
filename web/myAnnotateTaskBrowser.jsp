<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="cn.edu.pku.sei.pageAppend.SidebarAppender"%>
<%@ page import="cn.edu.pku.sei.pageAppend.LabelTaskBrowserAppender"%>
<%@ page import="cn.edu.pku.sei.CookieOperations" %>

<!-- saved from url=(0059)https://adminlte.io/themes/AdminLTE/pages/layout/boxed.html -->
<html class="gr__adminlte_io" style="height: auto; min-height: 100%;"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

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
    <link type="text/css" rel="stylesheet" href="css/labelTask.css">
    <link type="text/css" rel="stylesheet" href="css/general.css">
    <link type="text/css" rel="stylesheet" href="css/labelTaskBrowser.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <script src="js/sidebarMenuCreator.js"></script>
    <script type="text/javascript" src="js/labelTaskBrowser.js"></script>

    <![endif]-->

    <!-- Google Font -->
    <link rel="stylesheet" href="css/css.css">
</head>
<!-- ADD THE CLASS layout-boxed TO GET A BOXED LAYOUT -->
<body class="skin-blue sidebar-mini" data-gr-c-s-loaded="true" style="height: auto; min-height: 100%;">
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
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                标注任务
                <small>Blank example to the boxed layout</small>
            </h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="browser-section box">
                <div class="box-header with-border" >
                    <a href="myAnnotateTaskBrowser.jsp">我的标注任务</a>
                </div>
                <div class="box-body">
                    <ul id="taskBroswer" class="tree" data-widget="tree">
                    <%
                            int index;
                            try{
                                index = Integer.parseInt(request.getParameter("index"));
                            }catch(Exception e){
                                index = 0;
                            }
                            String userID = CookieOperations.getCookieValue(request , "userID");
                            System.out.println(userID);
                            out.print(LabelTaskBrowserAppender.getMyAnnotateBrowserAppendString(index,userID));
                    %>
                    </ul>
                </div>

                <div class="box-footer clearfix">
                    <ul id="taskBrowserFooter" class="pagination pagination-sm no-margin pull-right">
                    <%
                           out.print(LabelTaskBrowserAppender.getMyAnnotateBrowserFooterAppendString());
                    %>
                    </ul>
                </div>
            </div>
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
<script src="js/labelTaskBrowser.js"></script>

</body></html>