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
package com.nevernote.presenters;

import com.evernote.edam.type.Note;
import com.nevernote.views.NeverNoteListView;

import java.util.List;

/**
 * Created by Roberto on 24/7/15.
 *
 * Interface to control the presentation layer of the {@link com.nevernote.fragments.NeverNoteListFragment}
 * with utility methods that perform changes on the model.
 */
public interface NeverNoteListPresenter {

    /**
     * Sets the view interface to the presenter
     * @param view
     */
    void setListView(NeverNoteListView view);

    /**
     * Call the Evernote's SDK to retrieve a list of all the notes created by the logged user
     */
    void retrieveNotes();

    /**
     *
     * @return the list of notes retrieved
     */
    List<Note> getNotes();

}
