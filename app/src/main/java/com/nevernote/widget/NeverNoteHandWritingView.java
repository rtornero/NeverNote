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
package com.nevernote.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.nevernote.interfaces.OnBitmapForOCRSaveListener;

/**
 * Created by Roberto on 28/7/15.
 *
 * Special implementation of {@link View} that allows drawing by gestures on a canvas. It is used for OCR
 * purposes so the implementation isn't as polished as it should be (applying edge-softening and other features
 * resulted on a worse recognition...)
 */
public class NeverNoteHandWritingView extends View {

    /**
     * Sets our drawing appearance, color, width.
     */
	private Paint mHandWritingPaint;

    /**
     * Contains a collection of points that represent our drawing
     */
	private Path mHandWritingPath;

    /**
     * Measured width and height to create a screenshot with these dimensions
     */
	private int mCanvasWidth, mCanvasHeight;

    /**
     * These values controls our finger position from the beginning to the end of our gesture
     */
	private float mX, mY;

    /**
     * Number of pixels to discard small movements
     */
	private static final float TOUCH_TOLERANCE = 4;

    /**
     * The width of our drawing lines
     */
    private static final int STROKE_WIDTH = 8;

    /**
     * Value that allows to get density independent dimensions
     */
	private float mDensityMultiplier;

	public NeverNoteHandWritingView(Context context) {
		super(context);
		init();
	}

	public NeverNoteHandWritingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NeverNoteHandWritingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

    /**
     * Init all of our elements
     */
	private void init(){

		setDrawingCacheEnabled(true);
		setLayerType(View.LAYER_TYPE_HARDWARE, null);

		mDensityMultiplier = getResources().getDisplayMetrics().density;

		mHandWritingPaint = new Paint();
		mHandWritingPaint.setColor(Color.BLACK);
		mHandWritingPaint.setStyle(Paint.Style.STROKE);
		mHandWritingPaint.setStrokeWidth(STROKE_WIDTH * mDensityMultiplier);

		mHandWritingPath = new Path();

	}

    @Override
	public void onSizeChanged(int w, int h, int oldw, int oldh){

		super.onSizeChanged(w, h, oldw, oldh);

		mCanvasWidth = w;
		mCanvasHeight = h;
	}

	/**
	 *
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
        //Draw a white background
		canvas.drawColor(Color.WHITE);
        //Draw our collection of points using our own Paint
		canvas.drawPath(mHandWritingPath, mHandWritingPaint);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

        //Control gestures
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchStart(x, y);
				postInvalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touchMove(x, y);
				postInvalidate();
				break;
			case MotionEvent.ACTION_UP:
				touchEnd();
				postInvalidate();
				break;
		}
		return true;
	}

    /**
     * When the gesture begins, we set our first point of our collection
     * @param x
     * @param y
     */
	private void touchStart(float x, float y) {

		mHandWritingPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

    /**
     * When moving the finger, update our values with a quadratic bezier (smooth drawing of curves)
     * discarding small movements.
     *
     * @param x
     * @param y
     */
	private void touchMove(float x, float y) {

		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {

			mHandWritingPath.quadTo(mX, mY, (x + mX) / 2f, (y + mY) / 2f);
			mX = x;
			mY = y;
		}
	}

    /**
     * When gesture ends, finish the points collection drawing a line
     */
	private void touchEnd() {

		mHandWritingPath.lineTo(mX, mY);
	}

    /**
     * Clear our collection of points to start a new drawing
     */
    public void erasePath(){

        mHandWritingPath.reset();
        postInvalidate();
    }

	/**
	 * Retrieves asynchronously a screenshot of the current drawing (as a Bitmap object)
     *
	 * @param bitmapForOCRSaveListener detects when the bitmap has been retrieved
	 */
	public void processBitmapAsync(final OnBitmapForOCRSaveListener bitmapForOCRSaveListener){

		new AsyncTask<Void,Void,Bitmap>(){

			@Override
			protected Bitmap doInBackground(Void... params) {

				Canvas canvas;
                Bitmap bitmap;
				try {
					bitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight, Bitmap.Config.ARGB_8888);
					canvas = new Canvas(bitmap);
					canvas.drawColor(Color.WHITE);
					canvas.drawPath(mHandWritingPath, mHandWritingPaint);

				} catch (OutOfMemoryError e){
					return null;
				}

				return bitmap;
			}

			public void onPostExecute(Bitmap bitmap){

				if (bitmapForOCRSaveListener != null){
                    bitmapForOCRSaveListener.onBitmapSaved(bitmap);
				}
			}

		}.execute();

	}

}
