package com.example.girish.ayanasample;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 
 * @author Abraham
 *
 */
public class PublishingAsyncTask extends AsyncTask<Void,Void,Boolean>{

	HttpClient hc;
	HttpPost hp;
	HttpResponse r;
	ProgressDialog pd;
	Bundle b;
	Message msg;
    CoolRunnable mCoolRunnable;
	public PublishingAsyncTask(String function,HttpClient hc, HttpPost hp, CoolRunnable runnable, ProgressDialog pd)
	{
		super();
		this.hc=hc;
		this.hp=hp;
		this.pd=pd;
		this.b = new Bundle();
		b.putString("function", function);
        mCoolRunnable = runnable;
	}
	@Override
	protected void onPreExecute()
	{
		if(pd!=null)
			pd.show();
	}
	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			r = hc.execute(hp);
			//b.putString("json", EntityUtils.toString(r.getEntity()));
			return true;
		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	@Override
	protected void onPostExecute(Boolean result)
	{
		if(pd!=null)
		pd.dismiss();
        try {
            mCoolRunnable.run(EntityUtils.toString(r.getEntity()));
        }
        catch(IOException e)
        {

        }
	}

    static abstract interface CoolRunnable extends Runnable {

        public void run(String content);
    }
}

