package com.bx.lessons.asynctaskloader.data.rest;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by emedinaa on 27/02/17.
 */

public class NetworkAsyncTask<T> extends AsyncTaskLoader<T> {

    private RestHttpClient<T> restHttpClient;
    private T data;

    public NetworkAsyncTask(Context context, String path,Object body,Class<T> type,int method) {
        super(context);
        restHttpClient= new RestHttpClient<>(path,method,body,type);
    }

    @Override
    protected void onStartLoading() {
        if(data==null){
            forceLoad();
        }else{
            super.deliverResult(data);
        }
    }

    @Override
    public void deliverResult(T nResponse) {
        if (!isReset()) {
            // Store the new response and deliver it if we are started.
            data = nResponse;
            if (isStarted()) {
                // The superclass method deliver the results for us.
                super.deliverResult(nResponse);

            }
        }
    }

    @Override
    public T loadInBackground() {
        return restHttpClient.execute();
    }
}
