function Allocate ( taskID , itemnum , index) {
    var parameters={
        requestType:"addSubtask",
            taskID:taskID,
            itemnum:itemnum,
        index:index,
        subtaskNum: $("#subtasknum").val()
    };
    $.ajax({
        type:"Post",
        url:"SubTask",
        data:parameters,
        async: false,
        success:function (data) {
            if(data==0) {
                alert("Succeed!!");
            }
            else{
                alert("Fail!!");
            }
            window.location.href="mylabelTaskBrowser.jsp?index=" + index ;
        }
    });
}

function AddtoMyannotateList ( taskID , subtaskID , userID) {
    var parameters={
        requestType:"addtoSubtaskList",
        taskID:taskID,
        subtaskID:subtaskID,
        userID:userID
    };
    $.ajax({
        type:"Post",
        url:"SubTask",
        data:parameters,
        async: false,
        success:function (data) {
            if(data==0) {
                alert("Succeed!!");
            }
            else{
                alert("Fail!!");
            }
            window.location.href="myAnnotateTaskBrowser.jsp";
        }
    });
}