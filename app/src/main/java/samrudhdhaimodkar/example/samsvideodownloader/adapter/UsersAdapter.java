package samrudhdhaimodkar.example.samsvideodownloader.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import samrudhdhaimodkar.example.samsvideodownloader.R;
import samrudhdhaimodkar.example.samsvideodownloader.SelectedProfile;
import samrudhdhaimodkar.example.samsvideodownloader.model.UserData;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserData> usersList = new ArrayList<>();

    public UsersAdapter(Context context, ArrayList<UserData> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profiles_item,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserData data= usersList.get(position);
        holder.name.setText(data.getName());
        holder.username.setText("@"+data.getUsername());
        Glide.with(context).load(data.getProfilepicUrl()).into(holder.profileimage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID=data.getId();
                String username=data.getUsername();
                Intent intent=new Intent(context, SelectedProfile.class);
                intent.putExtra("uid",ID);
                intent.putExtra("username",username);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList==null? 0:usersList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,username;
        private CircleImageView profileimage;
        public ViewHolder(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.name);
            username= itemView.findViewById(R.id.username);
            profileimage= itemView.findViewById(R.id.profilePic);
        }
    }
}
