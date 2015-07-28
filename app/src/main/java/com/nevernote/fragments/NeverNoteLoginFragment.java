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
package com.nevernote.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.evernote.client.android.login.EvernoteLoginFragment;
import com.nevernote.R;
import com.nevernote.presenters.NeverNoteLoginPresenter;
import com.nevernote.presenters.NeverNoteLoginPresenterImpl;
import com.nevernote.views.NeverNoteLoginView;

/**
 * Created by Roberto on 23/7/15.
 *
 * Login UI with a button that connects with Evernote's SDK to authenticate users. Uses MVP pattern
 * with a {@link NeverNoteLoginPresenter} instance that calls the authentication process and
 * notifies with the results.
 */
public class NeverNoteLoginFragment extends Fragment implements NeverNoteLoginView, View.OnClickListener {

    public static final String TAG = NeverNoteLoginFragment.class.getSimpleName();

    private NeverNoteLoginPresenter loginPresenter;

    private Button loginButton;

    public NeverNoteLoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        loginPresenter = new NeverNoteLoginPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_never_note_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        super.onViewCreated(view, savedInstanceState);

        loginButton = (Button) view.findViewById(R.id.fragment_never_note_login_button);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //Call Evernote authentication through the presenter
        if (v.getId() == R.id.fragment_never_note_login_button)
            loginPresenter.onEvernoteAuthenticate(getActivity());
    }

    @Override
    public void enableLoginButton() {
        loginButton.setEnabled(true);
    }

    @Override
    public void disableLoginButton() {
        loginButton.setEnabled(false);
    }

    @Override
    public void onDestroy(){

        loginPresenter.setLoginView(null);
        loginPresenter = null;
        super.onDestroy();
    }

}
