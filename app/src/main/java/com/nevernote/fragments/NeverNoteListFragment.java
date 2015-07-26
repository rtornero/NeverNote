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
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
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
import com.nevernote.interfaces.OnNoteCreateListener;
import com.nevernote.presenters.NeverNoteListPresenterImpl;
import com.nevernote.presenters.NeverNoteListPresenter;
import com.nevernote.utils.DividerItemDecoration;
import com.nevernote.utils.OnRecyclerViewItemClickListener;
import com.nevernote.views.NeverNoteListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roberto on 24/7/15.
 *
 * This {@link Fragment} instance displays a list of {@link Note} through a {@link RecyclerView} and its Adapter.
 * It implements the MVP pattern with a {@link NeverNoteListPresenter} that retrieves the list of notes from Evernote's SDK
 * and notifies with new changes. It also implements our own {@link OnNoteCreateListener} interface to
 * detect when the user has finished creating his new note and reload the list of notes.
 *
 * Note creation is invoked by pressing the {@link FloatingActionButton} that is displayed
 * at the bottom right of the screen.
 */
public class NeverNoteListFragment extends Fragment implements NeverNoteListView, OnRecyclerViewItemClickListener,
        View.OnClickListener, OnNoteCreateListener {

    private ProgressBar progressBar;

    /**
     * This is the new Android's component to define scrolling views with multiple items.
     */
    private RecyclerView mRecyclerView;
    private NeverNoteListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * Android 5 UI component following material design guidelines to invoke
     * {@link Note} creation process.
     */
    private FloatingActionButton addNoteActionButton;

    private NeverNoteListPresenter listPresenter;

    /**
     * The same {@link NeverNoteMainNavigator} instance as defined in the {@link NeverNoteMainActivity}.
     * It is used to show both note details screen and note creation dialog.
     */
    private NeverNoteMainNavigator navigator;

    public static Fragment newInstance(){
        return new NeverNoteListFragment();
    }

    public NeverNoteListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        listPresenter = new NeverNoteListPresenterImpl(this);
    }

    @Override
    public void onAttach(Activity activity){

        super.onAttach(activity);
        try {
            //Handler to the class obtained through the Activity
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

        addNoteActionButton = (FloatingActionButton) view.findViewById(R.id.fragment_never_note_list_add_button);
        addNoteActionButton.setRippleColor(getResources().getColor(R.color.evernote_green));
        addNoteActionButton.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_never_note_list_recycler_view);
        //Show a separator between each RecyclerView's item
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        //For performance
        mRecyclerView.setHasFixedSize(true);

        //Vertical presentation of items
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NeverNoteListAdapter(listPresenter.getNotes());
        mAdapter.setItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        if (listPresenter.getNotes().isEmpty())
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
    public void updateNotes() {

        //List of notes has changed, so notify the adapter to refill the recycler view
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecyclerViewItemClicked(View view, int position) {

        //Retrieve the pressed note and show its details
        final Note note = listPresenter.getNotes().get(position);
        navigator.showNoteContentFragment(note.getGuid());
    }

    @Override
    public void onClick(View v) {

        //When the floating button has been pressed, show the note creation screen.
        if (v.getId() == R.id.fragment_never_note_list_add_button)
            navigator.showNoteCreateDialogFragment(this);

    }

    @Override
    public void onNoteCreated(Note note) {

        //A new note has been created, if it is not null (maybe the Dialog was dismissed from the system)
        //reload the list of notes
        if (note != null)
            listPresenter.retrieveNotes();
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
