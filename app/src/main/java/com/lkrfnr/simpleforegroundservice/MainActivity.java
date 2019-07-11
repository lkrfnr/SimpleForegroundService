package com.lkrfnr.simpleforegroundservice;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static String INTENT_EXTRAS_MESSAGE_KEY = "message";

    private final String MAIN_ACTIVITY_LOG_TAG = "MainActivity";

    EditText editText;
    Button startService,stopService;
    TextView serviceMessage=null;
    String message=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService = findViewById(R.id.start_service);
        stopService = findViewById(R.id.stop_service);
        editText = findViewById(R.id.edit_text);
        serviceMessage = findViewById(R.id.service_message);

        getIntentExtras();
        registerBroadcastReceiver();

        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
    }


    private void registerBroadcastReceiver(){
        try{
            BroadcastReceiver screenReceiver =  new ScreenBroadcastReceiver();
            IntentFilter screenFilter =  new IntentFilter();
            screenFilter.addAction(Intent.ACTION_SCREEN_ON);
            registerReceiver(screenReceiver,screenFilter);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(MAIN_ACTIVITY_LOG_TAG," ScreenBroadcastReceiver creating Error !");
        }
    }

    private void getIntentExtras(){
        try{
            message = getIntent().getStringExtra(ForegroundService.FOREGROUND_SERVICE_INTENT_EXTRAS_MESSAGE_KEY);
            serviceMessage.setText(message);
            Log.e(MAIN_ACTIVITY_LOG_TAG,message);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(MAIN_ACTIVITY_LOG_TAG,"There is no intent extras !");
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this,ForegroundService.class);

        switch (v.getId()){
            case R.id.start_service:

                Log.e(MAIN_ACTIVITY_LOG_TAG,"Start Service button clicked !");

                String message = editText.getText().toString();

                intent.putExtra(INTENT_EXTRAS_MESSAGE_KEY,message);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    startForegroundService(intent);
                    else
                    startService(intent);
                break;

            case R.id.stop_service:

                Log.e(MAIN_ACTIVITY_LOG_TAG,"Start Service button clicked !");

                stopService(intent);

                 break;
        }

    }
}
