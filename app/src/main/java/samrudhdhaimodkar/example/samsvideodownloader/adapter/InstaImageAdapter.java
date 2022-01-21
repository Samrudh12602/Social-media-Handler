package samrudhdhaimodkar.example.samsvideodownloader.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import samrudhdhaimodkar.example.samsvideodownloader.model.MediaData;

public class InstaImageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<MediaData> media = new ArrayList<>();

    public InstaImageAdapter(Context context,ArrayList<MediaData> media) {
        this.context = context;
        this.media=media;
    }

    @Override
    public int getCount() {
        return media.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//            VideoView vv = new VideoView(context);
            ImageView imageView= new ImageView(context);
//            ImageView playBtn = new ImageView(context);
//            playBtn.setImageResource(R.drawable.ic_play);
//            if(media.get(position).getMediaType().equalsIgnoreCase("graphVideo")) {
//                imageView.setVisibility(View.GONE);
//                Uri videoUri=Uri.parse(media.get(position).getMediaUrl());
//                vv.setVideoURI(videoUri);
//                playBtn.setOnClickListener(new View.OnClickListener() {
//                                               @Override
//                                               public void onClick(View v) {
//                                                   playBtn.setVisibility(View.GONE);
//                                                   MediaController mediaController = new MediaController(context);
//                                                   vv.setMediaController(mediaController);
//                                                   mediaController.setAnchorView(vv);
//                                                   mediaController.show();
//                                                   vv.start();
//                                               }
//                                           });
//                return vv;
//            }else {
//                vv.setVisibility(View.GONE);
//                playBtn.setVisibility(View.GONE);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(context).load(Uri.parse(media.get(position).getMediaUrl())).into(imageView);
                container.addView(imageView, 0);
                return imageView;
//            }
        }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
