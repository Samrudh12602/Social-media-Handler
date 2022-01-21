package samrudhdhaimodkar.example.samsvideodownloader.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import samrudhdhaimodkar.example.samsvideodownloader.FullScreenImage;
import samrudhdhaimodkar.example.samsvideodownloader.FullScreenVideo;
import samrudhdhaimodkar.example.samsvideodownloader.R;
import samrudhdhaimodkar.example.samsvideodownloader.Util;
import samrudhdhaimodkar.example.samsvideodownloader.model.MediaData;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MediaData> media= new ArrayList<>();


    public LinkAdapter(Context context, ArrayList<MediaData> media) {
        this.context = context;
        this.media = media;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.whatsapp_item_layout,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MediaData data=media.get(position);
        Glide.with(context).load(data.getMediaUrl()).into(holder.image);
        if(data.getMediaType().equalsIgnoreCase("graphVideo")){
            holder.play.setVisibility(View.VISIBLE);
        }
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.getMediaType().equalsIgnoreCase("graphImage"))
                {
                    Util.download(data.getMediaUrl(), Util.RootDirectoryInstagram,context,System.currentTimeMillis()+".jpg");
                }
                else {
                    Util.download(data.getMediaUrl(), Util.RootDirectoryInstagram, context, System.currentTimeMillis() + ".mp4");
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.getMediaType().equalsIgnoreCase("graphImage"))
                {
                    Intent i= new Intent(context, FullScreenImage.class);
                    i.setData(Uri.parse(data.getMediaUrl()));
                    i.putExtra("mode","Insta");
                    context.startActivity(i);
                }
                else {
                    Intent i= new Intent(context, FullScreenVideo.class);
                    i.setData(Uri.parse(data.getMediaUrl()));
                    i.putExtra("mode","Insta");
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return media.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image,play;
        private MaterialButton download;
        public ViewHolder(View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.statusImage);
            download=itemView.findViewById(R.id.download);
            play=itemView.findViewById(R.id.playBtn);
        }
    }
}
