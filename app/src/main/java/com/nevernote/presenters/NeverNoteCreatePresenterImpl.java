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

import android.text.TextUtils;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.nevernote.views.NeverNoteCreateView;

import java.util.List;

/**
 * Created by Roberto on 26/7/15.
 *
 * Implementation of {@link NeverNoteCreatePresenter}. It has a {@link NeverNoteCreateView} instance
 * to notify the view with the changes in the model, in this case, when the new note has been created.
 */
public class NeverNoteCreatePresenterImpl implements NeverNoteCreatePresenter {

    /**
     * The view interface to be notified by the presenter
     */
    private NeverNoteCreateView createView;
    /**
     * The recently created Note
     */
    private Note note;

    /**
     *
     */
    private String selectedGuid;

    /**
     * Evernote's callback that gets called when the creation process has finished.
     */
    private EvernoteCallback<Note> createNoteCallback = new EvernoteCallback<Note>() {
        @Override
        public void onSuccess(Note result) {

            //We save our new Note for later and notify the view
            note = result;
            createView.hideProgressBar();
            createView.enableButtons();
            createView.onNoteCreated();
        }

        @Override
        public void onException(Exception exception) {

            //If an error occurred, notify the view
            createView.hideProgressBar();
            createView.enableButtons();
            createView.onError(exception);
        }
    };

    private EvernoteCallback<List<Notebook>> notebooksListCallback = new EvernoteCallback<List<Notebook>>() {
        @Override
        public void onSuccess(List<Notebook> notebooks) {

            if (! notebooks.isEmpty())
                createView.onNotebooksRetrieved(notebooks);
        }

        @Override
        public void onException(Exception e) {}
    };

    public NeverNoteCreatePresenterImpl(NeverNoteCreateView createView){
        setCreateView(createView);
    }

    @Override
    public void createNote(String title, String content) {

        /*
        Field validation for title and content of the new note, notify the
        view if these are not correct.
         */
        if (title.isEmpty() || content.isEmpty()){
            createView.titleOrContentEmpty();
            return;
        }

        //New note instance with the desired title and content
        final Note note = new Note();
        note.setTitle(title);
        note.setContent(EvernoteUtil.NOTE_PREFIX + content + EvernoteUtil.NOTE_SUFFIX);

        //If notebook selection was available, take the guid of the user's choice
        if (!TextUtils.isEmpty(selectedGuid))
            note.setNotebookGuid(selectedGuid);

        createView.showProgressBar();
        createView.disableButtons();
        /*
        Get a handler to the EvernoteNoteStoreClient and send the entered details of the new note.
        This operation is processed asynchronously.
         */
        final EvernoteNoteStoreClient noteStoreClient =
                EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.createNoteAsync(note, createNoteCallback);
    }

    @Override
    public void retrieveNotebooks() {

        final EvernoteNoteStoreClient noteStoreClient =
                EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.listNotebooksAsync(notebooksListCallback);
    }

    @Override
    public void setSelectedNotebookGuid(String guid) {
        this.selectedGuid = guid;
    }

    @Override
    public void setCreateView(NeverNoteCreateView createView) {
        this.createView = createView;
    }

    @Override
    public Note getNote() {
        return note;
    }
}
