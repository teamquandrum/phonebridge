package com.example.girish.ayanasample;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class QAActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);

        Button but = (Button) findViewById(R.id.button2);

        but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub


                Intent in = new Intent(getApplicationContext(),
                        AskActivity.class);

                //in.putExtra(ID_EXTRA,n);
                startActivity(in);

            }
        });

        Button but2 = (Button) findViewById(R.id.button3);

        but2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub


                Intent in = new Intent(getApplicationContext(),
                        AnswerActivity.class);

                //in.putExtra(ID_EXTRA,n);
                startActivity(in);

            }
        });
/*
        Button but3 = (Button) findViewById(R.id.button5);

        but3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub


                Intent in = new Intent(getApplicationContext(),
                        RUReadyActivity.class);

                //in.putExtra(ID_EXTRA,n);
                startActivity(in);

            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qa, menu);
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
}
