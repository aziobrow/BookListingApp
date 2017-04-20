package com.example.android.booklistingapp;

/**
 * Created by teacher on 4/2/17.
 */

public class Book {
    private String mTitle;
    private String mAuthor;

    public Book(String title, String author) {
        mTitle = title;
        mAuthor = author;
    }

    public String getmTitle()   {   return mTitle;   }

    public String getmAuthor()  {   return mAuthor;  }
}
