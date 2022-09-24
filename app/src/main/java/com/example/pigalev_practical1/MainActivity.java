package com.example.pigalev_practical1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    Connection connection;
    String ConnectionResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void GoChange(View v)
    {
        startActivity(new Intent(this, ChangingData.class));
    }

    public void GetTextFromSql(View v)
    {
        TableLayout List = findViewById(R.id.List);
        List.removeAllViews();
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
                    if(resultSet.isFirst()){
                        TableRow dbOutputRow = new TableRow(this);
                        dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                        TextView outputID = new TextView(this);
                        params.weight = 1.0f;
                        outputID.setLayoutParams(params);
                        outputID.setText("ID");
                        outputID.setTypeface(Typeface.DEFAULT_BOLD);
                        outputID.setTextSize(18);
                        dbOutputRow.addView(outputID);

                        TextView outputMarka = new TextView(this);
                        params.weight = 3.0f;
                        outputMarka.setLayoutParams(params);
                        outputMarka.setText("Марка");
                        outputMarka.setTypeface(Typeface.DEFAULT_BOLD);
                        outputMarka.setTextSize(18);
                        dbOutputRow.addView(outputMarka);

                        TextView outputModel = new TextView(this);
                        params.weight = 3.0f;
                        outputModel.setLayoutParams(params);
                        outputModel.setText("Модель");
                        outputModel.setTypeface(Typeface.DEFAULT_BOLD);
                        outputModel.setTextSize(18);
                        dbOutputRow.addView(outputModel);

                        TextView outputYearProduction = new TextView(this);
                        params.weight = 3.0f;
                        outputYearProduction.setLayoutParams(params);
                        outputYearProduction.setText("Год производства");
                        outputYearProduction.setTypeface(Typeface.DEFAULT_BOLD);
                        outputYearProduction.setTextSize(18);
                        dbOutputRow.addView(outputYearProduction);

                        List.addView(dbOutputRow);
                    }
                    TableRow dbOutputRow = new TableRow(this);
                    dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                    TextView outputID = new TextView(this);
                    params.weight = 1.0f;
                    outputID.setLayoutParams(params);
                    outputID.setText(resultSet.getString(1).replaceAll("\\s+",""));
                    outputID.setTextSize(18);
                    dbOutputRow.addView(outputID);

                    TextView outputMarka = new TextView(this);
                    params.weight = 3.0f;
                    outputMarka.setLayoutParams(params);
                    outputMarka.setText(resultSet.getString(2).replaceAll("\\s+",""));
                    outputMarka.setTextSize(18);
                    dbOutputRow.addView(outputMarka);

                    TextView outputModel = new TextView(this);
                    params.weight = 3.0f;
                    outputModel.setLayoutParams(params);
                    outputModel.setText(resultSet.getString(3).replaceAll("\\s+",""));
                    outputModel.setTextSize(18);
                    dbOutputRow.addView(outputModel);

                    TextView outputYearProduction = new TextView(this);
                    params.weight = 3.0f;
                    outputYearProduction.setLayoutParams(params);
                    outputYearProduction.setText(resultSet.getString(4).replaceAll("\\s+",""));
                    outputYearProduction.setTextSize(18);
                    dbOutputRow.addView(outputYearProduction);

                    List.addView(dbOutputRow);
                }
            }
            else
            {
                ConnectionResult = "Check Connection";
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При выводе данных возникла ошибка", Toast.LENGTH_LONG).show();
        }

    }


}