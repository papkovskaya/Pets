package com.papkovskaya.olga.pets;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.papkovskaya.olga.pets.data.PetsListContract;
import com.papkovskaya.olga.pets.data.SQLiteHelper;
import com.papkovskaya.olga.pets.data.TestUtil;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private PetListAdapter mAdapter;

    // COMPLETED (1) Create a local field SQLiteDatabase called mDb
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView petlistRecyclerView;

        petlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_pets_list_view);
        petlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        mDb = dbHelper.getWritableDatabase();
        //TestUtil.insertFakeData(mDb, this);
        Cursor cursor = getAllPets();
        mAdapter = new PetListAdapter(this, cursor);
        petlistRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }



            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final long id = (long) viewHolder.itemView.getTag();
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(MainActivity.this);
                alertDlg.setMessage("Are you sure you want to delete");

                alertDlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removePet(id);
                        mAdapter.swapCursor(getAllPets());
                    }
                });
                alertDlg.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAdapter.swapCursor(getAllPets());
                    }
                });
                alertDlg.create().show();



            }
        }).attachToRecyclerView(petlistRecyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private Cursor getAllPets() {
        return mDb.query(
                PetsListContract.PetsListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                PetsListContract.PetsListEntry.COLUMN_NAME
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.menu_add:
                Intent intent = new Intent(this, AddPetActivity.class);
                startActivityForResult(intent,1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data.getStringExtra("add_pet_name") != null && data.getByteArrayExtra("add_pet_image") != null){
            String pet_name = data.getStringExtra("add_pet_name");
            byte[] pet_image =  data.getByteArrayExtra("add_pet_image");

            addNewPet(pet_name, pet_image);

            mAdapter.swapCursor(getAllPets());
        }
        if (data.getStringExtra("edit_pet_name") != null && data.getByteArrayExtra("edit_pet_image") != null){
            long id = data.getLongExtra("edit_pet_id", -1);
            String pet_name = data.getStringExtra("edit_pet_name");
            byte[] pet_image =  data.getByteArrayExtra("edit_pet_image");

            editPet(id,pet_name, pet_image);

            mAdapter.swapCursor(getAllPets());
        }
    }

    private void editPet(long id, String name, byte[] image) {
        ContentValues cv = new ContentValues();
        cv.put( PetsListContract.PetsListEntry.COLUMN_NAME, name);
        cv.put( PetsListContract.PetsListEntry.COLUMN_IMAGE, image);
        mDb.update(PetsListContract.PetsListEntry.TABLE_NAME, cv, PetsListContract.PetsListEntry._ID + " = ?", new String[]{String.valueOf(id)});
    }

    private long addNewPet(String name, byte[] image){
        ContentValues cv = new ContentValues();
        cv.put( PetsListContract.PetsListEntry.COLUMN_NAME, name);
        cv.put( PetsListContract.PetsListEntry.COLUMN_IMAGE, image);
        return mDb.insert(PetsListContract.PetsListEntry.TABLE_NAME, null, cv);
    }

    private boolean removePet(long id) {
        return mDb.delete(PetsListContract.PetsListEntry.TABLE_NAME, PetsListContract.PetsListEntry._ID + "=" + id, null) > 0;
    }


    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
