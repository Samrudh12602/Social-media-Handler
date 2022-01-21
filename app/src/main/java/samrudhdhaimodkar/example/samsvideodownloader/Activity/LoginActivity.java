package samrudhdhaimodkar.example.samsvideodownloader.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import samrudhdhaimodkar.example.samsvideodownloader.InstagramActivity;
import samrudhdhaimodkar.example.samsvideodownloader.R;
import samrudhdhaimodkar.example.samsvideodownloader.SharePrefs;

public class LoginActivity extends AppCompatActivity {

    LoginActivity activity;
    private WebView webView;
    private String cookies;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.activity = this;
        webView = findViewById(R.id.insta);
        Login();
        swipeRefreshLayout = findViewById(R.id.loginSwipe);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("WrongConstant")
            @Override
            public final void onRefresh() {
                Login();
            }
        });
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void Login() {
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.clearCache(true);
        this.webView.setWebViewClient(new MyBrowser());
        CookieSyncManager.createInstance(this.activity);
        CookieManager.getInstance().removeAllCookie();
        this.webView.loadUrl("https://www.instagram.com/accounts/login/");
        this.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
                if (i == 100) {
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
    }
    public String getCookie(String str, String str2) {
        String cookie = CookieManager.getInstance().getCookie(str);
            String[] split = cookie.split(";");
            for (String str3 : split) {
                if (str3.contains(str2)) {
                    return str3.split("=")[1];
                }
            }
        return null;
    }
    public class MyBrowser extends WebViewClient {
        private MyBrowser() {
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            webView.loadUrl(str);
            return true;
        }
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            LoginActivity.this.cookies = CookieManager.getInstance().getCookie(str);
            try {
                String cookie = LoginActivity.this.getCookie(str, "sessionid");
                String cookie2 = LoginActivity.this.getCookie(str, "csrftoken");
                String cookie3 = LoginActivity.this.getCookie(str, "ds_user_id");
                if (cookie != null && cookie2 != null && cookie3 != null) {
                    SharePrefs.getInstance(activity).setCookies(LoginActivity.this.cookies);
                    SharePrefs.getInstance(activity).setCsrf(cookie2);
                    SharePrefs.getInstance(activity).setSESSIONID(cookie);
                    SharePrefs.getInstance(activity).setUSERID(cookie3);
                    SharePrefs.getInstance(activity).setIsInstagramLogin(true);
                    LoginActivity.this.webView.destroy();
                    Intent intent = new Intent();
                    intent.putExtra("result", "result");
                    LoginActivity.this.setResult(-1, intent);
                    LoginActivity.this.finish();
                    startActivity(new Intent(LoginActivity.this, InstagramActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}