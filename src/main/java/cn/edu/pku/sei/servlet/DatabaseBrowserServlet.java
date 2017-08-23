package cn.edu.pku.sei.servlet;

import cn.edu.pku.sei.Database.DatabaseBrowser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by oliver on 2017/7/20.
 */
public class DatabaseBrowserServlet extends HttpServlet{
    private String sqlUrl;
    private String sqlPort;
    private String sqlDatabaseName;
    private String sqlUser;
    private String sqlPwd;
    private String sqlTableName;
    private DatabaseBrowser browser;

    protected void doGet(HttpServletRequest request , HttpServletResponse response) throws ServletException , IOException{
        doPost(request , response);
    }

    protected void doPost(HttpServletRequest request , HttpServletResponse response) throws ServletException , IOException{
        String requestType = request.getParameter("requestType");
        sqlUrl = request.getParameter("database_url");
        sqlPort = request.getParameter("database_port");
        sqlDatabaseName = request.getParameter("database_database");
        sqlUser = request.getParameter("database_user");
        sqlPwd = request.getParameter("database_pwd");
        sqlTableName = request.getParameter("database_tableName");

        String url = "jdbc:mysql://" + sqlUrl + ":" + sqlPort + "/" + sqlDatabaseName;
        browser = new DatabaseBrowser(url ,
                sqlUser ,
                sqlPwd,
                "com.mysql.jdbc.Driver");

        try {
            JSONObject returnData = new JSONObject();
            if(requestType.compareTo("getTablesInfo") == 0){
                returnData = getTablesInfo();
            }else if(requestType.compareTo("getColumnsInfo") == 0){
                returnData = getColumnsInfo();
            }

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(returnData.toString());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            //browser.close();
        }
    }

    JSONObject getTablesInfo(){
        browser.setSqlDatabaseName(sqlDatabaseName);
        List<String> tables = browser.getTables();
        return tablesInfoToJSON(tables);
    }

    JSONObject getColumnsInfo(){
        browser.setSqlDatabaseName(sqlDatabaseName);
        browser.setSqlTableName(sqlTableName);
        Map<String , String > columnsInfo = browser.getTableColumnInfos();
        return columnsInfoToJSON(columnsInfo);
    }

    JSONObject tablesInfoToJSON(List<String> tables){
        JSONObject result = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        for(String table : tables){
            JSONObject object = new JSONObject();
            object.put("table" , table);
            jsonArray.put(object);
        }
        result.put("tables" , jsonArray);

        return result;
    }

    /**
     * @param columns
     * @return
     * data structure:
     * {
     *     columns:[
     *          {
     *              columnName : ""
     *              dataType: ""
     *          }
     *          {
     *              ...
     *          }
     *     ]
     * }
     */

    JSONObject columnsInfoToJSON(Map<String , String> columns){
        JSONObject result = new JSONObject();

        JSONArray jsonArray = new JSONArray();

        String columnName ;
        String dataType;
        for(Entry<String ,String> column : columns.entrySet()){
            columnName = column.getKey();
            dataType = column.getValue();
            JSONObject object = new JSONObject();
            object.put("columnName" , columnName);
            object.put("dataType" , dataType);
            jsonArray.put(object);
        }

        result.put("columns" , jsonArray);
        return result;
    }

}
