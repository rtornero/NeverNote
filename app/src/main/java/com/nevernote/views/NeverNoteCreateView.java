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
package com.nevernote.views;

import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;

import java.util.List;

/**
 * Created by Roberto on 26/7/15.
 *
 * View interface to notify a {@link com.nevernote.fragments.NeverNoteCreateDialogFragment} with
 * the recently created {@link Note}
 */
public interface NeverNoteCreateView {

    /**
     * Tells the view there is a new note that has been created
     */
    void onNoteCreated();

    /**
     * Notifies the view with the retrieved list of notebooks
     * @param notebooks
     */
    void onNotebooksRetrieved(List<Notebook> notebooks);

    /**
     * Tell the view that the entered note details are empty
     */
    void titleOrContentEmpty();

    /**
     * Show or hide the progress bar whenever there is an action that needs waiting for it.
     */
    void showProgressBar();
    void hideProgressBar();

    /**
     * If there was an error, notify it through this method
     * @param e
     */
    void onError(Exception e);

}
