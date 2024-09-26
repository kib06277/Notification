package com.funshow.notification;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        /**接收點擊事件*/
        switch (intent.getAction()) {
            case "Hi":
                Toast.makeText(context, "哈囉！", Toast.LENGTH_SHORT).show();
                break;
            case "Close":
                NotificationManager manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
                manager.cancel(1);
                break;
        }
    }
}
