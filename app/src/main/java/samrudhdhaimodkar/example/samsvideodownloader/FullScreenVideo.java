package samrudhdhaimodkar.example.samsvideodownloader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import samrudhdhaimodkar.example.samsvideodownloader.databinding.ActivityFullScreenVideoBinding;

public class FullScreenVideo extends AppCompatActivity {
    private ActivityFullScreenVideoBinding binding;
    private FullScreenVideo activity;
    private String saveFilePath= Util.RootDirectoryWhatsapp+"/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_full_screen_video);
        activity=this;
        Intent i=getIntent();
        Uri videoUri=i.getData();
        String mode=i.getStringExtra("mode");
        binding.clickedVideo.setVideoURI(videoUri);
        binding.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.playBtn.setVisibility(View.GONE);
                MediaController mediaController = new MediaController(FullScreenVideo.this);
                binding.clickedVideo.setMediaController(mediaController);
                mediaController.setAnchorView(binding.clickedVideo);
                mediaController.show();
                binding.clickedVideo.start();
            }
        });
        binding.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equalsIgnoreCase("insta")){
                    Util.download(videoUri.toString(), Util.RootDirectoryInstagram,FullScreenVideo.this,System.currentTimeMillis()+".mp4");
                }else{
                    Util.createFolder();
                    final File file=new File(videoUri.getPath());
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
                Intent intent= new Intent(Intent.ACTION_SEND);
                intent.setType("video/*");
                intent.setData(videoUri);
                startActivityForResult(intent,1000);
                Toast.makeText(getApplicationContext(),"Under Development",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){

        }
    }
}