package com.bx.lessons.asynctaskloader.presenter;

import com.bx.lessons.asynctaskloader.data.AuthenticationCallback;
import com.bx.lessons.asynctaskloader.data.DataProvider;
import com.bx.lessons.asynctaskloader.data.model.LogInRaw;
import com.bx.lessons.asynctaskloader.data.model.LogInResponse;
import com.bx.lessons.asynctaskloader.model.UserEntity;

/**
 * Created by em on 8/06/16.
 */
public class LogInPresenter {

    private static final String TAG = "LogInPresenter";
    private final String ERROR_MESSAGE= "Ocurriò un error";
    private final String LOGIN_PATH="http://api.backendless.com/v1/users/login";
    private final int METHOD_POST=2;

    private LogInView logInView;
    private String email;
    private String password;

    private final  DataProvider<LogInResponse> dataProvider;

    public LogInPresenter(DataProvider<LogInResponse> dataProvider) {
        this.dataProvider = dataProvider;
    }

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

        //http://api.backendless.com/v1/users/login
        dataProvider.run(authenticationCallback,LOGIN_PATH,logInRaw,LogInResponse.class,
                METHOD_POST);

    }

    private AuthenticationCallback authenticationCallback= new AuthenticationCallback() {
        @Override
        public void onAuthenticationSuccess(Object object) {
            loginSuccess((LogInResponse)(object));
        }

        @Override
        public void onAuthenticationError(Exception exception) {
            loginError(exception.getMessage());
        }
    };

    public void loginSuccess(LogInResponse loginResponse){

        logInView.hideLoading();
        if(loginResponse!=null){
            UserEntity userEntity= new UserEntity();
            userEntity.setEmail(loginResponse.getEmail());
            userEntity.setName(loginResponse.getName());
            userEntity.setObjectId(loginResponse.getObjectId());
            userEntity.setToken(loginResponse.getToken());
            logInView.gotoMain(userEntity);
        }
    }
    public void loginError(String messageError){
        logInView.hideLoading();
        logInView.onMessageError(messageError);
    }
}
