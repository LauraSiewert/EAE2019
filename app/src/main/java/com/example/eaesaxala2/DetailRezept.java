package com.example.eaesaxala2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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

        registerForContextMenu(name);

        //Navigationbar ändern
        setTitle("Rezept Details");

        TextView rezeptId = findViewById(R.id.REZEPT_ID);
        Intent i = getIntent();
        String id =  i.getStringExtra("id");
        Log.d("SL", "das ist die ID:"+id);
        rezeptId.setText(id);

        if (!(id.equals(null))){

            Rezept rezept =  db.selectRezept(id);
            name.setText(rezept.getName());

            Bitmap bitmap = BitmapFactory.decodeFile(rezept.foto);
            foto.setImageBitmap(bitmap);

            zeit.setText(Integer.toString(rezept.getZeit()));
            Log.d("XL","Bewertung: " + Integer.toString(rezept.getBewertung()));

            Log.d("XL", "Schwierigkeit: " + Integer.toString(rezept.getSchwierigkeitsgrad()));
            schwierigkeit.setProgress(rezept.getSchwierigkeitsgrad());

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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.delete_edit_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ITEM_EDIT:
                Toast.makeText(this,"EDIT SELECTED", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ITEM_DELETE2:
                Intent i = getIntent();
                String id =  i.getStringExtra("id");
                if (id != null) {
                    int id_zahl = Integer.parseInt(id);
                    db.deleteRezept(id_zahl);
                    Intent mainActivity = new Intent(this, MainActivity.class);
                    startActivity(mainActivity);
                    Toast.makeText(this,"gelöscht", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Toast.makeText(this,"UPS", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

}
