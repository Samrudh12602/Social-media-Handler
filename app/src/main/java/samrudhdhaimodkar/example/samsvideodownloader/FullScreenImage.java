package samrudhdhaimodkar.example.samsvideodownloader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import samrudhdhaimodkar.example.samsvideodownloader.databinding.ActivityFullScreenImageBinding;

public class FullScreenImage extends AppCompatActivity {
    private ActivityFullScreenImageBinding binding;
    private FullScreenImage activity;
    private String saveFilePath= Util.RootDirectoryWhatsapp+"/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_full_screen_image);
        activity=this;
        Intent i=getIntent();
        Uri imageUri=i.getData();
        String mode=i.getStringExtra("mode");
        Glide.with(getApplicationContext()).load(imageUri).into(binding.clickedImage);
        binding.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equalsIgnoreCase("insta")){
                    Util.download(imageUri.toString(), Util.RootDirectoryInstagram,FullScreenImage.this,System.currentTimeMillis()+".jpg");
                }else{
                    Util.createFolder();
                    final File file=new File(imageUri.getPath());
                    File destinationFile= new File(saveFilePath);
                    try {
                        FileUtils.copyFileToDirectory(file,destinationFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),"Saved to:"+ saveFilePath,Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Under Development",Toast.LENGTH_LONG).show();
            }
        });
//        if(imageUri.toString().endsWith(".mp4")){
//            binding.playBtn.setVisibility(View.VISIBLE);
//            binding.playBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Download the video to play",Toast.LENGTH_LONG).show();
//                }
//            });
//        }else{
//            binding.playBtn.setVisibility(View.GONE);
//        }
    }
}