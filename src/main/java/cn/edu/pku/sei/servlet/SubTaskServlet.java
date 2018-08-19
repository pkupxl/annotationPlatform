package cn.edu.pku.sei.servlet;
import cn.edu.pku.sei.CookieOperations;
import mySql.SqlConnector;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.io.PrintWriter;

public class SubTaskServlet extends HttpServlet{
    public void doGet(HttpServletRequest request , HttpServletResponse response) throws ServletException , IOException{
        doPost(request,response);
    }
    public void doPost(HttpServletRequest request , HttpServletResponse response) throws ServletException , IOException{

        String requestType = request.getParameter("requestType");
        String sqlUrl="";
        String sqlUser="";
        String sqlPwd="";
        String sqlDriver="";
        SqlConnector conn;
        if(requestType.equals("addSubtask"))
        {
            int taskID=Integer.parseInt(request.getParameter("taskID")) ;
            int subtaskNum=Integer.parseInt(request.getParameter("subtaskNum"));
            int index = Integer.parseInt(request.getParameter("index"));
            int itemnum = Integer.parseInt(request.getParameter("itemnum"));
            String sql= "update tasks set subtaskNum = ? where taskID = ?";
            String sql1= "insert into TABLENAME (SubtaskId ,Annotator ,start ,end ) values (?,?,?,?)";
            ResourceBundle bundle;
            try {
                bundle = ResourceBundle.getBundle("database");
                sqlUrl = bundle.getString("platformInfoDatabaseURL");
                sqlUser = bundle.getString("platformInfoDatabaseUser");
                sqlPwd = bundle.getString("platformInfoDatabasePwd");
                sqlDriver = bundle.getString("platformInfoDatabaseDriver");
                conn = new SqlConnector(sqlUrl , sqlUser , sqlPwd , sqlDriver);
                conn.start();
                conn.setPreparedStatement(sql);
                conn.setInt(1,subtaskNum);
                conn.setInt(2, taskID);
                conn.executeUpdate();
                conn.close();

                sqlUrl = bundle.getString("subtasksInfoURL");
                sqlUser = bundle.getString("subtasksInfoUser");
                sqlPwd = bundle.getString("subtasksInfoPwd");
                sqlDriver = bundle.getString("subtasksInfoDriver");
                int t=subtaskNum;
                int itemPerSubtask;
                if(itemnum%t==0)
                    itemPerSubtask=itemnum/t;
                else
                    itemPerSubtask=(itemnum/t+1);
                int start=1;
                int end=itemPerSubtask;
                while(t>0)
                {
                    SqlConnector conn1= new SqlConnector(sqlUrl , sqlUser , sqlPwd , sqlDriver);
                    String temp="subtasks_"+taskID;
                    sql1 = sql1.replace("TABLENAME" , temp);
                    conn1.start();
                    conn1.setPreparedStatement(sql1);
                    conn1.setInt(1,subtaskNum-t+1);
                    conn1.setString(2, "");
                    conn1.setInt(3,start);
                    conn1.setInt(4,end);
                    conn1.execute();
                    t--;
                    start=start+itemPerSubtask;
                    if(t==1) {
                        end=itemnum;
                    }
                    else {
                        end=end+itemPerSubtask;
                    }
                }
                PrintWriter out = response.getWriter();
                out.print(0);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }else
        {
            int taskID=Integer.parseInt(request.getParameter("taskID")) ;
            int subtaskID=Integer.parseInt(request.getParameter("subtaskID")) ;
            String userID=request.getParameter("userID");

            String sql0="select Annotator from TABLENAME where SubtaskId = ?";

            String sql= "update TABLENAME set Annotator = ? where SubtaskId = ?";
            String sql1="update usertaskinfo set annotatetask = ? where userID = ?";
            String sql2="select * from usertaskinfo where userID = ?";

            String tablename="subtasks_"+taskID;
            sql=sql.replace("TABLENAME",tablename);
            sql0=sql0.replace("TABLENAME",tablename);


            ResourceBundle bundle;

            try {
                bundle = ResourceBundle.getBundle("database");
                sqlUrl = bundle.getString("subtasksInfoURL");
                sqlUser = bundle.getString("subtasksInfoUser");
                sqlPwd = bundle.getString("subtasksInfoPwd");
                sqlDriver = bundle.getString("subtasksInfoDriver");
                SqlConnector conn1 = new SqlConnector(sqlUrl , sqlUser , sqlPwd , sqlDriver);
                conn1.start();
                conn1.setPreparedStatement(sql0);
                conn1.setInt(1,subtaskID);
                String AllAnnotator=null;
                ResultSet rs= conn1.executeQuery();
                if(rs.next())
                {
                    AllAnnotator=rs.getString(1);
                    if(AllAnnotator.isEmpty()) {
                        AllAnnotator = userID;
                    }else{
                        AllAnnotator = AllAnnotator+" , "+userID;
                    }
                }

                conn1.setPreparedStatement(sql);
                conn1.setString(1,AllAnnotator);
                conn1.setInt(2,subtaskID);
                conn1.executeUpdate();
                conn1.close();

                sqlUrl=bundle.getString("userInfoDatabaseUrl");
                sqlUser = bundle.getString("userInfoDatabaseUser");
                sqlPwd = bundle.getString("userInfoDatabasePwd");
                sqlDriver = bundle.getString("userInfoDatabaseDriver");
                SqlConnector conn2 = new SqlConnector(sqlUrl , sqlUser , sqlPwd , sqlDriver);

                conn2.start();
                conn2.setPreparedStatement(sql2);
                conn2.setString(1,userID);
                String taskinfo=Integer.toString(taskID)+","+Integer.toString(subtaskID);
                rs = conn2.executeQuery();
                if(rs.next()){
                    if(rs.getString(3)!="")
                        taskinfo=rs.getString(3)+";"+taskinfo;
                }
                conn2.setPreparedStatement(sql1);
                conn2.setString(1,taskinfo);
                conn2.setString(2,userID);
                conn2.executeUpdate();
                conn2.close();

                PrintWriter out = response.getWriter();
                out.print(0);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
