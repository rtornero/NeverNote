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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nevernote.R;
import com.nevernote.activities.NeverNoteOCRActivity;
import com.nevernote.interfaces.OnBitmapForOCRSaveListener;
import com.nevernote.presenters.NeverNoteOCRPresenter;
import com.nevernote.presenters.NeverNoteOCRPresenterImpl;
import com.nevernote.views.NeverNoteOCRView;
import com.nevernote.widget.NeverNoteHandWritingView;

/**
 * Created by Roberto on 28/7/15.
 *
 * This {@link Fragment} instance displays a special view {@link NeverNoteHandWritingView} that allows
 * drawing shapes on it by doing gestures and serves as a canvas for text recognition.
 *
 * It implements the MVP pattern with a {@link NeverNoteOCRPresenter} that connects with the
 * OCR API to send a screenshot of the view and retrieves the recognised text.
 */
public class NeverNoteOCRFragment extends Fragment implements NeverNoteOCRView, View.OnClickListener {

    public static final String TAG = NeverNoteOCRFragment.class.getSimpleName();

    private final static String TITLE_OR_CONTENT = "titleOrContent";

    /**
     * Control flag to determine whether the requested drawing
     * needs to be recognised and assigned to the title (true)
     * or to the content (false) input field.
     */
    private boolean isTitleOrContent;

    private NeverNoteOCRPresenter ocrPresenter;

    private NeverNoteHandWritingView handWritingView;

    private Button processButton;
    private ImageButton eraseButton;

    private ProgressBar progressBar;

    /**
     * Instance creator
     * @param isTitleOrContent comes from the {@link NeverNoteOCRActivity} and determines
     *                         if the user requested to recognize the title or the content
     * @return new {@link NeverNoteOCRFragment} instance
     */
    public static NeverNoteOCRFragment newInstance(boolean isTitleOrContent){
        final NeverNoteOCRFragment fragment = new NeverNoteOCRFragment();
        final Bundle args = new Bundle();
        args.putBoolean(TITLE_OR_CONTENT, isTitleOrContent);
        fragment.setArguments(args);
        return fragment;
    }

    public NeverNoteOCRFragment() {
        ocrPresenter = new NeverNoteOCRPresenterImpl(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        isTitleOrContent = getArguments().getBoolean(TITLE_OR_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_never_note_ocr, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        super.onViewCreated(view, savedInstanceState);

        final TextView titleTextView = (TextView) view.findViewById(R.id.fragment_never_note_ocr_title);
        titleTextView.setText(String.format(getString(R.string.note_ocr_title),
                getString(isTitleOrContent ? R.string.note_ocr_enter_title
                        : R.string.note_ocr_enter_content)));

        processButton = (Button) view.findViewById(R.id.fragment_never_note_ocr_button);
        processButton.setOnClickListener(this);

        eraseButton = (ImageButton) view.findViewById(R.id.fragment_never_note_ocr_erase_button);
        eraseButton.setOnClickListener(this);

        handWritingView = (NeverNoteHandWritingView) view.findViewById(R.id.fragment_never_note_ocr_view);

        progressBar = (ProgressBar) view.findViewById(R.id.fragment_never_note_ocr_progress);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.fragment_never_note_ocr_button:
                /*
                Tell the handwriting view to return its current drawing content
                 */
                handWritingView.processBitmapAsync(new OnBitmapForOCRSaveListener() {
                    @Override
                    public void onBitmapSaved(final Bitmap bitmap) {
                        /*
                        Once the canvas content is retrieved as a Bitmap,
                        request to start the recognition process.
                         */
                        ocrPresenter.processOCRForBitmapAsync(getActivity(), bitmap);
                    }
                });
                break;
            case R.id.fragment_never_note_ocr_erase_button:
                //Clears the current drawing
                handWritingView.erasePath();

        }
    }

    @Override
    public void updateWithOCRText(String result) {

        final Intent data = new Intent();
        data.putExtra(NeverNoteOCRActivity.TITLE_OR_CONTENT, isTitleOrContent);
        data.putExtra(NeverNoteOCRActivity.OCR_TEXT_RETURNED, result);
        /*
        Set the result and finish the current Activity, as it was
        started expecting a result from it. Also include the flag to know
        if we need to set the resulting text as a title or as the content of the note.
         */
        final FragmentActivity activity = getActivity();
        activity.setResult(Activity.RESULT_OK, data);
        activity.finish();
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
    public void enableButtons() {
        processButton.setEnabled(true);
        eraseButton.setEnabled(true);
    }

    @Override
    public void disableButtons() {
        processButton.setEnabled(false);
        eraseButton.setEnabled(false);
    }

    @Override
    public void onDestroy(){

        ocrPresenter.setOCRView(null);
        ocrPresenter = null;
        super.onDestroy();
    }
}
