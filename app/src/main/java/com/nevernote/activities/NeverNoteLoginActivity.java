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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.evernote.client.android.login.EvernoteLoginFragment;
import com.nevernote.R;

/**
 * Created by Roberto on 23/7/15.
 */
public class NeverNoteLoginActivity extends AppCompatActivity implements EvernoteLoginFragment.ResultCallback {

    public static final String TAG = NeverNoteLoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_never_note_login);
    }

    /**
     * The Evernote session callback is required to be on a FragmentActivity subclass.
     * Tried to put it on {@link com.nevernote.fragments.NeverNoteLoginFragment} but it didn't worked.
     * @param authenticated
     */
    @Override
    public void onLoginFinished(boolean authenticated) {
        if (authenticated)
            Log.d(TAG, "Login achieved");
        else
            Log.d(TAG, "Login NOT achieved");
    }
}
