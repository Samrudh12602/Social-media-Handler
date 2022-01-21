package samrudhdhaimodkar.example.samsvideodownloader.model;

import static samrudhdhaimodkar.example.samsvideodownloader.SharePrefs.SESSIONID;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;
import java.util.Objects;

import samrudhdhaimodkar.example.samsvideodownloader.SharePrefs;
import samrudhdhaimodkar.example.samsvideodownloader.Util;

public class InstaVideo {

    private static DatabaseReference database;
    private static long counter=0;

    public static void downloadVideo(Context context, String postUrl) {
        database= FirebaseDatabase.getInstance().getReference("user");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child(SESSIONID).getValue().equals(SharePrefs.getInstance(context).getSESSIONID()))
                        counter= ((long) Objects.requireNonNull(ds.child("Counter").getValue()));                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        String replacedUrl;
        final String[] finalVideoUrl = new String[1];

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(context);

        if (TextUtils.isEmpty(postUrl)) {
            Log.e("VideoURLErrors", "Provided String is empty.");
        } else {
            if (postUrl.contains("?utm_source=ig_web_copy_link")) {
                String partToRemove = "?utm_source=ig_web_copy_link";
                replacedUrl = postUrl.replace(partToRemove, "");
            } else if (postUrl.contains("?utm_source=ig_web_button_share_sheet")) {
                String partToRemove = "?utm_source=ig_web_button_share_sheet";
                replacedUrl = postUrl.replace(partToRemove, "");
            } else if (postUrl.contains("?utm_medium=share_sheet")) {
                String partToRemove = "?utm_medium=share_sheet";
                replacedUrl = postUrl.replace(partToRemove, "");
            } else if (postUrl.contains("?utm_medium=copy_link")) {
                String partToRemove = "?utm_medium=copy_link";
                replacedUrl = postUrl.replace(partToRemove, "");
            }
//            else if (postUrl.contains("?utm_source=ig_story_item_share&utm_medium=share_sheet")) {
//                String partToRemove = "?utm_source=ig_story_item_share&utm_medium=share_sheet";
//                replacedUrl = postUrl.replace(partToRemove, "");
//            }
            else {
                replacedUrl = postUrl;
            }
            try {
                // instantiate CookieManager
                CookieManager manager = new CookieManager();
                CookieHandler.setDefault(manager);
                CookieStore cookieJar =  manager.getCookieStore();

                // create cookie
                HttpCookie cookie = new HttpCookie("csrftoken", SharePrefs.getInstance(context).getCsrf());
                HttpCookie cookie1 = new HttpCookie("sessionid",SharePrefs.getInstance(context).getSESSIONID());
                // add cookie to CookieStore for a
                // particular URL
                URL url = new URL(replacedUrl);
                cookieJar.add(url.toURI(), cookie);
                cookieJar.add(url.toURI(), cookie1);
            } catch(Exception e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    replacedUrl + "?__a=1", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    JSONObject Obj1 = null;
                    JSONObject Obj2 = null;
                    try {
                        Obj1 = response.getJSONObject("graphql");
                        Obj2 = Obj1.getJSONObject("shortcode_media");
                        finalVideoUrl[0] = Obj2.getString("video_url");
                        database.child(SharePrefs.getInstance(context).getSESSIONID()).child("Downloads").child(String.valueOf(counter)).setValue(finalVideoUrl[0]);
                        database.child(SharePrefs.getInstance(context).getSESSIONID()).child("Counter").setValue(counter);
                        database.child(SharePrefs.getInstance(context).getSESSIONID()).child(SESSIONID).setValue(SharePrefs.getInstance(context).getSESSIONID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(finalVideoUrl[0]==null){
                        try {
                            JSONObject ob3 = Obj2.getJSONObject("edge_sidecar_to_children");
                            JSONArray array= ob3.getJSONArray("edges");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject node = array.getJSONObject(i).getJSONObject("node");
                                String type=node.getString("__typename");
                                if(type.equalsIgnoreCase("GraphImage")){
                                    finalVideoUrl[0] = node.getString("display_url");
                                    database.child(SharePrefs.getInstance(context).getSESSIONID()).child("Downloads").child(String.valueOf(counter)).setValue(finalVideoUrl[0]);
                                    database.child(SharePrefs.getInstance(context).getSESSIONID()).child("Counter").setValue(counter);
                                    database.child(SharePrefs.getInstance(context).getSESSIONID()).child(SESSIONID).setValue(SharePrefs.getInstance(context).getSESSIONID());
                                    Util.download(finalVideoUrl[0], Util.RootDirectoryInstagram, context, System.currentTimeMillis() + ".jpg");
                                }else if(type.equalsIgnoreCase("GraphVideo")) {
                                    finalVideoUrl[0] = node.getString("video_url");
                                    database.child(SharePrefs.getInstance(context).getSESSIONID()).child("Downloads").child(String.valueOf(counter)).setValue(finalVideoUrl[0]);
                                    database.child(SharePrefs.getInstance(context).getSESSIONID()).child("Counter").setValue(counter);
                                    database.child(SharePrefs.getInstance(context).getSESSIONID()).child(SESSIONID).setValue(SharePrefs.getInstance(context).getSESSIONID());
                                    Util.download(finalVideoUrl[0], Util.RootDirectoryInstagram, context, System.currentTimeMillis() + ".mp4");
                                }
                            }
                        } catch (JSONException jsonException) {
                            try {
                                finalVideoUrl[0]=Obj2.get("display_url").toString();
                                database.child(SharePrefs.getInstance(context).getSESSIONID()).child("Downloads").child(String.valueOf(counter)).setValue(finalVideoUrl[0]);
                                database.child(SharePrefs.getInstance(context).getSESSIONID()).child("Counter").setValue(counter);
                                database.child(SharePrefs.getInstance(context).getSESSIONID()).child(SESSIONID).setValue(SharePrefs.getInstance(context).getSESSIONID());
                                Util.download(finalVideoUrl[0], Util.RootDirectoryInstagram, context, System.currentTimeMillis() + ".jpg");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        Util.download(finalVideoUrl[0], Util.RootDirectoryInstagram, context, System.currentTimeMillis() + ".mp4");
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VideoURLErrors", "Something went wrong" + error);
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            });

            requestQueue.add(jsonObjectRequest);
        }


    }

    public static void downloadImage(Context context, String postUrl) {

        String replacedUrl;
        final String[] finalImageUrl = new String[1];

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(context);

        if (TextUtils.isEmpty(postUrl)) {
            Log.e("VideoURLErrors", "Provided String is empty.");
        } else {
            if (postUrl.contains("?utm_source=ig_web_copy_link")) {
                String partToRemove = "?utm_source=ig_web_copy_link";
                replacedUrl = postUrl.replace(partToRemove, "");
            } else if (postUrl.contains("?utm_source=ig_web_button_share_sheet")) {
                String partToRemove = "?utm_source=ig_web_button_share_sheet";
                replacedUrl = postUrl.replace(partToRemove, "");
            } else if (postUrl.contains("?utm_medium=share_sheet")) {
                String partToRemove = "?utm_medium=share_sheet";
                replacedUrl = postUrl.replace(partToRemove, "");
            } else if (postUrl.contains("?utm_medium=copy_link")) {
                String partToRemove = "?utm_medium=copy_link";
                replacedUrl = postUrl.replace(partToRemove, "");
            } else {
                replacedUrl = postUrl;
            }
            try {
                // instantiate CookieManager
                CookieManager manager = new CookieManager();
                CookieHandler.setDefault(manager);
                CookieStore cookieJar =  manager.getCookieStore();

                HttpCookie cookie = new HttpCookie("csrftoken", SharePrefs.getInstance(context).getCsrf());
                HttpCookie cookie1 = new HttpCookie("sessionid",SharePrefs.getInstance(context).getSESSIONID() );

                // add cookie to CookieStore for a
                // particular URL
                URL url = new URL(replacedUrl);
                cookieJar.add(url.toURI(), cookie);
                cookieJar.add(url.toURI(), cookie1);
            } catch(Exception e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    replacedUrl + "?__a=1", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject Obj1 = null;
                    try {
                        Obj1 = response.getJSONObject("graphql");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject Obj2 = null;
                    try {
                        Obj2 = Obj1.getJSONObject("shortcode_media");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        finalImageUrl[0] = Obj2.getString("display_url");
                        database.child(SharePrefs.getInstance(context).getSESSIONID()).child("Downloads").child(String.valueOf(counter)).setValue(finalImageUrl[0]);
                        database.child(SharePrefs.getInstance(context).getSESSIONID()).child("Counter").setValue(counter);
                        database.child(SharePrefs.getInstance(context).getSESSIONID()).child(SESSIONID).setValue(SharePrefs.getInstance(context).getSESSIONID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("finalURL", finalImageUrl[0]);
                    Util.download(finalImageUrl[0],Util.RootDirectoryInstagram , context, System.currentTimeMillis() + ".jpg");
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VideoURLErrors", "Something went wrong" + error);
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    }
}
