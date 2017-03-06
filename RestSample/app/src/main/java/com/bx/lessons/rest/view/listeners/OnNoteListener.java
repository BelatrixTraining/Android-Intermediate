package com.bx.lessons.rest.view.listeners;


import com.bx.lessons.rest.entity.NoteEntity;
import com.bx.lessons.rest.storage.db.CRUDOperations;

/**
 * Created by emedinaa on 15/09/15.
 */
public interface OnNoteListener {

     CRUDOperations getCrudOperations();
     void deleteNote(NoteEntity noteEntity);
     void showParentLoading();
     void hideParentLoading();
     void showMessage(String message);
}
