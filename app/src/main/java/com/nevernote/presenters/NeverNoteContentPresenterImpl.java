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
import com.evernote.edam.type.Note;
import com.nevernote.views.NeverNoteContentView;

/**
 * Created by Roberto on 25/7/15.
 */
public class NeverNoteContentPresenterImpl implements NeverNoteContentPresenter {

    private NeverNoteContentView contentView;

    private EvernoteCallback<Note> noteContentCallback = new EvernoteCallback<Note>() {
        @Override
        public void onSuccess(Note note) {
            contentView.hideProgressBar();
            contentView.bindNoteContent(note);
        }

        @Override
        public void onException(Exception e) {
            contentView.hideProgressBar();
            contentView.onError(e);
        }
    };

    public NeverNoteContentPresenterImpl(NeverNoteContentView view){
        setContentView(view);
    }

    @Override
    public void setContentView(NeverNoteContentView view) {
        contentView = view;
    }

    @Override
    public void retrieveNoteContent(String noteGuid) {

        contentView.showProgressBar();
        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance()
                .getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.getNoteAsync(noteGuid, true, true, false, false, noteContentCallback);
    }
}
