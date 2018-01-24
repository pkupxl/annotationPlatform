package cn.edu.pku.sei.pageAppend;

/**
 * Created by oliver on 2017/7/28.
 */
public class SidebarAppender implements Appender{
    public String getAppendString(){
        return "";
    }

    public static String sidebarString(){
        String result;
        String urlTemp;
        result = "<li class=\"header\">MAIN </li>";
        //region labelTaskConstructPage
       result += getLabelTaskString();
       result += getLabelTaskBrowserString();
        return result;
    }

    public static String getLabelTaskString(){
        String result = "";
        String urlTemp = "userMainPage.jsp";
        result +=
            "<li class ='header'>" +
                "<a href='"+ urlTemp + "'>" +
                    "<i class='fa fa-dashboard'></i><span>创建标注任务</span>" +
                "</a>" +
            "</li>";
        return result;
    }

    public static String getLabelTaskBrowserString(){
        String result = "";
        String urlTemp = "labelTaskBrowser.jsp";
        result +=
            "<li class ='active treeview menu-open'>" +
                "<a href='"+ urlTemp + "'>" +
                    "<i class='fa fa-dashboard'></i><span>浏览标注任务</span>" +
                        "<span class='pull-right-container'>"+
                            "<i class='fa fa-angle-left pull-right'></i>"+
                        "</span>"+
                "</a>" +
                    "<ul class='treeview-menu'>"+
                        "<li class='active'><a href='labelTaskBrowser.jsp'><i class='fa fa-circle-o'></i>全部任务列表</a></li>"+
                        "<li class='active'><a href='mylabelTaskBrowser.jsp'><i class='fa fa-circle-o'></i>我的创建任务</a></li>"+
                         "<li class='active'><a href='myAnnotateTaskBrowser.jsp'><i class='fa fa-circle-o'></i>我的标注任务</a></li>"+
                    "</ul>"+
            "</li>";
        return result;
    }
}