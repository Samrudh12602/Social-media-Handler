package samrudhdhaimodkar.example.samsvideodownloader.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import samrudhdhaimodkar.example.samsvideodownloader.R;
import samrudhdhaimodkar.example.samsvideodownloader.adapter.WhatsappAdapter;
import samrudhdhaimodkar.example.samsvideodownloader.databinding.FragmentImageBinding;
import samrudhdhaimodkar.example.samsvideodownloader.model.WhatsappStatusModel;


public class ImageFragment extends Fragment {
    private FragmentImageBinding binding;
    private ArrayList<WhatsappStatusModel> list;
    private WhatsappAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding= DataBindingUtil.inflate(inflater,R.layout.fragment_image,container,false);
       list=new ArrayList<>();
       getData();
       binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               list=new ArrayList<>();
               getData();
               binding.refreshLayout.setRefreshing(false);
           }
       });
       return binding.getRoot();
       
    }

    private void getData() {
        WhatsappStatusModel model;

        String targetPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
        File targetDirectory= new File(targetPath);
        File[] allFiles=targetDirectory.listFiles();

        Arrays.sort(allFiles,((o1, o2) -> {
            if(o1.lastModified()>o2.lastModified()){
                return -1;
            }else if(o1.lastModified()<o2.lastModified()){
                return +1;
            }else{
                return 0;
            }
        }));

        for(int i = 0; i< Objects.requireNonNull(allFiles).length; i++){
            File file=allFiles[i];
            if(Uri.fromFile(file).toString().endsWith(".png")||Uri.fromFile(file).toString().endsWith(".jpg")){
                model=new WhatsappStatusModel("whats"+i,Uri.fromFile(file),allFiles[i].getAbsolutePath(),file.getName());
                list.add(model);
            }
        }
        adapter= new WhatsappAdapter(list,getActivity());
        binding.whatsappRecycler.setAdapter(adapter);
    }
}