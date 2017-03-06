package com.bx.lessons.rest.presenter;


/**
 * Created by em on 8/06/16.
 */
public interface AddNoteView {

    void showLoading();
    void hideLoading();

    void onMessageError(String message);
    void onAddNoteSuccess();
}
