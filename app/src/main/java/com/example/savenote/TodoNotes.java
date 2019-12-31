package com.example.savenote;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class TodoNotes extends Application {
    private static final String TAG = TodoNotes.class.getCanonicalName();
    public static Context mContext = null;
    public static final String Version = "1.0";
    public static final String AppName = "Todo Notes";
    private static SQLiteDatabase mydatabase;


    @Override
    public void onCreate() {
        super.onCreate();
        initContext(this.getApplicationContext());

        mydatabase = openOrCreateDatabase("notes", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Notes(DocId VARCHAR,Title VARCHAR,Content VARCHAR,Status int);");
    }

    public static SQLiteDatabase getMydatabase() {
        return mydatabase;
    }

    public static Context getContext() {
        return mContext;
    }

    private void initContext(Context context) {
        mContext = context;
    }


    public static String getAppName() {
        return AppName;
    }

    public static String getVersion() {
        return Version;
    }

}
