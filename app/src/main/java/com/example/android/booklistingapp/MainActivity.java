package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>>, View.OnClickListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String GOOGLE_BOOKS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    private String mUserQuery = "";

    private static final int BOOK_LOADER_ID = 0;

    private BookAdapter mAdapter;

    private TextView mEmptyStateTextView;

    private ProgressBar mProgressBar;

    private ListView mBookListView;

    private boolean mInitialLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBookListView = (ListView) findViewById(R.id.list);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        mBookListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mEmptyStateTextView.setVisibility(GONE);

        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        mProgressBar.setVisibility(GONE);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(BOOK_LOADER_ID, null, this);
        } else {
            mEmptyStateTextView.setText("No internet connection");
            mEmptyStateTextView.setVisibility(VISIBLE);
        }

        Button button = (Button) findViewById(R.id.search_btn);
        button.setOnClickListener(this);
    }

    public void onClick(View v) {

        EditText userQueryEditText = (EditText) findViewById(R.id.edit_text);

        mUserQuery = userQueryEditText.getText().toString();
        if (mUserQuery == "") {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_LONG).show();
        } else {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                mProgressBar.setVisibility(VISIBLE);
                mEmptyStateTextView.setVisibility(GONE);
                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, this);
            } else {
                mBookListView.setVisibility(GONE);
                mEmptyStateTextView.setText("No internet connection");
                mEmptyStateTextView.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        //return new BookLoader(this, GOOGLE_BOOKS_REQUEST_URL + mUserQuery + "&maxResults=15");
        return new BookLoader(this, GOOGLE_BOOKS_REQUEST_URL + mUserQuery + "&maxResults=15");
    }

    @Override
    public void onLoadFinished
            (Loader<List<Book>> loader, List<Book> books) {
        mProgressBar.setVisibility(GONE);
        mBookListView.setVisibility(VISIBLE);

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }   else {
            if (mInitialLoad) {
                mEmptyStateTextView.setText("No results found. Please try another search term.");
                mEmptyStateTextView.setVisibility(VISIBLE);
            }
        }

        mInitialLoad = true;
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }


}

