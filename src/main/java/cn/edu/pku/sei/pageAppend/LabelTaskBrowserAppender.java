package cn.edu.pku.sei.pageAppend;

import mySql.SqlConnector;

import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Created by oliver on 2017/8/11.
 */
public class LabelTaskBrowserAppender {
    private static final int TASK_PER_PAGE = 5;
    private static String taskBrowserFooterAppendString = "";
    private static String mytaskBrowserFooterAppendString = "";
    private static String[] taskBrowserTableHeader;

    public static String getTaskBrowserFooterAppendString(){
        return taskBrowserFooterAppendString;
    }
    public static String getMyTaskBrowserFooterAppendString(){
        return mytaskBrowserFooterAppendString;
    }


    public static String getMyTaskBrowserAppendString(int index,String userID){
        String result = "";
        ResourceBundle bundle = ResourceBundle.getBundle("database");
        String url = bundle.getString("platformInfoDatabaseURL");
        String user = bundle.getString("platformInfoDatabaseUser");
        String pwd = bundle.getString("platformInfoDatabasePwd");
        String driver = bundle.getString("platformInfoDatabaseDriver");
        String tableName = bundle.getString("tasksTableName");

        SqlConnector conn = null ;
        try{
            conn = new SqlConnector(url , user , pwd , driver);
            conn.start();
            String sql = "select count(taskID) from " + tableName + " where userID=?";
            conn.start();
            conn.setPreparedStatement(sql);
            conn.setString(1, userID);
            ResultSet rs = conn.executeQuery();
            int taskCount = 0;
            if(rs.next()){
                taskCount = rs.getInt(1);
            }

            // initiate for the table header for taskBrowser
            initTaskTableHeader();
            result += appendAnTaskToTaskBrowser(taskBrowserTableHeader , null);

            int display = 0 ; // 记录实际上有几个显示
            int end = (index + 1)* TASK_PER_PAGE;
            end = end > taskCount ? taskCount : end;
            display = end - index * TASK_PER_PAGE;
            if(end > 0 && display > 0){
                sql = "select * from (select * from (select * from (select taskID , userID , tableName ,databaseURL , databaseUser from tasks where userID = \""+userID+"\" order by taskID  limit ENDINDEX ) a order by taskID desc) b limit DISPLAY ) c order by taskID asc" ;
                sql = sql.replace("ENDINDEX" , end + "").replace("DISPLAY" , display + "");
                conn.setPreparedStatement(sql);
                rs = conn.executeQuery();
                int columnCount = rs.getMetaData().getColumnCount();
                while(rs.next()){
                    String[] columns = new String[columnCount];
                    for(int i = 0 ; i < columnCount ; i++)
                        columns[i] = rs.getString(i + 1);
                    result += appendAnTaskToTaskBrowser(columns , new String[1]);
                }
                getMyTaskBrowserFooterAppendString(index , taskCount);
            }
        }catch(Exception e){
            result = "";
            System.out.println(e.getMessage());
        }finally{
            conn.close();
            return result;
        }
    }


