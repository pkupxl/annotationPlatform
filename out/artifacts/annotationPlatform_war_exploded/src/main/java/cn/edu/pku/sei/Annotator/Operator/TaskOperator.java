package cn.edu.pku.sei.Annotator.Operator;

import cn.edu.pku.sei.LabelTask.Task;
import mySql.SqlConnector;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Created by oliver on 2017/8/12.
 */
public class TaskOperator {
    private String databaseURL = "";
    private String databaseUser = "";
    private String databasePwd = "";
    private String databaseDriver = "com.mysql.jdbc.Driver";
    private String taskDataTable = ""; // the task data resource table
    private String primaryKey = "";
    private int ITEM_COUNT = 10; // the number of rows in the data resource table

    private String displayColumns = "";


    // region getter and setter
    public String[] getDisplayColumns (){
        if(displayColumns.length() > 0)
            return displayColumns.split(",");
        else
            return null;
    }
    // endregion
    public static void main(String[] args) throws SQLException {
        /*SqlConnector conn_getter = new SqlConnector("jdbc:mysql://127.0.0.1:3306/stackoverflow" , "root" , "woxnsk" ,
                "com.mysql.jdbc.Driver");
        String sql = "select id , parentID from answerwithcode ";
        conn_getter.start();
        conn_getter.setPreparedStatement(sql);
        ResultSet rs = conn_getter.executeQuery();

        sql = "update answerwithcode set tags=? where id =?";
        conn_getter.setPreparedStatement(sql);

        SqlConnector conn_setter= new SqlConnector("jdbc:mysql://127.0.0.1:3306/stackoverflow" , "root" , "woxnsk" ,
                "com.mysql.jdbc.Driver");
        sql = "select Tags from lucene_question where Id=?";
        conn_setter.start();
        conn_setter.setPreparedStatement(sql);


        while(rs.next()){
            int id = rs.getInt(1);
            int parentID = rs.getInt(2);
            conn_setter.setInt(1 , parentID);
            ResultSet temp = conn_setter.executeQuery();
            if(temp.next()) {
                conn_getter.setString(1, temp.getString(1).replace("><" , " || ").replace("<", "").replace(">" , ""));
                conn_getter.setInt(2, id);
                conn_getter.execute();
            }
        }
        conn_getter.close();
        conn_setter.close();*/
        SqlConnector conn_getter = new SqlConnector("jdbc:mysql://127.0.0.1:3306/stackoverflow" , "root" , "woxnsk" ,
                "com.mysql.jdbc.Driver");
        SqlConnector conn_setter= new SqlConnector("jdbc:mysql://127.0.0.1:3306/stackoverflow" , "root" , "woxnsk" ,
                "com.mysql.jdbc.Driver");

        String sql_question = "select Id , AcceptedAnswerId , Title , Body , Tags from lucene_question where AcceptedAnswerId <>\"\"";
        String sql_answer = "select Id , Body from lucene_answer where Id=?";
        String sql_insert = "insert into answerwithcode values (? , ? , ? , ? , ? , ?)";

        conn_getter.start();
        conn_setter.start();
        conn_setter.setPreparedStatement(sql_insert);

        conn_getter.setPreparedStatement(sql_question);
        ResultSet rs = conn_getter.executeQuery();
        conn_getter.setPreparedStatement(sql_answer);
        while(rs != null && rs.next()){
            int parentID = rs.getInt(1);
            int answerID = rs.getInt(2);
            conn_getter.setInt(1 , answerID);
            ResultSet rs2 = conn_getter.executeQuery();
            String tags = rs.getString(5);
            if(rs2.next() && (tags.contains("solr") || tags.contains("nutch")||tags.contains("java"))) {
                String questionBody = rs2.getString(2);
                if(questionBody.contains("<code>")){
                    conn_setter.setInt(1,answerID);
                    conn_setter.setInt(2 , parentID);
                    conn_setter.setString(3 , rs.getString(3));
                    conn_setter.setString(4 , rs.getString(4));
                    conn_setter.setString(5 , rs2.getString(2));
                    conn_setter.setString(6 , rs.getString(5).replace("><" , " || ").replace("<", "").replace(">" , "") );
                    conn_setter.execute();
                }
            }
        }


    }

    public TaskOperator(int taskID){
        init(taskID);
    }

    private void init(int taskID){
        initDataSourceInfo(taskID);
        initDisplayItemInfo(taskID);
        initItemCountInfo();
    }

