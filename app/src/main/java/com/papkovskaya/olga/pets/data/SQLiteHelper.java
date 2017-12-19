package com.papkovskaya.olga.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by olga on 19.12.17.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pets.db";

    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PESTLIST_TABLE = "CREATE TABLE " +
                PetsListContract.PetsListEntry.TABLE_NAME + " (" +
                PetsListContract.PetsListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PetsListContract.PetsListEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                PetsListContract.PetsListEntry.COLUMN_IMAGE + " BLOB NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_PESTLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PetsListContract.PetsListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