    public static String getTaskBrowserAppendString(int index){
        String result = "";
        ResourceBundle bundle = ResourceBundle.getBundle("database");
        String url = bundle.getString("platformInfoDatabaseURL");
        String user = bundle.getString("platformInfoDatabaseUser");
        String pwd = bundle.getString("platformInfoDatabasePwd");
        String driver = bundle.getString("platformInfoDatabaseDriver");
        String tableName = bundle.getString("tasksTableName");

        SqlConnector conn = null ;
        try{
            conn = new SqlConnector(url , user , pwd , driver);
            conn.start();
            String sql = "select count(taskID) from " + tableName;
            conn.start();
            conn.setPreparedStatement(sql);
            ResultSet rs = conn.executeQuery();
            int taskCount = 0;
            if(rs.next()){
                taskCount = rs.getInt(1);
            }

            // initiate for the table header for taskBrowser
            initTaskTableHeader();
            result += appendAnTaskToTaskBrowser(taskBrowserTableHeader , null);

            int display = 0 ; // 记录实际上有几个显示
            int end = (index + 1)* TASK_PER_PAGE;
            end = end > taskCount ? taskCount : end;
            display = end - index * TASK_PER_PAGE;
            if(end > 0 && display > 0){
                sql = "select * from (select * from (select * from (select taskID , userID , tableName ,databaseURL , databaseUser from tasks order by taskID limit ENDINDEX ) a order by taskID desc) b limit DISPLAY ) c order by taskID asc" ;
                sql = sql.replace("ENDINDEX" , end + "").replace("DISPLAY" , display + "");
                conn.setPreparedStatement(sql);
                rs = conn.executeQuery();
                int columnCount = rs.getMetaData().getColumnCount();
                while(rs.next()){
                    String[] columns = new String[columnCount];
                    for(int i = 0 ; i < columnCount ; i++)
                        columns[i] = rs.getString(i + 1);
                    result += appendAnTaskToTaskBrowser(columns , new String[1]);
                }
                getTaskBrowserFooterAppendString(index , taskCount);
            }
        }catch(Exception e){
            result = "";
            System.out.println(e.getMessage());
        }finally{
            conn.close();
            return result;
        }
    }




    private static void getMyTaskBrowserFooterAppendString(int index , int taskCount){
        mytaskBrowserFooterAppendString = "";

        String template =
                "<li>" +
                        "<a href='mylabelTaskBrowser.jsp?index=INDEXNUMBER'>DISPLAY</a>" +
                        "</li>";

        if(index > 0){
            mytaskBrowserFooterAppendString += template.replace("INDEXNUM" +
                    "" +
                    "BER" , (index - 1) + "").replace("DISPLAY" , "«");
        }
        mytaskBrowserFooterAppendString += template.replace("INDEXNUMBER" , index + "").replace("DISPLAY" , index + 1 +"");
        if((index + 1) * TASK_PER_PAGE < taskCount){
            mytaskBrowserFooterAppendString += template.replace("INDEXNUMBER" , index + 1 + "").replace("DISPLAY" , "»");
        }
    }
    private static void getTaskBrowserFooterAppendString(int index , int taskCount){
        taskBrowserFooterAppendString = "";

        String template =
                "<li>" +
                    "<a href='labelTaskBrowser.jsp?index=INDEXNUMBER'>DISPLAY</a>" +
                "</li>";

        if(index > 0){
            taskBrowserFooterAppendString += template.replace("INDEXNUM" +
                    "" +
                    "BER" , (index - 1) + "").replace("DISPLAY" , "«");
        }
        taskBrowserFooterAppendString += template.replace("INDEXNUMBER" , index + "").replace("DISPLAY" , index + 1 +"");
        if((index + 1) * TASK_PER_PAGE < taskCount){
            taskBrowserFooterAppendString += template.replace("INDEXNUMBER" , index + 1 + "").replace("DISPLAY" , "»");
        }
    }

