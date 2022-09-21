package com.example.pigalev_practical1;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SingleEntryChange extends AppCompatActivity {

    Connection connection;
    Integer index;
    static final int GALLERY_REQUEST = 1;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_entry_change);

        EditText textMarka = findViewById(R.id.textMarka);
        EditText textModel = findViewById(R.id.textModel);
        EditText textYearProduction = findViewById(R.id.textYearProduction);

        textMarka.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                textMarka.setHint("");
            else
                textMarka.setHint("Марка");
        });

        textModel.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                textModel.setHint("");
            else
                textModel.setHint("Модель");
        });

        textYearProduction.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                textYearProduction.setHint("");
            else
                textYearProduction.setHint("Год производства");
        });

        Bundle arguments = getIntent().getExtras();
        index = arguments.getInt("key");
        GetData();
    }
    public void Exit(View v)
    {
        startActivity(new Intent(this, ChangingData.class));
    }

    public void GetData()
    {
        EditText textMarka = findViewById(R.id.textMarka);
        EditText textModel = findViewById(R.id.textModel);
        EditText textYearProduction = findViewById(R.id.textYearProduction);
        TextView deletePicture = findViewById(R.id.deletePicture);
        ImageView picture = findViewById(R.id.Picture);
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if(connection != null) {
                String query = "Select * From Cars where ID = " + index;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next())
                {
                    textMarka.setText(resultSet.getString(2).replaceAll("\\s+",""));
                    textModel.setText(resultSet.getString(3).replaceAll("\\s+",""));
                    textYearProduction.setText(resultSet.getString(4).replaceAll("\\s+",""));
                    if(resultSet.getString(5) == null)
                    {
                        picture.setImageResource(R.drawable.absence);
                        deletePicture.setVisibility(View.INVISIBLE);
                    }
                    else
                    {

                    }
                }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При выводе данных произошла ошибка", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteLine(View v)
    {
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if(connection != null) {
                String query = "Delete From Cars where ID = " + index;
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При удаление данных возникла ошибка", Toast.LENGTH_LONG).show();
        }
        Exit(v);
    }

    public void updateLine(View v)
    {
        EditText textMarka = findViewById(R.id.textMarka);
        EditText textModel = findViewById(R.id.textModel);
        EditText textYearProduction = findViewById(R.id.textYearProduction);
        if(textMarka.getText().length() == 0 || textModel.getText().length() == 0 || textYearProduction.getText().length() == 0)
        {
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_LONG).show();
            return;
        }
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if(connection != null) {
                String query = "Update Cars Set Marka = '" + textMarka.getText() + "', Model = '" + textModel.getText() + "', YearProduction = '" + textYearProduction.getText() + "' where ID = " + index;
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При изменение данных в БД возникла ошибка", Toast.LENGTH_LONG).show();
        }
        Exit(v);
    }
    public void updatePicture(View v)
    {
        mGetContent.launch("image/*");
        /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
         */
    }


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;
        ImageView imageView = (ImageView) findViewById(R.id.Picture);

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(null);
                    imageView.setImageBitmap(bitmap);
                    TextView deletePicture = findViewById(R.id.deletePicture);
                    deletePicture.setVisibility(View.VISIBLE);
                }
        }
    }
     */

    public void deletePicture(View v)
    {
        ImageView picture = (ImageView) findViewById(R.id.Picture);
        picture.setImageBitmap(null);
        TextView deletePicture = findViewById(R.id.deletePicture);
        picture.setImageResource(R.drawable.absence);
        deletePicture.setVisibility(View.INVISIBLE);
    }
}