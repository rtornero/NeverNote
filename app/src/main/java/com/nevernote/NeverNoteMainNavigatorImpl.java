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

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.nevernote.activities.NeverNoteOCRActivity;
import com.nevernote.fragments.NeverNoteContentFragment;
import com.nevernote.fragments.NeverNoteCreateDialogFragment;
import com.nevernote.fragments.NeverNoteListFragment;
import com.nevernote.fragments.NeverNoteOCRFragment;
import com.nevernote.interfaces.OnNoteCreateListener;

/**
 * Created by Roberto on 25/7/15.
 *
 * Implementation for {@link NeverNoteMainNavigator} that performs navigation
 * for our {@link com.nevernote.activities.NeverNoteMainActivity}
 */
public class NeverNoteMainNavigatorImpl implements NeverNoteMainNavigator {

    private static final int FRAGMENT_CONTAINER_ID = R.id.fragment_never_note_container;

    private FragmentActivity activity;

    public NeverNoteMainNavigatorImpl(FragmentActivity activity){
        this.activity = activity;
    }

    @Override
    public void showNoteListFragment(){

        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(FRAGMENT_CONTAINER_ID, NeverNoteListFragment.newInstance())
                .commit();

        activity.supportInvalidateOptionsMenu();
    }

    @Override
    public void showNoteContentFragment(String noteGuid){

        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right)
                .replace(FRAGMENT_CONTAINER_ID, NeverNoteContentFragment.newInstance(noteGuid))
                .addToBackStack(null)
                .commit();

        activity.supportInvalidateOptionsMenu();
    }

    @Override
    public void showNoteCreateDialogFragment(OnNoteCreateListener onNoteCreateListener) {

        final DialogFragment dialogFragment =
                (DialogFragment) activity
                .getSupportFragmentManager()
                .findFragmentByTag(NeverNoteCreateDialogFragment.TAG);
        if (dialogFragment != null)
            activity
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .remove(dialogFragment).commit();

        NeverNoteCreateDialogFragment.newInstance(onNoteCreateListener)
                .show(activity.getSupportFragmentManager(), NeverNoteCreateDialogFragment.TAG);
    }

    @Override
    public void showNoteOCRActivity(boolean isTitleOrContent) {

        final Intent intent = new Intent(activity,
                NeverNoteOCRActivity.class);
        intent.putExtra(NeverNoteOCRActivity.TITLE_OR_CONTENT, isTitleOrContent);
        activity.startActivityForResult(intent, NeverNoteCreateDialogFragment.OCR_REQUEST);
    }

    @Override
    public void setActivity(FragmentActivity activity){
        this.activity = activity;
    }
}
