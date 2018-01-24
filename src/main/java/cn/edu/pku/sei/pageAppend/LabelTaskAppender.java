package cn.edu.pku.sei.pageAppend;

import cn.edu.pku.sei.Annotator.Operator.TaskOperator;
import cn.edu.pku.sei.Annotator.Operator.TaskResultOperator;

/**
 * Created by oliver on 2017/8/12.
 */
public class LabelTaskAppender {
    private static final int DISPLAY_ITEM_PER_PAGE = 100;
    private String userID;
    private int taskID;
    private int subtaskID;
    private String[][]content;
    private boolean[] hasResults;
    private int currentPageIndex = -1;
    private TaskOperator taskOperator;
    private TaskResultOperator taskResultOperator;

    public LabelTaskAppender(int taskID ,int subtaskID, String userID){
        currentPageIndex = -1;
        taskOperator = new TaskOperator(taskID,subtaskID);
        taskResultOperator = new TaskResultOperator(taskID);
        this.userID = userID;
        this.taskID = taskID;
        this.subtaskID = subtaskID;
    }
    public void Update(int taskID, int subtaskID){
        this.taskID = taskID;
        this.subtaskID = subtaskID;
        currentPageIndex = -1;
        taskOperator=new TaskOperator(taskID,subtaskID);
    }

    private void updateContent(int pageIndex){
        if(pageIndex != currentPageIndex) {
            content = taskOperator.getItemsByPageIndex(pageIndex, DISPLAY_ITEM_PER_PAGE);
            hasResults = taskResultOperator.getHasResult(content , userID);
            currentPageIndex = pageIndex;
        }
        if((content == null || content.length == 0) && pageIndex > 0){
            currentPageIndex = -1;
            updateContent(pageIndex - 1);
        }
    }

    public String getDisplayItemNavigateAppendString(int pageIndex , int dataIndex){
        updateContent(pageIndex);
        //content = taskOperator.getItemsByPageIndex(pageIndex , DISPLAY_ITEM_PER_PAGE);

        String result = "";
        if(pageIndex > 0)
            result += appendAnItemToNavigate(currentPageIndex - 1 , DISPLAY_ITEM_PER_PAGE - 1 , "<<" , -1);
        for(int i = 0 ; i < content.length ; i++){
            result += appendAnItemToNavigate(currentPageIndex , i , pageIndex * DISPLAY_ITEM_PER_PAGE + i + 1 + "" , dataIndex);
        }
        result += appendAnItemToNavigate(currentPageIndex + 1 , 0 , ">>" , -1);
        return result;
    }

    private String appendAnItemToNavigate(int pageIndex , int index  , String displayString , int dataIndex) {
        String parameters = "taskID=" + taskID + "&subtaskID="+subtaskID+"&" +"pageIndex=" + pageIndex + "&" + "dataIndex=" + index;
        if(index == dataIndex)
            return convert(parameters , displayString , "item-current");
        else if(hasResults[index])
            return convert(parameters , displayString , "item-withResult");
        else
            return convert(parameters , displayString , "item-others");
    }

    private String convert(String parameters , String displayString , String style){
        String template ="<a type='button' class='CLASS'  href='labelTask.jsp?PARAMETERS'>DISPLAY_STRING </a>";
        String clazz = "display-item pull-left btn placeholders btn-default " + style;
        String result = template.replace("CLASS" , clazz);
        result = result.replace("PARAMETERS" , parameters);
        result = result.replace("DISPLAY_STRING" , displayString);
        return result;
    }

    public String getItemDisplayInfoString(int pageIndex ,int dataIndex){
        updateContent(pageIndex);
        String result = "" ;
        if(dataIndex < content.length) {
            result = appendARowToDisplayInfoTable("#" , "COLUMN" , "DETAIL");
            String[] columns = taskOperator.getDisplayColumns();
            for(int i = 0 ; i < columns.length; i++){
                result += appendARowToDisplayInfoTable(i + 1 + "." , columns[i] , content[dataIndex][i]);
            }

            result =
                "<div class='box-body'>" +
                    "<table class='table table-bordered'>"+
                        "<tbody>" +
                            result +
                        "</tbody>"+
                    "</table>" +
                "</div>";
            return result ;
        }else
            return "";
    }

    public String appendARowToDisplayInfoTable(String index , String columnName , String value){
        String result ;
        result =
            "<tr>" +
                "<th class='displayInfo-index'>" + index + "</th>" +
                "<th class='displayInfo-columnName'>" + columnName + "</th>" +
                "<th class='displayInfo-name'>" + value + "</th>" +
            "</tr>";
        return result;
    }

    public String getOperationSectionAppendString(int dataIndex){
        int itemID = Integer.parseInt(content[dataIndex][0]);
        String[] results = taskResultOperator.getAnnotationResults(itemID , userID);
        String resultDisplayString = "";
        if(results != null){
            for(int i = 0 ; i < results.length ; i++){
                resultDisplayString += appendARowToOperationSection(i + 1 + "." , results[i] , dataIndex);
            }
        }

        resultDisplayString =
            "<div class='box-body'>" +
                "<table class='table'>" +
                    appendHeaderToOperationSection() +
                    resultDisplayString +
                    appendFooterToOperationSection(dataIndex) +
                "</table>" +
            "</div>";

        return resultDisplayString;
    }

    public String appendHeaderToOperationSection(){
        String result =
            "<tr>" +
                "<th class='operationSection-index'>#</th>" +
                "<th class='operationSection-annotation'>结果</th>" +
                "<th class='operationSection-operation'>操作</th>" +
            "</tr>";
        return result;
    }

    public String appendARowToOperationSection(String index , String annotation , int dataIndex){
        String result =
            "<tr>"+
                "<th class='operationSection-index'>" + index + "</th>" +
                "<th class='operationSection-annotation'>" + annotation + "</th>" +
                "<th class='operationSection-operation'>" +
                    "<i class='operation fa fa-minus bg-yellow' onclick='deleteAnnotation(\""+userID+"\" , " + currentPageIndex + " , " + dataIndex + " , \"" + annotation +"\""+" , " + taskID+" , " + subtaskID +")'></i>" +
                "</th>" +
            "</tr>";
        return result;
    }

    public String appendFooterToOperationSection(int dataIndex){
        String result =
            "<tr>"+
                "<th colspan='2'>" +
                    "<input id='addAnnotation' type='text' class='operationSection-input'>" +
                "</th>" +
                "<th class='operationSection-operation'>" +
                    "<i class='operation fa fa-plus bg-yellow' onclick='addAnnotation(\"" + userID + "\" , " + currentPageIndex + " , " + dataIndex + " , " + taskID+" , " + subtaskID +");'></i>" +
                "</th>" +
            "</tr>";
        return result;
    }

    public void addAnnotation(int pageIndex , int dataIndex , String annotation){
        updateContent(pageIndex);
        taskResultOperator.addAnnotation(Integer.parseInt(content[dataIndex][0]) , userID , annotation);
    }

    public void deleteAnnotation(int pageIndex , int dataIndex , String annotation){
        updateContent(pageIndex);
        taskResultOperator.deleteAnnotation(Integer.parseInt(content[dataIndex][0]) , userID , annotation);
    }
}