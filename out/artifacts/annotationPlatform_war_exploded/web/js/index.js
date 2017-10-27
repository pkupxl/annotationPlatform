/**
 * Created by oliver on 2017/8/1.
 */

function validCheck(){
    if($("#login_user").text()== "" || $("#login_pwd").text()== "") return false;
    else return true;
}


function userRegister(){
    var user  = $("#register_user").val();
    var pwd = $("#register_pwd").val();
    var confirm_pwd = $("#confirm_pwd").val();

    if(user == ""){
        alert("用户名不能为空！");
        return false;
    }
    else if(pwd == ""){
        alert("密码不能为空！");
        return false;
    }
    else if(pwd != confirm_pwd){
        alert("前后密码不一致！");
        return false;
    }
    var parameters = {
        requestType:"register",
        user: user,
        pwd: pwd
    };
    $.ajax({
        type:"Post",
        url:"UserLogin",
        data:parameters,
        async: false,
        success:function (data) {
            if(data==0)
                alert("注册成功！请返回登录界面进行登录！");
            else if(data ==1){
                alert("账户已存在，请重新注册！");
            }
        }
    });
    return true;
}