package com.bx.lessons.asynctaskloader.data;

/**
 * Created by emedinaa on 27/02/17.
 */

public interface AuthenticationCallback {

    void onAuthenticationSuccess(Object object);
    void onAuthenticationError(Exception exception);
}
