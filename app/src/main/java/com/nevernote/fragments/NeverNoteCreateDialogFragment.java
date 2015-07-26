/*
The MIT License (MIT)

Copyright (c) 2015 Roberto Tornero

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */
package com.nevernote.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.evernote.edam.type.Note;
import com.nevernote.R;
import com.nevernote.interfaces.OnNoteCreateListener;
import com.nevernote.presenters.NeverNoteCreatePresenter;
import com.nevernote.presenters.NeverNoteCreatePresenterImpl;
import com.nevernote.views.NeverNoteCreateView;

/**
 * Created by Roberto on 26/7/15.
 *
 * Dialog that allows to create new {@link Note} instances and send them to Evernote's SDK.
 * Implements MVP pattern with a {@link NeverNoteCreatePresenter}
 * instance that sends the note details to Evernote's SDK and notifies the view when they have finished.
 */
public class NeverNoteCreateDialogFragment extends DialogFragment implements NeverNoteCreateView, View.OnClickListener {

    public static final String TAG = NeverNoteCreateDialogFragment.class.getSimpleName();

    private OnNoteCreateListener onNoteCreateListener;

    private NeverNoteCreatePresenter createPresenter;

    private EditText noteTitleEdit, noteContentEdit;
    private Button createButton;

    private ProgressBar progressBar;

    /**
     * Method that calls the constructor and adds arguments to it so the default constructor is not exposed.
     *
     * @param onNoteCreateListener the listener that notifies when the new Note has been created
     * @return new instance of {@link NeverNoteCreateDialogFragment}
     */
    public static NeverNoteCreateDialogFragment newInstance(OnNoteCreateListener onNoteCreateListener){

        final NeverNoteCreateDialogFragment createDialogFragment = new NeverNoteCreateDialogFragment();
        createDialogFragment.setOnNoteCreateListener(onNoteCreateListener);
        return createDialogFragment;
    }

    public NeverNoteCreateDialogFragment(){

        createPresenter = new NeverNoteCreatePresenterImpl(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Show no title on the Dialog
        setStyle(STYLE_NO_TITLE,
                android.support.v7.appcompat.R.style.Theme_AppCompat_Light_Dialog_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_dialog_never_note_create, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        noteTitleEdit = (EditText) view.findViewById(R.id.fragment_dialog_never_note_create_title);
        noteContentEdit = (EditText) view.findViewById(R.id.fragment_dialog_never_note_create_content);

        createButton = (Button) view.findViewById(R.id.fragment_dialog_never_note_create_button);
        createButton.setOnClickListener(this);

        progressBar = (ProgressBar) view.findViewById(R.id.fragment_dialog_never_note_create_progress);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.fragment_dialog_never_note_create_button) {

            final String title = noteTitleEdit.getText().toString();
            final String content = noteContentEdit.getText().toString();
            createPresenter.createNote(title, content);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        /*
        We use this listener to notify when the note has been created.
        This method is called every time we dismiss the dialog, even when pressing
        Back, but in this case, our Note will be null so it won't be treated.
         */
        if (onNoteCreateListener != null)
            onNoteCreateListener.onNoteCreated(createPresenter.getNote());

        super.onDismiss(dialog);
    }

    @Override
    public void onNoteCreated() {

        //When the note has been created, we just dismiss the dialog. We have previously
        //make it implement onDismiss() to listen to this situation.
        dismiss();
    }

    @Override
    public void titleOrContentEmpty() {

        //When the user has pressed Create button but hasn't entered any details
        Toast.makeText(getActivity(), R.string.alert_title_or_content_empty,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(Exception e) {

    }

    public void setOnNoteCreateListener(OnNoteCreateListener onNoteCreateListener) {
        this.onNoteCreateListener = onNoteCreateListener;
    }

    @Override
    public void onDestroy(){

        createPresenter.setCreateView(null);
        createPresenter = null;
        super.onDestroy();
    }

}
