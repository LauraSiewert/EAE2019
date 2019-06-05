package com.example.eaesaxala2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
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

public class DetailRezept extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    DatenBankManager db;
    Context ctx = this;
    String id;
    int id_zahl;
    TextView schwierigkeit_int;
    SeekBar schwierigkeit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rezept);
        setTheme(R.style.AppTheme);

        //Navigationbar mit zurück-Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatenBankManager(this);

        //UI
        TextView name = findViewById(R.id.NAMEDETAIL);
        ImageView foto = findViewById(R.id.FOTODETAIl);
        TextView zeit = findViewById(R.id.ZEITDETAIL);
        schwierigkeit = findViewById(R.id.SCHWIERIGKEITSDETAIL);
        ImageView hauptkategorie = findViewById(R.id.HAUPTKATEGORIEDETAIL);
        ImageView unterkategorie = findViewById(R.id.UNTERKATEGORIEDETAIL);
        TextView vorgehensweise = findViewById(R.id.VORGEHENSWEISE_EDITTEXT);
        ListView zutatenListe = (ListView) findViewById(R.id.ZUTATEN_LISTE_DETAIL);
        RatingBar rating = findViewById(R.id.BEWERTUNGDETAIL);



        //Menu
        registerForContextMenu(name);

        //Intents
        Intent i = getIntent();
        String id =  i.getStringExtra("id");
        Log.d("SL", "das ist die ID:"+id);

        if (!(id.equals(null))){

            Rezept rezept =  db.selectRezept(id);
            name.setText(rezept.getName());

            //Navigationbar ändern
            setTitle("Rezept");

            Bitmap bitmap = BitmapFactory.decodeFile(rezept.foto);
            foto.setImageBitmap(bitmap);

            zeit.setText(Integer.toString(rezept.getZeit()));
            Log.d("XL","Bewertung: " + Integer.toString(rezept.getBewertung()));
            rating.setRating(rezept.getBewertung());

            Log.d("XL", "Schwierigkeit: " + Integer.toString(rezept.getSchwierigkeitsgrad()));
            schwierigkeit.setProgress(rezept.getSchwierigkeitsgrad());
            schwierigkeit.setOnSeekBarChangeListener(this);
            setSchwierigkeit_int(schwierigkeit);
            Log.e("SL", "das ist mein progress: "+ rezept.getSchwierigkeitsgrad());
            Log.e("SL", "das ist mein progress2: "+ schwierigkeit.getProgress());

            //schwierigkeit_int.setText(Integer.toString(rezept.getSchwierigkeitsgrad()));

            if ((rezept.hauptkategorie).equals("Backen")){
                hauptkategorie.setImageResource(R.drawable.cupcake);
            } else if ((rezept.hauptkategorie).equals("Kochen")){
                hauptkategorie.setImageResource(R.drawable.cooking);
            }

            if ((rezept.unterkategorie).equals("vegan")){ //Backen vegan
                unterkategorie.setImageResource(R.drawable.leaves);
            } else if ((rezept.unterkategorie).equals("vegetarisch")){ //Kochen vegetarisch
                unterkategorie.setImageResource(R.drawable.broccoli);
            } else if ((rezept.unterkategorie).equals("nicht vegan2")) { //Kochen nicht vegan
                unterkategorie.setImageResource(R.drawable.meat);
            } else if ((rezept.unterkategorie).equals("vegan2")) { //Kochen vegan
                unterkategorie.setImageResource(R.drawable.leaves);
            } else if ((rezept.unterkategorie).equals("nicht vegan")) { //Backen nicht vegan 
                unterkategorie.setImageResource(R.drawable.broccoli);
            }

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
        id =  i.getStringExtra("id");
        id_zahl = Integer.parseInt(id);

        switch (item.getItemId()){
            case R.id.ITEM_EDIT:
                Intent nextActivity = new Intent(this, RezeptEdit.class);
                nextActivity.putExtra("id",id);
                startActivity(nextActivity);
                return true;
            case R.id.ITEM_DELETE2:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Löschen");
                dialog.setMessage("Möchtest du das Rezept wirklich löschen?");
                dialog.setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (id != null) {
                            Toast.makeText(DetailRezept.this, db.getRezeptName(id_zahl) + " gelöscht", Toast.LENGTH_SHORT).show();
                            db.deleteRezept(id_zahl);
                            Intent mainActivity = new Intent(DetailRezept.this, MainActivity.class);
                            startActivity(mainActivity);
                        }
                    }
                }).setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.create();
                dialog.show();
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

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


    }

    public void setSchwierigkeit_int (SeekBar bar){
        schwierigkeit_int = findViewById(R.id.SCHWIERIGKEITSDETAIL_INT);
        Log.e("SL", "progress ist: "+bar.getProgress());
        Log.e("SL", "progress ist auch: " + bar.getProgress());
        if(bar.getProgress()==0){
            schwierigkeit_int.setText("leicht");
            Log.e("SL", "grad ist 0");
        }
        else if(bar.getProgress()==1){
            schwierigkeit_int.setText("mittel");
            Log.e("SL", "grad ist 1");
        }
        else if(bar.getProgress()==2){
            schwierigkeit_int.setText("schwer");
            Log.e("SL", "grad ist 2");
        }
        else{
            schwierigkeit_int.setText("k.A.");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
