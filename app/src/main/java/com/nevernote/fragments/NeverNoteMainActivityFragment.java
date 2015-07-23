package com.nevernote.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nevernote.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NeverNoteMainActivityFragment extends Fragment {

    public NeverNoteMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_never_note_main, container, false);
    }
}
