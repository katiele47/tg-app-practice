package com.tgapp.mytranscribeglass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView minuteResult;
    Intent funIntent;
    Intent resultIntent;
    private final static String TAG = BluetoothLeService.class.getSimpleName();
    public static final int REQUEST_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        minuteResult = findViewById(R.id.minute_result);
        resultIntent = new Intent(this, FunActivity.class);
        Toast.makeText(this, "onCreate done", Toast.LENGTH_SHORT).show();
        Log.i("MainActivity", "onCreate");
    }
    public void activateGgASR(View v) {
        funIntent = new Intent(this, FunActivity.class);
//        startActivity(funIntent);
        startActivityForResult(funIntent, REQUEST_CODE);
        Log.i(TAG, "Button clicked");
    }
    public void goToImplicitAction(View view) {
        Intent impIntent = new Intent(this, ImplicitIntent.class);
        startActivity(impIntent);
        Log.i(TAG, "User went to ImplicitIntent action");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) { //in case there are multiple codes ~ multiple requests
            if(resultCode == RESULT_OK) {
                String replyMessage = data.getStringExtra(FunActivity.EXTRA_REPLY_KEY);
                minuteResult.setVisibility(View.VISIBLE);
                minuteResult.setText(replyMessage);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart done", Toast.LENGTH_SHORT).show();
        Log.i("MainActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume done", Toast.LENGTH_SHORT).show();
        Log.i("MainActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause done", Toast.LENGTH_SHORT).show();
        Log.i("MainActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop done", Toast.LENGTH_SHORT).show();
        Log.i("MainActivity", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart done", Toast.LENGTH_SHORT).show();
        Log.i("MainActivity", "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boolean boo = isFinishing();
        String b= Boolean.toString(boo);
        Toast.makeText(this, b, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "onDestroy done", Toast.LENGTH_SHORT).show();
        Log.i("MainActivity", "onDestroy");
    }



}