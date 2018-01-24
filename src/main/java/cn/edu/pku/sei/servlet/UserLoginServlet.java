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
import java.util.ResourceBundle;
import java.io.PrintWriter;
import java.io.PrintWriter;
/**
 * Created by oliver on 2017/8/1.
 */
public class UserLoginServlet extends HttpServlet{
    String userInfoDatabaseUrl = "userInfoDatabaseUrl";
    String userInfoDatabaseUser = "userInfoDatabaseUser";
    String userInfoDatabasePwd ="userInfoDatabasePwd";
    String userInfoDatabaseDriver = "userInfoDatabaseDriver";
    String userInfoTableName = "userInfoTableName";
    String sqlLogin = "select * from TABLENAME where ID=? AND pwd=?";
    String sqlregister = "insert into account (ID , pwd) values(? , ?)";
    String sqlinittaskinfo="insert into usertaskinfo (userID , createdtask , annotatetask) values(? , ? , ?)";
    String sqlregisterSearch ="select * from account where ID=?";
    SqlConnector Queryconn;
    SqlConnector Insertconn;
    SqlConnector Inittaskconn;
    String sqlUrl;
    String sqlUser;
    String sqlPwd;
    String sqlDriver;

    @Override
    protected void doGet(HttpServletRequest request , HttpServletResponse response)throws ServletException , IOException{
        doPost(request , response);
    }

    //
    // insert into account (ID , PWD) values(? , ?);
    // conn.setpr(sql);
    // conn.setString


    protected void doPost(HttpServletRequest request , HttpServletResponse response)throws ServletException , IOException{
        String requestType = request.getParameter("requestType");
        if(requestType == null){
            String user = request.getParameter("login_user");
            String pwd = request.getParameter("login_pwd");
            try {
                Queryconn.start();
                Queryconn.setPreparedStatement(sqlLogin);//必须在start之后
                Queryconn.setString(1, user);
                Queryconn.setString(2, pwd);
                JSONObject result = new JSONObject();
                if (Queryconn.executeQuery().next()) {
                    result.put("condition", "succeed");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("userMainPage.jsp");
                    Cookie userInfo = CookieOperations.getCookie(request , "userID");
                    if(userInfo != null){
                        userInfo.setValue(user);
                    }else{
                        userInfo = new Cookie("userID" , user);
                    }
                    response.addCookie(userInfo);
                    System.out.println(user + "登录成功！");
                    dispatcher.forward(request , response);
                } else {
                    System.out.println(user + "登录失败");
                    result.put("condition", "fail");
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else
        {
            String user = request.getParameter("user");
            String pwd = request.getParameter("pwd");
            try {
                Inittaskconn.start();
                Inittaskconn.setPreparedStatement(sqlinittaskinfo);
                Inittaskconn.setString(1,user);
                Inittaskconn.setString(2,"");
                Inittaskconn.setString(3,"");
                Inittaskconn.executeUpdate();

                Queryconn.start();
                Queryconn.setPreparedStatement(sqlregisterSearch);//必须在start之后
                Queryconn.setString(1, user);
                if (Queryconn.executeQuery().next()) {
                    PrintWriter out = response.getWriter();
                    out.print(1);
                    System.out.println("插入失败！");
                }
                else{
                    Insertconn.start();
                    Insertconn.setPreparedStatement(sqlregister);//必须在start之后
                    Insertconn.setString(1, user);
                    Insertconn.setString(2, pwd);
                    Insertconn.executeUpdate();
                    System.out.println("插入成功！");
                    PrintWriter out = response.getWriter();
                    out.print(0);
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }finally{
                Inittaskconn.close();
                Queryconn.close();
            }
        }
    }

    public void init() throws ServletException{
        System.out.println("initiate the user login servlet");
        ResourceBundle bundle;
        try {

            ClassLoader temdp = ClassLoader.getSystemClassLoader();
            bundle = ResourceBundle.getBundle("database");
            sqlUrl = bundle.getString(userInfoDatabaseUrl);
            sqlUser = bundle.getString(userInfoDatabaseUser);
            sqlPwd = bundle.getString(userInfoDatabasePwd);
            sqlDriver = bundle.getString(userInfoDatabaseDriver);
            String temp = bundle.getString(userInfoTableName);
            sqlLogin = sqlLogin.replace("TABLENAME" , temp);
            Queryconn = new SqlConnector(sqlUrl , sqlUser , sqlPwd , sqlDriver);
            Insertconn = new SqlConnector(sqlUrl , sqlUser , sqlPwd , sqlDriver);
            Inittaskconn = new SqlConnector(sqlUrl , sqlUser , sqlPwd , sqlDriver);

        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
