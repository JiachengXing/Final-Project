package com.example.project.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * sqlite
 */
public class DatabaseUtil {

    private static final String TAG = "DatabaseUtil";

    /**
     * Database Name
     */
    private static final String DATABASE_NAME = "app_plan";

    /**
     * Database Version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Table Name
     */
    private static final String DATABASE_TABLE = "tb_plan";

    private static final String DATABASE_USER_TABLE = "tb_user";


    /**
     * Table columns
     */
    public static final String KEY_NAME = "name";
    public static final String KEY_START_DATE = "start";
    public static final String KEY_END_DATE = "end";
    public static final String KEY_ACTIVITY_CLASS = "activity_class";
    public static final String KEY_DEL = "del";
    public static final String KEY_DONE = "done";
    public static final String KEY_SHARE = "share";
    public static final String KEY_DONE_DATE = "done_date";
    public static final String KEY_THUMB = "thumb";
    public static final String KEY_ROWID = "_id";

    /**
     * Table user columns
     *
     * String name;
     *     String userName;
     *     String pass;
     *     String gender;
     *     String birthDate;
     */
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_PASS = "pass";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_BIRTHDATE = "birth";

    /**
     * Database creation sql statement
     */
    private static final String CREATE_TABLE =
            "create table " + DATABASE_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_NAME + " text not null, " + KEY_START_DATE + " text not null, " + KEY_END_DATE +" text not null, " + KEY_ACTIVITY_CLASS + " text not null, " + KEY_DEL + " text not null , " + KEY_DONE + " text not null , " + KEY_SHARE + " text not null, " + KEY_DONE_DATE + " text not null, " + KEY_THUMB + " integer not null);";

    private static final String CREATE_USER_TABLE =
            "create table " + DATABASE_USER_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_NAME + " text not null, " + KEY_USER_NAME + " text not null, " + KEY_PASS +" text not null, " + KEY_GENDER + " text not null, " + KEY_BIRTHDATE + " text not null);";

    /**
     * Context
     */
    private final Context mCtx;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Inner private class. Database Helper class for creating and updating database.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * onCreate method is called for the 1st time when database doesn't exists.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Creating DataBase: " + CREATE_TABLE);
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_USER_TABLE);
        }

        /**
         * onUpgrade method is called when database version changes.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public DatabaseUtil(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * This method is used for creating/opening connection
     *
     * @return instance of DatabaseUtil
     * @throws SQLException
     */
    public DatabaseUtil open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * This method is used for closing the connection.
     */
    public void close() {
        mDbHelper.close();
    }

    /**
     * This method is used to create/insert new record record.
     *
     * @param name
     * @return long
     */
    public long insert(String name, String startDate, String endDate, String activityClass) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_START_DATE, startDate);
        initialValues.put(KEY_END_DATE, endDate);
        initialValues.put(KEY_ACTIVITY_CLASS, activityClass);
        initialValues.put(KEY_DEL,"0");
        initialValues.put(KEY_DONE,"0");
        initialValues.put(KEY_SHARE, "0");
        initialValues.put(KEY_DONE_DATE, "");
        initialValues.put(KEY_THUMB, 0);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public long insertUser(String name, String userName, String pass, String gender, String birthDate) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_USER_NAME, userName);
        initialValues.put(KEY_PASS, pass);
        initialValues.put(KEY_GENDER, gender);
        initialValues.put(KEY_BIRTHDATE,birthDate);
        return mDb.insert(DATABASE_USER_TABLE, null, initialValues);
    }

    /**
     * This method will delete record.
     *
     * @param rowId
     * @return boolean
     */
    public boolean delete(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * This method will deleteAll record.
     *
     * @return
     */
    public boolean deleteAll() {
        return mDb.delete(DATABASE_TABLE, " 1 ", null) > 0;
    }

    public boolean deleteAllUser() {
        return mDb.delete(DATABASE_USER_TABLE, " 1 ", null) > 0;
    }

    /**
     * This method will return Cursor holding all the records.
     *
     * @return Cursor
     */
    public Cursor fetchAll() {
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME,
                KEY_START_DATE, KEY_END_DATE, KEY_ACTIVITY_CLASS, KEY_DEL, KEY_DONE, KEY_SHARE, KEY_DONE_DATE, KEY_THUMB}, null, null, null, null, null);
    }

    public Cursor fetchAllUser() {
        return mDb.query(DATABASE_USER_TABLE, new String[]{KEY_ROWID, KEY_NAME,
                KEY_USER_NAME, KEY_PASS, KEY_GENDER, KEY_BIRTHDATE}, null, null, null, null, null);
    }

    /**
     * This method will return Cursor holding the specific record.
     *
     * @param id
     * @return Cursor
     * @throws SQLException
     */
    public Cursor fetch(long id) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,
                                KEY_NAME, KEY_START_DATE, KEY_END_DATE, KEY_ACTIVITY_CLASS, KEY_DEL, KEY_DONE, KEY_SHARE, KEY_DONE_DATE, KEY_THUMB}, KEY_ROWID + "=" + id, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchUser(long id) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_USER_TABLE, new String[]{KEY_ROWID,
                                KEY_NAME, KEY_USER_NAME, KEY_PASS, KEY_GENDER, KEY_BIRTHDATE}, KEY_ROWID + "=" + id, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * This method will update record.
     *
     * @param id
     * @param name
     * @return boolean
     */
    public boolean update(int id, String name, String startDate, String endDate, String activityClass, String isDel, String isDone, String isShare, String doneDate, int thumb) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_START_DATE, startDate);
        args.put(KEY_END_DATE, endDate);
        args.put(KEY_ACTIVITY_CLASS, activityClass);
        args.put(KEY_DEL, isDel);
        args.put(KEY_DONE, isDone);
        args.put(KEY_SHARE, isShare);
        args.put(KEY_DONE_DATE, doneDate);
        args.put(KEY_THUMB, thumb);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null) > 0;
    }

    public boolean updateUser(int id, String name, String userName, String pass, String gender, String birth) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_USER_NAME, userName);
        args.put(KEY_PASS, pass);
        args.put(KEY_GENDER, gender);
        args.put(KEY_BIRTHDATE, birth);
        return mDb.update(DATABASE_USER_TABLE, args, KEY_ROWID + "=" + id, null) > 0;
    }
}
