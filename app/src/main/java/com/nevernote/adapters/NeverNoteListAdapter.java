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
package com.nevernote.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evernote.edam.type.Note;
import com.nevernote.R;
import com.nevernote.utils.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by Roberto on 24/7/15.
 */
public class NeverNoteListAdapter extends RecyclerView.Adapter<NeverNoteListAdapter.ViewHolder> {

    private List<Note> neverNotesList;
    private OnRecyclerViewItemClickListener itemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView noteTitleTextView;

        public ViewHolder(View v) {
            super(v);
            noteTitleTextView = (TextView) v.findViewById(R.id.fragment_never_note_list_item_title);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onRecyclerViewItemClicked(v, getAdapterPosition());
        }
    }

    public void setItemClickListener(OnRecyclerViewItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public NeverNoteListAdapter(List<Note> nNotesList) {
        neverNotesList = nNotesList;
    }

    @Override
    public NeverNoteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {

        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_never_note_list_item, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Note note = neverNotesList.get(position);
        holder.noteTitleTextView.setText(note.getTitle());

    }

    @Override
    public int getItemCount() {
        return neverNotesList.size();
    }
}
