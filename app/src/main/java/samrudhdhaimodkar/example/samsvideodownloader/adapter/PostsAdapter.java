package samrudhdhaimodkar.example.samsvideodownloader.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import samrudhdhaimodkar.example.samsvideodownloader.Activity.PostImageActivity;
import samrudhdhaimodkar.example.samsvideodownloader.R;
import samrudhdhaimodkar.example.samsvideodownloader.Util;
import samrudhdhaimodkar.example.samsvideodownloader.model.PostData;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PostData> posts= new ArrayList<>();


    public PostsAdapter(Context context, ArrayList<PostData> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_posts_layout,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostData data=posts.get(position);
        Glide.with(context).load(data.getPostUrl()).into(holder.post);
        if(data.getPost_type().equalsIgnoreCase("graphVideo")){
            holder.play.setVisibility(View.VISIBLE);
        }
        else if(data.getPost_type().equalsIgnoreCase("graphsidecar")){
            holder.side.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(context, PostImageActivity.class);
                i.putExtra("shortcode",data.getShortCode());
                context.startActivity(i);
            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.download(data.getPostUrl(), Util.RootDirectoryInstagram,context,System.currentTimeMillis()+".jpg");
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView post,download,play,side;
        public ViewHolder(View itemView) {
            super(itemView);
            post=itemView.findViewById(R.id.postImage);
            download=itemView.findViewById(R.id.download);
            play=itemView.findViewById(R.id.playBtn);
            side=itemView.findViewById(R.id.multiple);
        }
    }
}
