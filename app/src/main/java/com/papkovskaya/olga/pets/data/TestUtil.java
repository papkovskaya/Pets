package com.papkovskaya.olga.pets.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.papkovskaya.olga.pets.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olga on 19.12.17.
 */

public class TestUtil {

    public static void insertFakeData(SQLiteDatabase db, Context context){
        if(db == null){
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(PetsListContract.PetsListEntry.COLUMN_NAME, "Котя");
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.test);
        cv.put(PetsListContract.PetsListEntry.COLUMN_IMAGE, getBitmapAsByteArray(icon));
        list.add(cv);

        cv = new ContentValues();
        cv.put(PetsListContract.PetsListEntry.COLUMN_NAME, "Котик");
        icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.test);
        cv.put(PetsListContract.PetsListEntry.COLUMN_IMAGE, getBitmapAsByteArray(icon));
        list.add(cv);

//        cv = new ContentValues();
//        cv.put(PetsListContract.PetsListEntry.COLUMN_NAME, "Tim");
//        cv.put(PetsListContract.PetsListEntry.COLUMN_IMAGE, 2);
//        list.add(cv);
//
//        cv = new ContentValues();
//        cv.put(PetsListContract.PetsListEntry.COLUMN_NAME, "Jessica");
//        cv.put(PetsListContract.PetsListEntry.COLUMN_IMAGE, 99);
//        list.add(cv);
//
//        cv = new ContentValues();
//        cv.put(PetsListContract.PetsListEntry.COLUMN_NAME, "Larry");
//        cv.put(PetsListContract.PetsListEntry.COLUMN_IMAGE, 1);
//        list.add(cv);
//
//        cv = new ContentValues();
//        cv.put(PetsListContract.PetsListEntry.COLUMN_NAME, "Kim");
//        cv.put(PetsListContract.PetsListEntry.COLUMN_IMAGE, 45);
//        list.add(cv);

        //insert all guests in one transaction
        try
        {
            db.beginTransaction();
            //clear the table first
            db.delete (PetsListContract.PetsListEntry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(PetsListContract.PetsListEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}