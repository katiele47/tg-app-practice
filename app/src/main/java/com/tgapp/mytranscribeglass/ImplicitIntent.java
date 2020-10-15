package com.tgapp.mytranscribeglass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ImplicitIntent extends AppCompatActivity {

    private final static String TAG = ImplicitIntent.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implicit_intent);
    }

    public void doSomething(View view) {
        switch(view.getId()) {
            case R.id.imp_btn1:
                Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/reference/android/content/Intent"));
                if (i1.resolveActivity(getPackageManager()) != null) {
                    startActivity(i1);
                }
                else {
                    Log.d(TAG, "Can't handle Open Web!");
                }
                break;
            case R.id.imp_btn2:
                Intent i2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:84393953774"));
                if (i2.resolveActivity(getPackageManager()) != null) {
                    startActivity(i2);
                } else {
                    Log.d(TAG, "Can't handle Dialing!");
                }
                break;
            case R.id.imp_btn3:
                Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse("geo: 40.2010, 77.2003"));
                if (i3.resolveActivity(getPackageManager()) != null) {
                    startActivity(i3);
                } else {
                    Log.d(TAG, "Can't handle Open Map!");
                }
                break;
        }
    }
}