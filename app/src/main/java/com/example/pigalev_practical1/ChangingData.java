package com.example.pigalev_practical1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ChangingData extends AppCompatActivity {

    Connection connection;
    List<Mask> data;
    ListView listView;
    AdapterMask pAdapter;

    EditText textMarka, textModel, textYearProduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changing_data);

        listView = findViewById(R.id.lvData);


        textMarka = findViewById(R.id.textMarka);
        textModel = findViewById(R.id.textModel);
        textYearProduction = findViewById(R.id.textYearProduction);

        RequestExecution();

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

    public void enterMobile() {
        pAdapter.notifyDataSetInvalidated();
        listView.setAdapter(pAdapter);
    }
    public void RequestExecution() {
        data = new ArrayList<Mask>();
        listView = findViewById(R.id.lvData);
        pAdapter = new AdapterMask(ChangingData.this, data);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Go((int)id);
            }
        });
        try {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if (connection != null)
            {
                String query = "Select * From Cars";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next())
                {
                    Mask tempMask = new Mask
                            (resultSet.getInt("ID"),
                                    resultSet.getString("Marka"),
                                    resultSet.getString("Model"),
                                    resultSet.getString("YearProduction"),
                                    resultSet.getString("Picture")
                            );
                    data.add(tempMask);
                    pAdapter.notifyDataSetInvalidated();
                }
                connection.close();
            }
            else
            {

            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        enterMobile();
    }


    public void AddData(View v)
    {
        if(textMarka.getText().length() == 0 || textModel.getText().length() == 0 || textYearProduction.getText().length() == 0){
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_LONG).show();
            return;
        }
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if(connection != null) {
                String query = "Insert into Cars(Marka, Model, YearProduction) Values('" + textMarka.getText() + "', '" + textModel.getText() + "', '" + textYearProduction.getText() + "')";
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При добавление данных в БД возникла ошибка", Toast.LENGTH_LONG).show();
        }
        textMarka.setText("");
        textModel.setText("");
        textYearProduction.setText("");
        RequestExecution();
    }
    public void GoExit(View v)
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void Go(int id)
    {
        Intent intent = new Intent(this, SingleEntryChange.class);
        Bundle b = new Bundle();
        b.putInt("key", id);
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
                statement.executeUpdate(query);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При удаление данных возникла ошибка", Toast.LENGTH_LONG).show();
        }
        RequestExecution();
    }
}