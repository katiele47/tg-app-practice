package com.tgapp.mytranscribeglass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BroadcastInBuilt extends BroadcastReceiver {
    public BroadcastInBuilt(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.mainText.setText("LOw battery is real");
        Toast.makeText(context, "Battery is low. Please charge", Toast.LENGTH_SHORT).show();
        Log.i("my broadcast lollipop", "BATTERY IS LOW HELL YEAH");

    }
}
