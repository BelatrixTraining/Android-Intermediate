package com.bx.lessons.rest.presenter;

import android.util.Log;


import com.bx.lessons.rest.entity.NoteEntity;
import com.bx.lessons.rest.storage.entity.NotesResponse;
import com.bx.lessons.rest.storage.request.ApiClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by em on 8/06/16.
 */
public class NotesPresenter {

    private static final String TAG = "NotesPresenter";
    private final String ERROR_MESSAGE= "Ocurriò un error";

    private NotesView notesView;

    public   void attachedView(NotesView notesView){
        this.notesView = notesView;
    }

    public  void detachView(){
        this.notesView=null;
    }

    public void loadNotes(){
        notesView.showLoading();

        Call<NotesResponse> call= ApiClient.getMyApiClient().notes();
        call.enqueue(new Callback<NotesResponse>() {
            @Override
            public void onResponse(Call<NotesResponse> call, Response<NotesResponse> response) {
                if(response.isSuccessful()){

                    notesSuccess(response.body());
                }else {
                    notesError(ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<NotesResponse> call, Throwable t) {
                String json="Error ";
                try {
                    json= new StringBuffer().append(t.getMessage()).toString();
                }catch (NullPointerException e) {}
                Log.v(TAG, "json >>>> " + json);

                notesError(json);
            }
        });
    }

    private void notesSuccess(NotesResponse notesResponse) {
        notesView.hideLoading();

        if(notesResponse!=null){
            List<NoteEntity> notes= notesResponse.getData();
            notesView.renderNotes(notes);
        }

    }
    private void notesError(String messageError){
        notesView.hideLoading();
        notesView.onMessageError(messageError);
    }
}
