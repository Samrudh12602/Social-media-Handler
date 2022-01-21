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

import samrudhdhaimodkar.example.samsvideodownloader.databinding.ActivityShareChatBinding;

public class ShareChatActivity extends AppCompatActivity {
    private ActivityShareChatBinding binding;
    private ShareChatActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_share_chat);

        binding.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getShareChatData();
            }
        });
        binding.pasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager= (ClipboardManager) getApplicationContext().getSystemService(CLIPBOARD_SERVICE);

                ClipData data=clipboardManager.getPrimaryClip();
                ClipData.Item item= data.getItemAt(0);

                binding.shareChatURL.setText(item.getText().toString());
            }
        });
    }
    private void getShareChatData() {
        URL url= null;
        try {
            url = new URL(Objects.requireNonNull(binding.shareChatURL.getText()).toString());
            String host=url.getHost();
            if(host.contains("sharechat")){
                new CallGetScData().execute(binding.shareChatURL.getText().toString());
            }else{
                Toast.makeText(this,"URL invalid, Please use ShareChat URL only",Toast.LENGTH_LONG).show();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
class CallGetScData extends AsyncTask<String,Void, Document> {

    Document scDoc;
    @Override
    protected Document doInBackground(String... strings) {
        try {
            scDoc= Jsoup.connect(strings[0]).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scDoc;
    }

    @Override
    protected void onPostExecute(Document document) {
        String videoUrl = document.select("meta[property=\"og:video:secure_url\"]").last().attr("content");
        if(!videoUrl.equals("")){
            Util.download(videoUrl,Util.RootDirectoryShareChat,activity,"ShareChat"+System.currentTimeMillis()+".mp4");
        }
    }
}
}