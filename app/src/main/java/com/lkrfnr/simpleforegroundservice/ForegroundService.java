package com.lkrfnr.simpleforegroundservice;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class ForegroundService extends Service {

    public static final String FOREGROUND_SERVICE_INTENT_EXTRAS_MESSAGE_KEY = "Foreground_Service_Current_Message";

    public final String FOREGROUND_SERVICE_TAG= "Foreground Service";
    public final String FOREGROUND_SERVICE_CHANNEL_ID = "Foreground_Service_1";
    public final int FOREGROUND_SERVICE_REQUEST_CODE = 1001;
    public final int FOREGROUND_SERVICE_CODE = 1002;

    /**
     * onCreate() will run just once at first creation time of the foreground service.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(FOREGROUND_SERVICE_TAG,"Service onCreate !");
        Toast.makeText(this,"Foreground Service Created !",Toast.LENGTH_LONG).show();
    }

    /*
    *
     */

    /**
     * @param intent This intent has sent from activity that has been started this service
     * @param flags Intent flags
     * @param startId Intent id
     * @return START_STICKY,START_NOT_STICKY or START_REDELIVER_INTENT
     * @see <a href="https://developer.android.com/reference/android/app/Service"> Services </a>
     * for return values explanations.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(FOREGROUND_SERVICE_TAG,"Service onStartCommand !");

        String message =  intent.getStringExtra(MainActivity.INTENT_EXTRAS_MESSAGE_KEY);

        /* This intent will start when clicked the notification that we have been created
           and attached the service in below.
         */
        Intent mIntent =  new Intent(this,MainActivity.class);

        /* We can send a message to the our activity with intent extras either our app killed or not
           because our foreground service will run until user os or user kill it so if user click the
           service our main activity will start and we will understand the our service situation with this message.
          */
        mIntent.putExtra(FOREGROUND_SERVICE_INTENT_EXTRAS_MESSAGE_KEY,message);
        // This flag will clear all tasks in the activity you can change the flag with what you need.
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent startMainActivityPendingIntent =  PendingIntent.getActivity(getApplicationContext(),
                FOREGROUND_SERVICE_REQUEST_CODE,
                mIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        /* If the current device's android version higher or equal the ANDROID_O then we have to set a notification channel
           to create a notification also you have to set the FOREGROUND_SERVICE permission in your manifest file to
           start foreground service on ANDROID_P and higher versions.
          */

        Notification notification =  new NotificationCompat.Builder(this,createChannel())
                    .setContentTitle("ForegroundService")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(startMainActivityPendingIntent)
                    .setOngoing(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .build();

        //notification.notify();

        startForeground(FOREGROUND_SERVICE_CODE,notification);

        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(FOREGROUND_SERVICE_TAG,"Service onDestroy !");
        Toast.makeText(this,"Foreground Service Destroyed !",Toast.LENGTH_LONG).show();
    }


    @TargetApi(Build.VERSION_CODES.O)
    private String createChannel(){
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        NotificationChannel mChannel = new NotificationChannel(FOREGROUND_SERVICE_CHANNEL_ID,
                "Foreground_Service",
                // you can set the notification
                NotificationManager.IMPORTANCE_HIGH);

        // If you want to set notification sound you can set the sound uri and also audio attributes.

        mChannel.setSound(null,null);

        mNotificationManager.createNotificationChannel(mChannel);

        return FOREGROUND_SERVICE_CHANNEL_ID;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
