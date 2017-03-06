package com.bx.lessons.asynctaskloader.data;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.bx.lessons.asynctaskloader.data.rest.NetworkAsyncTask;

/**
 * Created by emedinaa on 27/02/17.
 */

public class DataProvider<T> implements LoaderManager.LoaderCallbacks<T> {
    private final Context context;
    private final LoaderManager loaderManager;
    private final int LOADER_ID= 100;

    private  AuthenticationCallback authenticationCallback;
    private T response;
    private String path;
    private Object body;
    private Class<T> type;
    private int method;

    public DataProvider(Context context, LoaderManager loaderManager) {
        this.context = context;
        this.loaderManager = loaderManager;
    }

    public void run( AuthenticationCallback authenticationCallback, String path, Object body, Class<T> type, int method){
        this.authenticationCallback = authenticationCallback;
        this.path = path;
        this.body = body;
        this.type = type;
        this.method = method;

        this.loaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<T> onCreateLoader(int id, Bundle args) {
        return new NetworkAsyncTask<T>(context,path,body,type,method);
    }

    @Override
    public void onLoadFinished(Loader<T> loader, T data) {
        this.response= data;
        if(this.response!=null){
            authenticationCallback.onAuthenticationSuccess(this.response);
        }else{
            Exception exception= new Exception("Ocurrió un error");
            authenticationCallback.onAuthenticationError(exception);
        }

    }

    @Override
    public void onLoaderReset(Loader<T> loader) {
        response = null;
    }

    protected void destroyLoader() {
        loaderManager.destroyLoader(LOADER_ID);
    }
}
