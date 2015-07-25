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
 * A simple {@link Fragment} subclass.
 * Use the {@link NeverNoteContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NeverNoteContentFragment extends Fragment implements NeverNoteContentView {

    public static final String TAG = NeverNoteContentFragment.class.getSimpleName();

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String NOTE_GUID = "noteGuid";

    private String noteGuid;
    private Note note;

    private TextView titleTextView;
    private TextView contentTextView;
    private TextView detailsTextView;

    private ProgressBar progressBar;

    private NeverNoteContentPresenter contentPresenter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param noteGuid
     * @return A new instance of fragment NeverNoteContentFragment.
     */
    public static NeverNoteContentFragment newInstance(String noteGuid) {

        final NeverNoteContentFragment fragment = new NeverNoteContentFragment();
        Bundle args = new Bundle();
        args.putString(NOTE_GUID, noteGuid);
        fragment.setArguments(args);
        return fragment;
    }

    public NeverNoteContentFragment() {
        // Required empty public constructor
    }

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

        contentPresenter.retrieveNoteContent(noteGuid);
    }

    @Override
    public void bindNoteContent(Note n) {

        note = n;
        titleTextView.setText(note.getTitle());
        contentTextView.setText(Html.fromHtml(note.getContent()), TextView.BufferType.SPANNABLE);

        final DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

        final StringBuilder builder = new StringBuilder()
                .append(getString(R.string.note_created))
                .append(formatter.format(new Date(note.getCreated())))
                .append(getString(R.string.note_by))
                .append(note.getAttributes().getAuthor());
        detailsTextView.setText(builder.toString());

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
