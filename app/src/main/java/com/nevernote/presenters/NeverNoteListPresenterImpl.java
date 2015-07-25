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

import android.os.AsyncTask;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.nevernote.utils.AbortableCountDownLatch;
import com.nevernote.views.NeverNoteListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roberto on 24/7/15.
 */
public class NeverNoteListPresenterImpl implements NeverNoteListPresenter {

    private final static int MIN_NOTES_RETRIEVED = 0;
    private final static int MAX_NOTES_RETRIEVED = 100;

    private NeverNoteListView listView;
    private List<Note> neverNotesList;

    /**
     * Utility instance of {@see CountDownLatch} that can be aborted
     * to finish the count and notify the interface.
     */
    private AbortableCountDownLatch latch;

    /**
     * Evernote's SDK callback for retrieving the logged user's notebook collection
     */
    private EvernoteCallback<List<Notebook>> notebooksCallback = new EvernoteCallback<List<Notebook>>() {

        @Override
        public void onSuccess(final List<Notebook> result) {

            if (result.isEmpty())
                listView.hideProgressBar();
            else
                findNotes(result);
        }

        @Override
        public void onException(Exception exception) {

            listView.hideProgressBar();
            listView.onError(exception);
        }
    };

    /**
     * Evernote's SDK callback for retrieving the notes contained on a specified notebook
     */
    private EvernoteCallback<NoteList> notebookNotesListCallback = new EvernoteCallback<NoteList>() {
        @Override
        public void onSuccess(NoteList noteList) {

            //Add all the notes retrieved and tell the latch that there is one new operation completed.
            neverNotesList.addAll(noteList.getNotes());
            latch.countDown();
        }

        @Override
        public void onException(Exception e) {

            //Abort latch if a single error occurs
            latch.abort();
        }
    };

    public NeverNoteListPresenterImpl(NeverNoteListView listView){

        this.neverNotesList = new ArrayList<>();
        setListView(listView);
    }

    @Override
    public void setListView(NeverNoteListView view) {
        this.listView = view;
    }

    /**
     * In order to retrieve all the notes that a user has, Evernote's API requires fetching
     * first all his notebooks.
     */
    @Override
    public void retrieveNotes() {

        neverNotesList.clear();
        listView.showProgressBar();
        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance()
                .getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.listNotebooksAsync(notebooksCallback);

    }

    /**
     * To fetch all the user's notes we need to iterate through each of the notebooks and
     * merge the result in a single list. To achieve this, two options could be implemented: serial
     * or parallel execution. I decided to go for the second one by using a CountDownLatch, that waits
     * for as many countdowns as specified when creating it.
     * @param notebooks
     */
    private void findNotes(final List<Notebook> notebooks){

        //We create a new CountDownLatch with the same size as our notebooks list.
        //This is because we need to fetch the notes contained on each of the notebooks.
        latch = new AbortableCountDownLatch(notebooks.size());

        //We start a new AsyncTask because the CountDownLacth should be executed on a
        //different thread (it waits for the countdown to complete blocking execution).
        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {

                for (Notebook notebook : notebooks)
                    findNotesForNotebook(notebook);

                try {
                    latch.await();
                } catch (InterruptedException e) {

                    //If an error occurred, latch should be aborted
                    if (e instanceof AbortableCountDownLatch.AbortedException)
                        return true;
                }

                return false;
            }

            @Override
            public void onPostExecute(Boolean aborted){

                //If everything went well, we can notify our view with the results
                listView.hideProgressBar();
                if (aborted)
                    listView.onError(new AbortableCountDownLatch.AbortedException());

                else listView.updateNotes(neverNotesList);

            }
        }.execute();

    }

    /**
     *
     * @param notebook
     */
    private void findNotesForNotebook(Notebook notebook){

        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance()
                .getEvernoteClientFactory().getNoteStoreClient();
        final NoteFilter filter = new NoteFilter();
        filter.setNotebookGuid(notebook.getGuid());
        noteStoreClient.findNotesAsync(filter, MIN_NOTES_RETRIEVED, MAX_NOTES_RETRIEVED, notebookNotesListCallback);
    }

}
