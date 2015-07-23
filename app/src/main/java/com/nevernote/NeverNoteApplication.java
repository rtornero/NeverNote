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

import android.app.Application;

import com.evernote.client.android.EvernoteSession;

/**
 * Created by Roberto on 23/7/15.
 *
 * Extended Application class to define a global context in which the Evernote singleton manager is initialized
 */
public class NeverNoteApplication extends Application {

    /**
     * Defines the Evernote environment to use. In this case we'll be pointing to Sandbox.
     */
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

    @Override
    public void onCreate() {
        super.onCreate();

        /*
        Initialize Evernote's singleton. We can use it by calling
        EvernoteSession.getInstance() wherever in the app
         */
        new EvernoteSession.Builder(this)
                .setEvernoteService(EVERNOTE_SERVICE)
                .build(BuildConfig.EVERNOTE_CONSUMER_KEY, BuildConfig.EVERNOTE_CONSUMER_SECRET)
                .asSingleton();

        /*
        We want to listen to activity lifecycle changes in order to detect whenever there
        is no Evernote session available (nice behavior adapted from Evernote SDK Demo app).
         */
        registerActivityLifecycleCallbacks(new NeverNoteActivityListener());
    }
}
