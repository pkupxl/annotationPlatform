package cn.edu.pku.sei;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by oliver on 2017/8/11.
 */
public class CookieOperations {
    public static Cookie getCookie(HttpServletRequest request , String name){
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals(name)){
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getCookieValue(HttpServletRequest request , String name){
        Cookie cookie = getCookie(request , name);
        if(cookie != null)
            return cookie.getValue();
        else
            return null;
    }
}
