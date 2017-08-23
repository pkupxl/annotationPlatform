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

    SqlConnector conn;
    String sqlUrl;
    String sqlUser;
    String sqlPwd;
    String sqlDriver;
    @Override
    protected void doGet(HttpServletRequest request , HttpServletResponse response)throws ServletException , IOException{
        doPost(request , response);
    }

    protected void doPost(HttpServletRequest request , HttpServletResponse response)throws ServletException , IOException{
        String user = request.getParameter("login_user");
        String pwd = request.getParameter("login_pwd");
        try {
            conn.start();
            conn.setPreparedStatement(sqlLogin);
            conn.setString(1, user);
            conn.setString(2, pwd);

            JSONObject result = new JSONObject();
            if (conn.executeQuery().next()) {
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

            conn = new SqlConnector(sqlUrl , sqlUser , sqlPwd , sqlDriver);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }



    }


}
