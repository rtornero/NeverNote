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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evernote.edam.type.Note;
import com.nevernote.R;
import com.nevernote.presenters.NeverNoteContentPresenter;
import com.nevernote.presenters.NeverNoteContentPresenterImpl;
import com.nevernote.views.NeverNoteContentView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Roberto on 25/7/15.
 *
 * An instance of {@link Fragment} that shows the details of a {@link Note},
 * such as title, content and creation date. Implements MVP pattern with a {@link NeverNoteContentPresenter}
 * instance that retrieves the details from Evernote's SDK and notifies the view with changes.
 */
public class NeverNoteContentFragment extends Fragment implements NeverNoteContentView {

    public static final String TAG = NeverNoteContentFragment.class.getSimpleName();

    private static final String NOTE_GUID = "noteGuid";

    private String noteGuid;

    private TextView titleTextView;
    private TextView contentTextView;
    private TextView detailsTextView;

    private ProgressBar progressBar;

    private NeverNoteContentPresenter contentPresenter;

    /**
     * Method that calls the constructor and adds arguments to it so the default constructor is not exposed.
     *
     * @param noteGuid the identifier of the {@link Note} whose details are about to be retrieved
     * @return a new instance of {@link NeverNoteContentFragment}
     */
    public static NeverNoteContentFragment newInstance(String noteGuid) {

        final NeverNoteContentFragment fragment = new NeverNoteContentFragment();
        Bundle args = new Bundle();
        args.putString(NOTE_GUID, noteGuid);
        fragment.setArguments(args);
        return fragment;
    }

    public NeverNoteContentFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteGuid = getArguments().getString(NOTE_GUID);
        }

        contentPresenter = new NeverNoteContentPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_never_note_content, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        super.onViewCreated(view, savedInstanceState);

        titleTextView = (TextView) view.findViewById(R.id.fragment_never_note_content_title);
        contentTextView = (TextView) view.findViewById(R.id.fragment_never_note_content_text);
        detailsTextView = (TextView) view.findViewById(R.id.fragment_never_note_content_details);

        progressBar = (ProgressBar) view.findViewById(R.id.fragment_never_note_content_progress);

        //Retrieve note details from Evernote's SDK
        contentPresenter.retrieveNoteContent(noteGuid);
    }

    @Override
    public void bindNoteContent(Note note) {

        titleTextView.setText(note.getTitle());
        contentTextView.setText(Html.fromHtml(note.getContent()), TextView.BufferType.SPANNABLE);

        final DateFormat formatter = new SimpleDateFormat(getString(R.string.note_date_format));
        final String details = String.format(getString(R.string.note_created_by),
                formatter.format(new Date(note.getCreated())),
                formatter.format(new Date(note.getUpdated())));
        detailsTextView.setText(details);

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

    @Override
    public void onDestroy(){

        contentPresenter.setContentView(null);
        contentPresenter = null;
        super.onDestroy();
    }
}
