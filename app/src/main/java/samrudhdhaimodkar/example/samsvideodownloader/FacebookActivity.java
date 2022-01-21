package samrudhdhaimodkar.example.samsvideodownloader;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import samrudhdhaimodkar.example.samsvideodownloader.databinding.ActivityFacebookBinding;

public class FacebookActivity extends AppCompatActivity {
    private ActivityFacebookBinding binding;
    private FacebookActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_facebook);
        activity=this;

        binding.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFaceBookData();
            }
        });
        binding.pasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager= (ClipboardManager) getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData data=clipboardManager.getPrimaryClip();
                ClipData.Item item= data.getItemAt(0);
                binding.fbUrl.setText(item.getText().toString());

                binding.fbVideo.setVideoPath(item.getText().toString());

                binding.playBtn.setVisibility(View.VISIBLE);

                binding.playBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.fbVideo.start();
                    }
                });
            }
        });
    }
    private void getFaceBookData() {
        URL url= null;
        try {
            url = new URL(Objects.requireNonNull(binding.fbUrl.getText()).toString());
            String host=url.getHost();
            if(host.contains("facebook.com")){
                new CallGetFbData().execute(binding.fbUrl.getText().toString());
            }else{
                Toast.makeText(this,"URL invalid, Please use FaceBook URL only",Toast.LENGTH_LONG).show();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
     class CallGetFbData extends AsyncTask<String,Void, Document>{

        Document fbDoc;
         @Override
         protected Document doInBackground(String... strings) {
             try {
                 fbDoc= Jsoup.connect(strings[0]).get();
             } catch (IOException e) {
                 e.printStackTrace();
             }
             return fbDoc;
         }

         @Override
         protected void onPostExecute(Document document) {
             String videoUrl = document.select("meta[property=\"og:video\"]").last().attr("content");
             if(!videoUrl.equals("")){
                 Util.download(videoUrl,Util.RootDirectoryFacebook,activity,"Facebook"+System.currentTimeMillis()+".mp4");
             }
         }
     }
}