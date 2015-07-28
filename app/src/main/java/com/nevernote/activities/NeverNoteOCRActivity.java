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
package com.nevernote.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.koushikdutta.ion.Ion;
import com.nevernote.R;
import com.nevernote.fragments.NeverNoteOCRFragment;

/**
 * Created by Roberto on 28/7/15.
 *
 * This is a container for the {@link NeverNoteOCRFragment}. It also serves as a
 * mechanism to communicate OCR results with {@link com.nevernote.fragments.NeverNoteCreateDialogFragment}
 * through {@link com.nevernote.fragments.NeverNoteCreateDialogFragment#onActivityResult(int, int, Intent)}
 */
public class NeverNoteOCRActivity extends AppCompatActivity {

    /**
     * Parameter name to detect if the OCR request need to return text for a title or a content
     */
    public final static String TITLE_OR_CONTENT = "titleOrContent";
    /**
     * Parameter name to send through a {@link Intent} as a result text
     */
    public final static String OCR_TEXT_RETURNED = "ocrTextReturned";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_never_note_ocr);

        //Sets the fragment for OCR passing the boolean parameter as argument
        if (savedInstanceState == null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_never_note_ocr_container,
                            NeverNoteOCRFragment.newInstance(getIntent()
                                    .getBooleanExtra(TITLE_OR_CONTENT, true)))
                    .commit();
    }

    @Override
    public void onPause(){
        super.onPause();

        //Cancel all Ion requests in case we go back without finishing them
        Ion.getDefault(this).cancelAll();
    }
}
