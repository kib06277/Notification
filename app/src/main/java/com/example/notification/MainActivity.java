package com.example.notification;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button button;
    boolean isOpened;
    String channelId = "channel1";
    String channelName = "name";
    NotificationManager Manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            button = findViewById(R.id.btn_Notify);
            button.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    isOpened = Manager.areNotificationsEnabled();
                    if(!isOpened){
                        Toast.makeText(MainActivity.this,"請開啟通知權限", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, MainActivity.this.getPackageName());
                        intent.putExtra(Settings.EXTRA_CHANNEL_ID, MainActivity.this.getApplicationInfo().uid);
                        MainActivity.this.startActivity(intent);
                    }

                    NotificationChannel channel = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                        Manager.createNotificationChannel(channel);
                        Notification.Builder builder = new Notification.Builder(MainActivity.this,channelId);
                        builder.setSmallIcon(R.mipmap.ic_launcher)
                                .setChannelId(channelId)
                                .setContentTitle("標題")
                                .setContentText("內容").build();

                        Manager.createNotificationChannel(channel);
                        Manager.notify(1, builder.build());
                    }
                }
            });
        } catch (Exception e){
            Log.i("Error" , "e = " + e);
        }
    }
}