package com.example.pigalev_practical1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SingleEntryChange extends AppCompatActivity {

    Connection connection;
    Integer index;

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
}