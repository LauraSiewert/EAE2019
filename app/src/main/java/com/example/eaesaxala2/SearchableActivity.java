package com.example.eaesaxala2;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SearchableActivity extends AppCompatActivity {
    DatenBankManager db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        db = new DatenBankManager(this);
        ListView searchList = findViewById(R.id.SEARCHLIST);
        Context cxt = this;
        int itemLayout = android.R.layout.simple_list_item_1;
        Cursor cursor = db.selectAllRezepte();
        String[] from = new String[] {db.SPALTE_REZEPT_NAME};
        int[] to = new int[] {android.R.id.text1};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(cxt, itemLayout, cursor, from, to, 0);
        searchList.setAdapter(adapter);
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

    }

}
