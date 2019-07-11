package com.lkrfnr.simpleforegroundservice;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Objects;

public class ScreenBroadcastReceiver extends BroadcastReceiver {

    /*
     This receiver report us when device screen on and
     we start our service again and can show our notification
     on lock screen.
      */

    private final String TAG = "ScreenBroadcastReceiver";

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent serviceIntent = new Intent(context, ForegroundService.class);
        serviceIntent.putExtra(MainActivity.INTENT_EXTRAS_MESSAGE_KEY,"Lock Screen");

        // You have to add this action to broadcast receiver block in manifest file, this is important !

        if (Objects.requireNonNull(intent.getAction()).equalsIgnoreCase(Intent.ACTION_SCREEN_ON)){

            Log.e(TAG," SCREEN ON ");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(serviceIntent);
            else
                context.startService(serviceIntent);
        }

    }

}
