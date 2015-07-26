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
package com.nevernote;

import android.support.v4.app.FragmentActivity;

import com.nevernote.interfaces.OnNoteCreateListener;

/**
 * Created by Roberto on 25/7/15.
 *
 * Navigation interface for our main context.
 */
public interface NeverNoteMainNavigator {

    /**
     * Sets the context to navigate through.
     * @param activity to perform navigation
     */
    void setActivity(FragmentActivity activity);

    /**
     * Starts a new Fragment transaction and displays the list of notes.
     */
    void showNoteListFragment();

    /**
     * Starts a new Fragment transaction and displays the details of a note.
     * @param noteGuid the note identifier to display its details
     */
    void showNoteContentFragment(String noteGuid);

    /**
     * Shows the note creation dialog
     * @param onNoteCreateListener that detects when the creation process has finished.
     */
    void showNoteCreateDialogFragment(OnNoteCreateListener onNoteCreateListener);

}
