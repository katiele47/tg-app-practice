package com.tgapp.mytranscribeglass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class FunActivity extends AppCompatActivity {

    private EditText replyMessage;
    public static final String EXTRA_REPLY_KEY =
            "baby.funny.calculation";
    MyProgressTask m1;
    public TextView mt1;
    private static final String TEXT_STATE = "currentText";
    MyInternetAsync mi1;
    static TextView myInternetText;
    static ImageView myImage;

    ConnectivityManager myConnManager;
    NetworkInfo myInfo;
    MyImageAsync mimg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun);
//        replyMessage = (EditText) findViewById(R.id.number_text);
//        mt1 = findViewById(R.id.mtv1);
        /*
            Connect to the Internet practice
        */
//        myInternetText = findViewById(R.id.myResult);
//        myImage = findViewById(R.id.myImgResult);

        myConnManager= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        myInfo = myConnManager.getActiveNetworkInfo();

        //instance state of MySecondAsync
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

    public void doAsync(View view) {
//        m1 = new MyProgressTask(this);
//        m1.execute();
        mt1.setText("Nothing happened yet");
        new MySecondAsync(mt1).execute();
    }
    /*
        Connect to the Internet practice
    */
    public void doConnectWebpage(View view) {
        mi1 = new MyInternetAsync(this);
        mi1.execute("http://www.google.com");
    }

    public void downloadImage(View view) {
        boolean isNetworkConnect = myInfo.isConnected();
        if (myInfo != null && isNetworkConnect){
            mimg1 = new MyImageAsync(this);
            Toast.makeText(this, "Connected to Network", Toast.LENGTH_SHORT).show();
            mimg1.execute("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/hbz-lucas-bravo-lead-1602873650.jpg");
        } else {
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_LONG).show();
        }
    }
    public class MyImageAsync extends AsyncTask<String, Void, Bitmap>{

        Context ctx;
        public MyImageAsync(Context ct) {
            ctx = ct;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            String s1 = strings[0];
            InputStream in = null;
            HttpURLConnection myConn = null;

            try {
                URL myUrl = new URL(s1);
                myConn = (HttpURLConnection) myUrl.openConnection();
                myConn.setReadTimeout(10000);
                myConn.setConnectTimeout(20000);
                myConn.setRequestMethod("GET");
                myConn.connect();

                in = myConn.getInputStream();
                Bitmap myMap = BitmapFactory.decodeStream(in);
                return myMap;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.disconnect();
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            FunActivity.myImage.setImageBitmap(bitmap);
        }
    }
    public class MyInternetAsync extends AsyncTask<String, Void, String> {

        Context ctx;
        public MyInternetAsync(Context ct){
            ctx = ct;
        }

        @Override
        protected String doInBackground(String... strings) {
            String s1 = strings[0];
            InputStream in = null;
             HttpURLConnection myConn = null;

            try {
                URL myUrl = new URL(s1);
                myConn = (HttpURLConnection) myUrl.openConnection();
                myConn.setReadTimeout(10000);
                myConn.setConnectTimeout(20000);
                myConn.setRequestMethod("GET");
                myConn.connect();

                in = myConn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder st = new StringBuilder(); //format my string
                String line ="";

                while((line = reader.readLine()) != null){
                    st.append(line + " \n");
                }
                if (st.length() == 0){
                    return null;
                }
                reader.close();
                return st.toString();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.disconnect();
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            FunActivity.myInternetText.setText(s);
        }
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