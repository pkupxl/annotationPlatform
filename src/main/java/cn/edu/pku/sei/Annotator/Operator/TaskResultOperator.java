package cn.edu.pku.sei.Annotator.Operator;

import cn.edu.pku.sei.LabelTask.Task;
import mySql.SqlConnector;

import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Created by oliver on 2017/8/12.
 */
public class TaskResultOperator {
    private String databaseURL = "";
    private String databaseUser ="";
    private String databasePwd ="";
    private String databaseDriver = "";
    private String resultsTable ="";

    public static void main(String[] args) {
        /*TaskResultOperator op = new TaskResultOperator(1);
        op.addAnnotation("1" , "cyk" , "test");
        op.addAnnotation("1" , "cyk" , "test1");
        op.addAnnotation("1" , "cyk" , "test");
        op.addAnnotation("2" , "cyk" , "test");
        op.addAnnotation("1" , "xb" , "test");
        op.addAnnotation("3" , "cyk" , "test");
        op.deleteAnnotation("1" , "xb" , "test");
        op.deleteAnnotation("2" , "cyk" , "null");
        op.deleteAnnotation("1" , "xb" , "null");*/

    }

    public TaskResultOperator(int taskID){
        initDatabaseInfo(taskID);
    }

    private void initDatabaseInfo( int taskID){
        ResourceBundle bundle = ResourceBundle.getBundle("database");

        databaseURL = bundle.getString("labelResultsInfoDatabaseURL");
        databaseUser = bundle.getString("labelResultsInfoDatabaseUser");
        databasePwd = bundle.getString("labelResultsInfoDatabasePwd");
        databaseDriver = bundle.getString("userInfoDatabaseDriver");
        resultsTable = Task.getResultsTableName(taskID);
    }

    /**
     * This function is used to find weather the string contain the annotation
     *
     * @param string: consists of apis contained in a document in form (annotation1,annotation2,annotation3,...)
     * @param annotation: the annotation
     * @return the start position of apiName , if string contain apiName,
     *         -1 , else
     *         but , it should be clear that if the string = "IndexReader,IndexWriter" and apiName = "Index" , the return value will be -1
     */
    private int indexOfAnnotation(String string , String annotation){
        int index = -1;
        string = "," + string + ",";
        if(string.contains("," + annotation +",")){
            index = string.indexOf("," + annotation +",");// the situation is actually the start position of the first character of apiName
        }
        return index;
    }

    /**
     * deleteApiInString is used to delete a annotation in the oriString
     * when annotation = "Index" , it should be deleted only in 3 cases
     * 								 1.at the start of the oriString , i.e. oriString = "Index,..." or "Index"
     * 								 2.at the middle of the oriString , i.e. oriString = "...,Index,..."
     * 								 3.at the end of the oriString , i.e. oriString = "...,Index" or "Index"
     * but be careful, when apiName just a part of another API name, for example oriString = "...,IndexWriter,..." , it should not be deleted.
     * @param oriString: the parameter consist of a set of APIs , and the APIs are organized in the form "API1,API2,API3,..."
     *  				 the form means the APIs are separated by comma
     * @param annotation: the name of a API which will be deleted from the oriString.
     * @return the changed string , if the oriString contains apiName , and delete apiName successfully
     * 		   null , other.
     */
    private String deleteAnnotationFromString(String oriString , String annotation){
        if(oriString == null || annotation == null){
            return null;
        }else{
            oriString = oriString.trim();
            annotation = annotation.trim();
        }

        if(oriString.length() == 0 || annotation.length() == 0){
            return null;
        }

        String result = oriString;
        int startPosition = indexOfAnnotation(oriString , annotation);

        //apiName should be deleted from the oriString
        if(startPosition != -1){
            result = "";
            if(startPosition != 0)
                result = oriString.substring(0 , startPosition - 1) ; // before the apiName , there is a comma, we should delete
            if(startPosition + annotation.length() < oriString.length() ){
                if(result.length() > 0)
                    result += ",";
                result += oriString.substring(startPosition + annotation.length() + 1);
            }
        }
        return result;
    }

    private String addAnnotationIntoString(String oriString , String annotation){
        if(oriString == null)
            oriString = "";
        else if (annotation == null)
            return null;
        else{
            oriString = oriString.trim();
            annotation = annotation.trim();
        }

        if(annotation.length() == 0)
            return null;

        oriString = oriString.trim();
        annotation = annotation.trim();

        String result = oriString;
        int startPosition = indexOfAnnotation(oriString , annotation);
        if(startPosition == -1){
            if(oriString.length() == 0){
                result = annotation;
            }else {
                result = oriString + "," + annotation;
            }
        }
        return result;
    }

