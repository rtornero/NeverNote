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

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.notestore.NotesMetadataList;
import com.evernote.edam.notestore.NotesMetadataResultSpec;
import com.evernote.edam.type.NoteSortOrder;
import com.nevernote.views.NeverNoteListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Roberto on 24/7/15.
 *
 * Implementation of {@link NeverNoteListPresenter}. It has a {@link NeverNoteListView} instance
 * to notify the view with the changes in the model, in this case, when the list of notes has been retrieved.
 */
public class NeverNoteListPresenterImpl implements NeverNoteListPresenter {

    /**
     * Number of notes to retrieve by pages. Each page will contain up to this number of notes.
     */
    private final static int MAX_NOTES_BY_PAGE = 50;

    /**
     * The number of notes we have currently retrieved. Next requests will read and update this value.
     */
    private int pageOffset;

    private NeverNoteListView listView;

    /**
     * The list of notes retrieved
     */
    private List<NoteMetadata> notes;

    /**
     * Flag to control if the presenter is retrieving data from the network.
     */
    private boolean mLoading;

    /**
     * The filter that will use Evernote's SDK when requesting the list notes. This
     * will make the list to be sorted by this filter. It is initialized by default to
     * {@link com.evernote.edam.type.NoteSortOrder#TITLE}
     */
    private NoteFilter sortFilter;

    /**
     * Evernote's SDK callback for retrieving the user's notes.
     */
    private EvernoteCallback<NotesMetadataList> notesMetadataListCallback = new EvernoteCallback<NotesMetadataList>() {
        @Override
        public void onSuccess(NotesMetadataList notesMetadataList) {

            mLoading = false;

            //Add all the notes and notify the view with them
            pageOffset += notesMetadataList.getNotesSize();

            //FIXME Default order by title filter is reversed!
            if (sortFilter.getOrder() == NoteSortOrder.TITLE.getValue())
                Collections.reverse(notesMetadataList.getNotes());

            notes.addAll(notesMetadataList.getNotes());

            listView.hideProgressBar();
            listView.updateNotes();
        }

        @Override
        public void onException(Exception e) {

            mLoading = false;

            listView.hideProgressBar();
            listView.onError(e);
        }
    };

    public NeverNoteListPresenterImpl(NeverNoteListView listView){

        this.notes = new ArrayList<>();
        this.mLoading = false;
        //Note filter intialized to order by title
        this.sortFilter = new NoteFilter();
        this.sortFilter.setOrder(NoteSortOrder.TITLE.getValue());
        setListView(listView);
    }

    @Override
    public void setListView(NeverNoteListView view) {
        this.listView = view;
    }

    /**
     * Loads the fist number of notes by setting the offset to zero.
     */
    @Override
    public void loadFirstNotes(){

        pageOffset = 0;
        loadNotes();
    }

    /**
     * Retrieves the list of notes from Evernote's API. It uses {@link NoteMetadata}
     * to improve performance by retrieving only the parameters that are required by our UI.
     */
    @Override
    public void loadNotes() {

        mLoading = true;

        //If we need to reload the notes, we need to clear our current collection
        if (pageOffset == 0)
            notes.clear();

        listView.showProgressBar();

        //Return the title, dateCreated and dateUpdated from each Note
        final NotesMetadataResultSpec spec = new NotesMetadataResultSpec();
        spec.setIncludeTitle(true);
        spec.setIncludeCreated(true);
        spec.setIncludeUpdated(true);

        /*
        Call Evernote's API to retrieve the notes defined by pageOffset and MAX_NOTES_BY_PAGE, using
        the sortFilter to order them. This calls is executed asynchronously.
         */
        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance()
                .getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.findNotesMetadataAsync(sortFilter, pageOffset, MAX_NOTES_BY_PAGE,
                spec, notesMetadataListCallback);

    }

    @Override
    public void setSortFilter(NoteFilter filter) {

        //Set the new filter coming from the menu view and reload notes
        this.sortFilter = filter;
        loadFirstNotes();
    }

    @Override
    public List<NoteMetadata> getNotes() {
        return notes;
    }

    @Override
    public boolean isLoading() {
        return mLoading;
    }

}
