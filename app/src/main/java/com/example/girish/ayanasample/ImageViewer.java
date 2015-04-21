package com.example.girish.ayanasample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageViewer extends Activity implements View.OnTouchListener {
    ImageView img;
    float[] values;

    Bitmap bitmap;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Toast.makeText(this,url,Toast.LENGTH_SHORT).show();
        Log.e("URL",url);
        setContentView(R.layout.activity_image_view);
        img = (ImageView) findViewById(R.id.girupie);
        img.setImageResource(R.drawable.floating2);
        //UrlImageViewHelper.setUrlDrawable(img, url);
        //new DownloadImageTask(img).execute(url);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);


        //DownloadImageFromPath(url);
        pDialog = new ProgressDialog(this);
        new LoadImage().execute(url);


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
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

    @Override
    public boolean onTouch (View v, MotionEvent event){
        int[] viewCoords = new int[2];
        img.getLocationOnScreen(viewCoords);
        //int touchX = (int) event.getX();
        //int touchY = (int) event.getY();
        //int imageX = touchX - viewCoords[0];
        //int imageY = touchY - viewCoords[1];
        float imageX = (event.getX() - values[2]) / values[0];
        float imageY = (event.getY() - values[5]) / values[4];
        float dx = img.getMeasuredWidth();
        float dy = img.getMeasuredHeight();
        imageX = imageX / dx;
        imageY = imageY / dy;
        imageX = imageX - 0.125f;
        imageY = imageY - 0.125f;
        Toast.makeText(this, "X: " + imageX + "\nY: " + imageY, Toast.LENGTH_SHORT).show();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("controller","gcm");
        params.put("action","sendCoordMessage");
        params.put("askerid",10);
        params.put("helperid",12);
        params.put("x",imageX);
        params.put("y",imageY);


        client.get("http://192.168.100.104:8080/phonebridge/index.php/manager", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                Toast.makeText(getApplicationContext(), "Contacting Server!",
                        Toast.LENGTH_SHORT).show();
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
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

        return false;
    }

    public void DownloadImageFromPath(String path){
        InputStream in =null;
        Bitmap bmp=null;
        //ImageView iv = (ImageView)findViewById(R.id.imgv);
        int responseCode = -1;
        try{

            URL url = new URL(path);//"http://192.xx.xx.xx/mypath/img1.jpg
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true);
            con.connect();
            responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                //download
                in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();
                if(img==null)
                {
                    Log.e("iv", "null");
                }
                if(bmp==null)
                {
                    Log.e("gya", "gay shit");
                }
                img.setImageBitmap(bmp);
            }

        }
        catch(Exception ex){
            Log.e("Exception",ex.toString());
        }
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ImageViewer.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();
        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
                DownloadImageFromPath(args[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image) {
            if(image != null){
                img.setImageBitmap(image);
                pDialog.dismiss();

                //BitmapDrawable bd = new BitmapDrawable(image);
                //img.setImageDrawable();

                //img.setImageResource(R.drawable.floating2);*/
                Log.e("umma", "success");
                values = new float[9];
                img.getMatrix().getValues(values);
                img.setOnTouchListener(ImageViewer.this);

            }else{
                pDialog.dismiss();
                Toast.makeText(ImageViewer.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
