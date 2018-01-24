package cn.edu.pku.sei.LabelTask;

import mySql.SqlConnector;

import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Created by oliver on 2017/8/10.
 */
public class Task {
    private String databaseURL ="";
    private String databaseUser = "";
    private String databasePwd = "";
    private String databaseDriver = "com.mysql.jdbc.Driver";
    private String databaseTableName = "";
    private String primaryKey="";
    private String[] displayColumns = null;
    private String creator = "";
    private String detailInfo = "";
    private int itemNum = 0;
    private int subtaskNum=0;

    private String addSQL = "insert into TASKSTABLENAME (taskID , userID , databaseURL , databaseUser , databasePwd , databaseDriver , tableName , primaryKey , itemNum , subtaskNum ) VALUES(?,?,?,?,?,?,?,?,?,?)";

    // region getter and setter
    public String getDatabaseURL(){
        return databaseURL;
    }
    public void setDatabaseURL(String value){
        databaseURL = value;
    }

    public String getDatabaseUser(){
        return databaseUser;
    }
    public void setDatabaseUser(String value){
        databaseUser = value;
    }

    public String getDatabasePwd(){
        return databasePwd;
    }
    public void setDatabasePwd(String value){
        databasePwd = value;
    }

    public String getDatabaseDriver(){
        return databaseDriver;
    }
    public void setDatabaseDriver(String value){
        databaseDriver = value;
    }

    public String getCreator(){
        return  creator;
    }
    public void setCreator(String value){
        creator = value;
    }

    public String getDetailInfo(){
        return detailInfo;
    }
    public void setDetailInfo(String value){
        detailInfo = value;
    }

    public String getDatabaseTableName(){
        return databaseTableName;
    }
    public void setDatabaseTableName(String value){
        databaseTableName = value;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }
    public void setPrimaryKey(String value){
        primaryKey = value;
    }

    public String[] getDisplayColumns(){
        return displayColumns;
    }
    public void setDisplayColumns(String[] values){
        int length = values.length;
        displayColumns = new String[length];
        for(int i = 0 ; i < length ; i ++){
            displayColumns[i] = values[i];
        }
    }
    // endregion

    boolean isCompleted(){
        if(databaseURL.length() == 0)
            return false;
        else if(databaseUser.length() == 0)
            return false;
        else if(databasePwd.length() == 0)
            return false;
        else if(databaseDriver.length() == 0)
            return false;
        else if(creator.length() == 0)
            return false;
        else
            return true;
    }

    public boolean addToDatabase(){
        if(!isCompleted()) return false;
        boolean result = true;
        SqlConnector conn = null;
        SqlConnector conn1 = null;
        try{
            ResourceBundle bundle = ResourceBundle.getBundle("database");
            String url = bundle.getString("platformInfoDatabaseURL" );
            String user = bundle.getString("platformInfoDatabaseUser");
            String pwd = bundle.getString("platformInfoDatabasePwd");
            String driver = bundle.getString("platformInfoDatabaseDriver");
            String tasksTableName = bundle.getString("tasksTableName");

            int taskID = 0;

            conn = new SqlConnector(url , user , pwd , driver);
            conn.start();

            String sql = "select Max(TaskID) from " + tasksTableName;
            conn.setPreparedStatement(sql);
            ResultSet rs = conn.executeQuery();
            if(rs.next()){
                taskID = rs.getInt(1) + 1;
            }
            conn1=new SqlConnector(databaseURL , databaseUser , databasePwd ,  databaseDriver);
            String sql1= null;
            sql1="select count(Id) from "+databaseTableName;
            conn1.start();
            conn1.setPreparedStatement(sql1);
            rs = conn1.executeQuery();
            if(rs.next()) {
                itemNum = rs.getInt(1);
            }
            conn1.close();

            sql = addSQL.replace("TASKSTABLENAME" , tasksTableName);
            conn.setPreparedStatement(sql);

            conn.setInt(1 , taskID);
            conn.setString(2 , creator);
            conn.setString(3 , databaseURL);
            conn.setString(4 , databaseUser);
            conn.setString(5 , databasePwd);
            conn.setString(6 , databaseDriver);
            conn.setString(7 , databaseTableName);
            conn.setString(8 , primaryKey);
            conn.setInt(9 ,itemNum);
            conn.setInt(10,subtaskNum);
            conn.execute();

            addTaskInfotoDatabase(taskID);
            addDisplayColumnsToDatabase(taskID);
            addResultsTableToDatabase(taskID);
            addSubTasksToDatabase(taskID);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            conn.close();
            return result;
        }
    }

