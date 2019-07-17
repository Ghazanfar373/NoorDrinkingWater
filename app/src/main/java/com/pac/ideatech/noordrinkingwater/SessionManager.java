package com.pac.ideatech.noordrinkingwater;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MianGhazanfar on 4/24/2016.
 */
public class SessionManager {
    private static String TAG=SessionManager.class.getSimpleName();

    //SharedPrefrence
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    private static final String PREF_NAME="aocpref";
    private static final String KEY_ISLOGEDIN="isloggedin";
    private static final String ISRIDERLOGEDIN="isriderloggedin";
    private static final String USER_NAME="uname";
    private static final String USER_FNAME="fname";
    private static final String USER_Pass="pass";
    private static final String USER_ADD="address";
    private static final String USER_country="country";
    private static final String USER_House_No="state";
    private static final String USER_CITY="city";
    private static final String USER_Area="area";

    private static final String USER_Cust_Type="cust_type";
    private static final String USER_Pref_dlvry="delivry";
    private static final String USER_MOBILE="mobile";

    private static final String USER_EMAIL="uemail";
    private static final String USER_ID="uid";
    private static final String Created_AT="c_at";
    private static final String USER_NUMBER  = "user";
    private static final String USER_TYPE  = "admin_type";
    public SessionManager(Context context){
        this._context=context;
        pref=_context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor=pref.edit();
    }

    public static String getTAG() {
        return TAG;
    }

    public static void setTAG(String TAG) {
        SessionManager.TAG = TAG;
    }
    public void addUserType(String userName, String userType,String useremail){
        editor.putString(USER_NAME,userName);
        editor.putString(USER_EMAIL,useremail);
        editor.putString(userType,userType);
        editor.commit();
    }
    public String getUserType() {
        return pref.getString(USER_TYPE, "");
    }

    public String getUserNumber() {
        return pref.getString(USER_NUMBER,"");
    }

    public void setLoggedin(boolean login){
        editor.putBoolean(KEY_ISLOGEDIN,login);
        editor.commit();
    }
    public boolean isRiderLoggedIn(){
        return pref.getBoolean(ISRIDERLOGEDIN,false);
    }
    public void setRiderLoggedin(boolean login){
        editor.putBoolean(ISRIDERLOGEDIN,login);
        editor.commit();
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_ISLOGEDIN,false);
    }
 public void setUserID(String id){
     editor.putString(USER_ID,id);
     editor.commit();
 }
    public void addUser(String fullname, String pass, String email, String uid, String userType, String address, String city,
                        String area, String house_no, String mobile, String created_at, String pref_dlvry) {
        editor.putString(USER_ID, uid);
        editor.putString(USER_FNAME, fullname);
        editor.putString(USER_Pass, pass);
        editor.putString(USER_EMAIL, email);
        editor.putString(USER_CITY, city);
        editor.putString(USER_Area, area);
        editor.putString(USER_ADD, address);

        editor.putString(USER_House_No, house_no);
        editor.putString(USER_Cust_Type, userType);
        editor.putString(USER_Pref_dlvry, pref_dlvry);

        editor.putString(USER_MOBILE, mobile);
        editor.putString(Created_AT, created_at);

        editor.commit();
    }
   // {"result":"true","module":"login","msg":"success","page":"dashboard","user_details":{"id":"1416","name":"Talha Saleem","password":"asdasd","city_id":"9","credit_limit":"0","area_id":"0","house_no":"552","street":"Testing...","address":"14 Street #34 - Sharjah - United Arab Emirates","customer_address_lat":"25.3267281","customer_address_lng":"55.44515990000002","email":"talha.saleem787@gmail.com","trn":"","delivery_required":"Mon","customer_grp":"0","contact_nos":"03087748680","username":"talha.saleem787@gmail.com","how_to_know":"0","describe":"From friend","remove":"0","update":"1526035002"}}

    public void addPickupParams(String date,int time_frame,boolean isfabric_collect){
        editor.putString("date",date);
        editor.putInt("time_frame",time_frame);
        editor.putBoolean("isfabric_collect",isfabric_collect);
        editor.commit();
    }
    public String getUser_fName(){
        return pref.getString(USER_FNAME,null);
    }
    public String getUser_Pass(){
        return pref.getString(USER_Pass,null);
    }
    public String getUserEmail(){
        return pref.getString(USER_EMAIL,null);
    }

    public String getUser_Address(){
        return pref.getString(USER_ADD,null);
    }
    public String getUser_Area(){
        return pref.getString(USER_Area,null);
    }

    public String getUser_City(){
        return pref.getString(USER_CITY,null);
    }
    public String getUSER_House_No(){
        return pref.getString(USER_House_No,null);
    }
    public String getUser_Type(){
        return pref.getString(USER_Cust_Type,null);
    }
    public String getUSER_Pref_dlvry(){
        return pref.getString(USER_Pref_dlvry,null);
    }
    public String getUser_Mobile(){
        return pref.getString(USER_MOBILE,null);
    }
    public String getpickUp_Date(){
        return pref.getString("date",null);
    }
    public int    getpickUp_time_frame(){
        return pref.getInt("time_frame",0);
    }
    public boolean getpickUp_isfabric_collect(){
        return pref.getBoolean("isfabric_collect",false);
    }
    public void setUserName(String user){
        editor.putString(USER_NAME,user);
        editor.commit();
    }
    public String getUserName(){
        return pref.getString(USER_NAME,null);
    }

    public String getUserId(){
        return pref.getString(USER_ID,null);
    }
    public String getCreated_AT(){
        return pref.getString(Created_AT,null);
    }
public void logout(){
    editor.clear();
    editor.commit();
}
}
