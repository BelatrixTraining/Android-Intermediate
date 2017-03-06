package com.bx.lessons.rest.presenter;

import android.util.Log;


import com.bx.lessons.rest.entity.NoteEntity;
import com.bx.lessons.rest.storage.entity.NoteRaw;
import com.bx.lessons.rest.storage.entity.NoteResponse;
import com.bx.lessons.rest.storage.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by em on 8/06/16.
 */
public class NotePresenter {

    private static final String TAG = "NotePresenter";
    private final String ERROR_MESSAGE= "Ocurriò un error";
    private AddNoteView addNoteView;
    private String name,description;

    public   void attachedView(AddNoteView addNoteView){
        this.addNoteView = addNoteView;
    }

    public  void detachView(){
        this.addNoteView=null;
    }

    public void addNote(String name, String desc ){
        NoteRaw noteRaw= new NoteRaw();
        noteRaw.setName(name);
        noteRaw.setDescription(desc);

        addNoteView.showLoading();
        Call<NoteResponse> call = ApiClient.getMyApiClient().addNote(noteRaw);
        call.enqueue(new Callback<NoteResponse>() {
            @Override
            public void onResponse(Call<NoteResponse> call, Response<NoteResponse> response) {
                if(response.isSuccessful()){
                    addNoteSuccess(response.body());
                }else {
                    addNoteError(ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<NoteResponse> call, Throwable t) {
                String json="Error ";
                try {
                    json= new StringBuffer().append(t.getMessage()).toString();
                }catch (NullPointerException e) {}
                Log.v(TAG, "json >>>> " + json);

                addNoteError(json);
            }
        });

    }
    public void addNoteSuccess(NoteResponse noteResponse){

        if(noteResponse!=null){
            NoteEntity noteEntity= new NoteEntity();
            noteEntity.setObjectId(noteResponse.getObjectId());
            noteEntity.setName(noteResponse.getName());
            noteEntity.setDescription(noteResponse.getDescription());
        }
        addNoteView.hideLoading();
        addNoteView.onAddNoteSuccess();
    }

    public void addNoteError(String messageError){
        addNoteView.hideLoading();
        addNoteView.onMessageError(messageError);
    }
}
