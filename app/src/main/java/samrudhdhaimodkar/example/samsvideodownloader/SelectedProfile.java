package samrudhdhaimodkar.example.samsvideodownloader;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import samrudhdhaimodkar.example.samsvideodownloader.adapter.PostsAdapter;
import samrudhdhaimodkar.example.samsvideodownloader.adapter.StoriesAdapter;
import samrudhdhaimodkar.example.samsvideodownloader.model.PostData;
import samrudhdhaimodkar.example.samsvideodownloader.model.StoriesData;
import samrudhdhaimodkar.example.samsvideodownloader.model.Taggedusers;

public class SelectedProfile extends AppCompatActivity {
    private String uid;
    private String username, profilepicurl,post_url,post_caption,post_type,post_shortCode;
    private String no_of_posts=null;
    private CircleImageView profilepic;
    private TextView posts_no,followed_by,nametext,usernametext,followingtext;
    private ProgressDialog pd;
    private ImageButton downloadprofilepic;
    private PostsAdapter adapter;
    private StoriesAdapter storiesAdapter;
    private RecyclerView recyclerView;
    private ArrayList<PostData> posts;
    private ArrayList<StoriesData> stories;
    private SwipeRefreshLayout layout;
    private DatabaseReference database;
    private long counter=1;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        database= FirebaseDatabase.getInstance().getReference("user");
        uid=getIntent().getStringExtra("uid");
        username=getIntent().getStringExtra("username");
        posts_no=findViewById(R.id.posts_no);
        followed_by=findViewById(R.id.followers_count);
        nametext=findViewById(R.id.name);
        usernametext=findViewById(R.id.username);
        profilepic=findViewById(R.id.profilePic);
        followingtext=findViewById(R.id.following_count);
        downloadprofilepic=findViewById(R.id.profilePicDownload);
        layout=findViewById(R.id.swipe_layout_profile);
        recyclerView=findViewById(R.id.postsRecycler);
        pd= new ProgressDialog(this);
        posts=new ArrayList<>();
        stories= new ArrayList<>();
        fetchUserData();
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchUserData();
                layout.setRefreshing(false);
            }
        });
        downloadprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.download(profilepicurl, Util.RootDirectoryInstagram,SelectedProfile.this,System.currentTimeMillis()+".jpg");
            }
        });
    }
    private void fetchUserData(){
        ProgressDialog pd= new ProgressDialog(SelectedProfile.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Profile..");
        pd.show();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String apiUrl="https://www.instagram.com/graphql/query/?query_id=17888483320059182&variables=%7B%22id%22:%22"+uid+"%22,%22first%22:1000,%22after%22:null%7D";
        try {
            // instantiate CookieManager
            CookieManager manager = new CookieManager();
            CookieHandler.setDefault(manager);
            CookieStore cookieJar =  manager.getCookieStore();

            // create cookie
            HttpCookie cookie = new HttpCookie("csrftoken", SharePrefs.getInstance(getApplicationContext()).getCsrf());
            HttpCookie cookie1 = new HttpCookie("sessionid", SharePrefs.getInstance(getApplicationContext()).getSESSIONID());

            // add cookie to CookieStore for a
            // particular URL
            URL url = new URL(apiUrl);
            cookieJar.add(url.toURI(), cookie);
            cookieJar.add(url.toURI(), cookie1);
        } catch(Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(
                Request.Method.GET,
                apiUrl, null,
                response -> {
                    try {
                        posts.clear();
                        JSONObject ob= response.getJSONObject("data");
                        JSONObject ob1=ob.getJSONObject("user");
                        JSONObject ob2=ob1.getJSONObject("edge_owner_to_timeline_media");
                         no_of_posts=ob2.getString("count");
                         posts_no.setText(no_of_posts);
                         JSONArray ob3=ob2.getJSONArray("edges");
                         for(int i=0;i<ob3.length();i++){
                             JSONObject node= ob3.getJSONObject(i).getJSONObject("node");
                             post_type=node.getString("__typename");
//                             JSONObject caption=node.getJSONObject("edge_media_to_caption");
//                             JSONArray edges=caption.getJSONArray("edges");
//                             JSONObject node1=edges.getJSONObject(i).getJSONObject("node");
//                             post_caption=node1.getString("text");
                             post_caption="text";
                             post_shortCode=node.getString("shortcode");
                             post_url=node.getString("display_url");
                             PostData data= new PostData(post_url,post_caption,post_type,post_shortCode);
                             posts.add(data);
                             database.child(SharePrefs.getInstance(SelectedProfile.this).getSESSIONID()).child("profiles").child(username).child("posts").child(String.valueOf(counter)).setValue(data);
                             counter++;
                         }
                        adapter=new PostsAdapter(SelectedProfile.this,posts);
                         recyclerView.setHasFixedSize(true);
                         recyclerView.setAdapter(adapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                         pd.dismiss();
                    } catch (Exception e42) {
                        Toast.makeText(SelectedProfile.this,e42.toString(),Toast.LENGTH_LONG).show();
                    }
                },Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
        String storiesUrl="https://www.instagram.com/graphql/query/?query_hash=45246d3fe16ccc6577e0bd297a5db1ab&variables={%22highlight_reel_ids%22:[],%22reel_ids%22:[%22"+uid+"%22],%22location_ids%22:[],%22precomposed_overlay%22:false}";
        try {
            CookieManager manager = new CookieManager();
            CookieHandler.setDefault(manager);
            CookieStore cookieJar =  manager.getCookieStore();
            HttpCookie cookie = new HttpCookie("csrftoken", SharePrefs.getInstance(getApplicationContext()).getCsrf());
            HttpCookie cookie1 = new HttpCookie("sessionid", SharePrefs.getInstance(getApplicationContext()).getSESSIONID());
            URL Url = new URL(storiesUrl);
            cookieJar.add(Url.toURI(), cookie);
            cookieJar.add(Url.toURI(), cookie1);
        } catch(Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET,storiesUrl,null,response -> {
            try {
                String storyUrl;
                JSONObject ob = response.getJSONObject("data");
                JSONArray ob1=ob.getJSONArray("reels_media");
                for(int i=0;i<ob1.length();i++){
                    JSONArray ob2=ob1.getJSONObject(i).getJSONArray("items");
                    if(ob2.isNull(0)){
                        profilepic.setBackground(null);
                        pd.dismiss();
                        break;
                    }
                    else{
                        for(int j=0;j<ob2.length();j++){
                            JSONObject ob3=ob2.getJSONObject(j);
                            String type=ob3.getString("__typename");
                            String id=ob3.getString("id");
                            if(type.equalsIgnoreCase("GraphStoryVideo")){
                                JSONArray display =ob3.getJSONArray("video_resources");
                                storyUrl=display.getJSONObject(0).getString("src");
                            }else{
                                storyUrl=ob3.getString("display_url");
                            }
                            Taggedusers taggedusers = null;
                            Double takenAt=ob3.getDouble("taken_at_timestamp");
                            Double expiry=ob3.getDouble("expiring_at_timestamp");
                            JSONArray tag=ob3.getJSONArray("tappable_objects");
                            for( int l=0;l<tag.length();l++){
                                String tagtype=tag.getJSONObject(l).getString("__typename");
                                if(tagtype.equalsIgnoreCase("GraphTappableMention")){
                                    String tagUsername=tag.getJSONObject(l).getString("username");
                                    String tagName=tag.getJSONObject(l).getString("full_name");
                                    taggedusers= new Taggedusers(tagUsername,tagName);
                                }
                            }
                            StoriesData storiesData= new StoriesData(type,storyUrl,id,takenAt,expiry,taggedusers);
                            stories.add(storiesData);
                            storiesAdapter=new StoriesAdapter(stories, SelectedProfile.this);
                            pd.dismiss();
                    }
                    }
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        },Throwable::printStackTrace);
        requestQueue.add(request);
        String url="https://www.instagram.com/"+username+"/?__a=1";
        try {
            // instantiate CookieManager
            CookieManager manager = new CookieManager();
            CookieHandler.setDefault(manager);
            CookieStore cookieJar =  manager.getCookieStore();

            // create cookie
            HttpCookie cookie = new HttpCookie("csrftoken", SharePrefs.getInstance(getApplicationContext()).getCsrf());
            HttpCookie cookie1 = new HttpCookie("sessionid", SharePrefs.getInstance(getApplicationContext()).getSESSIONID());

            // add cookie to CookieStore for a
            // particular URL
            URL Url = new URL(url);
            cookieJar.add(Url.toURI(), cookie);
            cookieJar.add(Url.toURI(), cookie1);
        } catch(Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request1=new JsonObjectRequest(Request.Method.GET,url,null,response -> {
            try {
                JSONObject ob = response.getJSONObject("graphql");
                JSONObject ob1=ob.getJSONObject("user");
                JSONObject ob2=ob1.getJSONObject("edge_followed_by");
                String followers=ob2.getString("count");
                name=ob1.getString("full_name");
                String username=ob1.getString("username");
                profilepicurl=ob1.getString("profile_pic_url_hd");
                JSONObject ob3=ob1.getJSONObject("edge_follow");
                String following=ob3.getString("count");
                followed_by.setText(followers);
                nametext.setText(name);
                usernametext.setText(username);
                followingtext.setText(following);
                Glide.with(getApplicationContext()).load(Uri.parse(profilepicurl)).into(profilepic);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        },Throwable::printStackTrace);
        requestQueue.add(request1);
    }
}