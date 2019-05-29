package com.example.eaesaxala2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.database.DatabaseUtils;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DetailRezept extends AppCompatActivity {
    DatenBankManager db;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rezept);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatenBankManager(this);

        //get View Elemente
        TextView name = findViewById(R.id.NAMEDETAIL);
        ImageView foto = findViewById(R.id.FOTODETAIl);
        TextView zeit = findViewById(R.id.ZEITDETAIL);
        SeekBar schwierigkeit = findViewById(R.id.SCHWIERIGKEITSDETAIL);
        ImageView hauptkategorie = findViewById(R.id.HAUPTKATEGORIEDETAIL);
        ImageView unterkategorie = findViewById(R.id.UNTERKATEGORIEDETAIL);
        TextView vorgehensweise = findViewById(R.id.VORGEHENSWEISE_EDITTEXT);
        ListView zutatenListe = (ListView) findViewById(R.id.ZUTATEN_LISTE_DETAIL);
        RatingBar rating = findViewById(R.id.BEWERTUNGDETAIL);
        TextView schwierigkeit_int = findViewById(R.id.SCHWIERIGKEITSDETAIL_INT);

        registerForContextMenu(name);

        //Navigationbar ändern
        setTitle("Rezept Details");

        Intent i = getIntent();
        String id =  i.getStringExtra("id");
        Log.d("SL", "das ist die ID:"+id);

        if (!(id.equals(null))){

            Rezept rezept =  db.selectRezept(id);
            name.setText(rezept.getName());

            Bitmap bitmap = BitmapFactory.decodeFile(rezept.foto);
            foto.setImageBitmap(bitmap);

            zeit.setText(Integer.toString(rezept.getZeit()));
            Log.d("XL","Bewertung: " + Integer.toString(rezept.getBewertung()));
            rating.setRating(rezept.getBewertung());

            Log.d("XL", "Schwierigkeit: " + Integer.toString(rezept.getSchwierigkeitsgrad()));
            schwierigkeit.setProgress(rezept.getSchwierigkeitsgrad());

            schwierigkeit_int.setText(Integer.toString(rezept.getSchwierigkeitsgrad()));

            if ((rezept.hauptkategorie).equals("Backen")){
                hauptkategorie.setImageResource(R.drawable.cupcake);
            } else if ((rezept.hauptkategorie).equals("Kochen")){
                hauptkategorie.setImageResource(R.drawable.cooking);
            }

            if ((rezept.unterkategorie).equals("vegan")){
                unterkategorie.setImageResource(R.drawable.leaves);
            } else if ((rezept.unterkategorie).equals("vegetarisch")){
                unterkategorie.setImageResource(R.drawable.broccoli);
            } else {
                unterkategorie.setImageResource(R.drawable.meat);
            }

            Log.d("SL", "Das ist die Vorgehensweise: "+ rezept.vorgehensweise);

            vorgehensweise.setText(rezept.vorgehensweise);

            ArrayList <Zutaten> zutaten = rezept.getZutaten();
            Log.d("SL", "das sind die zutaten" + zutaten.toString());
            ZutatenAdapter mAdapter = new ZutatenAdapter(ctx, zutaten);
            zutatenListe.setAdapter(mAdapter);

            setListViewHeightBasedOnChildren(zutatenListe);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.delete_edit_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i = getIntent();
        String id =  i.getStringExtra("id");
        int id_zahl = Integer.parseInt(id);

        switch (item.getItemId()){
            case R.id.ITEM_EDIT:
                Toast.makeText(this,"EDIT SELECTED", Toast.LENGTH_SHORT).show();

                Intent nextActivity = new Intent(this, RezeptEdit.class);
                nextActivity.putExtra("id",id);
                startActivity(nextActivity);

                return true;
            case R.id.ITEM_DELETE2:
                if (id != null) {
                    Intent mainActivity = new Intent(this, MainActivity.class);
                    startActivity(mainActivity);
                    Toast.makeText(this,"gelöscht", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Toast.makeText(this,"UPS", Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    //Höhe für die ListView berechnen, da sich diese in einer Scrollview befindet
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+20;
        listView.setLayoutParams(params);
    }

}
