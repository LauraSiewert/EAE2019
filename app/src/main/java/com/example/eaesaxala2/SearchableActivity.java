package com.example.eaesaxala2;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    String query;
    TextView searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        setTheme(R.style.AppTheme);

        setTitle("Rezept suche");

        db = new DatenBankManager(this);

        searchList = findViewById(R.id.SEARCHLIST);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ID des Eintrags mit übergeben, um dort Details dieses EIntrags zu laden
                TextView idTextView = view.findViewById(R.id.REZEPT_ID_LIST_SEARCH);
                String idR = idTextView.getText().toString();

                //ID des Eintrags mit übergeben, um dort Details dieses EIntrags zu laden
                Intent detailR_viewInt = new Intent(getApplicationContext(), DetailRezept.class);
                detailR_viewInt.putExtra("id", idR);

                startActivity(detailR_viewInt);
            }
        });

        searchText = findViewById(R.id.SEARCHTEXT);

        handleIntent(getIntent());
        if(query==null){
            searchText.setText("Du hast noch nichts gesucht.");
        }


    }

    @Override protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
            Log.d("SL", "mein query ist: " + query);
        }
    }

    private void doMySearch(String query){
        List<ItemObject> dictionaryObject = db.searchDictionaryWords(query);
        SearchAdapter mSearchAdapter = new SearchAdapter(SearchableActivity.this, dictionaryObject);
        searchList.setAdapter(mSearchAdapter);
        searchText.setText("Du hast nach "+ query +" gesucht.");
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
