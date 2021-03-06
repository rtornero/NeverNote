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
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.nevernote.NeverNoteMainNavigator;
import com.nevernote.R;
import com.nevernote.activities.NeverNoteMainActivity;
import com.nevernote.adapters.NeverNoteListAdapter;
import com.nevernote.interfaces.OnNoteCreateListener;
import com.nevernote.presenters.NeverNoteListPresenter;
import com.nevernote.presenters.NeverNoteListPresenterImpl;
import com.nevernote.utils.DividerItemDecoration;
import com.nevernote.utils.OnRecyclerViewItemClickListener;
import com.nevernote.views.NeverNoteListView;

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
    private LinearLayoutManager mLayoutManager;

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

    /**
     * Scroll listener that makes this RecyclerView have an 'endless scroll'. If the user
     * gets to the bottom of the scroll, we detect it and ask for more Notes to add to our list.
     *
     * This is the usual behavior for requests with pagination.
     */
    private RecyclerView.OnScrollListener pageScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            //Get the total items that are in our adapter
            final int totalItem = mLayoutManager.getItemCount();
            if (totalItem == 0)
                return;

            //Get the last visible item
            final int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

            /*
            If the last item that is visible is the same as the last item of the list,
            we have reached the bottom of the scroll and we need to ask for more notes.
             */
            if (! listPresenter.isLoading()
                    && lastVisibleItem == totalItem - 1) {
                listPresenter.loadNotes();
            }
        }
    };

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

        mAdapter = new NeverNoteListAdapter(view.getContext(), listPresenter.getNotes());
        mAdapter.setItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        //Set a scroll listener to detect when the user has scrolled to the bottom and we need to load more info
        mRecyclerView.addOnScrollListener(pageScrollListener);

        if (listPresenter.getNotes().isEmpty())
            listPresenter.loadFirstNotes();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_never_note_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        NoteFilter filter = null;

        //Handle the menu option clicked
        int id = item.getItemId();
        switch(id){

            //Create the appropriate filter depending on the selected menu option
            case R.id.action_order_by_title:
                filter = new NoteFilter();
                filter.setOrder(NoteSortOrder.TITLE.getValue());
                break;
            case R.id.action_order_by_date_created:
                filter = new NoteFilter();
                filter.setOrder(NoteSortOrder.CREATED.getValue());
                break;
            case R.id.action_order_by_date_updated:
                filter = new NoteFilter();
                filter.setOrder(NoteSortOrder.UPDATED.getValue());
        }

        //Just reload data if we have selected a filtering option
        if (filter != null)
            listPresenter.setSortFilter(filter);

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
        final NoteMetadata note = listPresenter.getNotes().get(position);
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
            listPresenter.loadFirstNotes();
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
