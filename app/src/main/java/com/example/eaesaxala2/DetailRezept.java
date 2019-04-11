package com.example.eaesaxala2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.database.DatabaseUtils;

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

        //Navigationbar ändern
        setTitle("Rezept Details");

        TextView rezeptId = findViewById(R.id.REZEPT_ID);
        Intent i = getIntent();
        String id =  i.getStringExtra("id");
        Log.d("SL", "das ist die ID:"+id);
        rezeptId.setText(id);

        if (!(id.equals(null))){
            TextView name = findViewById(R.id.NAMEDETAIL);
            Rezept rezept =  db.selectRezept(id);
            name.setText(rezept.getName());
            ImageView foto = findViewById(R.id.FOTODETAIl);
            Bitmap bitmap = BitmapFactory.decodeFile(rezept.foto);
            foto.setImageBitmap(bitmap);
            TextView zeit = findViewById(R.id.ZEITDETAIL);
            zeit.setText(Integer.toString(rezept.getZeit()));
            TextView bewertung = findViewById(R.id.BEWERTUNGDETAIL);
            bewertung.setText(Integer.toString(rezept.getBewertung()));
            TextView schwierigkeitsgrad = findViewById(R.id.SCHWIERIGKEITSGRADDETAIL);
            schwierigkeitsgrad.setText(Integer.toString(rezept.getSchwierigkeitsgrad()));
            TextView hauptkategorie = findViewById(R.id.HAUPTKATEGORIEDETAIL);
            hauptkategorie.setText(rezept.hauptkategorie);
            TextView unterkategorie = findViewById(R.id.UNTERKATEGORIEDETAIL);
            unterkategorie.setText(rezept.unterkategorie);
            ListView zutatenListe = (ListView) findViewById(R.id.ZUTATEN_LISTE_DETAIL);
            ArrayList <Zutaten> zutaten = rezept.getZutaten();
            Log.d("SL", "das sind die zutaten" + zutaten.toString());
            ZutatenAdapter mAdapter = new ZutatenAdapter(ctx, zutaten);
            zutatenListe.setAdapter(mAdapter);
        }
    }
}
