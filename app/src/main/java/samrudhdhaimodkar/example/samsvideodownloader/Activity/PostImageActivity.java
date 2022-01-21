package samrudhdhaimodkar.example.samsvideodownloader.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;
import java.util.ArrayList;

import samrudhdhaimodkar.example.samsvideodownloader.R;
import samrudhdhaimodkar.example.samsvideodownloader.SharePrefs;
import samrudhdhaimodkar.example.samsvideodownloader.adapter.InstaImageAdapter;
import samrudhdhaimodkar.example.samsvideodownloader.adapter.LinkAdapter;
import samrudhdhaimodkar.example.samsvideodownloader.model.MediaData;

public class PostImageActivity extends AppCompatActivity {
    private ArrayList<MediaData> media = new ArrayList<>();
    private String post_shortcode;
    final String[] finalVideoUrl = new String[1];
    final String[] finalImageUrl = new String[1];
    private LinkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image);
        Intent i= getIntent();
        post_shortcode=i.getStringExtra("shortcode");

        RequestQueue requestQueue=  Volley.newRequestQueue(this);
        String apiurl="https://www.instagram.com/p/"+post_shortcode+"/?__a=1";
        try {
            // instantiate CookieManager
            CookieManager manager = new CookieManager();
            CookieHandler.setDefault(manager);
            CookieStore cookieJar =  manager.getCookieStore();

            // create cookie
            HttpCookie cookie = new HttpCookie("csrftoken", SharePrefs.getInstance(getApplicationContext()).getCsrf());
            HttpCookie cookie1 = new HttpCookie("sessionid",SharePrefs.getInstance(getApplicationContext()).getSESSIONID());
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
                media.clear();
                JSONObject Obj1 = null;
                try {
                    Obj1 = response.getJSONObject("graphql");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject Obj2 = null;
                try {
                    assert Obj1 != null;
                    Obj2 = Obj1.getJSONObject("shortcode_media");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    assert Obj2 != null;
                    finalVideoUrl[0] = Obj2.getString("video_url");
                    if(finalVideoUrl[0]!=null){
                        MediaData data1= new MediaData(finalVideoUrl[0],"graphVideo");
                        media.add(data1);
                    }
                    ViewPager viewPager= findViewById(R.id.vpgr1);
                    InstaImageAdapter adapter= new InstaImageAdapter(getApplicationContext(),media);
                    viewPager.setAdapter(adapter);
                } catch (JSONException e) {
                    try {
                        JSONObject ob3= Obj2.getJSONObject("edge_sidecar_to_children");
                        JSONArray array= ob3.getJSONArray("edges");
                        for(int i=0;i<array.length();i++) {
                            JSONObject node = array.getJSONObject(i).getJSONObject("node");
                            String type=node.getString("__typename");
                            finalImageUrl[0] = node.getString("display_url");
                            if(finalImageUrl[0]!=null){
                                MediaData data1= new MediaData(finalImageUrl[0],type);
                                media.add(data1);
                            }
                        }
                        ViewPager viewPager= findViewById(R.id.vpgr1);
                        InstaImageAdapter adapter= new InstaImageAdapter(getApplicationContext(),media);
                        viewPager.setAdapter(adapter);

                    } catch (JSONException jsonException) {
                        try {
                            finalImageUrl[0]= Obj2.getString("display_url");
                            if(finalImageUrl[0]!=null){
                                MediaData data1= new MediaData(finalImageUrl[0],"graphImage");
                                media.add(data1);
                            }
                            ViewPager viewPager= findViewById(R.id.vpgr1);
                            InstaImageAdapter adapter= new InstaImageAdapter(getApplicationContext(),media);
                            viewPager.setAdapter(adapter);
                        } catch (JSONException exception) {
                            e.printStackTrace();
                        }
                    }
                    ViewPager viewPager= findViewById(R.id.vpgr1);
                    InstaImageAdapter adapter= new InstaImageAdapter(getApplicationContext(),media);
                    viewPager.setAdapter(adapter);
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