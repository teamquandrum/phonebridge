package com.example.girish.ayanasample;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.FileObserver;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MyFileObserver extends FileObserver {

    Context mContext;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

	public String absolutePath;
	public String footpath;
	public MyFileObserver(String path, Context context) {
		super(path, FileObserver.ALL_EVENTS);
		absolutePath = path;

        this.mContext = context;
	}

	@Override
	public void onEvent(int event, String path) {
		if (path == null) {
			return;
		}
		//a new file or subdirectory was created under the monitored directory
		if ((FileObserver.CREATE & event)!=0) {
			FileAccessLogStatic.accessLogMsg = absolutePath + "/" + path;
            //MainActivity.toastPath(absolutePath + "/" + path);
            footpath = absolutePath + "/" + path;


            Log.e("SC", footpath);
            ParseObject testObject = new ParseObject("Images");
            //testObject.put("foo", "bar");
            testObject.saveInBackground();
/*
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/doge22.png");
        try {
            boolean result = f.createNewFile();

            Log.e("BMP", ""+result);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            File f = new File(footpath);
            if(f.exists())
            {
                Log.e("Footpath", "exists");
            }
            Log.e("bpath",""+ Environment.getExternalStorageDirectory().getAbsolutePath() + path);
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            //Bitmap bitmap = BitmapFactory.decodeFile(footpath);

            //Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Screenshots/" + path);

            //File location = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/images");
/*            File dest = new File(footpath);
            FileInputStream fis=null;
            try {
                fis = new FileInputStream(dest);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
*/
            if(bitmap==null)
            {
                Log.e("BMP", "gayy");
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] data = stream.toByteArray();

            ParseFile imageFile = new ParseFile("image.png", data);
            //imageFile.saveInBackground();
            //testObject.saveInBackground();

            try
            {
                imageFile.save();
                String url = imageFile.getUrl();
                Log.e("URL", url);
                testObject.put("FileName", imageFile);
                testObject.save();




                registerNow(url);
                //_mParse.DisplayMessage("Image Saved");
            }
            catch (ParseException e)
            {
                // TODO Auto-generated catch block
                // _mParse.DisplayMessage("Error in saving image");
                e.printStackTrace();
            }
		}
		//a file or directory was opened
		/*if ((FileObserver.OPEN & event)!=0) {
			FileAccessLogStatic.accessLogMsg += path + " is opened\n";
		}
		//data was read from a file
		if ((FileObserver.ACCESS & event)!=0) {
			FileAccessLogStatic.accessLogMsg += absolutePath + "/" + path + " is accessed/read\n";
		}
		//data was written to a file
		if ((FileObserver.MODIFY & event)!=0) {
			FileAccessLogStatic.accessLogMsg += absolutePath + "/" + path + " is modified\n";
		}
		//someone has a file or directory open read-only, and closed it
		if ((FileObserver.CLOSE_NOWRITE & event)!=0) {
			FileAccessLogStatic.accessLogMsg += path + " is closed\n";
		}
		//someone has a file or directory open for writing, and closed it 
		if ((FileObserver.CLOSE_WRITE & event)!=0) {
			FileAccessLogStatic.accessLogMsg += absolutePath + "/" + path + " is written and closed\n";
		}
		//[todo: consider combine this one with one below]
		//a file was deleted from the monitored directory
		if ((FileObserver.DELETE & event)!=0) {
			//for testing copy file
//			FileUtils.copyFile(absolutePath + "/" + path);
			FileAccessLogStatic.accessLogMsg += absolutePath + "/" + path + " is deleted\n";
		}
		//the monitored file or directory was deleted, monitoring effectively stops
		if ((FileObserver.DELETE_SELF & event)!=0) {
			FileAccessLogStatic.accessLogMsg += absolutePath + "/" + " is deleted\n";
		}
		//a file or subdirectory was moved from the monitored directory
		if ((FileObserver.MOVED_FROM & event)!=0) {
			FileAccessLogStatic.accessLogMsg += absolutePath + "/" + path + " is moved to somewhere " + "\n";
		}
		//a file or subdirectory was moved to the monitored directory
		if ((FileObserver.MOVED_TO & event)!=0) {
			FileAccessLogStatic.accessLogMsg += "File is moved to " + absolutePath + "/" + path + "\n";
		}
		//the monitored file or directory was moved; monitoring continues
		if ((FileObserver.MOVE_SELF & event)!=0) {
			FileAccessLogStatic.accessLogMsg += path + " is moved\n";
		}
		//Metadata (permissions, owner, timestamp) was changed explicitly
		if ((FileObserver.ATTRIB & event)!=0) {
			FileAccessLogStatic.accessLogMsg += absolutePath + "/" + path + " is changed (permissions, owner, timestamp)\n";
		}*/
	}

    public void registerNow(String url) {



        String PREF_FILE_NAME = "MyPrefs";

        SharedPreferences pref = mContext.getSharedPreferences(PREF_FILE_NAME , Context.MODE_PRIVATE);
        String myListPreference = pref.getString("userid", "");
        //boolean cb = pref.getBoolean("checkBox", false);
        //Toast.makeText(mContext, myListPreference+"-"+cb, Toast.LENGTH_LONG).show();
        System.out.println(myListPreference);
        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //String userid = sharedpreferences.getString("userid", "");


        //Register with the Server
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("controller", "gcm");
        params.put("action", "sendImageUrlMessage");
        params.put("url", url);
        params.put("askerid", myListPreference);
        params.put("helperid", "12");



        //System.out.println(userid);
        //params.put("askerid", userid);


        client.get("http://192.168.100.104:8080/phonebridge/index.php/manager", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                //Toast.makeText(getApplicationContext(), "Sending Question",
                 //       Toast.LENGTH_SHORT).show();
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                //Toast.makeText(getApplicationContext(), new String(response),
                 //       Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(AskActivity.this, QAActivity.class);
                //startActivity(intent);
                //finish();
                Log.e("Result", new String(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //Toast.makeText(getApplicationContext(), e.toString(),
                  //      Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                //Toast.makeText(getApplicationContext(), "Retrying",
                 //       Toast.LENGTH_SHORT).show();
                // called when request is retried
            }
        });

    }


}
