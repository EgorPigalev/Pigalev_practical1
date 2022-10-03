package com.example.pigalev_practical1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    View v;
    Connection connection;
    List<Mask> data;
    ListView listView;
    AdapterMask pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = findViewById(com.google.android.material.R.id.ghost_view);


        TextView textSearch = findViewById(R.id.search);
        textSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                textSearch.setHint("");
            else
                textSearch.setHint("Введите значение");
        });

    }

    public void enterMobile() {
        pAdapter.notifyDataSetInvalidated();
        listView.setAdapter(pAdapter);
    }
    public void RequestExecution(String query) {
        data = new ArrayList<Mask>();
        listView = findViewById(R.id.lvData);
        pAdapter = new AdapterMask(MainActivity.this, data);
        try {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if (connection != null)
            {
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

    public void GoChange(View v)
    {
        startActivity(new Intent(this, ChangingData.class));
    }

    public void GetTextFromSql(View v)
    {
        String query = "Select * From Cars";
        RequestExecution(query);
        TextView textView = findViewById(R.id.headerSearch);
        Button button = findViewById(R.id.OutputList);
        TableLayout tableSearch = findViewById(R.id.tableSearch);
        button.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        tableSearch.setVisibility(View.VISIBLE);

    }
    public void search(View v)
    {
        Spinner chingSearch = findViewById(R.id.chingSearch);
        EditText search = findViewById(R.id.search);
        Spinner sorting1 = findViewById(R.id.sorting1);
        Spinner order1 = findViewById(R.id.order1);
        Spinner sorting2 = findViewById(R.id.sorting2);
        Spinner order2 = findViewById(R.id.order2);
        String query;
        Boolean b = true;
        if(search.getText().toString().equals("") && sorting1.getSelectedItem().toString().equals("") && sorting2.getSelectedItem().toString().equals("")){
            query = "Select * From Cars";
            b = false;
        }
        else if(!search.getText().toString().equals("") && sorting1.getSelectedItem().toString().equals("") && sorting2.getSelectedItem().toString().equals("")){
            query = "Select * From Cars Where "+ fild(chingSearch.getSelectedItem().toString()) +" = '"+ search.getText() + "'";
        }
        else if(search.getText().toString().equals("") && !sorting1.getSelectedItem().toString().equals("") && sorting2.getSelectedItem().toString().equals("")){
            query = "Select * From Cars Order BY " + fild(sorting1.getSelectedItem().toString()) +" " + order(order1.getSelectedItem().toString());
        }
        else if(search.getText().toString().equals("") && sorting1.getSelectedItem().toString().equals("") && !sorting2.getSelectedItem().toString().equals("")){
            query = "Select * From Cars Order BY " + fild(sorting2.getSelectedItem().toString()) +" " + order(order2.getSelectedItem().toString());
        }
        else if(!search.getText().toString().equals("") && !sorting1.getSelectedItem().toString().equals("") && sorting2.getSelectedItem().toString().equals("")){
            query = "Select * From Cars Where "+ fild(chingSearch.getSelectedItem().toString()) +" = '"+ search.getText() + "' Order BY " + fild(sorting1.getSelectedItem().toString()) +" " + order(order1.getSelectedItem().toString());
        }
        else if(!search.getText().toString().equals("") && sorting1.getSelectedItem().toString().equals("") && !sorting2.getSelectedItem().toString().equals("")){
            query = "Select * From Cars Where "+ fild(chingSearch.getSelectedItem().toString()) +" = '"+ search.getText() + "' Order BY " + fild(sorting2.getSelectedItem().toString()) +" " + order(order2.getSelectedItem().toString());
        }
        else if(search.getText().toString().equals("") && !sorting1.getSelectedItem().toString().equals("") && !sorting2.getSelectedItem().toString().equals("")){
            query = "Select * From Cars Order BY " + fild(sorting1.getSelectedItem().toString()) +" " + order(order1.getSelectedItem().toString()) + ", " + fild(sorting2.getSelectedItem().toString()) +" " + order(order2.getSelectedItem().toString());
        }
        else{
            query = "Select * From Cars Where "+ fild(chingSearch.getSelectedItem().toString()) +" = '"+ search.getText() + "' Order BY " + fild(sorting1.getSelectedItem().toString()) +" " + order(order1.getSelectedItem().toString()) + ", " + fild(sorting2.getSelectedItem().toString()) +" " + order(order2.getSelectedItem().toString());
        }
        RequestExecution(query);
        TextView deleteSearch = findViewById(R.id.deleteSearch);
        if(b == true)
        {
            deleteSearch.setVisibility(View.VISIBLE);
        }
        else{
            deleteSearch.setVisibility(View.GONE);
        }
    }

    public String fild(String str)
    {
        if(str.equals("Марка")){
            return "Marka";
        }
        else if(str.equals("Модель")){
            return  "Model";
        }
        else{
            return "YearProduction";
        }
    }

    public String order(String str){
        if(str.equals("Возрастание")){
            return "ASC";
        }
        else{
            return "DESC";
        }
    }

    public void SearchModule(View v)
    {
        TextView textView = findViewById(R.id.headerSearch);
        TableLayout tableSearch = findViewById(R.id.tableSearch);
        if(tableSearch.getVisibility() == View.VISIBLE)
        {
            tableSearch.setVisibility(View.GONE);
            textView.setText("Модуль поиска ↓");
        }
        else
        {
            tableSearch.setVisibility(View.VISIBLE);
            textView.setText("Модуль поиска ↑");
        }
    }

    public void deleteSearch(View v)
    {
        Spinner chingSearch = findViewById(R.id.chingSearch);
        EditText search = findViewById(R.id.search);
        Spinner sorting1 = findViewById(R.id.sorting1);
        Spinner order1 = findViewById(R.id.order1);
        Spinner sorting2 = findViewById(R.id.sorting2);
        Spinner order2 = findViewById(R.id.order2);
        chingSearch.setSelection(0);
        search.setText(null);
        sorting1.setSelection(0);
        order1.setSelection(0);
        sorting2.setSelection(0);
        order2.setSelection(0);
        search(v);
        TextView deleteSearch = findViewById(R.id.deleteSearch);
        deleteSearch.setVisibility(View.GONE);
    }
}