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
            "<li class ='header'>" +
                "<a href='"+ urlTemp + "'>" +
                    "<i class='fa fa-dashboard'></i><span>浏览标注任务</span>" +
                "</a>" +
            "</li>";
        return result;
    }
}