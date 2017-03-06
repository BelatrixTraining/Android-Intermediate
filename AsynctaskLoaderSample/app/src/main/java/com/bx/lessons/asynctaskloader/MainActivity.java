package com.bx.lessons.asynctaskloader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bx.lessons.asynctaskloader.data.DataProvider;
import com.bx.lessons.asynctaskloader.data.model.LogInResponse;
import com.bx.lessons.asynctaskloader.model.UserEntity;
import com.bx.lessons.asynctaskloader.presenter.LogInPresenter;
import com.bx.lessons.asynctaskloader.presenter.LogInView;

/**
 * username : admin@gmail.com
 * password : 123456
 */

public class MainActivity extends AppCompatActivity implements LogInView {

    private Button btnLogin;
    private EditText eteUsername;
    private EditText etePassword;
    private String username;
    private String password;
    private View rlayLoading,container;

    private LogInPresenter logInPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataProvider<LogInResponse> dataProvider= new DataProvider<>(getApplicationContext(),
                getSupportLoaderManager());

        logInPresenter= new LogInPresenter(dataProvider);
        logInPresenter.attachedView(this);
        init();
    }

    private void init() {
        eteUsername=(EditText)findViewById(R.id.eteUsername);
        etePassword=(EditText)findViewById(R.id.etePassword);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        rlayLoading=findViewById(R.id.rlayLoading);
        container=findViewById(R.id.container);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    //gotoMain();
                    logInPresenter.logIn(username,password);
                }
            }
        });


        etePassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event!=null){
                    Log.v("CONSOLE ","keycode "+event.getKeyCode()+ " actionId "+actionId);
                }

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (validateForm()) {
                        logInPresenter.logIn(username,password);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void gotoMain(UserEntity userEntity) {
        savePreferences();
        Bundle bundle= new Bundle();
        bundle.putSerializable("USER", userEntity);
        Intent intent= new Intent(this,HomeActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void savePreferences() {
        //PreferencesHelper.saveSession(this,username,password);
    }

    private boolean validateForm() {
        username= eteUsername.getText().toString();
        password= etePassword.getText().toString();

        if(username.isEmpty())
        {
            eteUsername.setError("Error campo username");
            return false;
        }
        if(password.isEmpty())
        {
            etePassword.setError("Error campo password");
            return false;
        }
        return true;
    }


    @Override
    public void showLoading() {
        this.rlayLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        this.rlayLoading.setVisibility(View.GONE);
    }


    @Override
    public void onMessageError(String message) {
        Snackbar snackbar = Snackbar
                .make(container,message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}
