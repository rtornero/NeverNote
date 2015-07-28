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
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.koushikdutta.async.future.FutureCallback;
import com.nevernote.utils.OCRApiClient;
import com.nevernote.views.NeverNoteContentView;
import com.nevernote.views.NeverNoteOCRView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Roberto on 28/7/15.
 *
 * Implementation of {@link NeverNoteOCRPresenter}. It has a {@link NeverNoteOCRView} instance
 * to notify the view with the changes in the model, in this case, when the recognition process
 * has ended and returned a result.
 */
public class NeverNoteOCRPresenterImpl implements NeverNoteOCRPresenter {

    /**
     * Directory and file name for our temporary screenshot
     */
    private final static String OCR_TEMP_DIR_NAME = "NeverNote_temp";
    private final static String OCR_TEMP_FILE_NAME = "ocr_text.jpg";

    private NeverNoteOCRView ocrView;

    /**
     * Ion's mechanism to return results from asynchronous
     * executions. This callback is called when the text recognition
     * process has ended.
     */
    private FutureCallback<String> ocrTextCallback = new FutureCallback<String>() {
        @Override
        public void onCompleted(Exception e, String result) {

            ocrView.enableButtons();
            ocrView.hideProgressBar();
            /*
            We notify the view with the text that has been recognised
             */
            ocrView.updateWithOCRText(result);
        }
    };

    public NeverNoteOCRPresenterImpl(NeverNoteOCRView ocrView){
        setOCRView(ocrView);
    }

    /**
     * Starts a new {@link AsyncTask} to compress the bitmap to a File. Once this bitmap is
     * compressed, it calls the {@link OCRApiClient} to start the recognition process using a
     * REST API.
     *
     * @param context
     * @param bitmap to compress as a File and send
     */
    @Override
    public void processOCRForBitmapAsync(final Context context, final Bitmap bitmap) {

        new AsyncTask<Void,Void,String>(){

            @Override
            public void onPreExecute(){

                ocrView.disableButtons();
                ocrView.showProgressBar();
            }

            @Override
            protected String doInBackground(Void... params) {

                /*
                Create our temporary directory if necessary
                 */
                final String root = Environment.getExternalStorageDirectory().toString();
                final File dir = new File(root + File.separator + OCR_TEMP_DIR_NAME);
                if (!dir.exists())
                    dir.mkdirs();

                /*
                Prepare our temporary File
                 */
                final File file = new File(dir, OCR_TEMP_FILE_NAME);
                if (file.exists())
                    file.delete();

                try {
                    /*
                    Compress the bitmap in our File
                     */
                    final FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                    return file.getAbsolutePath();

                } catch (Exception e) {}

                return null;
            }

            @Override
            public void onPostExecute(final String path){

                /*
                If the bitmap was successfully compressed, we should
                retrieve its absolute path to upload to our OCR API.
                 */
                if (!TextUtils.isEmpty(path))
                    OCRApiClient.convertToText(context, path, ocrTextCallback);

                else {
                    ocrView.enableButtons();
                    ocrView.hideProgressBar();
                    ocrView.onError(null);
                }
            }

        }.execute();
    }

    @Override
    public void setOCRView(NeverNoteOCRView ocrView) {
        this.ocrView = ocrView;
    }

}
