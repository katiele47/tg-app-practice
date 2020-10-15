package com.tgapp.mytranscribeglass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FunActivity extends AppCompatActivity {

    private EditText replyMessage;
    public static final String EXTRA_REPLY_KEY =
            "baby.funny.calculation";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun);
        replyMessage = (EditText) findViewById(R.id.minutes_text_content);
    }

    public void returnCalculationInMins(View view) {
        String message = replyMessage.getText().toString();
        Intent calcIntent = new Intent();
        calcIntent.putExtra(EXTRA_REPLY_KEY, message);
        setResult(RESULT_OK, calcIntent);
        finish();
    }
}