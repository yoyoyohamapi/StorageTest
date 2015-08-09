package com.storagetest.utils;

import android.os.AsyncTask;

import com.storagetest.entity.Base;


/**
 * LocalSaveTask
 * Desc:
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-28
 * Time: 19:38
 */
public class LocalSaveTask extends AsyncTask<Void, Void, Void> {
    private final LocalSaveCallback cacheSaveCallback;


    public LocalSaveTask(final LocalSaveCallback cacheSaveCallback) {
        this.cacheSaveCallback = cacheSaveCallback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        cacheSaveCallback.done();
    }

    /**
     * 存储回调接口
     */
    public interface LocalSaveCallback<T extends Base> {
        void done();
    }

}