    private void initDataSourceInfo(int taskID){
        ResourceBundle bundle = ResourceBundle.getBundle("database");

        String url = bundle.getString("platformInfoDatabaseURL");
        String user = bundle.getString("platformInfoDatabaseUser");
        String pwd = bundle.getString("platformInfoDatabasePwd");
        String driver = "com.mysql.jdbc.Driver";
        String tasksTable = bundle.getString("tasksTableName");

        SqlConnector conn = new SqlConnector(url , user , pwd , driver);

        try {
            String sql = "select databaseURL , databaseUser , databasePwd , tableName ,primaryKey from " + tasksTable +" where taskID=" + taskID;
            conn.start();
            conn.setPreparedStatement(sql);
            ResultSet rs = conn.executeQuery();
            if (rs != null && rs.next()) {
                databaseURL = rs.getString(1);
                databaseUser = rs.getString(2);
                databasePwd = rs.getString(3);
                taskDataTable = rs.getString(4);
                primaryKey = rs.getString(5);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            conn.close();
        }
    }

    private void initDisplayItemInfo(int taskID){
        ResourceBundle bundle = ResourceBundle.getBundle("database");

        String url = bundle.getString("displayColumnsInfoDatabaseURL");
        String user = bundle.getString("displayColumnsInfoDatabaseUser");
        String pwd = bundle.getString("displayColumnsInfoDatabasePwd");
        String driver = "com.mysql.jdbc.Driver";
        String tasksTable = Task.getDisplayColumnsTableName(taskID);

        SqlConnector conn = null ;
        try {
            conn =new SqlConnector(url, user, pwd, driver);
            String sql = "select columnName from " + tasksTable + " order by isPrimaryKey desc , positionIndex asc";
            conn.start();
            conn.setPreparedStatement(sql);
            ResultSet rs = conn.executeQuery();
            if(rs.next()){
                displayColumns = "";
                displayColumns += rs.getString(1);
                while(rs.next()){
                    displayColumns += (" , " + rs.getString(1));
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            conn.close();
        }

    }

    private void initItemCountInfo(){
        SqlConnector conn = new SqlConnector(databaseURL , databaseUser ,databasePwd ,databaseDriver);
        try {
            String sql = "select count(*) from " + taskDataTable;
            conn.start();
            conn.setPreparedStatement(sql);
            ResultSet rs = conn.executeQuery();
            if(rs!= null && rs.next()){
                ITEM_COUNT = rs.getInt(1);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            conn.close();
        }
    }

    private  boolean isValid(){
        return true;
    }

    /**
     * In the labelTask.jsp page , every page will show a const number(ITEM_COUNT) of items
     * This function is used to find the items content of the target page(pageIndex) .
     * @param pageIndex : the target page
     * @param itemPerPage: the index of target item (0 - ITEM_COUNT -1)
     * @return All the items content of the target page ,
     *          be careful with that every row of content contains
     *             1.the columns of the item will display
     *             2.the primary key of this item at the first column
     */
    public String[][] getItemsByPageIndex(int pageIndex , int itemPerPage){
        if(isValid()){
            SqlConnector conn = new SqlConnector(databaseURL , databaseUser , databasePwd , databaseDriver);
            String sql =
                "select * from " +
                    "(" +
                        "select * from " +
                        "(" +
                            "select * from " +
                            "(" +
                                "select DISPLAY_ITEMS from TASK_TABLE order by PRIMARY_KEY limit END_INDEX " +
                            ") a order by PRIMARY_KEY desc" +
                        ") b limit DISPLAY_COUNT " +
                    ") c order by PRIMARY_KEY asc" ;

            int end = ( pageIndex + 1 ) * itemPerPage;
            end = end > ITEM_COUNT ? ITEM_COUNT: end;
            int displayCount = end - pageIndex * itemPerPage;
            if(end > 0 && displayCount > 0) {
                sql = sql.replace("DISPLAY_ITEMS", displayColumns).replace("TASK_TABLE", taskDataTable);
                sql = sql.replace("END_INDEX", end + "").replace("DISPLAY_COUNT", displayCount + "");
                sql = sql.replace("PRIMARY_KEY" , primaryKey);
                try {
                    conn.start();
                    conn.setPreparedStatement(sql);
                    ResultSet rs = conn.executeQuery();
                    if (rs != null) {
                        int columns = rs.getMetaData().getColumnCount();
                        List<String[]> resultList = new ArrayList<String[]>();
                        while(rs.next()){
                            String[] temp = new String[columns];
                            for(int i = 0 ; i < columns ; i++){
                                temp[i] = rs.getString( i + 1);
                            }
                            resultList.add(temp);
                        }
                        return resultList.toArray(new String[0][]);
                    }
                }catch (Exception e){
                    System.out.println(sql);
                    System.out.println(e.getMessage());
                }finally {
                    conn.close();
                }
            }
        }
        return null;
    }
}
