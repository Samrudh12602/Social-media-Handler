package samrudhdhaimodkar.example.samsvideodownloader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import samrudhdhaimodkar.example.samsvideodownloader.Activity.LoginActivity;


public class Login {


    public static AlertDialog dialog;

    public static void users(Context context) {
        if (SharePrefs.getInstance(context).getIsInstagramLogin()) {
        } else {
            Toast.makeText(context, "Please login first.", Toast.LENGTH_SHORT).show();
        }
    }
    public static void login(Context context) {
        ((Activity) context).startActivityForResult(new Intent(context, LoginActivity.class), 100);
    }
    public static boolean isLogin(Context context) {
        return SharePrefs.getInstance(context).getIsInstagramLogin();
    }
    public static void logout(Context context) {
        SharePrefs.getInstance(context).setIsInstagramLogin(false);
        SharePrefs.getInstance(context).setCookies("");
        SharePrefs.getInstance(context).setCsrf("");
        SharePrefs.getInstance(context).setSESSIONID("");
        SharePrefs.getInstance(context).setUSERID("");
    }
}
