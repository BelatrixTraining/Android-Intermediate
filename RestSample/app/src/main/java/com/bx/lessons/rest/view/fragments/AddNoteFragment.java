package com.bx.lessons.rest.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bx.lessons.rest.R;
import com.bx.lessons.rest.entity.NoteEntity;
import com.bx.lessons.rest.presenter.AddNoteView;
import com.bx.lessons.rest.presenter.NotePresenter;
import com.bx.lessons.rest.view.listeners.OnNoteListener;


public class AddNoteFragment extends Fragment  implements AddNoteView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText eteName;
    private EditText eteDesc;
    private EditText eteNote;
    private Button btnAddNote;

    private String name;
    private String desc;
    private String note;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnNoteListener mListener;
    private NotePresenter notePresenter;

    // TODO: Rename and change types and number of parameters
    public static AddNoteFragment newInstance(String param1, String param2) {
        AddNoteFragment fragment = new AddNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnNoteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eteName=(EditText)getView().findViewById(R.id.eteName);
        eteDesc=(EditText)getView().findViewById(R.id.eteDesc);
        eteNote=(EditText)getView().findViewById(R.id.eteNote);
        btnAddNote=(Button)getView().findViewById(R.id.btnAddNote);

        notePresenter= new NotePresenter();
        notePresenter.attachedView(this);

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addNote();
                addNoteCloud();
            }
        });
    }

    private void addNote() {
        name= eteName.getText().toString().trim();
        desc= eteDesc.getText().toString().trim();
        note= eteNote.getText().toString().trim();

        NoteEntity noteEntity= new NoteEntity(name,desc,null);
        mListener.getCrudOperations().addNote(noteEntity);
        getActivity().finish();
    }

    private void addNoteCloud(){
        name= eteName.getText().toString().trim();
        desc= eteDesc.getText().toString().trim();
        note= eteNote.getText().toString().trim();

        notePresenter.addNote(name,desc);
    }

    @Override
    public void showLoading() {
        mListener.showParentLoading();
    }

    @Override
    public void hideLoading() {
        mListener.hideParentLoading();
    }

    @Override
    public void onMessageError(String message) {
        mListener.showMessage(message);
    }

    @Override
    public void onAddNoteSuccess() {
        getActivity().finish();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