    boolean addTaskInfotoDatabase(int taskID){
        boolean result=true;
        String Querysql="select * from usertaskinfo where userID = ?";
        String sql="update usertaskinfo set createdtask = ? where userID = ?";
        SqlConnector conn = null;
        SqlConnector conn1= null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("database");
            String url = bundle.getString("userInfoDatabaseUrl");
            String user = bundle.getString("userInfoDatabaseUser");
            String pwd = bundle.getString("userInfoDatabasePwd");
            String driver = bundle.getString("userInfoDatabaseDriver");
            conn = new SqlConnector(url, user, pwd, driver);
            conn.start();
            conn.setPreparedStatement(Querysql);
            conn.setString(1,creator);
            ResultSet rs = conn.executeQuery();
            if(rs.next())
            {
                String Createdtask= rs.getString(2);
                if(Createdtask.isEmpty()) {
                    Createdtask=Integer.toString(taskID);
                }
                else {
                    Createdtask=Createdtask+","+Integer.toString(taskID);
                }
                conn1 = new SqlConnector(url, user, pwd, driver);
                conn1.start();
                conn1.setPreparedStatement(sql);
                conn1.setString(1,Createdtask );
                conn1.setString(2, creator);
                conn1.executeUpdate();
                conn1.close();
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            conn.close();
            return result;
        }
    }

    boolean addSubTasksToDatabase(int taskID){
        boolean result =true;
        String subtasksTableName = getSubTasksTableName( taskID);
        SqlConnector conn = null;

        try{
            ResourceBundle bundle = ResourceBundle.getBundle("database");
            String url = bundle.getString("subtasksInfoURL");
            String user = bundle.getString("subtasksInfoUser");
            String pwd = bundle.getString("subtasksInfoPwd");
            String driver = bundle.getString("subtasksInfoDriver");

            conn = new SqlConnector(url , user , pwd , driver);
            conn.start();
            String sql = "create table " + subtasksTableName + "(" +
                    "SubtaskId int(11) NOT NULL,"+
                    "Annotator varchar(50) ,"+
                    "start int(11) NOT NULL," +
                    "end int(11) NOT NULL,"+
                    "primary key(SubtaskId)"+
                    ")ENGINE=InnoDB default charset=utf8;";
            conn.setPreparedStatement(sql);
            conn.execute();
        }catch(Exception e){
            System.out.println(e.getMessage());
            result = false;
        }finally {
            conn.close();
            return result;
        }
    }


    boolean addDisplayColumnsToDatabase(int taskID){
        boolean result = true;
        if(displayColumns == null || displayColumns.length == 0)
            return false;
        String displayColumnsTableName = getDisplayColumnsTableName(taskID);

        SqlConnector conn = null;
        try{
            ResourceBundle bundle = ResourceBundle.getBundle("database");
            String url = bundle.getString("displayColumnsInfoDatabaseURL");
            String user = bundle.getString("displayColumnsInfoDatabaseUser");
            String pwd = bundle.getString("displayColumnsInfoDatabasePwd");
            String driver = bundle.getString("displayColumnsInfoDatabaseDriver");

            conn = new SqlConnector(url , user , pwd , driver);
            conn.start();
            String sql = "create table `" + displayColumnsTableName + "`(" +
                    "`columnName` varchar(50) NOT NULL,"+
                    "`isPrimaryKey` int(11) NOT NULL," +
                    "`positionIndex` int(11) NOT NULL," +
                    "primary key(`columnName`)"+
                    ")ENGINE=InnoDB default charset=utf8;";
            conn.setPreparedStatement(sql);
            conn.execute();

            sql = "insert into " + displayColumnsTableName + " (columnName , isPrimaryKey , positionIndex) values (? , ? , ?)";
            for(int i = 0 ; i < displayColumns.length ; i ++){
                conn.setPreparedStatement(sql);
                conn.setString(1, displayColumns[i]);
                conn.setInt(2, displayColumns[i].equals(primaryKey)? 1 : 0);
                conn.setInt(3 , i);
                conn.execute();
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
            result = false;
        }finally {
            conn.close();
            return result;
        }
    }

    boolean addResultsTableToDatabase(int taskID){
        boolean result = true;
        SqlConnector conn = null;
        try{
            ResourceBundle bundle = ResourceBundle.getBundle("database");
            String url = bundle.getString("labelResultsInfoDatabaseURL");
            String user = bundle.getString("labelResultsInfoDatabaseUser");
            String pwd = bundle.getString("labelResultsInfoDatabasePwd");
            String driver = bundle.getString("labelResultsInfoDatabaseDriver");

            String resultsTableName = getResultsTableName(taskID);

            conn = new SqlConnector(url , user , pwd , driver);
            conn.start();

            String sql = "CREATE TABLE `"+resultsTableName+ "` (" +
                    "`itemID` int(10) NOT NULL," +
                    "`userID` varchar(255) NOT NULL," +
                    "`result` text NOT NULL," +
                    "PRIMARY KEY  (`itemID`,`userID`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

            conn.setPreparedStatement(sql);
            conn.execute();
        }catch(Exception e){
            result = false;
            System.out.println(e.getMessage());
        }finally {
            conn.close();
            return result;
        }
    }

    public static String getDisplayColumnsTableName(int taskID){
        return "displayColumns_"+ taskID;
    }

    public static String getResultsTableName(int taskID){
        return "results_" + taskID;
    }

    public static String getSubTasksTableName(int taskID){
        return "subtasks_"+ taskID;
    }
}