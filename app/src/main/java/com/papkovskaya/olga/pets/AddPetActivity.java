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
import android.widget.Spinner;

import java.io.InputStream;

public class AddPetActivity extends AppCompatActivity {

    ImageView mImageView;
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        mEditText = (EditText) findViewById(R.id.editText);
        mImageView = findViewById(R.id.add_img);


    }

    public void onClickAdd(View view) {


        Intent intent = new Intent();
        intent.putExtra("add_pet_name", mEditText.getText().toString());
        intent.putExtra("add_pet_image", MainActivity.getBitmapAsByteArray(((BitmapDrawable) mImageView.getDrawable()).getBitmap()));

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
