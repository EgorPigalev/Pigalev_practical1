package com.example.pigalev_practical1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SingleEntryChange extends AppCompatActivity {

    Connection connection;
    Integer index;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Bitmap bitmap = null;
                    ImageView imageView = (ImageView) findViewById(R.id.Picture);
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri selectedImage = result.getData().getData();
                        try
                        {
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
                        String varcharPicture = BitMapToString(bitmap);
                        addPicture(varcharPicture);
                    }
                }
            });

    public String BitMapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public void addPicture(String varcharPicture)
    {
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if(connection != null) {
                String query;
                if(varcharPicture == ""){
                    query = "Update Cars Set Picture = null where ID = " + index;
                }
                else{
                    query = "Update Cars Set Picture = '" + varcharPicture + "' where ID = " + index;
                }
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При добавление картинки возникла ошибка!", Toast.LENGTH_LONG).show();
        }
    }

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
                        Bitmap bitmap = StringToBitMap(resultSet.getString(5));
                        picture.setImageBitmap(bitmap);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При выводе данных произошла ошибка", Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
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
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        someActivityResultLauncher.launch(photoPickerIntent);
    }

    public void deletePicture(View v)
    {
        ImageView picture = (ImageView) findViewById(R.id.Picture);
        picture.setImageBitmap(null);
        addPicture("");
        TextView deletePicture = findViewById(R.id.deletePicture);
        picture.setImageResource(R.drawable.absence);
        deletePicture.setVisibility(View.INVISIBLE);
    }
}