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
package com.nevernote.utils;

import android.content.Context;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

/**
 * Created by Roberto on 28/7/15.
 *
 * A helper class to connect with a REST API that performs text recognition, http://ocrapiservice.com/.
 * It is not a free API, but it has a trial period with 100 executions, so it should fit
 * our needs for testing purposes.
 *
 * I've made some research regarding OCR on Android, and there were better options such as Tesseract (free offline
 * library built with NDK) but they exceeded our available implementation time. I'll try this approach on my own and
 * compile Tesseract for learning purposes.
 *
 * The API of choice is a little prone to recognition errors, but almost all my 'HELLO' tests returned the
 * right result.
 */
public class OCRApiClient {

    public static final String OCR_API_URL = "http://api.ocrapiservice.com/1.0/rest/ocr";

    /**
     * Param names for the API endpoint call.
     */
    private static final String OCR_PARAM_IMAGE = "image";
    private static final String OCR_PARAM_LANGUAGE = "language";
    private static final String OCR_PARAM_APIKEY = "apikey";

    /**
     * API key for authentication, it has 100 available executions
     */
    private static final String OCR_VALUE_APIKEY = "bUSEnMmcra";
    private static final String OCR_VALUE_LANGUAGE = "en";

    /**
     * This method uses Ion (a free http request library) to post a multipart
     * form with the required information (i.e. the image file to recognise). It uses
     * a callback to return asynchronously.
     *
     * @param context
     * @param filePath the path to the image file
     * @param convertCallback
     */
    public static void convertToText(final Context context, final String filePath,
                              final FutureCallback<String> convertCallback) {

        Ion
                .with(context)
                .load(OCR_API_URL)
                .setMultipartFile(OCR_PARAM_IMAGE, new File(filePath))
                .setMultipartParameter(OCR_PARAM_LANGUAGE, OCR_VALUE_LANGUAGE)
                .setMultipartParameter(OCR_PARAM_APIKEY, OCR_VALUE_APIKEY)
                .asString()
                .setCallback(convertCallback);
    }

}
