package samrudhdhaimodkar.example.samsvideodownloader.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import samrudhdhaimodkar.example.samsvideodownloader.FullScreenImage;
import samrudhdhaimodkar.example.samsvideodownloader.FullScreenVideo;
import samrudhdhaimodkar.example.samsvideodownloader.R;
import samrudhdhaimodkar.example.samsvideodownloader.Util;
import samrudhdhaimodkar.example.samsvideodownloader.databinding.WhatsappItemLayoutBinding;
import samrudhdhaimodkar.example.samsvideodownloader.model.WhatsappStatusModel;

public class WhatsappAdapter extends RecyclerView.Adapter<WhatsappAdapter.ViewHolder> {
   private ArrayList<WhatsappStatusModel> list;
   private Context context;
   private LayoutInflater inflater;
   private String saveFilePath= Util.RootDirectoryWhatsapp+"/";

    public WhatsappAdapter(ArrayList<WhatsappStatusModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(inflater==null){
            inflater=LayoutInflater.from(parent.getContext());
        }
        return new ViewHolder(DataBindingUtil.inflate(inflater, R.layout.whatsapp_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WhatsappAdapter.ViewHolder holder, int position) {
        WhatsappStatusModel item= list.get(position);
        if(item.getUri().toString().endsWith(".mp4")){
            holder.binding.playBtn.setVisibility(View.VISIBLE);
        }else{
            holder.binding.playBtn.setVisibility(View.GONE);
        }
        Glide.with(context).load(item.getPath()).into(holder.binding.statusImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!item.getUri().toString().endsWith(".mp4")){
                    Intent i= new Intent(context, FullScreenImage.class);
                    i.setData(item.getUri());
                    context.startActivity(i);
                }else{
                    Intent i= new Intent(context, FullScreenVideo.class);
                    i.setData(item.getUri());
                    context.startActivity(i);
                }
            }
        });
        holder.binding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.createFolder();
                final String path=item.getPath();
                final File file=new File(path);
                File destinationFile= new File(saveFilePath);

                try {
                    FileUtils.copyFileToDirectory(file,destinationFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context,"Saved to:"+ saveFilePath,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        WhatsappItemLayoutBinding binding;
        public ViewHolder(WhatsappItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
