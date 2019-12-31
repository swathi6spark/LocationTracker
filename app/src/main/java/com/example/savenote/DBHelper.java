package com.example.savenote;

import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.core.app.NavUtils;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "todonotes.db";
    public static final String TodoNotes_TABLE_NAME = "notes";
    public static final String NOTES_DOC_ID = "docId";
    public static final String NOTES_TITLE = "title";
    public static final String NOTES_CONTENT = "content";
    public static final String NOTES_STATUS = "status";
    public static final String DB_TAG = "DBHelper";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table notes " +
                        "(docId text, title text,content text,status integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }

    public boolean insertNote(String docId, String title, String content, Integer isDeleted) {

        Log.d("MainActivity", docId + "-" + title + "-" + content + "-" + isDeleted);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docId", docId);
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("status", isDeleted);
        db.insert("notes", null, contentValues);
        return true;
    }

    public Cursor getData(String docId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from notes where docId=" + docId + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TodoNotes_TABLE_NAME);
        return numRows;
    }

    public boolean updateNote(String docId, String title, String content, Integer isDeleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docId", docId);
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("status", isDeleted);
        db.update("notes", contentValues, "docId = ? ", new String[]{docId});
        return true;
    }

    public Integer deleteNote(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("notes",
                "docId = ? ",
                new String[]{id});
    }

    public ArrayList<Notes> getActiveNotes() {

        ArrayList<Notes> notesList = new ArrayList<Notes>();

        Cursor resultSet = this.getReadableDatabase().rawQuery("Select * from notes WHERE status = 1  ORDER BY docId DESC", null);
        resultSet.moveToFirst();

        // 0 docId , 1 title , 2 content

        try {
            while (!resultSet.isAfterLast()) {
                Notes note = new Notes(resultSet.getString(1), resultSet.getString(2), resultSet.getString(0), resultSet.getInt(3));
                notesList.add(note);
                resultSet.moveToNext();
            }
            Log.d(DB_TAG, "All rows processed");
            return notesList;
        } finally {
            resultSet.close();
            Log.d(DB_TAG, "Closing cursor");
        }
    }


    public ArrayList<Notes> getTrashNotes() {

        ArrayList<Notes> notesList = new ArrayList<Notes>();

        Cursor resultSet = this.getReadableDatabase().rawQuery("Select * from notes WHERE status = 0 ORDER BY docId DESC", null);
        resultSet.moveToFirst();

        // 0 docId , 1 title , 2 content

        try {
            while (!resultSet.isAfterLast()) {
                Notes note = new Notes(resultSet.getString(1), resultSet.getString(2), resultSet.getString(0), resultSet.getInt(3));
                notesList.add(note);
                resultSet.moveToNext();
            }
            Log.d(DB_TAG, "All rows processed");
            return notesList;
        } finally {
            resultSet.close();
            Log.d(DB_TAG, "Closing cursor");
        }
    }
}