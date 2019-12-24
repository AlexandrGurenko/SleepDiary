package com.alexandr.gurenko.sleepdiary;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    List<DreamList> dreamLists;
    ListView action_list;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new DBHelper(this);
        dreamLists = dbHelper.getAllData();
        action_list = findViewById(R.id.action_list);
        CustomAdapter adapter = new CustomAdapter(this, dreamLists);
        action_list.setAdapter(adapter);

    }
}
