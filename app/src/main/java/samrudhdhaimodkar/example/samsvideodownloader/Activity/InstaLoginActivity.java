package samrudhdhaimodkar.example.samsvideodownloader.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import samrudhdhaimodkar.example.samsvideodownloader.InstagramActivity;
import samrudhdhaimodkar.example.samsvideodownloader.R;
import samrudhdhaimodkar.example.samsvideodownloader.Login;
import samrudhdhaimodkar.example.samsvideodownloader.databinding.ActivityInstaLoginBinding;

public class InstaLoginActivity extends AppCompatActivity {
    ActivityInstaLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_insta_login);
        if(!Login.isLogin(this)){
            Login.login(this);
        }
        else{
            startActivity(new Intent(InstaLoginActivity.this, InstagramActivity.class));
        }
    }
}