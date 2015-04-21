package com.example.girish.ayanasample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


@SuppressWarnings("ALL")
public class RegActivity extends ActionBarActivity {

    GoogleCloudMessaging gcm;
    SharedPreferences sharedpreferences;
    String PROJECT_NUMBER = "522357325933", regid;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void register(View view) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg;
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = regid;

                    Log.i("GCM", msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage() + "\nPlease try once again when Internet Connectivity is Enabled";

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(), msg,
                        Toast.LENGTH_LONG).show();
                registerNow();
            }

            private void registerNow() {
                EditText name = (EditText) findViewById(R.id.name);
                String na = name.getText().toString();
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String imei = telephonyManager.getDeviceId();
                String device = Devices.getDeviceName();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("imei",imei);
                editor.putString("name",na);
                editor.putString("gcm",regid);
                editor.putString("device",device);

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;

                editor.putInt("height",height);
                editor.putInt("width",width);


                editor.apply();
                //Register with the Server
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();

                params.put("controller","user");
                params.put("action","newUser");
                params.put("imei",imei);
                params.put("name",na);
                params.put("gcmregid",regid);
                params.put("model",device);


                client.get("http://192.168.100.104:8080/phonebridge/index.php/manager", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        Toast.makeText(getApplicationContext(), "Attempting registration.",
                                Toast.LENGTH_SHORT).show();
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"
                        Toast.makeText(getApplicationContext(), "Registered successfully!",
                                Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject obj  = new JSONObject(new String(response));
                            obj = obj.getJSONObject("result");

                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString("userid", obj.getString("userid"));
                            editor.apply();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(RegActivity.this, QAActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Toast.makeText(getApplicationContext(), e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        Toast.makeText(getApplicationContext(), "Retrying",
                                Toast.LENGTH_SHORT).show();
                        // called when request is retried
                    }
                });

            }
        }.execute(null, null, null);

    }

}
