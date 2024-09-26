package com.funshow.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // 定義請求通知權限的請求碼
    private static final int REQUEST_NOTIFICATION_PERMISSION = 200;

    private String CHANNEL_ID = "Coder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            // 檢查並請求通知權限
            checkNotificationPermission();

            /**初始化介面控件與點擊事件*/
            Button btDefault, btCustom;
            btDefault = findViewById(R.id.button_DefaultNotification);
            btCustom = findViewById(R.id.button_CustomNotification);
            btDefault.setOnClickListener(onDefaultClick);
            btCustom.setOnClickListener(onCustomClick);
        } catch (Exception e) {
            Log.i("FunshowError" , "e = " + e);
        }
    }

    /**點選"系統預設通知"*/
    private View.OnClickListener onDefaultClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                /**建置通知欄位的內容*/
                NotificationCompat.Builder builder
                        = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_accessible_forward_24)
                        .setContentTitle("哈囉你好！")
                        .setContentText("跟你打個招呼啊～")
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE);

                /**發出通知*/
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
                Log.i("ActivityLog" , "POST_NOTIFICATIONS = " + ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                notificationManagerCompat.notify(1, builder.build());
            } catch (Exception e) {
                Log.i("FunshowError" , "e = " + e);
            }
        }
    };

    /**點選"客製化通知"*/
    private View.OnClickListener onCustomClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                /**建立要嵌入在通知裡的介面*/
                RemoteViews view = new RemoteViews(getPackageName(), R.layout.custom_notification);

                /**初始化Intent，攔截點擊事件*/
                Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);

                /**設置通知內"Hi"這個按鈕的點擊事件(以Intent的Action傳送標籤，標籤為Hi)*/
                intent.setAction("Hi");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                /**設置通知內"Close"這個按鈕的點擊事件(以Intent的Action傳送標籤，標籤為Close)*/
                intent.setAction("Close");
                PendingIntent close = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                /**設置通知內的控件要做的事*/
                /*設置標題*/
                view.setTextViewText(R.id.textView_Title, "哈囉你好！");
                /*設置圖片*/
                view.setImageViewResource(R.id.imageView_Icon, R.drawable.ic_baseline_directions_bike_24);
                /*設置"Hi"按鈕點擊事件(綁pendingIntent)*/
                view.setOnClickPendingIntent(R.id.button_Noti_Hi, pendingIntent);
                /*設置"Close"按鈕點擊事件(綁close)*/
                view.setOnClickPendingIntent(R.id.button_Noti_Close, close);

                /**建置通知欄位的內容*/
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_directions_bike_24)
                        .setContent(view)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE);
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
                Log.i("ActivityLog" , "POST_NOTIFICATIONS = " + ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS));
                /**發出通知*/
                if (ActivityCompat.checkSelfPermission(MainActivity.this , android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                notificationManagerCompat.notify(1, builder.build());
            } catch (Exception e) {
                Log.i("FunshowError" , "e = " + e);
            }
        }
    };

    // 檢查並請求通知權限
    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 (API 33) or higher
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // 沒有權限，請求權限
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS} , REQUEST_NOTIFICATION_PERMISSION);
            } else {
                // 已經有權限
                Toast.makeText(this, "Notification permission already granted", Toast.LENGTH_SHORT).show();
                /**檢查手機版本是否支援通知；若支援則新增"頻道"*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "DemoCode", NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                }
            }
        } else {
            // Android 13 以下不需要請求通知權限
            Toast.makeText(this, "Notification permission not required for this version", Toast.LENGTH_SHORT).show();
        }
    }

    // 處理權限請求結果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 使用者授予通知權限
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // 使用者拒絕通知權限
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}