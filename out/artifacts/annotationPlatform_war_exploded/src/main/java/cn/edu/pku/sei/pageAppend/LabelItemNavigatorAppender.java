package cn.edu.pku.sei.pageAppend;

import cn.edu.pku.sei.configuration.URL;

import java.util.ResourceBundle;

/**
 * Created by oliver on 2017/8/8.
 */
public class LabelItemNavigatorAppender implements Appender{
    int currentIndex = -1;
    int end = -1;
    int indexStep = -1;

    //region getter and setter

    public int getCurrentIndex(){
        return currentIndex;
    }
    public void setCurrentIndex(int index){
        this.currentIndex = index;
    }

    public int getEnd(){
        return end;
    }
    public void setEnd(int end){
        this.end = end;
    }

    public int getIndexStep(){
        return indexStep;
    }
    public void setIndexStep(int step){
        this.indexStep = step;
    }
    //endregion

    public static void main(String[] args){
        ResourceBundle bundle = ResourceBundle.getBundle("database");
        String test = bundle.getString("userInfoDatabaseUrl");
    }


    public void labelItemNavigatorAppender(int currentIndex , int endIndex , int indexStep){
        this.currentIndex = currentIndex;
        this.end = currentIndex;
        this.indexStep = indexStep;
    }

    public String getAppendString(){
        if(currentIndex > end || currentIndex < 0 || end < 0)
            return "";

        int startIndex = currentIndex / indexStep * indexStep ;
        int endIndex = startIndex + indexStep - 1 > end ? end : startIndex + indexStep - 1;

        String result = "<div>";
        if(startIndex > 0)
            result += produceOneItem(startIndex - 1 , "<<<");
        for(int index = startIndex ; index <= endIndex ; index++){
            result += produceOneItem(index , index + "");
        }
        if(endIndex < end)
            result += produceOneItem(endIndex + 1 , ">>>");
        result += "</div>";

        return result;
    }

    private String produceOneItem(int index , String displayString){
        String background = getDisplayColor(index);

        String result = "<a type='PARA_TYPE' class='PARA_CLASS' style='PARA_STYLE' href='PARA_HREF'>PARA_DISPLAY</a>";
        String PARA_TYPE = "button";
        String PARA_CLASS = "pull-left btn placeholders btn-default";
        String PARA_STYLE = "text-align:center;width:40px;height:25px;background:" + background + ";color:white";
        String PARA_HREF = URL.URL_LABEL_TASK;

        result = result.replace("PARA_TYPE" , PARA_TYPE);
        result = result.replace("PARA_CLASS" , PARA_CLASS);
        result = result.replace("PARA_STYLE" , PARA_STYLE);
        result = result.replace("PARA_HREF" , PARA_HREF);
        result = result.replace("PARA_DISPLAY" , displayString);

        return result;
    }

    private String getDisplayColor(int index){
        if(index == currentIndex)
            return "#000000";
        else
            return "#FFFFFF";
    }

}
