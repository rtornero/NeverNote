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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evernote.edam.type.Notebook;
import com.nevernote.R;

import java.util.List;

/**
 * Created by Roberto on 27/7/15.
 *
 * Simple spinner adapter to display the names of a list of Notebooks and
 * allow to select one.
 */
public class NeverNoteNotebookSpinnerAdapter extends ArrayAdapter<Notebook> {

    private final int dropdownPadding;

    public NeverNoteNotebookSpinnerAdapter(Context context, List<Notebook> objects) {
        super(context, android.R.layout.simple_spinner_item, objects);

        //Padding only for dropdown
        dropdownPadding = (int) context.getResources().getDimension(R.dimen.fragment_list_item_padding);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        final TextView spinnerTextView = new TextView(getContext());
        spinnerTextView.setText(getItem(position).getName());
        return spinnerTextView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){

        final TextView spinnerTextView = new TextView(getContext());
        spinnerTextView.setText(getItem(position).getName());
        spinnerTextView.setPadding(dropdownPadding, dropdownPadding, dropdownPadding, dropdownPadding);
        return spinnerTextView;
    }
}
