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
package com.nevernote.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Roberto on 24/7/15.
 */
public class AbortableCountDownLatch extends CountDownLatch {

    protected boolean aborted = false;

    public AbortableCountDownLatch(int count) {
        super(count);
    }

    /**
     * Unblocks all threads waiting on this latch and cause them to receive an
     * AbortedException.  If the latch has already counted all the way down,
     * this method does nothing.
     */
    public void abort() {

        if (getCount()==0 )
            return;

        this.aborted = true;
        while (getCount() > 0)
            countDown();
    }


    @Override
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {

        final boolean ret = super.await(timeout,unit);
        if (aborted)
            throw new AbortedException();
        return ret;
    }

    @Override
    public void await() throws InterruptedException {
        super.await();
        if (aborted)
            throw new AbortedException();
    }


    public static class AbortedException extends InterruptedException {
        public AbortedException() {}

        public AbortedException(String detailMessage) {
            super(detailMessage);
        }
    }
}
