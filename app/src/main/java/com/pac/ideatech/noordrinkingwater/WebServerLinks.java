package com.pac.ideatech.noordrinkingwater;

/**
 * Created by MianGhazanfar on 10/30/2016.
 */
public class WebServerLinks {


    public static String mainURL = "http://codingbiz.com/projects/new_erp/ajax/";
    public static String loginURL=mainURL+"login-exec.php";
    public static String signup=mainURL+"process.php";


    public static String getLoginURL(String userid,String pswd){
        return mainURL+"process.php"+"&user_email="+userid+"&user_pwd="+pswd+"&login_from_app=login_from_app";
    }



}
