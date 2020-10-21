package com.tgapp.mytranscribeglass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Random;

public class FunActivity extends AppCompatActivity {

    private EditText replyMessage;
    public static final String EXTRA_REPLY_KEY =
            "baby.funny.calculation";
    MyProgressTask m1;
    public TextView mt1;
    private static final String TEXT_STATE = "currentText";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun);
        replyMessage = (EditText) findViewById(R.id.number_text);
        mt1 = findViewById(R.id.mtv1);
        if(savedInstanceState!=null) {
            mt1.setText(savedInstanceState.getString(TEXT_STATE));
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state of the TextView
        outState.putString(TEXT_STATE, mt1.getText().toString());

    }


    public void returnCalculationInMins(View view) {
        String message = replyMessage.getText().toString();
        Intent calcIntent = new Intent();
        calcIntent.putExtra(EXTRA_REPLY_KEY, message);
        setResult(RESULT_OK, calcIntent);
        finish();
    }

    public void doAsynch(View view) {
//        m1 = new MyProgressTask(this);
//        m1.execute();
        mt1.setText("Nothing happened yet");
        new MySecondAsync(mt1).execute();
    }
    public class MySecondAsync extends AsyncTask<Void, Void, String> {
        private WeakReference<TextView> mTextView;

        public MySecondAsync(TextView tv) {
            mTextView =  new WeakReference<>(tv);
        }

        @Override
        protected String doInBackground(Void... voids) {
            Random r = new Random();
            int n = r.nextInt(11);

            // Make the task take long enough that we have
            // time to rotate the phone while it is running
            int s = n * 200;

            // Sleep for the random amount of time
            try {
                Thread.sleep(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Awake after sleeping for " + s + " miliseconds";
        }

        @Override
        protected void onPostExecute(String result) {
            mTextView.get().setText(result);//get() because mTextView is a weak reference
        }
    }


    public class MyProgressTask extends AsyncTask<Void, Integer, String> {
    // doesn't really work because of uncertain use of ProgressBar
        Context ctx;
        ProgressBar pb;
        ProgressDialog pd;
        public MyProgressTask(Context ct){
            ctx=ct;
        }
        @Override
        protected void onPreExecute() {
            //show progress dialog
            pb = new ProgressBar(ctx);
            pb.setTooltipText("Downloading");
            pb.setMax(10);
            pb.setBackgroundColor(Color.BLUE);

//            pd = new ProgressDialog(ctx);
//            pd.setTitle("Downloading");
//            pd.setMessage("Please Wait..");
//            pd.setMax(10);
//            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            pd.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.cancel();
//                      cancel(true);
//                }

//            });
        }
        // When updating the progress dialog in real life, you'll use a different mechanism
        // other than the AsyncTask and for loop (the actual time wait will depend on your
        // api/network connection. But updating the UI is the same.
        @Override
        protected String doInBackground(Void... voids) {
            try {
                for(int i=1; i<=10;i++){
                    Thread.sleep(3000);
                    Log.i("Thread", "Execute " + i);
                    publishProgress(i);
                }
                return "Successful";
            } catch (Exception e) {
                Log.i("Exception", e.getMessage());
                return "Failure";
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int myValue = values[0];
//            pd.setProgress(myValue);
            pb.setProgress(myValue);
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
            pb.clearFocus();
        }
    }

}