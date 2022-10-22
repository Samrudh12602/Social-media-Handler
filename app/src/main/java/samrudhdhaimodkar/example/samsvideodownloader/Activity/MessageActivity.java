package samrudhdhaimodkar.example.samsvideodownloader.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import samrudhdhaimodkar.example.samsvideodownloader.R;

public class MessageActivity extends AppCompatActivity {
    private Button send;
    private EditText number,message;
    private String phoneNumber,userMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        number=findViewById(R.id.phoneBox);
        message=findViewById(R.id.message);
        send=findViewById(R.id.sendMessage);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber="+91"+number.getText().toString();
                userMsg=message.getText().toString();
                String url = "https://api.whatsapp.com/send?phone="+phoneNumber+"&text="+userMsg;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}