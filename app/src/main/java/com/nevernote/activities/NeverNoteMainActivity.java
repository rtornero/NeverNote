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

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nevernote.NeverNoteMainNavigator;
import com.nevernote.NeverNoteMainNavigatorImpl;
import com.nevernote.R;
import com.nevernote.fragments.NeverNoteListFragment;

public class NeverNoteMainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private NeverNoteMainNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_never_note_main);

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        navigator = new NeverNoteMainNavigatorImpl(this);

        if (savedInstanceState == null)
            navigator.showNoteListFragment();

    }

    @Override
    public void onBackStackChanged() {

        final boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
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
