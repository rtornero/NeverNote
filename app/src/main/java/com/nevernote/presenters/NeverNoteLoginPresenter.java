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

import android.support.v4.app.FragmentActivity;

import com.nevernote.views.NeverNoteLoginView;

/**
 * Created by Roberto on 23/7/15.
 *
 * Interface to control the presentation layer of the {@link com.nevernote.fragments.NeverNoteLoginFragment}
 * with utility methods that perform changes on the model.
 */
public interface NeverNoteLoginPresenter {

    /**
     * Sets the view interface to the presenter
     * @param view
     */
    void setLoginView(NeverNoteLoginView view);

    /**
     * Performs authentication on Evernote's oauth system.
     * @param activity to which the authentication callback will be redirected.
     */
    void onEvernoteAuthenticate(FragmentActivity activity);
}
