package com.wgu.zbentz2.semestertracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.wgu.zbentz2.semestertracker.databasecontentprovider";

    public static final String ASSESSMENT_CONTENT_ITEM = "foo";
    public static final String ASSESSMENT_PATH = DatabaseOpenHelper.ASSESSMENT_TABLE_NAME;

    public static final String COURSE_CONTENT_ITEM = "bar";
    public static final String COURSE_PATH = DatabaseOpenHelper.COURSE_TABLE_NAME;

    public static final String NOTE_CONTENT_ITEM = "biz";
    public static final String NOTE_PATH = DatabaseOpenHelper.NOTE_TABLE_NAME;

    public static final String TERM_CONTENT_ITEM = "baz";
    private static final String TERM_PATH = DatabaseOpenHelper.TERM_TABLE_NAME;

    public static final int TERMS = 1;
    public static final int TERMS_ID = 2;
    public static final int COURSES = 3;
    public static final int COURSES_ID = 4;
    public static final int NOTES = 5;
    public static final int NOTES_ID = 6;
    public static final int ASSESSMENTS = 7;
    public static final int ASSESSMENTS_ID = 8;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final Uri TERMS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TERM_PATH);
    public static final Uri COURSES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSE_PATH);
    public static final Uri NOTES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTE_PATH);
    public static final Uri ASSESSMENTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ASSESSMENT_PATH);

    private SQLiteDatabase DATABASE;

    static {

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
        DATABASE = dbHelper.getWritableDatabase();

        return true;
    }

    @Override public Cursor query(Uri uri,
                                  String[] projection,
                                  String selection,
                                  String[] selectionArgs,
                                  String sortOrder) {

        switch(uriMatcher.match(uri)) {

            case TERMS:

                return DATABASE.query(
                    DatabaseOpenHelper.TERM_TABLE_NAME,
                    DatabaseOpenHelper.TERM_COLUMNS,
                    selection,
                    null,
                    null,
                    null,
                    DatabaseOpenHelper.TERM_ID + " ASC"
                );

            case COURSES:

                return DATABASE.query(
                    DatabaseOpenHelper.COURSE_TABLE_NAME,
                    DatabaseOpenHelper.COURSE_COLUMNS,
                    selection,
                    null,
                    null,
                    null,
                    DatabaseOpenHelper.COURSE_ID + " ASC"
                );

            case NOTES:

                return DATABASE.query(
                    DatabaseOpenHelper.NOTE_TABLE_NAME,
                    DatabaseOpenHelper.NOTE_COLUMNS,
                    selection,
                    null,
                    null,
                    null,
                    DatabaseOpenHelper.NOTE_ID + " ASC"
                );

            case ASSESSMENTS:

                return DATABASE.query(
                    DatabaseOpenHelper.ASSESSMENT_TABLE_NAME,
                    DatabaseOpenHelper.ASSESSMENT_COLUMNS,
                    selection,
                    null,
                    null,
                    null,
                    DatabaseOpenHelper.ASSESSMENT_ID + " ASC"
                );

            default:

                return null;

        }

    }

    @Override public String getType(Uri uri) {

        return null;

    }

    @Override public Uri insert(Uri uri, ContentValues values) {

        long object_id;

        switch(uriMatcher.match(uri)) {

            case TERMS:

                object_id = DATABASE.insert(
                    DatabaseOpenHelper.TERM_TABLE_NAME,
                    null,
                    values
                );

                return Uri.parse(TERM_PATH + "/" + object_id);

            case COURSES:

                object_id = DATABASE.insert(
                    DatabaseOpenHelper.COURSE_TABLE_NAME,
                    null,
                    values
                );

                return Uri.parse(COURSE_PATH + "/" + object_id);

            case NOTES:

                object_id = DATABASE.insert(
                    DatabaseOpenHelper.NOTE_TABLE_NAME,
                    null,
                    values
                );

                return Uri.parse(NOTE_PATH + "/" + object_id);

            case ASSESSMENTS:

                object_id = DATABASE.insert(
                    DatabaseOpenHelper.ASSESSMENT_TABLE_NAME,
                    null,
                    values
                );

                return Uri.parse(ASSESSMENT_PATH + "/" + object_id);

            default:

                return null;

        }

    }

    @Override public int delete(Uri uri, String selection, String[] selectionArgs) {

        switch (uriMatcher.match(uri)) {

            case TERMS:

                DATABASE.delete(
                    DatabaseOpenHelper.TERM_TABLE_NAME,
                    selection,
                    selectionArgs
                );


            case COURSES:

                return DATABASE.delete(
                    DatabaseOpenHelper.COURSE_TABLE_NAME,
                    selection,
                    selectionArgs
                );

            case NOTES:

                return DATABASE.delete(
                    DatabaseOpenHelper.NOTE_TABLE_NAME,
                    selection,
                    selectionArgs
                );

            case ASSESSMENTS:

                return DATABASE.delete(
                    DatabaseOpenHelper.ASSESSMENT_TABLE_NAME,
                    selection,
                    selectionArgs
                );

            default:

                return 0;

        }

    }

    @Override public int update(Uri uri,
                                ContentValues values,
                                String selection,
                                String[] selectionArgs) {

        switch (uriMatcher.match(uri)) {

            case TERMS:

                DATABASE.update(
                    DatabaseOpenHelper.TERM_TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
                );


            case COURSES:

                return DATABASE.update(
                    DatabaseOpenHelper.COURSE_TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
                );

            case NOTES:

                return DATABASE.update(
                    DatabaseOpenHelper.NOTE_TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
                );

            case ASSESSMENTS:

                return DATABASE.update(
                    DatabaseOpenHelper.ASSESSMENT_TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
                );

            default:

                return 0;

        }

    }

}
