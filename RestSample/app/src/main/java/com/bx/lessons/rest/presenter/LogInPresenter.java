package com.bx.lessons.rest.presenter;

import android.util.Log;


import com.bx.lessons.rest.entity.UserEntity;
import com.bx.lessons.rest.storage.entity.LogInRaw;
import com.bx.lessons.rest.storage.entity.LogInResponse;
import com.bx.lessons.rest.storage.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by em on 8/06/16.
 */
public class LogInPresenter {

    private static final String TAG = "LogInPresenter";
    private final String ERROR_MESSAGE= "Ocurriò un error";
    private LogInView logInView;
    private String email;
    private String password;

    public   void attachedView(LogInView logInView){
        this.logInView = logInView;
    }

    public  void detachView(){
        this.logInView=null;
    }

    public void logIn(String email,String password) {
        this.email = email;
        this.password = password;
        LogInRaw logInRaw= new LogInRaw();
        logInRaw.setLogin(this.email);
        logInRaw.setPassword(this.password);

        logInView.showLoading();

        Call<LogInResponse> call = ApiClient.getMyApiClient().login(logInRaw);
        call.enqueue(new Callback<LogInResponse>() {
            @Override
            public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                if(response.isSuccessful()){

                    loginSuccess(response.body());
                }else {
                    loginError(ERROR_MESSAGE);
                }

            }

            @Override
            public void onFailure(Call<LogInResponse> call, Throwable t) {
                String json="Error ";
                try {
                    json= new StringBuffer().append(t.getMessage()).toString();
                }catch (NullPointerException e) {}
                Log.v(TAG, "json >>>> " + json);

                loginError(json);
            }
        });

    }
    public void loginSuccess(LogInResponse loginResponse){
        if(loginResponse!=null){
            UserEntity userEntity= new UserEntity();
            userEntity.setEmail(loginResponse.getEmail());
            userEntity.setName(loginResponse.getName());
            userEntity.setObjectId(loginResponse.getObjectId());
            userEntity.setToken(loginResponse.getToken());
        }
        logInView.hideLoading();
        logInView.gotoMain();
    }
    public void loginError(String messageError){
        logInView.hideLoading();
        logInView.onMessageError(messageError);
    }
}