    /**
     * when otherInfo equals null , this function will add the table head to taskTable
     * @param columns
     * @param otherInfo
     * @return
     */
    private static String appendAnTaskToTaskBrowser(String[] columns , String[] otherInfo) {
        String result = "";
        String ul_otherInfo = "";
        if (otherInfo != null) {
            ul_otherInfo =
                    "<ul class='treeview-menu'>" +
                            "<li>" +
                            "<div style='margin:10px'>" +

                           // "<button type=\"button\" class=\"btn btn-block btn-default btn-flat\" style=\"text-align:left\">查看任务标注情况 </button>"+
                       //     "<div class=\"box-header\"> <h3 class=\"box-title\">任务标注情况</h3> </div>"+
             /*          "  <table id=\"example2\" class=\"table table-bordered table-hover dataTable\" role=\"grid\" aria-describedby=\"example2_info\">"+
               " <thead>"+
                "<tr role=\"row\">"+
                    "<th rowspan=\"1\" colspan=\"1\">标注人</th>"+
                    "<th rowspan=\"1\" colspan=\"1\">分配任务数量</th>"+
                    "<th rowspan=\"1\" colspan=\"1\">已标注任务数量</th>"+
                    "<th rowspan=\"1\" colspan=\"1\">剩余任务数量</th>"+
                    "<th rowspan=\"1\" colspan=\"1\">是否标完</th>"+
                "</tr></thead>"+
                     "<tr role=\"row\"><td></td> <td></td> <td></td> <td></td><td></td> </tr>"+
                            "<tr role=\"row\"><td></td> <td></td> <td></td> <td></td><td></td> </tr>"+
                            "<tr role=\"row\"><td></td> <td></td> <td></td> <td></td><td></td> </tr>"+
                            "<tr role=\"row\"><td></td> <td></td> <td></td> <td></td><td></td> </tr>"+
                            "<tr role=\"row\"><td></td> <td></td> <td></td> <td></td><td></td> </tr>"+
                            "<tr role=\"row\"><td>总剩余任务数量</td><td></td> </tr>"+
                       "</table>"+*/

                          //         "<input type='text' name='q' class='form-control' placeholder='Search...'>" +
                          //       "<a href='labelTask.jsp?taskID=" + columns[0] + "&pageIndex=0&dataIndex=0'>加入</a>" +
                            "<a href='labelTask.jsp?taskID=" + columns[0] + "&pageIndex=0&dataIndex=0'>进入</a>" +
                          "</div>" +
                            "</li>" +
                            "</ul>";
        }

        result =
            "<li style='list-style-type:none;margin:0px;padding:0px' class='treeview'>" +
                "<a href='http://www.baidu.com'>" +
                    "<span>" +
                        "<table class='table table-bordered' style='margin:0px'>" +
                            "<tbody>" +
                                "<tr id='tasksdTable'>" +
                                    getAnTaskTableString(columns , otherInfo == null) +
                                "</tr>" +
                            "</tbody>" +
                        "</table>" +
                    "</span>" +
                "</a>" +
                ul_otherInfo +
            "</li>" ;
        return result;
    }

    /**
     * every task will show as an table in taskBrowser, this function is used to get the html string
     * @param columns : the meta data of a task
     * @return
     */
    private static String getAnTaskTableString(String[] columns , boolean isTableHeader){
        String result =
                "<th class='task-ID'>"+ columns[0]+"</th>" +
                "<th class='task-creator'>" + columns[1] + "</th>" +
                "<th class='task-tableName'>" + columns[2] + "</th>" +
                "<th class='task-databaseURL'>" + columns[3] + "</th>" +
                "<th class='task-databaseUser'>" + columns[4] + "</th>" ;
        if(isTableHeader)
                result += "<th class='task-operation'>" + columns[5]+ "</th>";
        else{
            result += (
                    "<th class='task-operation'>" +
                            //"<a href='labelTask.jsp>查看任务标注情况</a>"+
                            "<a href='labelTask.jsp?taskID=" + columns[0] + "&pageIndex=0&dataIndex=0'>查看任务情况</a>"+
                  //      "<button href='labelTask.jsp?taskID="+ columns[0] + "'>加入</a>" +
                    "</th>"
            );
        }
        return result;
    }

    private static void initTaskTableHeader(){
        taskBrowserTableHeader = new String[6];
        taskBrowserTableHeader[0] = "#";
        taskBrowserTableHeader[1] = "创建者";
        taskBrowserTableHeader[2] = "数据表名";
        taskBrowserTableHeader[3] = "数据库地址";
        taskBrowserTableHeader[4] = "数据库用户";
        taskBrowserTableHeader[5] = "操作";
    }
}