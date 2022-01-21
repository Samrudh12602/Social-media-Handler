package samrudhdhaimodkar.example.samsvideodownloader;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationBarView;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import samrudhdhaimodkar.example.samsvideodownloader.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {
    private ActivityMainBinding binding;
    private AdView adView;
    private InterstitialAd interad;
    private String amt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adView=findViewById(R.id.adView);
        setContentView(R.layout.activity_main);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.bottomNavigationView.setSelectedItemId(R.id.home);
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:return true;
                    case R.id.settings:startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });
        if(!checkPermissions()){
            showPermission();
        }
        binding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showAD();
                startActivity(new Intent(MainActivity.this,WhatsappActivity.class));
            }
        });
        binding.facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showAD();
                startActivity(new Intent(MainActivity.this,FacebookActivity.class));
            }
        });
        binding.shareChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showAD();
                startActivity(new Intent(MainActivity.this,ShareChatActivity.class));
            }
        });
        binding.instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showAD();
                startActivity(new Intent(MainActivity.this, InstagramActivity.class));
            }
        });
        binding.payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showForgotDialog(MainActivity.this);
            }
        });
    }
    private void pay() {
        int amount =Integer.parseInt(amt);
        Checkout co= new Checkout();
        JSONObject object= new JSONObject();

        try {
            object.put("name","Samrudh Dhaimodkar");
            object.put("Description","Pay the developers");
            object.put("image","https://console.firebase.google.com/project/covid-help-7e852/storage/covid-help-7e852.appspot.com/files/~2Fusers~2FOTD6lAEkS6ZXlIk1EMSNoi71XUE3");
            object.put("currency","INR");
            object.put("amount",amount*100);

            JSONObject prefill= new JSONObject();
            prefill.put("contact","+917798889355");
            prefill.put("email","samdhaimodkar@gmail.com");
            object.put("prefill",prefill);

            co.open(MainActivity.this,object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void showForgotDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Select Amount")
                .setMessage("Enter the Amount you would like to contribute")
                .setView(taskEditText)
                .setPositiveButton("Continue to Payment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        amt = String.valueOf(taskEditText.getText());
                        pay();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
    private void showAD(){
        AdRequest adRequest= new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull @NotNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                interstitialAd.show(MainActivity.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        });
    }
    private void showPermission(){
        if(SDK_INT>=Build.VERSION_CODES.R){
            try {
                Intent intent=new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent,2000);
            } catch (Exception e) {
                Intent intent=new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent,2000);
            }
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},333);
        }
    }
    private boolean checkPermissions(){
        if(SDK_INT>= Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }
        else{
            int write= ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read= ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE);

            return write== PackageManager.PERMISSION_GRANTED
            && read== PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2000) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (grantResults.length > 0) {
                    boolean read = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean write = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (read && write) {
                    } else {
                        Toast.makeText(this, "Permissions not Granted", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2000){
            if(SDK_INT>=Build.VERSION_CODES.R){
                if(Environment.isExternalStorageManager()){
                    Toast.makeText(this,"Permissions Granted",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this,"Permissions not Granted",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(MainActivity.this,"Payment Successful",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}