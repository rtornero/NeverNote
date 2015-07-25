package com.nevernote;

import android.support.v4.app.FragmentActivity;

/**
 * Created by Roberto on 25/7/15.
 */
public interface NeverNoteMainNavigator {

    void setActivity(FragmentActivity activity);
    void showNoteListFragment();
    void showNoteContentFragment(String noteGuid);

}
