package samrudhdhaimodkar.example.samsvideodownloader;

import static samrudhdhaimodkar.example.samsvideodownloader.SharePrefs.getInstance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import samrudhdhaimodkar.example.samsvideodownloader.databinding.ActivityInstagramBinding;
import samrudhdhaimodkar.example.samsvideodownloader.fragments.SearchFragment;
import samrudhdhaimodkar.example.samsvideodownloader.fragments.URLFragment;

public class InstagramActivity extends AppCompatActivity {
    private ActivityInstagramBinding binding;
    private InstagramActivity activity;
    private ViewPagerAdapter adapter;
    final String[] finalVideoUrl = new String[1];
    final String[] finalImageUrl = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_instagram);
        activity = this;
        if(!Login.isLogin(this)){
            Login.login(this);
        }else{
            initView();
        }
    }
    @SuppressLint("SetTextI18n")
    private void initView() {
        adapter= new InstagramActivity.ViewPagerAdapter(activity.getSupportFragmentManager(), activity.getLifecycle());
        adapter.addFragment(new SearchFragment(),"Profile Search");
        adapter.addFragment(new URLFragment(),"Download by Url");

        binding.viewPager1.setAdapter(adapter);
        binding.viewPager1.setOffscreenPageLimit(1);

        new TabLayoutMediator(binding.tabLayout,binding.viewPager1,
                (tab, position) ->{
                    tab.setText(adapter.fragmentTitleList.get(position));
                }).attach();

        for(int i=0;i<binding.tabLayout.getTabCount();i++){
            TextView tv= (TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tab,null);
            Objects.requireNonNull(binding.tabLayout.getTabAt(i)).setCustomView(tv);
        }
        binding.loginName.setText("Logged in as: "+ getInstance(getApplicationContext()).getUSERID());
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login.logout(InstagramActivity.this);
                startActivity(new Intent(InstagramActivity.this,InstagramActivity.class));
            }
        });
    }

    static class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList= new ArrayList<>();
        private final List<String> fragmentTitleList= new ArrayList<>();
        public ViewPagerAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }
        public void addFragment(Fragment fragment,String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
        @NonNull
        @org.jetbrains.annotations.NotNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position) ;
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}