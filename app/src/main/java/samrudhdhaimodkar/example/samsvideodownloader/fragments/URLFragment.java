package samrudhdhaimodkar.example.samsvideodownloader.fragments;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import samrudhdhaimodkar.example.samsvideodownloader.R;
import samrudhdhaimodkar.example.samsvideodownloader.SharePrefs;
import samrudhdhaimodkar.example.samsvideodownloader.adapter.LinkAdapter;
import samrudhdhaimodkar.example.samsvideodownloader.databinding.FragmentURLBinding;
import samrudhdhaimodkar.example.samsvideodownloader.model.InstaVideo;
import samrudhdhaimodkar.example.samsvideodownloader.model.MediaData;

public class URLFragment extends Fragment {
    private FragmentURLBinding binding;
    final String[] finalVideoUrl = new String[1];
    final String[] finalImageUrl = new String[1];
    private ArrayList<MediaData> media;
    private LinkAdapter adapter;
    private DatabaseReference database;
    private long counter=0;
    private static final String USER= "user";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,
                R.layout.fragment_u_r_l,
                container,
                false);
        RequestQueue requestQueue;
        media= new ArrayList<>();
        database=FirebaseDatabase.getInstance().getReference(USER);
        adapter=new LinkAdapter(getContext(),media);
        binding.whatsappRecycler.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());

//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds: snapshot.getChildren()){
//                    if((ds.hasChild(SESSIONID))&&(ds.child(SESSIONID).getValue().equals(SharePrefs.getInstance(getContext()).getSESSIONID())))
//                        counter= (long) Objects.requireNonNull(ds.child("Counter").getValue());
//                    }
//                }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
        binding.pasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager= (ClipboardManager) requireActivity().getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData data=clipboardManager.getPrimaryClip();
                ClipData.Item item= data.getItemAt(0);
                binding.instaUrl.setText(item.getText().toString());
                String replacedUrl;
                if (TextUtils.isEmpty(item.getText().toString())) {
                    Log.e("VideoURLErrors", "Provided String is empty.");
                } else {
                    if (item.getText().toString().contains("?utm_source=ig_web_copy_link")) {
                        String partToRemove = "?utm_source=ig_web_copy_link";
                        replacedUrl = item.getText().toString().replace(partToRemove, "");
                    } else if (item.getText().toString().contains("?utm_source=ig_web_button_share_sheet")) {
                        String partToRemove = "?utm_source=ig_web_button_share_sheet";
                        replacedUrl = item.getText().toString().replace(partToRemove, "");
                    } else if (item.getText().toString().contains("?utm_medium=share_sheet")) {
                        String partToRemove = "?utm_medium=share_sheet";
                        replacedUrl = item.getText().toString().replace(partToRemove, "");
                    } else if (item.getText().toString().contains("?utm_medium=copy_link")) {
                        String partToRemove = "?utm_medium=copy_link";
                        replacedUrl = item.getText().toString().replace(partToRemove, "");
                    }
                    else {
                        replacedUrl = item.getText().toString();
                    }
                    try {
                        // instantiate CookieManager
                        CookieManager manager = new CookieManager();
                        CookieHandler.setDefault(manager);
                        CookieStore cookieJar =  manager.getCookieStore();

                        // create cookie
                        HttpCookie cookie = new HttpCookie("csrftoken", SharePrefs.getInstance(getContext()).getCsrf());
                        HttpCookie cookie1 = new HttpCookie("sessionid", SharePrefs.getInstance(getContext()).getSESSIONID());

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
                                    counter++;
//                                    database.child(SharePrefs.getInstance(getContext()).getSESSIONID()).child("Downloads").child(String.valueOf(counter)).setValue(data1);
//                                    database.child(SharePrefs.getInstance(getContext()).getSESSIONID()).child("Counter").setValue(counter);
//                                    database.child(SharePrefs.getInstance(getContext()).getSESSIONID()).child(SESSIONID).setValue(SharePrefs.getInstance(getContext()).getSESSIONID());
                                }
                                adapter=new LinkAdapter(getContext(),media);
                                binding.whatsappRecycler.setHasFixedSize(false);
                                binding.whatsappRecycler.setAdapter(adapter);
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
                                            counter++;
//                                            database.child(SharePrefs.getInstance(getContext()).getSESSIONID()).child("Downloads").child(String.valueOf(counter)).setValue(data1);
//                                            database.child(SharePrefs.getInstance(getContext()).getSESSIONID()).child("Counter").setValue(counter);
//                                            database.child(SharePrefs.getInstance(getContext()).getSESSIONID()).child(SESSIONID).setValue(SharePrefs.getInstance(getContext()).getSESSIONID());
                                        }
                                    }
                                    adapter=new LinkAdapter(getContext(),media);
                                    binding.whatsappRecycler.setHasFixedSize(false);
                                    binding.whatsappRecycler.setAdapter(adapter);
                                } catch (JSONException jsonException) {
                                    try {
                                        finalImageUrl[0]= Obj2.getString("display_url");
                                        if(finalImageUrl[0]!=null){
                                            MediaData data1= new MediaData(finalImageUrl[0],"graphImage");
                                            media.add(data1);
                                            counter++;
//                                            database.child(SharePrefs.getInstance(getContext()).getSESSIONID()).child("Downloads").child(String.valueOf(counter)).setValue(data1);
//                                            database.child(SharePrefs.getInstance(getContext()).getSESSIONID()).child("Counter").setValue(counter);
//                                            database.child(SharePrefs.getInstance(getContext()).getSESSIONID()).child(SESSIONID).setValue(SharePrefs.getInstance(getContext()).getSESSIONID());
                                        }
                                        adapter=new LinkAdapter(getContext(),media);
                                        binding.whatsappRecycler.setHasFixedSize(false);
                                        binding.whatsappRecycler.setAdapter(adapter);
                                    } catch (JSONException exception) {
                                        e.printStackTrace();
                                    }
                                }
                                adapter=new LinkAdapter(getContext(),media);
                                binding.whatsappRecycler.setHasFixedSize(false);
                                binding.whatsappRecycler.setAdapter(adapter);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VideoURLErrors", "Something went wrong" + error);
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
            }
        });

        binding.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InstaVideo.downloadVideo(getContext(),binding.instaUrl.getText().toString());
                }catch (Exception e){
                    InstaVideo.downloadImage(getContext(),binding.instaUrl.getText().toString());
                }
            }
        });
        return binding.getRoot();
    }
    private static final int sColumnWidth = 120; // assume cell width of 120dp
    private void calculateSize() {
        int spanCount = (int) Math.floor(binding.whatsappRecycler.getWidth() / convertDPToPixels(sColumnWidth));
        ((GridLayoutManager) binding.whatsappRecycler.getLayoutManager()).setSpanCount(spanCount);
    }
    private float convertDPToPixels(int dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
        return dp * logicalDensity;
    }
}