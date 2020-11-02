package com.tgapp.mytranscribeglass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    static TextView mainText;
    static TextView minuteResult;
    Intent funIntent;
    Intent resultIntent;
    private final static String TAG = BluetoothLeService.class.getSimpleName();
    public static final int REQUEST_CODE = 2;


    //registering system broadcast within Activity context
    BroadcastReceiver br = new BroadcastInBuilt();

    // ------ custom broadcast
    BroadcastReceiver cbr = new CustomBroadcast();
    public static final String CUSTOM_ACTION = "com.mylollipop.rita.MyCustomReceiver.call";
    IntentFilter filterCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        minuteResult = findViewById(R.id.minute_result);
        resultIntent = new Intent(this, FunActivity.class);
        mainText = findViewById(R.id.main_text);

        // ------ system broadcast
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        this.registerReceiver(br, filter);

        // ------ custom broadcast
        filterCustom = new IntentFilter();
        filterCustom.addAction(CUSTOM_ACTION);
        filterCustom.addCategory(Intent.CATEGORY_DEFAULT);
        this.registerReceiver(cbr, filterCustom);
        Toast.makeText(this, "onCreate done", Toast.LENGTH_SHORT).show();
        Log.i("MainActivity", "onCreate");
    }

    public void startBluetoothService(View view) {
        Intent bleIntent = new Intent(this, BluetoothLeService.class);
        startService(bleIntent);
    }
    public void stopBluetoothService(View view) {
        Intent bleIntent = new Intent(this, BluetoothLeService.class);
        stopService(bleIntent);
    }
    public void callCustomBroadcast(View view) {
        Intent customIntent = new Intent();
        customIntent.setAction(CUSTOM_ACTION);
        sendBroadcast(customIntent);
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Toast.makeText(this, "onStart done", Toast.LENGTH_SHORT).show();
//        Log.i("MainActivity", "onStart");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(this, "onResume done", Toast.LENGTH_SHORT).show();
//        Log.i("MainActivity", "onResume");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Toast.makeText(this, "onPause done", Toast.LENGTH_SHORT).show();
//        Log.i("MainActivity", "onPause");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Toast.makeText(this, "onStop done", Toast.LENGTH_SHORT).show();
//        Log.i("MainActivity", "onStop");
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Toast.makeText(this, "onRestart done", Toast.LENGTH_SHORT).show();
//        Log.i("MainActivity", "onRestart");
//    }
//
    public void goToImplicitAction(View view) {
        Intent impIntent = new Intent(this, ImplicitIntent.class);
        startActivity(impIntent);
        Log.i(TAG, "User went to ImplicitIntent action");
    }
    public void goToFunActivity(View v) {
        funIntent = new Intent(this, FunActivity.class);
        startActivityForResult(funIntent, REQUEST_CODE);
        Log.i(TAG, "Button clicked");
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE) { //in case there are multiple codes ~ multiple requests
//            if(resultCode == RESULT_OK) {
//                String replyMessage = data.getStringExtra(FunActivity.EXTRA_REPLY_KEY);
//                minuteResult.setVisibility(View.VISIBLE);
//                minuteResult.setText(replyMessage);
//            }
//        }
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        boolean boo = isFinishing();
        String b= Boolean.toString(boo);
        Toast.makeText(this, b, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "onDestroy done", Toast.LENGTH_SHORT).show();

        //unregister in onDestroy bc register in onCreate
        unregisterReceiver(br);
        unregisterReceiver(cbr);

        Log.i("MainActivity", "onDestroy");
    }



}