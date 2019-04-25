package com.wgu.zbentz2.semestertracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseContentProvider extends ContentProvider {

    private static final String AUTHORITY;

    public static final String ASSESSMENT_CONTENT_ITEM;
    public static final String ASSESSMENT_PATH;

    public static final String COURSE_CONTENT_ITEM;
    public static final String COURSE_PATH;

    public static final String NOTE_CONTENT_ITEM;
    public static final String NOTE_PATH;

    public static final String TERM_CONTENT_ITEM;
    private static final String TERM_PATH;

    public static final int TERMS;
    public static final int TERMS_ID;
    public static final int COURSES;
    public static final int COURSES_ID;
    public static final int NOTES;
    public static final int NOTES_ID;
    public static final int ASSESSMENTS;
    public static final int ASSESSMENTS_ID;

    private static final UriMatcher uriMatcher;

    public static final Uri TERMS_CONTENT_URI;
    public static final Uri COURSES_CONTENT_URI;
    public static final Uri NOTES_CONTENT_URI;
    public static final Uri ASSESSMENTS_CONTENT_URI;

    static {

        AUTHORITY = "com.wgu.zbentz2.semestertracker.databasecontentprovider";

        ASSESSMENT_CONTENT_ITEM = "foo";
        ASSESSMENT_PATH = DatabaseOpenHelper.ASSESSMENT_TABLE_NAME;

        COURSE_CONTENT_ITEM = "bar";
        COURSE_PATH = DatabaseOpenHelper.COURSE_TABLE_NAME;

        NOTE_CONTENT_ITEM = "biz";
        NOTE_PATH = DatabaseOpenHelper.NOTE_TABLE_NAME;

        TERM_CONTENT_ITEM = "baz";
        TERM_PATH = DatabaseOpenHelper.TERM_TABLE_NAME;

        TERMS = 1;
        TERMS_ID = 2;
        COURSES = 3;
        COURSES_ID = 4;
        NOTES = 5;
        NOTES_ID = 6;
        ASSESSMENTS = 7;
        ASSESSMENTS_ID = 8;

        TERMS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TERM_PATH);
        COURSES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSE_PATH);
        NOTES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTE_PATH);
        ASSESSMENTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ASSESSMENT_PATH);

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, TERM_PATH, TERMS);
        uriMatcher.addURI(AUTHORITY, TERM_PATH + "/#", TERMS_ID);
        uriMatcher.addURI(AUTHORITY, COURSE_PATH, COURSES);
        uriMatcher.addURI(AUTHORITY, COURSE_PATH + "/#", COURSES_ID);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH + "/#", NOTES_ID);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_PATH, ASSESSMENTS);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_PATH + "/#", ASSESSMENTS_ID);

    }

    @Override public boolean onCreate() {

        DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(getContext());
        SQLiteDatabase DATABASE = dbHelper.getWritableDatabase();

        return true;
    }

    @Override public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return null;

    }

    @Override public String getType(Uri uri) {

        return null;

    }

    @Override public Uri insert(Uri uri, ContentValues values) {

        return null;

    }

    @Override public int delete(Uri uri, String selection, String[] selectionArgs) {

        return 0;

    }

    @Override public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        return 0;

    }

}
