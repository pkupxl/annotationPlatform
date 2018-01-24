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
    private static String myAnnotateBrowserFooterAppendString="";
    private static String[] taskBrowserTableHeader;

    public static String getTaskBrowserFooterAppendString(){
        return taskBrowserFooterAppendString;
    }

    public static String getMyTaskBrowserFooterAppendString(){
        return mytaskBrowserFooterAppendString;
    }

    public static String getMyAnnotateBrowserFooterAppendString(){
        return myAnnotateBrowserFooterAppendString;
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
            result += appendAnTaskTomyTaskBrowser(taskBrowserTableHeader , null,0,"");

            int display = 0 ; // 记录实际上有几个显示
            int end = (index + 1)* TASK_PER_PAGE;
            end = end > taskCount ? taskCount : end;
            display = end - index * TASK_PER_PAGE;
            if(end > 0 && display > 0){
                sql = "select * from (select * from (select * from (select taskID , userID , tableName ,databaseURL , databaseUser , itemNum , subtaskNum from tasks where userID = \""+userID+"\" order by taskID  limit ENDINDEX ) a order by taskID desc) b limit DISPLAY ) c order by taskID asc" ;
                sql = sql.replace("ENDINDEX" , end + "").replace("DISPLAY" , display + "");
                conn.setPreparedStatement(sql);
                rs = conn.executeQuery();
                int columnCount = rs.getMetaData().getColumnCount();
                while(rs.next()){
                    String[] columns = new String[columnCount];
                    for(int i = 0 ; i < columnCount-2 ; i++)
                        columns[i] = rs.getString(i + 1);
                    columns[columnCount-2]=Integer.toString(rs.getInt(columnCount-1));
                    columns[columnCount-1]=Integer.toString(rs.getInt(columnCount));
                    result += appendAnTaskTomyTaskBrowser(columns , new String[1],index,userID);
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


    public static String getTaskBrowserAppendString(int index,String userID){
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
            result += appendAnTaskToTaskBrowser(taskBrowserTableHeader , null,0,"");

            int display = 0 ; // 记录实际上有几个显示
            int end = (index + 1)* TASK_PER_PAGE;
            end = end > taskCount ? taskCount : end;
            display = end - index * TASK_PER_PAGE;
            if(end > 0 && display > 0){
                sql = "select * from (select * from (select * from (select taskID , userID , tableName ,databaseURL , databaseUser , itemNum ,subtaskNum from tasks order by taskID limit ENDINDEX ) a order by taskID desc) b limit DISPLAY ) c order by taskID asc" ;
                sql = sql.replace("ENDINDEX" , end + "").replace("DISPLAY" , display + "");
                conn.setPreparedStatement(sql);
                rs = conn.executeQuery();
                int columnCount = rs.getMetaData().getColumnCount();
                while(rs.next()) {
                    String[] columns = new String[columnCount];
                    for (int i = 0; i < columnCount - 2; i++)
                        columns[i] = rs.getString(i + 1);
                    columns[columnCount - 2] = Integer.toString(rs.getInt(columnCount-1));
                    columns[columnCount - 1] = Integer.toString(rs.getInt(columnCount));
                    result += appendAnTaskToTaskBrowser(columns, new String[1], index ,userID);
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

    public static String getMyAnnotateBrowserAppendString(int index,String userID){
        String result = "";
        result="<th >创建者</th>" +
                "<th >任务编号</th>" +
                "<th >子任务编号</th>" +
                "<th >标注条目数量</th>"+
                "<th >#</th>";


        ResourceBundle bundle = ResourceBundle.getBundle("database");
        String url = bundle.getString("userInfoDatabaseUrl");
        String user = bundle.getString("userInfoDatabaseUser");
        String pwd = bundle.getString("userInfoDatabasePwd");
        String driver = bundle.getString("userInfoDatabaseDriver");
        String tableName = bundle.getString("userTaskInfoTableName");

        SqlConnector conn = null ;
        try{
            conn = new SqlConnector(url , user , pwd , driver);
            conn.start();
            String sql = "select annotatetask from " + tableName + " where userID = ?";
            conn.setPreparedStatement(sql);
            conn.setString(1,userID);
            ResultSet rs = conn.executeQuery();
            String [] annotates = null;
            int taskCount=0;
            if(rs.next()){
                String annotate=rs.getString(1);
                annotates = annotate.split(";");
                taskCount = annotates.length;
            }
            conn.close();

            int display = 0 ; // 记录实际上有几个显示
            int start = index * TASK_PER_PAGE + 1 ;
            int end = (index + 1)* TASK_PER_PAGE;
            end = end > taskCount ? taskCount : end;
            display = end - index * TASK_PER_PAGE;
            url = bundle.getString("platformInfoDatabaseURL");
            user = bundle.getString("platformInfoDatabaseUser");
            pwd = bundle.getString("platformInfoDatabasePwd");
            driver = bundle.getString("platformInfoDatabaseDriver");

            conn = new SqlConnector(url , user , pwd , driver);
            conn.start();
            String tmp="";
            for( int i = start; i <= end; ++i) {
                String [] info=annotates[i-1].split(",");
                int taskID = Integer.parseInt(info[0]);
                int subtaskID = Integer.parseInt(info[1]);
                sql = "select taskID , userID , tableName ,databaseURL , databaseUser , itemNum , subtaskNum from tasks where taskID = ?";
                conn.start();
                conn.setPreparedStatement(sql);
                conn.setInt(1,taskID);
                rs = conn.executeQuery();
                int columnCount = rs.getMetaData().getColumnCount();
                String[] columns = new String[columnCount];

                if(rs.next())
                {
                    for (int j = 0; j < columnCount - 2; ++j)
                        columns[j] = rs.getString(j + 1);
                    columns[columnCount - 2] = Integer.toString(rs.getInt(columnCount-1));
                    columns[columnCount - 1] = Integer.toString(rs.getInt(columnCount));
                    int totalitemnum=Integer.parseInt(columns[5]);
                    int subtasknum=Integer.parseInt(columns[6]);
                    int average;
                    int itemnum;
                    if(totalitemnum%subtasknum==0){
                        average=totalitemnum/subtasknum;
                    }else{
                        average=totalitemnum/subtasknum+1;
                    }
                    if(subtaskID<subtasknum){
                        itemnum=average;
                    }else{
                        itemnum=totalitemnum-(subtasknum-1)*average;
                    }
                    tmp=tmp+  "<tr >" +
                            "<th >"+columns[1]+
                            "</th >"+
                            "<th >"+taskID+
                            "</th >"+
                            "<th >"+subtaskID+
                            "</th >"+
                            "<th >"+itemnum+
                            "</th >"+
                            "<th >"+"<a href = labelTask.jsp?taskID=" + taskID +"&subtaskID="+subtaskID+ "&pageIndex=0&dataIndex=0>进入标注界面  </a>"+
                            "</th >"+
                            "</tr>" ;
                }
            }
            tmp="<ul class='treeview-menu'>" +
                   tmp+
                    "</ul>";
            result=result+tmp;
            result="<li style='list-style-type:none;margin:0px;padding:0px' class='treeview'>" +
                    "<span>" +
                    "<table class='table table-bordered' style='margin:0px'>" +
                    "<tbody>" +
                    "<tr id='tasksdTable'>" +
                    result+
                    "</tr>" +
                    "</tbody>" +
                    "</table>" +
                    "</span>" +
                    "</li>" ;
            getMyAnnotateBrowserFooterAppendString(index , taskCount);
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

    private static void getMyAnnotateBrowserFooterAppendString(int index , int taskCount){
        myAnnotateBrowserFooterAppendString = "";
        String template =
                "<li>" +
                        "<a href='myAnnotateTaskBrowser.jsp?index=INDEXNUMBER'>DISPLAY</a>" +
                        "</li>";
        if(index > 0){
            myAnnotateBrowserFooterAppendString += template.replace("INDEXNUM" +
                    "" +
                    "BER" , (index - 1) + "").replace("DISPLAY" , "«");
        }
        myAnnotateBrowserFooterAppendString += template.replace("INDEXNUMBER" , index + "").replace("DISPLAY" , index + 1 +"");
        if((index + 1) * TASK_PER_PAGE < taskCount){
            myAnnotateBrowserFooterAppendString += template.replace("INDEXNUMBER" , index + 1 + "").replace("DISPLAY" , "»");
        }
    }


    /**
     * when otherInfo equals null , this function will add the table head to taskTable
     * @param columns
     * @param otherInfo
     * @return
     */
    private static String appendAnTaskToTaskBrowser(String[] columns , String[] otherInfo, int index ,String userID){
        String result = "";
        String ul_otherInfo = "";
        if (otherInfo != null){
            String tmp="0";
            if(columns[6].equals(tmp)){
                ul_otherInfo =
                        "<ul class='treeview-menu'>" +
                                "<div >" +
                                "<span>" +
                                "<table class='table table-bordered' style='margin:0px'>" +
                                "<tbody>" +
                                "<div >" +
                                "<tr >" +
                                "<th >" + "标注条目数量" + "</th>" +
                                "<th >" + "是否已分组分配" + "</th>" +
                                "</tr >" +
                                "<tr >" +
                                "<th >" + columns[5] + "</th>" +
                                "<th >" + "no" + "</th>" +
                                "</tr >" +
                                "</div>" +
                                "</tbody>" +
                                "</table>" +
                                "</span>" +
                                "</div>" +
                                "</ul>";
            }
            else{
              ul_otherInfo = "<ul class='treeview-menu'>" +
                      appendSubTaskToTaskBrowser( columns ,index,userID)+
                      "</ul>";
            }
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

    private static String appendAnTaskTomyTaskBrowser(String[] columns , String[] otherInfo, int index ,String userID){
        String result = "";
        String ul_otherInfo = "";
        if (otherInfo != null){
            String tmp="0";
            if(columns[6].equals(tmp))
                ul_otherInfo =
                        "<ul class='treeview-menu'>" +
                                "<div >" +
                                "<span>" +
                                "<table class='table table-bordered' style='margin:0px'>" +
                                "<tbody>" +

                                "<div >" +
                                "<tr >" +
                                "<th >" + "标注条目数量" + "</th>" +
                                "<th >" + "是否已分组" + "</th>" +
                                "</tr >" +
                                "<tr >" +
                                "<th >" + columns[5] + "</th>" +
                                "<th >" + "no" + "</th>" +
                                "</tr >" +
                                "</div>" +

                                "<div >" +
                                "<label for=\"subtasknum\">分成子任务个数</label>" +
                                "<input id=\"subtasknum\" name=\"subtaskNum\" type=\"text\" >" +
                                "<button onClick=\"Allocate("+ columns[0] + ","+columns[5]+"," +index +")\">确认分组</button>"+
                                "</div>" +

                                "</tbody>" +
                                "</table>" +
                                "</span>" +
                                "</div>" +
/*
                                "<div >" +
                                "<a href='labelTask.jsp?taskID=" + columns[0] + "&pageIndex=0&dataIndex=0'>进入</a>" +
                                "</div>" +*/
                                "</ul>";
            else{
                ul_otherInfo =
                        "<ul class='treeview-menu'>" +
                                appendSubTaskToTaskBrowser( columns ,index,userID)+
                   /*             "<div >" +
                                "<a href='labelTask.jsp?taskID=" + columns[0] + "&pageIndex=0&dataIndex=0'>进入</a>" +
                                "</div>" +*/
                                "</ul>";
            }
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
                result += "<th class='task-operation'>" + columns[5]+"</th>";
        else{
            result += (
                    "<th class='task-operation'>" +
                //            "<a href='labelTask.jsp?taskID=" + columns[0] + "&pageIndex=0&dataIndex=0'>查看任务情况</a>"+
                            "<a>查看任务情况</a>"+
                    "</th>"
            );
        }
        return result;
    }

    private static String appendSubTaskToTaskBrowser( String[] columns,int index,String userID) {
        String result="";
        String tmp="<tr >" +
                "<th >"+"子任务编号"+
                "</th >"+
                "<th >"+"是否分配"+
                "</th >"+
                "<th >"+"标注人"+
                "</th >"+
                "<th >"+"标注条目数量"+
                "</th >"+
                "<th >"+"#"+
                "</th >"+
                "</tr>" ;

        int subtasknum=Integer.parseInt(columns[6]);
        int taskID=Integer.parseInt(columns[0]);
        ResourceBundle bundle = ResourceBundle.getBundle("database");
        String url = bundle.getString("subtasksInfoURL");
        String user = bundle.getString("subtasksInfoUser");
        String pwd = bundle.getString("subtasksInfoPwd");
        String driver = bundle.getString("subtasksInfoDriver");
        String tableName = "subtasks_"+taskID;

        for(int i=0;i<subtasknum;++i)
        {
            SqlConnector conn = null ;
            try {
                conn = new SqlConnector(url, user, pwd, driver);
                conn.start();
                String sql = "select Annotator , start , end from " + tableName +" where SubtaskId = "+(i+1);
                conn.setPreparedStatement(sql);
                ResultSet rs = conn.executeQuery();
                if(rs.next())
                {
                    int start=rs.getInt(2);
                    int end=rs.getInt(3);
                    int subitemnum=end-start+1;
                    int subtaskID=i+1;
                    String Annotator=rs.getString(1);
                    String hasannotator="yes";
                    String Operate="";
                    if(Annotator.isEmpty())
                    {
                        hasannotator="no";
                        Operate="<button onClick=\"AddtoMyannotateList("+ taskID + ","+subtaskID+",'"+userID+"')\">加入标注</button>";
                    }
                    tmp=tmp+  "<tr >" +
                            "<th >"+subtaskID+
                            "</th >"+
                            "<th >"+hasannotator+
                            "</th >"+
                            "<th >"+Annotator+
                            "</th >"+
                            "<th >"+subitemnum+
                            "</th >"+
                            "<th >"+Operate+
                            "</th >"+
                            "</tr>" ;
                }
            }catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
        result= "<div >" +
                "<span>" +
                "<table class='table table-bordered' style='margin:0px'>" +
                "<tbody>"
                +tmp+ "</tbody>" + "</table>" + "</span>" + "</div>" ;
        return result;
    }

    private static void initTaskTableHeader(){
        taskBrowserTableHeader = new String[7];
        taskBrowserTableHeader[0] = "#";
        taskBrowserTableHeader[1] = "创建者";
        taskBrowserTableHeader[2] = "数据表名";
        taskBrowserTableHeader[3] = "数据库地址";
        taskBrowserTableHeader[4] = "数据库用户";
        taskBrowserTableHeader[5] = "操作";
        taskBrowserTableHeader[6] = "";
    }
}