package com.tgapp.mytranscribeglass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ImplicitIntent extends AppCompatActivity {

    private final static String TAG = ImplicitIntent.class.getSimpleName();
    EditText e1,e2;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implicit_intent);
        e1 = (EditText) findViewById(R.id.number_text);
        e2 = (EditText) findViewById(R.id.number_text2);
        t1 = (TextView) findViewById(R.id.number_result);

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

    public void doAdd(View view) {
        int num1 = Integer.parseInt(e1.getText().toString());
        int num2 = Integer.parseInt(e2.getText().toString());
        int sum = num1 + num2;
        t1.setText(sum);
        Log.i(TAG, "Executed last");
    }
    public double add(double i1, double i2) {
        double sum = i1 + i2;
        return sum;
    }
}