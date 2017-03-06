package com.bx.lessons.asynctaskloader.data.rest;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emedinaa on 27/02/17.
 */

public class RestHttpClient<T> {

    public static final int METHOD_GET= 1;
    public static final int METHOD_POST= 2;

    protected T response;
    private Gson gson;
    private HttpClient httpClient;
    private String path;
    private int method;
    private Object body;
    private Class<T> type;

    public RestHttpClient(String path, int method, Object body, Class<T> type) {
        gson= new Gson();
        httpClient= new DefaultHttpClient();
        this.path= path;
        this.method= method;
        this.body= body;
        this.type= type;
    }

    public T execute(){
        if(method== METHOD_GET){
            executeGetRequest();
        }else if(method==METHOD_POST){
            executePostRequest();
        }
        return response;
    }

    public T executeGetRequest() {
        HttpGet httpGet = new HttpGet(path);
        httpGet.setHeaders(buildHeader());
        try {
            response = responseData(httpClient.execute(httpGet));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public T executePostRequest() {
        HttpPost httpPost = new HttpPost(path);
        String postData = gson.toJson(body);
        try {
            httpPost.setHeaders(buildHeader());
            httpPost.setEntity(new StringEntity(postData));
            response = responseData(httpClient.execute(httpPost));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private Header[] buildHeader() {
        /*
          "Content-Type: application/json",
                "application-id: FF14D613-D0B0-6A6A-FF40-29E7E8E8D100",
                "secret-key: AA499423-00EC-1F0B-FF2E-C78F36087700",
                "application-type: REST"
         */
        Header contentType = new BasicHeader("Content-type", "application/json");
        Header applicationId = new BasicHeader("application-id", "F3C03837-D80E-A057-FF95-9F2CBB48AB00");
        Header secretKey = new BasicHeader("secret-key", "93CDF004-1B29-C89B-FF87-5C156F5BDB00");
        Header applicationType = new BasicHeader("application-type", "REST");
        List<Header> headers = new ArrayList<>();
        headers.add(contentType);
        headers.add(applicationId);
        headers.add(secretKey);
        headers.add(applicationType);

        Header[] mHeaders = new Header[headers.size()];
        mHeaders = headers.toArray(mHeaders);
        return mHeaders;
    }

    private T responseData(HttpResponse httpResponse) {
        try {
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent()));

                StringBuilder result = new StringBuilder();
                String mResponse;
                while ((mResponse = rd.readLine()) != null) {
                    result.append(mResponse);
                }

                Log.v("CONSOLE",result.toString() );

                if (!result.toString().isEmpty()) {
                    //response = gson.fromJson(result.toString(), type);
                    response= gson.fromJson(result.toString(), type);
                }
            }
        } catch (IOException e) { //TODO Handle applicable HTTP Status Code
            e.printStackTrace();
            Log.v("CONSOLE","error "+e.toString() );
        }
        return response;
    }
}
