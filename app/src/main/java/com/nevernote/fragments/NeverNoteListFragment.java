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

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.evernote.edam.type.Note;
import com.nevernote.NeverNoteMainNavigator;
import com.nevernote.R;
import com.nevernote.activities.NeverNoteMainActivity;
import com.nevernote.adapters.NeverNoteListAdapter;
import com.nevernote.presenters.NeverNoteListPresenterImpl;
import com.nevernote.presenters.NeverNoteListPresenter;
import com.nevernote.utils.DividerItemDecoration;
import com.nevernote.utils.OnRecyclerViewItemClickListener;
import com.nevernote.views.NeverNoteListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class NeverNoteListFragment extends Fragment implements NeverNoteListView, OnRecyclerViewItemClickListener {

    private ProgressBar progressBar;

    private RecyclerView mRecyclerView;
    private NeverNoteListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private NeverNoteListPresenter listPresenter;

    private NeverNoteMainNavigator navigator;

    private List<Note> notes;

    public static Fragment newInstance(){
        return new NeverNoteListFragment();
    }

    public NeverNoteListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        notes = new ArrayList<>();
        listPresenter = new NeverNoteListPresenterImpl(this);
    }

    @Override
    public void onAttach(Activity activity){

        super.onAttach(activity);
        try {
            navigator = ((NeverNoteMainActivity) activity).getNavigator();
        } catch (ClassCastException e){}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_never_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        super.onViewCreated(view, savedInstanceState);

        progressBar = (ProgressBar) view.findViewById(R.id.fragment_never_note_list_progress);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_never_note_list_recycler_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new NeverNoteListAdapter(notes);
        mAdapter.setItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        if (notes.isEmpty())
            listPresenter.retrieveNotes();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_never_note_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateNotes(List<Note> notesList) {

        notes.clear();
        notes.addAll(notesList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecyclerViewItemClicked(View view, int position) {

        final Note note = notes.get(position);
        navigator.showNoteContentFragment(note.getGuid());
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy(){

        listPresenter.setListView(null);
        listPresenter = null;
        super.onDestroy();
    }
}
