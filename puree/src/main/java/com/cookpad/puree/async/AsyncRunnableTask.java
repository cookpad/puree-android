package com.cookpad.puree.async;

import android.os.AsyncTask;

public abstract class AsyncRunnableTask extends AsyncTask<Void, Void, Void> implements Runnable {

    @Override
    protected Void doInBackground(Void... params) {
        run();
        return null;
    }
}
