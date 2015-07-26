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

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.evernote.client.android.EvernoteOAuthActivity;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.login.EvernoteLoginActivity;
import com.nevernote.activities.NeverNoteLoginActivity;
import com.nevernote.activities.NeverNoteMainActivity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Roberto on 23/7/15.
 *
 * Implementation of {@link Application.ActivityLifecycleCallbacks} that allows to
 * detect the Activity transitions and act when necessary.
 */
public class NeverNoteActivityListener implements Application.ActivityLifecycleCallbacks {

    /**
     * List of activities whose lifecycle callbacks shouldn't be listened to
     */
    private static final List<Class<? extends Activity>> IGNORED_ACTIVITIES = Arrays.asList(
            NeverNoteLoginActivity.class,
            EvernoteLoginActivity.class,
            EvernoteOAuthActivity.class
    );

    /**
     * The Intent that is starting. When there is no Evernote session and this Intent
     * should not be ignored, it is persisted for future usage.
     */
    private Intent currentIntent;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        /*
        If there is no user session available on Evernote and the activity
        that is creating should be listened to, then start our own login screen.
         */
        if (! EvernoteSession.getInstance().isLoggedIn()
                && ! shouldBeIgnored(activity)){
            currentIntent = activity.getIntent();
            activity.startActivity(new Intent(activity,
                    NeverNoteLoginActivity.class));
            activity.finish();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

        /*
        If there is user session available on Evernote and the activity
        that is going to the background (or finishing) is the login process,
        then start our main activity or the Intent that was persisted before.
         */
        if (activity instanceof NeverNoteLoginActivity
                && EvernoteSession.getInstance().isLoggedIn()) {
            if (currentIntent != null) {
                activity.startActivity(currentIntent);
                currentIntent = null;

            } else
                activity.startActivity(new Intent(activity,
                        NeverNoteMainActivity.class));
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    /**
     * Checks if an Activity is contained in our ignored list.
     * @param activity the Activity to check if is contained or not
     * @return true if activity is contained.
     */
    private boolean shouldBeIgnored(Activity activity) {
        return IGNORED_ACTIVITIES.contains(activity.getClass());
    }
}
