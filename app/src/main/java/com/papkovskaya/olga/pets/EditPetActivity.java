package com.papkovskaya.olga.pets;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.InputStream;

public class EditPetActivity extends AppCompatActivity {

    ImageView mImageView;
    EditText mEditText;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);

        mEditText = (EditText) findViewById(R.id.editText);
        mImageView = findViewById(R.id.edit_img);

        if (getIntent().getStringExtra("edit_pet_name") != null && getIntent().getByteArrayExtra("edit_pet_image") != null){
            String s = getIntent().getStringExtra("edit_pet_name");
            byte[] imgByte =  getIntent().getByteArrayExtra("edit_pet_image");
            id = getIntent().getLongExtra("edit_pet_id", -1);
            mEditText.setText(s);
            Bitmap bitmap =  BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            mImageView.setImageBitmap(bitmap);
        }
    }

    public void onClickSave(View view) {


        Intent intent = new Intent();
        intent.putExtra("edit_pet_name", mEditText.getText().toString());
        intent.putExtra("edit_pet_image", MainActivity.getBitmapAsByteArray(((BitmapDrawable) mImageView.getDrawable()).getBitmap()));
        intent.putExtra("edit_pet_id", id);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onSelectImage(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 2:
                if(resultCode == RESULT_OK){
                    Uri choosenImage = data.getData();

                    if(choosenImage !=null){
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(choosenImage);
                            Bitmap bp = BitmapFactory.decodeStream(inputStream);
                            mImageView.setImageBitmap(bp);
                        }
                        catch(Exception e){

                        }
                    }
                }
        }
    }
}