    public boolean addAnnotation(int itemID , String userID , String annotation) {
        boolean result = true;
        SqlConnector conn = new SqlConnector(databaseURL , databaseUser , databasePwd , databaseDriver);
        /*SqlConnector conn = new SqlConnector("jdbc:mysql://127.0.0.1:3306/stackoverflow" ,
                "root" ,
                "woxnsk" ,
                "com.mysql.jdbc.Driver");*/
        try{
            conn.start();
            String sql = "select result from " + resultsTable + " where itemID='" + itemID + "' AND userID='" + userID+ "'";
            conn.setPreparedStatement(sql);
            ResultSet rs = conn.executeQuery();
            if(rs != null && rs.next()){
                String oriString = rs.getString(1);
                if(indexOfAnnotation(oriString , annotation) == -1) {
                    String temp = addAnnotationIntoString(oriString, annotation);
                    sql = "Update " + resultsTable + " Set result='" + temp + "' where itemID='" + itemID + "' AND userID='" + userID + "'";
                    conn.setPreparedStatement(sql);
                    conn.execute();
                }
            }else{
                sql = "Insert into " + resultsTable + "(itemID , userID , result) Values ( ? , ? , ?)";
                conn.setPreparedStatement(sql);
                conn.setInt(1 , itemID);
                conn.setString(2 , userID);
                conn.setString(3 , annotation);
                conn.execute();
            }
            result = true;
        }catch (Exception e){
            result = false;
            System.out.println(e.getMessage());
        }finally {
            conn.close();
            return result;
        }
    }

    public boolean deleteAnnotation(int itemID , String userID , String annotation){
        boolean result = false;
        SqlConnector conn = new SqlConnector(databaseURL , databaseUser , databasePwd ,databaseDriver);
        try{
            conn.start();
            String sql = "select result from " + resultsTable + " where itemID='" + itemID + "' AND userID='" + userID+ "'";
            conn.setPreparedStatement(sql);
            ResultSet rs = conn.executeQuery();
            if(rs != null && rs.next()){
                String oriString = rs.getString(1);
                if(indexOfAnnotation(oriString , annotation) != -1){
                    String temp = deleteAnnotationFromString(oriString , annotation);
                    if(temp.length() != 0)
                        sql = "Update " + resultsTable + " Set result='" + temp + "' where itemID='" + itemID + "' AND userID='" + userID+ "'";
                    else
                        sql = "delete from " + resultsTable + " where itemID='" + itemID + "' AND userID='" + userID+ "'";
                    conn.setPreparedStatement(sql);
                    conn.execute();
                    result = true;
                }
            }
        }catch(Exception e){
            result = false;
            System.out.println(e.getMessage());
        }finally{
            conn.close();
            return result;
        }

    }

    public String[] getAnnotationResults(int itemID , String userID){
        String[] result = null;
        SqlConnector conn = null;
        try{
            conn = new SqlConnector(databaseURL , databaseUser , databasePwd , databaseDriver);
            String sql = "select result from " + resultsTable + " where itemID =? AND userID=?";
            conn.start();
            conn.setPreparedStatement(sql);
            conn.setInt(1 , itemID);
            conn.setString(2 , userID);
            ResultSet rs = conn.executeQuery();
            if(rs.next()){
                String results = rs.getString(1);
                result = results.split(",");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            conn.close();
            return result;
        }
    }

    public boolean[] getHasResult(String[][] content , String userID){
        int contentLength = content.length;
        boolean[] results = new boolean[contentLength];
        SqlConnector conn = new SqlConnector(databaseURL , databaseUser ,databasePwd ,databaseDriver);
        try{
            conn.start();
            String sql = "select itemID from " + resultsTable + " where itemID=? AND userID=?";
            conn.setPreparedStatement(sql);
            for(int i = 0 ; i < contentLength ; i++){
                int itemID = Integer.parseInt(content[i][0]);
                conn.setInt(1 , itemID);
                conn.setString(2 , userID);
                if(conn.executeQuery().next()){
                    results[i] = true;
                }else
                    results[i] = false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            conn.close();
            return results;
        }
    }
}
