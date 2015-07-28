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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.nevernote.NeverNoteMainNavigator;
import com.nevernote.NeverNoteMainNavigatorImpl;
import com.nevernote.R;
import com.nevernote.fragments.NeverNoteCreateDialogFragment;

/**
 * Created by Roberto on 24/7/15.
 *
 * Main container for our application. It is the access point and has a {@link NeverNoteMainNavigator} instance
 * to process {@link android.support.v4.app.Fragment} transactions.
 */
public class NeverNoteMainActivity extends AppCompatActivity
        implements FragmentManager.OnBackStackChangedListener {

    /**
     * Navigator allows to decouple view transactions from the Activity
     */
    private NeverNoteMainNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_never_note_main);

        //Add on back stack change listener to determine the action bar's appearance when changing fragments
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        navigator = new NeverNoteMainNavigatorImpl(this);
        if (savedInstanceState == null)
            navigator.showNoteListFragment();

    }

    /**
     * Used to listen to the returning of the OCR request
     * @param requestCode
     * @param responseCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {

        if (requestCode == NeverNoteCreateDialogFragment.OCR_REQUEST){

            //Deliver the message from onActivityResult to the fragment
            final NeverNoteCreateDialogFragment dialogFragment =
                    (NeverNoteCreateDialogFragment) getSupportFragmentManager().findFragmentByTag(NeverNoteCreateDialogFragment.TAG);
            if (dialogFragment != null){
                dialogFragment.onActivityResult(requestCode, responseCode, intent);
            }
        }

        else
            super.onActivityResult(requestCode, responseCode, intent);

    }

    @Override
    public void onBackStackChanged() {

        //If there are one or more Fragments in our stack...
        final boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
        //...we need to hide the title and show the home icon as an up icon
        getSupportActionBar().setDisplayShowTitleEnabled(!canGoBack);
        getSupportActionBar().setDisplayShowHomeEnabled(canGoBack);
        getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDestroy(){

        navigator.setActivity(null);
        navigator = null;
        super.onDestroy();
    }

    public NeverNoteMainNavigator getNavigator() {
        return navigator;
    }

}
