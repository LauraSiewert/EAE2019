package com.example.eaesaxala2;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

public class SearchableActivity extends AppCompatActivity {
    DatenBankManager db;
    ListView searchList;


    //TO-DO: MainList Adapter bentuzen
    //ÜBergang zur Detailview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        setTitle("Rezept suche");

        db = new DatenBankManager(this);

        //Alle Rezepte ausgeben
        searchList = findViewById(R.id.SEARCHLIST);
        /*Context cxt = this;
        int itemLayout = android.R.layout.simple_list_item_1;
        Cursor cursor = db.selectAllRezepte();
        String[] from = new String[] {db.SPALTE_REZEPT_NAME};
        int[] to = new int[] {android.R.id.text1};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(cxt, itemLayout, cursor, from, to, 0);
        searchList.setAdapter(adapter);*/


        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ID des Eintrags mit übergeben, um dort Details dieses EIntrags zu laden
                //TextView idTextView = view.findViewById(R.id.REZEPT_ID_LIST);
                //String id = idTextView.getText().toString();

                //ID des Eintrags mit übergeben, um dort Details dieses EIntrags zu laden
                Intent detailR_viewInt = new Intent(getApplicationContext(), DetailRezept.class);
                //detailR_viewInt.putExtra("id", id);

                startActivity(detailR_viewInt);
            }
        });

        handleIntent(getIntent());

    }

    @Override protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query){
        List<ItemObject> dictionaryObject = db.searchDictionaryWords(query);
        SearchAdapter mSearchAdapter = new SearchAdapter(SearchableActivity.this, dictionaryObject);
        searchList.setAdapter(mSearchAdapter);
    }


    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case R.id.ITEM_SUCHEN_DETAIL:
                onSearchRequested();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

}
