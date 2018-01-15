package cn.edu.pku.sei.servlet;


import cn.edu.pku.sei.Annotator.ItemAnnotator;
import cn.edu.pku.sei.Annotator.PageParameter;
import cn.edu.pku.sei.CookieOperations;
import cn.edu.pku.sei.LabelTask.Task;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * Created by oliver on 2017/7/26.
 */
public class ConstructLabelTaskServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request , HttpServletResponse response)throws ServletException , IOException {
        doPost(request , response);
    }

    protected void doPost(HttpServletRequest request , HttpServletResponse response) throws ServletException , IOException {
         String requestType = request.getParameter("requestType");
         System.out.println("create tasks");
         if(requestType.equals("createTask")){
             boolean result = createTask(request , response);
             JSONObject returnResult = new JSONObject();
             if(result){
                 returnResult.put("condition" , "succeed");
             }else{
                 returnResult.put("condition" , "failed");

             }
             response.getWriter().print(returnResult);
         }
    }

    private boolean createTask(HttpServletRequest request , HttpServletResponse response){
        boolean result = true;
        String databaseURL = request.getParameter("databaseURL");
        String databaseUser = request.getParameter("databaseUser");
        String databasePwd = request.getParameter("databasePwd");
        String databaseDriver = "com.mysql.jdbc.Driver";
        String databaseTableName = request.getParameter("databaseTableName");
        String primaryKey = request.getParameter("primaryKey");
        String displayColumns = request.getParameter("displayColumns");
        String creator = CookieOperations.getCookieValue(request , "userID");

        Task task = new Task();
        task.setDatabaseURL(databaseURL);
        task.setDatabaseUser(databaseUser);
        task.setDatabasePwd(databasePwd);
        task.setDatabaseDriver(databaseDriver);
        task.setDatabaseTableName(databaseTableName);
        task.setPrimaryKey(primaryKey);

        task.setCreator(creator);
        task.setDisplayColumns(displayColumns.split(","));
        task.setDetailInfo("");
        task.addToDatabase();

        return result;
    }
}
