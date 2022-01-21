package samrudhdhaimodkar.example.samsvideodownloader;

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

import samrudhdhaimodkar.example.samsvideodownloader.Activity.MessageActivity;
import samrudhdhaimodkar.example.samsvideodownloader.databinding.ActivityWhatsappBinding;
import samrudhdhaimodkar.example.samsvideodownloader.fragments.ImageFragment;
import samrudhdhaimodkar.example.samsvideodownloader.fragments.VideoFragment;

public class WhatsappActivity extends AppCompatActivity {
    private ActivityWhatsappBinding binding;
    private WhatsappActivity activity;
    private ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_whatsapp);
        activity=this;
        initView();
        binding.mssg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WhatsappActivity.this, MessageActivity.class));
            }
        });
    }

    private void initView() {
        adapter= new ViewPagerAdapter(activity.getSupportFragmentManager(), activity.getLifecycle());
        adapter.addFragment(new ImageFragment(),"Images");
        adapter.addFragment(new VideoFragment(),"Videos");

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(1);

        new TabLayoutMediator(binding.tabLayout,binding.viewPager,
                (tab, position) ->{
            tab.setText(adapter.fragmentTitleList.get(position));
                }).attach();

        for(int i=0;i<binding.tabLayout.getTabCount();i++){
            TextView tv= (TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tab,null);
            Objects.requireNonNull(binding.tabLayout.getTabAt(i)).setCustomView(tv);
        }
    }

    static class ViewPagerAdapter extends FragmentStateAdapter{
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