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
package com.nevernote.presenters;

import android.content.Context;
import android.graphics.Bitmap;

import com.koushikdutta.async.future.FutureCallback;
import com.nevernote.views.NeverNoteOCRView;

/**
 * Created by Roberto on 28/7/15.
 *
 * This interface takes control of the presentation layer of a {@link com.nevernote.fragments.NeverNoteOCRFragment}
 * and exposes methods to start a text recognition process.
 */
public interface NeverNoteOCRPresenter {

    /**
     * Starts the text recognition process by calling
     * {@link com.nevernote.utils.OCRApiClient#convertToText(Context, String, FutureCallback)}
     *
     * @param context the current Context
     * @param bitmap the bitmap to send as a file to the recogniser
     */
    void processOCRForBitmapAsync(Context context, Bitmap bitmap);

    /**
     * Sets the view interface to the presenter
     * @param ocrView
     */
    void setOCRView(NeverNoteOCRView ocrView);
}
