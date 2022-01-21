package samrudhdhaimodkar.example.samsvideodownloader;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;

public class SelectedMedia extends AppCompatActivity {
    private ImageView play,post;
    private String post_shortcode;
    private VideoView instaVideo;
    private Button download;
    final String[] finalVideoUrl = new String[1];
    final String[] finalImageUrl = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_media);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        post_shortcode=getIntent().getStringExtra("shortcode");
        play=findViewById(R.id.playBtn);
        post=findViewById(R.id.postImage);
        instaVideo=findViewById(R.id.instavideo);
        download=findViewById(R.id.download);
        getSupportActionBar().hide();
        RequestQueue requestQueue=  Volley.newRequestQueue(this);
        String apiurl="https://www.instagram.com/p/"+post_shortcode+"/?__a=1";
        try {
            // instantiate CookieManager
            CookieManager manager = new CookieManager();
            CookieHandler.setDefault(manager);
            CookieStore cookieJar =  manager.getCookieStore();

            // create cookie
            HttpCookie cookie = new HttpCookie("csrftoken", "GRQMLRrvdczZYdRVemJro0e7TBr1KqB0");
            HttpCookie cookie1 = new HttpCookie("sessionid", "2203350754%3AyA7RhzNVMCwRPH%3A13");

            // add cookie to CookieStore for a
            // particular URL
            URL url = new URL(apiurl);
            cookieJar.add(url.toURI(), cookie);
            cookieJar.add(url.toURI(), cookie1);
        } catch(Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                apiurl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray Obj1 = null;
                try {
                    Obj1 = response.getJSONArray("items");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject Obj2 = null;
                try {
                    Obj2 = Obj1.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    finalVideoUrl[0] = Obj2.getJSONArray("video_versions").getJSONObject(0).getString("url");
                    Uri uri = Uri.parse(finalVideoUrl[0]);
                    post.setVisibility(View.GONE);
                    instaVideo.setVisibility(View.VISIBLE);
                    play.setVisibility(View.VISIBLE);
                    instaVideo.setVideoURI(uri);
                    instaVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            instaVideo.requestFocus();
                        }
                    });
                    play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            play.setVisibility(View.GONE);
                            MediaController mediaController = new MediaController(SelectedMedia.this);
                            instaVideo.setMediaController(mediaController);
                            mediaController.setAnchorView(instaVideo);
                            instaVideo.start();
                        }
                    });
                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Util.download(finalVideoUrl[0], Util.RootDirectoryInstagram,getApplicationContext(),System.currentTimeMillis()+".mp4" );
                        }
                    });

                } catch (JSONException e) {
                    try {
                        JSONArray array= Obj2.getJSONArray("carousel_media");
                        for(int i=0;i<array.length();i++) {
                            JSONObject node = array.getJSONObject(i).getJSONObject("image_versions2");
                            finalImageUrl[0] = node.getJSONArray("candidates").getJSONObject(0).getString("url");
                            instaVideo.setVisibility(View.GONE);
                            post.setVisibility(View.VISIBLE);
                        }
                        Glide.with(getApplicationContext()).load(Uri.parse(finalImageUrl[0])).into(post);
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Util.download(finalImageUrl[0],Util.RootDirectoryInstagram,getApplicationContext(),System.currentTimeMillis()+".jpg" );
                            }
                        });
                    } catch (JSONException jsonException) {
                        try {
                            finalImageUrl[0]= Obj2.getJSONObject("image_versions2").getJSONArray("candidates").getJSONObject(0).getString("url");
                            instaVideo.setVisibility(View.GONE);
                            post.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(Uri.parse(finalImageUrl[0])).into(post);
                            download.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Util.download(finalImageUrl[0],Util.RootDirectoryInstagram,getApplicationContext(),System.currentTimeMillis()+".jpg" );
                                }
                            });
                        } catch (JSONException exception) {
                        }
                    }
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VideoURLErrors", "Something went wrong" + error);
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonObjectRequest);

    }
}