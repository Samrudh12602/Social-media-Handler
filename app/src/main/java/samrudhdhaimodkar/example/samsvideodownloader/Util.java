package samrudhdhaimodkar.example.samsvideodownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class Util {
    public static String RootDirectoryFacebook="/MyStorySaver/FB Downloads/";
    public static String RootDirectoryShareChat="/MyStorySaver/ShareChat Downloads/";
    public static String RootDirectoryInstagram="/MyStorySaver/Insta Downloads/";

    public static File RootDirectoryWhatsapp= new File(Environment.getExternalStorageDirectory()
            +"/Download/MyStorySaver/WhatsappStatus");

    public static void createFolder(){
        if(!RootDirectoryWhatsapp.exists()){
            RootDirectoryWhatsapp.mkdirs();
        }
    }
    public static void download(String downloadPath, String destinationPath, Context context,String fileName){
        Toast.makeText(context,"Downloading Started",Toast.LENGTH_LONG).show();
        Uri uri=Uri.parse(downloadPath);
        DownloadManager.Request request= new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE| DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,destinationPath+fileName);
        ((DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
    }
}
