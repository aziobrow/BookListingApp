package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by teacher on 4/2/17.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    /** Query URL */
    private String mUrl;
    List<Book> mBooks;

    /**
     * Constructs a new BookLoader
     *
     * @param context of the activity
     * @param url to load data from
     */
    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        mBooks = QueryUtils.fetchBookData(mUrl);
        return mBooks;
    }

}
