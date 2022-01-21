package samrudhdhaimodkar.example.samsvideodownloader.model;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import samrudhdhaimodkar.example.samsvideodownloader.Activity.MessageActivity;

public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "NotificationListener";
    private static final String WA_PACKAGE = "com.whatsapp";

    @Override
    public void onListenerConnected() {
        Log.i(TAG, "Notification Listener connected");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Intent i = new Intent(this, MessageActivity.class);
        if (!sbn.getPackageName().equals(WA_PACKAGE)) return;

        Notification notification = sbn.getNotification();
        Bundle bundle = notification.extras;

        String from = bundle.getString(NotificationCompat.EXTRA_TITLE);
        String message = bundle.getString(NotificationCompat.EXTRA_TEXT);

        Log.i(TAG, "From: " + from);
        Log.i(TAG, "Message: " + message);
        Bundle bun= new Bundle();
        bun.putString("from",from);
        bun.putString("message",message);
        i.putExtras(bun);
        startActivity(i);
    }
}