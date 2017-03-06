package com.bx.lessons.asynctaskloader.presenter;

import com.bx.lessons.asynctaskloader.model.UserEntity;

/**
 * Created by em on 8/06/16.
 */
public interface LogInView {

    void showLoading();
    void hideLoading();

    void onMessageError(String message);
    void gotoMain(UserEntity userEntity);
}
