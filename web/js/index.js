/**
 * Created by oliver on 2017/8/1.
 */

function validCheck(){
    if($("#login_user").textContent == "" || $("#login_pwd").textContent == "") return false;
    else return true;
}