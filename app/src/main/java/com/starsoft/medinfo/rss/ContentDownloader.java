package com.starsoft.medinfo.rss;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Aashish on 9/7/2016.
 */
public class ContentDownloader extends AsyncTask<Void,Void,String> {

    Context context;
    String url = "";
    private static final String TAG = ContentDownloader.class.getSimpleName();

    public ContentDownloader(Context context,String uri){
        this.context = context;
        this.url = uri;
    }

    @Override
    protected String doInBackground(Void... params) {

       /* OkHttpClient client = new OkHttpClient();
        try{
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            Log.d(TAG, ""+response.body().string());
            return response.body().string();
        }catch (Exception e){}*/

        return null;
    }
}
