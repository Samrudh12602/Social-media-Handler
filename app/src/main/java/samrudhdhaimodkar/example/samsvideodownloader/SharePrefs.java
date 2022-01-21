package samrudhdhaimodkar.example.samsvideodownloader;
import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefs {
    public static String PREFERENCE = "VIDEODOWNLOADER";
    SharedPreferences sharedPreferences;
    private static SharePrefs instance;
    public static String SESSIONID = "session_id";
    public static String USERID = "user_id";
    public static final String COOKIES = "Cookies";
    public static final String CSRF = "csrf";
    public static final String IS_INSTAGRAM_LOGIN = "Is_Instagram_Login";

    public SharePrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }
    public static SharePrefs getInstance(Context context) {
        if (instance == null) {
            instance = new SharePrefs(context);
        }
        return instance;
    }
    public void setCsrf(String state){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(CSRF,state);
        editor.commit();
    }
    public String getCsrf(){
        String state= sharedPreferences.getString(CSRF,"");
        return state;
    }
    public void setCookies(String state){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(COOKIES,state);
        editor.commit();
    }
    public String getCookies(){
        String state= sharedPreferences.getString(COOKIES,"");
        return state;
    }
    public void setUSERID(String state){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(USERID,state);
        editor.commit();
    }
    public String getUSERID(){
        String state= sharedPreferences.getString(USERID,"");
        return state;
    }
    public void setSESSIONID(String state){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(SESSIONID,state);
        editor.commit();
    }
    public String getSESSIONID(){
        String state= sharedPreferences.getString(SESSIONID,"");
        return state;
    }
    public void setIsInstagramLogin(boolean state){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(IS_INSTAGRAM_LOGIN,state);
        editor.commit();
    }
    public boolean getIsInstagramLogin(){
        boolean state= sharedPreferences.getBoolean(IS_INSTAGRAM_LOGIN,false);
        return state;
    }
}
