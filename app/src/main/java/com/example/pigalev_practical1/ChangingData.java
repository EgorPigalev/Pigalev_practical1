package com.example.pigalev_practical1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChangingData extends AppCompatActivity {

    Connection connection;
    String ConnectionResult = "";
    EditText textMarka, textModel, textYearProduction;
    String id; // Переменная для хранения индекса строки (используется при нажатие кнопки "Изменить запись")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changing_data);

        textMarka = findViewById(R.id.textMarka);
        textModel = findViewById(R.id.textModel);
        textYearProduction = findViewById(R.id.textYearProduction);

        UpdateTable();

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
    }

    public void UpdateTable()
    {
        TableLayout dbOutput = findViewById(R.id.dbOutput);
        dbOutput.removeAllViews();
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();

            if(connection != null)
            {
                String query = "Select * From Cars";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next())
                {
                    TableRow dbOutputRow = new TableRow(this);
                    dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                    TextView outputID = new TextView(this);
                    params.weight = 1.0f;
                    outputID.setLayoutParams(params);
                    outputID.setText(resultSet.getString(1).replaceAll("\\s+",""));
                    outputID.setTextSize(12);
                    dbOutputRow.addView(outputID);

                    TextView outputMarka = new TextView(this);
                    params.weight = 3.0f;
                    outputMarka.setLayoutParams(params);
                    outputMarka.setText(resultSet.getString(2).replaceAll("\\s+",""));
                    outputMarka.setTextSize(12);
                    dbOutputRow.addView(outputMarka);

                    TextView outputModel = new TextView(this);
                    params.weight = 3.0f;
                    outputModel.setLayoutParams(params);
                    outputModel.setText(resultSet.getString(3).replaceAll("\\s+",""));
                    outputModel.setTextSize(12);
                    dbOutputRow.addView(outputModel);

                    TextView outputYearProduction = new TextView(this);
                    params.weight = 3.0f;
                    outputYearProduction.setLayoutParams(params);
                    outputYearProduction.setText(resultSet.getString(4).replaceAll("\\s+",""));
                    outputYearProduction.setTextSize(12);
                    dbOutputRow.addView(outputYearProduction);

                    Button deleteBtn = new Button(this);
                    deleteBtn.setOnClickListener(this::Go);
                    params.weight = 1.0f;
                    deleteBtn.setLayoutParams(params);
                    deleteBtn.setText("Изменить\nзапись");
                    deleteBtn.setTextSize(12);
                    dbOutputRow.addView(deleteBtn);

                    dbOutput.addView(dbOutputRow);
                }
            }
            else
            {
                ConnectionResult = "Check Connection";
            }
        }
        catch (Exception ex)
        {
        }
    }
    public void AddData(View v)
    {
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if(connection != null) {
                String query = "Insert into Cars(Marka, Model, YearProduction) Values('" + textMarka.getText() + "', '" + textModel.getText() + "', '" + textYearProduction.getText() + "')";
                Statement statement = connection.createStatement();
                statement.executeQuery(query);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При изменение данных в БД возникла ошибка", Toast.LENGTH_LONG).show();
        }
        textMarka.setText("");
        textModel.setText("");
        textYearProduction.setText("");
        UpdateTable();
    }
    public void GoExit(View v)
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void Go(View v)
    {
        TableRow  tableRow = (TableRow) v.getParent();
        TextView textView = (TextView) tableRow.getChildAt(0);
        String idLine = textView.getText().toString();
        Intent intent = new Intent(this, SingleEntryChange.class);
        Bundle b = new Bundle();
        b.putInt("key", Integer.parseInt(idLine));
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    public void ClearData(View v)
    {
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if(connection != null) {
                String query = "Delete From Cars";
                Statement statement = connection.createStatement();
                statement.executeQuery(query);
            }
        }
        catch (Exception ex)
        {

        }
        UpdateTable();
    }
}