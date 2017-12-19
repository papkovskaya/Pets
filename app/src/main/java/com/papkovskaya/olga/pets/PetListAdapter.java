package com.papkovskaya.olga.pets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.papkovskaya.olga.pets.data.PetsListContract;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

/**
 * Created by olga on 19.12.17.
 */

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.PetViewHolder> {

    private Context mContext;

    private Cursor mCursor;


    public PetListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.pet_list_item, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PetViewHolder holder, final int position) {
        if(!mCursor.moveToPosition(position))
            return;

        String name = mCursor.getString(mCursor.getColumnIndex(PetsListContract.PetsListEntry.COLUMN_NAME));
        byte[] imgByte = mCursor.getBlob(mCursor.getColumnIndex(PetsListContract.PetsListEntry.COLUMN_IMAGE));
        Bitmap image =  BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        long id = mCursor.getLong(mCursor.getColumnIndex(PetsListContract.PetsListEntry._ID));
        holder.petNameTextView.setText(name);
        holder.petImageView.setImageBitmap(image);
        holder.itemView.setTag(id);
        holder.editPetImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.edit) {
                    Intent intent = new Intent(mContext, EditPetActivity.class);
                    intent.putExtra("edit_pet_id", (Long) holder.itemView.getTag());
                    intent.putExtra("edit_pet_name", holder.petNameTextView.getText().toString());
                    intent.putExtra("edit_pet_image", MainActivity.getBitmapAsByteArray(((BitmapDrawable) holder.petImageView.getDrawable()).getBitmap()));
                    ((MainActivity)mContext).startActivityForResult(intent, 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class PetViewHolder extends RecyclerView.ViewHolder {

        TextView petNameTextView;
        ImageView petImageView;
        ImageView editPetImageView;


        public PetViewHolder(View itemView) {
            super(itemView);
            petNameTextView = (TextView) itemView.findViewById(R.id.pet_name_text_view);
            petImageView = (ImageView) itemView.findViewById(R.id.pet_image_view);
            editPetImageView = (ImageView) itemView.findViewById(R.id.edit);
        }

    }
}
