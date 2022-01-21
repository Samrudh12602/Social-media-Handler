package samrudhdhaimodkar.example.samsvideodownloader;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.navigation.NavigationBarView;

import samrudhdhaimodkar.example.samsvideodownloader.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_settings);
        binding.bottomNavigationView.setSelectedItemId(R.id.settings);
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:startActivity(new Intent(SettingsActivity.this,MainActivity.class));
                        overridePendingTransition(0,0);;
                    case R.id.settings:
                        return true;
                }
                return false;
            }
        });
    }
}