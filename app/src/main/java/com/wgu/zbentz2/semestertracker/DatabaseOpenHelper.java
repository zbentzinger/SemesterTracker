package com.wgu.zbentz2.semestertracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    /***
     * Generic Strings
     ***/
    private static final String DB_NAME;
    private static final int DB_VERSION;

    private static final String DROP_TABLE;
    private static final String CREATE_TABLE;
    private static final String GENERIC_ID;

    /***
     * Table Strings
    ***/
    public static final String TERM_TABLE_NAME;
    public static final String TERM_ID;
    public static final String TERM_NAME;
    public static final String TERM_START_DATE;
    public static final String TERM_END_DATE;
    public static final String TERM_ACTIVE;
    public static final String TERM_CREATED_AT;
    public static final String[] TERM_COLUMNS;
    private static final String CREATE_TABLE_TERM;

    public static final String COURSE_TABLE_NAME;
    public static final String COURSE_ID;
    public static final String COURSE_TERM_ID;
    public static final String COURSE_NAME;
    public static final String COURSE_START_DATE;
    public static final String COURSE_END_DATE;
    public static final String COURSE_MENTOR_NAME;
    public static final String COURSE_MENTOR_PHONE;
    public static final String COURSE_MENTOR_EMAIL;
    public static final String COURSE_STATUS;
    public static final String COURSE_NOTIFICATION;
    public static final String COURSE_CREATED_AT;
    public static final String[] COURSE_COLUMNS;
    private static final String CREATE_TABLE_COURSE;

    public static final String NOTE_TABLE_NAME;
    public static final String NOTE_ID;
    public static final String NOTE_COURSE_ID;
    public static final String NOTE_BODY;
    public static final String NOTE_CREATED_AT;
    public static final String[] NOTE_COLUMNS;
    private static final String CREATE_TABLE_NOTE;

    public static final String ASSESSMENT_TABLE_NAME;
    public static final String ASSESSMENT_ID;
    public static final String ASSESSMENT_COURSE_ID;
    public static final String ASSESSMENT_NAME;
    public static final String ASSESSMENT_TYPE;
    public static final String ASSESSMENT_DUE_DATE;
    public static final String ASSESSMENT_NOTIFICATION;
    public static final String ASSESSMENT_CREATED_AT;
    public static final String[] ASSESSMENT_COLUMNS;
    private static final String CREATE_TABLE_ASSESSMENT;

    // I normally don't do it this way, but I haven't tried this before so why not.
    static {

        DB_NAME = "semester_tracker.db";
        DB_VERSION = 1;

        CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
        DROP_TABLE = "DROP TABLE IF EXISTS ";
        GENERIC_ID = "_id";

        TERM_TABLE_NAME = "terms";
        TERM_ID = GENERIC_ID;
        TERM_NAME = "term_name";
        TERM_START_DATE = "term_start_date";
        TERM_END_DATE = "term_end_date";
        TERM_ACTIVE = "term_active";
        TERM_CREATED_AT = "term_created_at";

        TERM_COLUMNS = new String[]{
            TERM_ID,
            TERM_NAME,
            TERM_START_DATE,
            TERM_END_DATE,
            TERM_ACTIVE,
            TERM_CREATED_AT
        };

        CREATE_TABLE_TERM = String.format(
            "%s %s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT," +
                "%s DATE," +
                "%s DATE," +
                "%s INTEGER," +
                "%s TEXT DEFAULT CURRENT_TIMESTAMP" +
            ")",
            CREATE_TABLE,
            TERM_TABLE_NAME,
            TERM_ID,
            TERM_NAME,
            TERM_START_DATE,
            TERM_END_DATE,
            TERM_ACTIVE,
            TERM_CREATED_AT
        );

        COURSE_TABLE_NAME = "courses";
        COURSE_ID = GENERIC_ID;
        COURSE_TERM_ID = "term_id";
        COURSE_NAME = "course_name";
        COURSE_START_DATE = "course_start_date";
        COURSE_END_DATE = "course_end_date";
        COURSE_MENTOR_NAME = "course_mentor_name";
        COURSE_MENTOR_PHONE = "course_mentor_phone";
        COURSE_MENTOR_EMAIL = "course_mentor_email";
        COURSE_STATUS = "course_status";
        COURSE_NOTIFICATION = "course_notification";
        COURSE_CREATED_AT = "course_created_at";

        COURSE_COLUMNS = new String[]{
            COURSE_ID,
            COURSE_TERM_ID,
            COURSE_NAME,
            COURSE_START_DATE,
            COURSE_END_DATE,
            COURSE_MENTOR_NAME,
            COURSE_MENTOR_PHONE,
            COURSE_MENTOR_EMAIL,
            COURSE_STATUS,
            COURSE_NOTIFICATION,
            COURSE_CREATED_AT,

        };

        CREATE_TABLE_COURSE = String.format(
            "%s %s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s INTEGER, " +
                "%s TEXT, " +
                "%s DATE, " +
                "%s DATE, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s INTEGER, " +
                "%s TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (%s) REFERENCES %s(%s)" +
            ")",
            CREATE_TABLE,
            COURSE_TABLE_NAME,
            COURSE_ID,
            COURSE_TERM_ID,
            COURSE_NAME,
            COURSE_START_DATE,
            COURSE_END_DATE,
            COURSE_MENTOR_NAME,
            COURSE_MENTOR_PHONE,
            COURSE_MENTOR_EMAIL,
            COURSE_STATUS,
            COURSE_NOTIFICATION,
            COURSE_CREATED_AT,
            COURSE_TERM_ID,
            TERM_TABLE_NAME,
            TERM_ID
        );

        NOTE_TABLE_NAME = "course_notes";
        NOTE_ID = GENERIC_ID;
        NOTE_COURSE_ID = "course_id";
        NOTE_BODY = "note_body";
        NOTE_CREATED_AT = "note_created_at";

        NOTE_COLUMNS = new String[]{
            NOTE_ID,
            NOTE_COURSE_ID,
            NOTE_BODY,
            NOTE_CREATED_AT
        };

        CREATE_TABLE_NOTE = String.format(
            "%s %s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s INTEGER, " +
                "%s TEXT, " +
                "%s TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (%s) REFERENCES %s(%s)" +
            ")",
            CREATE_TABLE,
            NOTE_TABLE_NAME,
            NOTE_ID,
            NOTE_COURSE_ID,
            NOTE_BODY,
            NOTE_CREATED_AT,
            NOTE_COURSE_ID,
            COURSE_TABLE_NAME,
            COURSE_ID
        );

        ASSESSMENT_TABLE_NAME = "assessments";
        ASSESSMENT_ID = GENERIC_ID;
        ASSESSMENT_COURSE_ID = "course_id";
        ASSESSMENT_NAME = "assessment_name";
        ASSESSMENT_TYPE = "assessment_type";
        ASSESSMENT_DUE_DATE = "assessment_due_date";
        ASSESSMENT_NOTIFICATION = "assessment_notification";
        ASSESSMENT_CREATED_AT = "assessment_created_at";

        ASSESSMENT_COLUMNS = new String[]{
            ASSESSMENT_ID,
            ASSESSMENT_COURSE_ID,
            ASSESSMENT_NAME,
            ASSESSMENT_TYPE,
            ASSESSMENT_DUE_DATE,
            ASSESSMENT_NOTIFICATION,
            ASSESSMENT_CREATED_AT
        };

        CREATE_TABLE_ASSESSMENT = String.format(
            "%s %s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, " +
                "%s DATE, " +
                "%s DATE, " +
                "%s INTEGER, " +
                "%s DATE, " +
                "%s TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (%s) REFERENCES %s(%s)" +
            ")",
            CREATE_TABLE,
            ASSESSMENT_TABLE_NAME,
            ASSESSMENT_ID,
            ASSESSMENT_COURSE_ID,
            ASSESSMENT_NAME,
            ASSESSMENT_TYPE,
            ASSESSMENT_DUE_DATE,
            ASSESSMENT_NOTIFICATION,
            ASSESSMENT_CREATED_AT,
            ASSESSMENT_COURSE_ID,
            COURSE_TABLE_NAME,
            COURSE_ID
        );

    }

    public DatabaseOpenHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_TERM);
        db.execSQL(CREATE_TABLE_COURSE);
        db.execSQL(CREATE_TABLE_NOTE);
        db.execSQL(CREATE_TABLE_ASSESSMENT);

    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE + CREATE_TABLE_TERM);
        db.execSQL(DROP_TABLE + CREATE_TABLE_COURSE);
        db.execSQL(DROP_TABLE + CREATE_TABLE_NOTE);
        db.execSQL(DROP_TABLE + CREATE_TABLE_ASSESSMENT);

        onCreate(db);

    }

}
