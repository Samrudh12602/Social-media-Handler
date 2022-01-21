package samrudhdhaimodkar.example.samsvideodownloader.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;
import java.util.ArrayList;

import samrudhdhaimodkar.example.samsvideodownloader.R;
import samrudhdhaimodkar.example.samsvideodownloader.SharePrefs;
import samrudhdhaimodkar.example.samsvideodownloader.adapter.UsersAdapter;
import samrudhdhaimodkar.example.samsvideodownloader.databinding.FragmentSearchBinding;
import samrudhdhaimodkar.example.samsvideodownloader.model.UserData;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    final String[] finalVideoUrl = new String[1];
    final String[] finalImageUrl = new String[1];
    private String searchresult;
    private ArrayList<UserData> user;
    private UsersAdapter adapter;
    private String name,username,userid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_search,container,false);
        user=new ArrayList<>();
        binding.searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchresult=editable.toString();
                binding.swipeLayout.setRefreshing(true);
                Handler h= new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fetchUserData();
                        binding.swipeLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchUserData();
                binding.swipeLayout.setRefreshing(false);
            }
        });
        return binding.getRoot();
    }
    private void fetchUserData(){
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        String apiUrl="https://www.instagram.com/web/search/topsearch/?context=blended&query="+searchresult;
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
                        JSONArray array = response.getJSONArray("users");
                        user.clear();
                        for (int i = 0; i < array.length(); i++) {
                            UserData object = new UserData();
                            object.setProfilepicUrl(array.getJSONObject(i).getJSONObject("user").get("profile_pic_url").toString());
                            object.setName(array.getJSONObject(i).getJSONObject("user").get("full_name").toString());
                            object.setUsername(array.getJSONObject(i).getJSONObject("user").get("username").toString());
                            object.setId(array.getJSONObject(i).getJSONObject("user").get("pk").toString());
                            user.add(object);
                        }
                    } catch (Exception e42) {
                        System.out.println(e42);
                    }
                    adapter= new UsersAdapter(getContext(),user);
                    binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerview.setHasFixedSize(true);
                    binding.recyclerview.setAdapter(adapter);
                },Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
    }
}