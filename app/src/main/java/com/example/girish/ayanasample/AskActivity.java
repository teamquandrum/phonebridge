package com.example.girish.ayanasample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;


@SuppressWarnings("ALL")
public class AskActivity extends ActionBarActivity {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ask, menu);
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



    public void registerNow(View view) {

        EditText titleText = (EditText) findViewById(R.id.editText);
        EditText contentText = (EditText) findViewById(R.id.editText2);
        String title = titleText.getText().toString();
        String content = contentText.getText().toString();


        //Register with the Server
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("controller", "question");
        params.put("action", "newQuestion");
        params.put("title", title);
        params.put("content", content);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userid = sharedpreferences.getString("userid", "");

        System.out.println(userid);
        params.put("askerid", userid);


        client.get("http://192.168.100.104:8080/phonebridge/index.php/manager", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                Toast.makeText(getApplicationContext(), "Sending Question",
                        Toast.LENGTH_SHORT).show();
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Toast.makeText(getApplicationContext(), new String(response),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AskActivity.this, QAActivity.class);
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
}

