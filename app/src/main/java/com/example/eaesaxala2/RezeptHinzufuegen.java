package com.example.eaesaxala2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RezeptHinzufuegen extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {
    Context ctx = this;
    Spinner mainspinner2;
    Spinner subspinner2;
    DatenBankManager db;
    Button ok;
    Button cancel;
    String [] unterkategorie = new String[]{"vegan", "nicht vegan", "vegetarisch", "nicht vegetarisch"};
    String [] hauptkategorie = new String []{"Backen", "Kochen"};
    String choosed;
    ImageView foto;
    Button fotob;
    private static final int CAM_REQUEST=1313;
    Bitmap bitmap;
    SeekBar schwierirgkeitsgrad;
    TextView gradText;
    RatingBar bewertung;
    ImageButton zutatenb;
    ArrayList <Zutaten> neueZutaten = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezept_hinzufuegen);
        db = new DatenBankManager(this);

        setTitle("Rezept hinzufügen");

        //Intents
        Intent i = getIntent();
        choosed = i.getStringExtra("CHOOSE");
        Log.d("SL", "Das ist "+ choosed);

        //Spinner setzen
        mainspinner2 = findViewById(R.id.MAIN_SPINNER2);
        setSpinner(mainspinner2, hauptkategorie);
        subspinner2 = findViewById(R.id.SUB_SPINNER2);
        mainspinner2.setOnItemSelectedListener(RezeptHinzufuegen.this);


        //Buttons setzen
        ok = findViewById(R.id.OK);
        ok.setOnClickListener(this);
        cancel = findViewById(R.id.CANCEL);
        cancel.setOnClickListener(this);


        //Für das Foto
        foto = findViewById(R.id.FOTO);
        fotob = findViewById(R.id.B_FOTO);
        fotob.setOnClickListener(this);

        //Schwierigkeitsgrad
        schwierirgkeitsgrad = findViewById(R.id.SCHWIERIGKEITSGRAD);
        schwierirgkeitsgrad.setOnSeekBarChangeListener(this);
        gradText = findViewById(R.id.GRAD_TEXT);

        //Bewertung
        bewertung = findViewById(R.id.BEWERRTUNG);

        //Zutaten
        zutatenb = findViewById(R.id.ZUTAT_B);
        zutatenb.setOnClickListener(this);


        //Ausgewählten SpinnerInhalt
        setDefaultSpinner(choosed);
    }

    public void rateMe(View view){
        Toast.makeText(getApplicationContext(),
                String.valueOf(bewertung.getRating()), Toast.LENGTH_LONG).show();
    }

    //Kamera
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_REQUEST){
            bitmap = (Bitmap) data.getExtras().get("data");
            foto.setImageBitmap(bitmap);
        }
    }


    //Methode um den Default-Wert des Spinners zu setzen
    public void setDefaultSpinner (String choosed){
        String[] subspinner = {"vegan", " nicht vegan", "vegetarisch"};
        if (choosed.equals("vegan")){//Backen vegan
            mainspinner2.setSelection(0);
        }
        else if (choosed.equals("nicht vegan")){//Backen nicht vegan
            mainspinner2.setSelection(0);
        }
        else if(choosed.equals("vegetarisch")){ //Kochen vegetarisch
            mainspinner2.setSelection(1);
        }
        else if(choosed.equals("nicht vegan2")){ //Kochen nicht vegan
            mainspinner2.setSelection(1);
        }
        else if(choosed.equals("vegan2")){ //Kochen vegan
            mainspinner2.setSelection(1);
        }
        else if (choosed.equals("leer")){
            Log.d("Sl", "leer");
        }
    }


    @Override
    public void onClick(View v) {
        if (v==ok){
            EditText nameRezeptEditText = (EditText) findViewById(R.id.NAME_EDITTEXT);
            EditText zeitEditText = (EditText) findViewById(R.id.ZEIT_EDITTEXT);
            EditText vorgehenEditText = (EditText)findViewById(R.id.VORGEHENSWEISE_EDITTEXT);
            String nameRezept = nameRezeptEditText.getText().toString();
            int zeit = Integer.parseInt(zeitEditText.getText().toString());
            String vorgehensweise = vorgehenEditText.getText().toString();
            int schwierigkeitsgradWert = schwierirgkeitsgrad.getProgress();
            int bewertungWert = (int) bewertung.getRating();

            long id = db.insertRezept(nameRezept, foto.toString(), schwierigkeitsgradWert, bewertungWert, vorgehensweise,zeit,mainspinner2.getSelectedItem().toString(), subspinner2.getSelectedItem().toString(), 0);
            String newId = Long.toString(id);

            //Alle Zutaten hinzufügen
            for (int i=0; i<neueZutaten.size(); i++){
                long id2 = db.insertZutaten(neueZutaten.get(i).name, neueZutaten.get(i).menge, neueZutaten.get(i).einheit, newId);
            }
            //Alle gespeicherten Zutaten löschen, da nicht mehr gebraucht.
            neueZutaten.clear();

            //Activity wechseln
            Intent detail_viewInt = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(detail_viewInt);

            //TO-DO: hier Toast, dass es tatsächlich gespeichert wurdes
            Toast saveToast = Toast.makeText(this, "Dein Rezept wurde gespeichert.", Toast.LENGTH_SHORT);
            saveToast.show();

        }
        else if(v==cancel){
            Intent detail_viewInt = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(detail_viewInt);
        }
        else if(v==fotob){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAM_REQUEST);
        }
        else if (v==zutatenb){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Neue Zutat hinzufügen");
            dialog.setMessage("Infortmation eintragen:");
            final View layout = this.getLayoutInflater().inflate(R.layout.dialog_add_rezept, null);
            dialog.setView(layout);
            dialog.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText nameEdit = (EditText) layout.findViewById(R.id.NAME);
                    EditText mengeEdit = (EditText) layout.findViewById(R.id.MENGE);
                    EditText einheitEdit = (EditText) layout.findViewById(R.id.EINHEIT);
                    String name = nameEdit.getText().toString();
                    double menge = Double.parseDouble(mengeEdit.getText().toString());
                    String einheit = einheitEdit.getText().toString();
                    Zutaten zutat;
                    zutat = new Zutaten (name, menge, einheit);
                    neueZutaten.add(zutat);


                    //Zutaten hinzufügen
                    ListView zutatenListe = findViewById(R.id.ZUTATEN_LISTE);
                    int itemRes = android.R.layout.simple_list_item_2;

                    //In Liste anzeigen
                    ArrayList <String> namen = new ArrayList<>();
                    ArrayList <String> menge2 = new ArrayList<>();
                    int textView1 = android.R.id.text1;
                    int textView2 = android.R.id.text2;
                    for(int j=0; j<neueZutaten.size(); j++){
                        namen.add(neueZutaten.get(j).getName());
                        Log.d("SL","ist:"+ neueZutaten.get(j).getName());
                        menge2.add(neueZutaten.get(j).getMenge()+" "+ neueZutaten.get(j).getEinheit());
                    }
                    ArrayAdapter zutatenAdapter = new ArrayAdapter(ctx, itemRes, textView1, namen);
                    ArrayAdapter zutatenAdapter2 = new ArrayAdapter(ctx, itemRes, textView2, menge2);
                    zutatenListe.setAdapter(zutatenAdapter);
                    zutatenListe.setAdapter(zutatenAdapter2);

                }
            });
            dialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            dialog.create();
            dialog.show();

        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int itemRes = android.R.layout.simple_spinner_dropdown_item;
        //Möglichkeiten für Hauptkategoriespinner im Dialog
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();
            if (position==0) {
                String[] inhalt = new String[2];
                inhalt[0] = unterkategorie[0];
                inhalt[1] = unterkategorie[1];
                setSpinner(subspinner2, inhalt);
                Log.d("SL", "Pos 0");

                //Ausgewählte Unterkategorie setzen
                if (choosed.equals("vegan")){
                    subspinner2.setSelection(0);
                }
                else if (choosed.equals("nicht vegan")){
                    subspinner2.setSelection(1);
                }
            } else if (position == 1) {
                String[] inhalt = new String[3];
                inhalt[0] = unterkategorie[0];
                inhalt[1] = unterkategorie[2];
                inhalt[2] = unterkategorie[1];
                setSpinner(subspinner2, inhalt);

                //Ausgewählte Unterkategorie setzen
                if (choosed.equals("vegan2")){
                    subspinner2.setSelection(0);
                }
                else if (choosed.equals("nicht vegan2")){
                    subspinner2.setSelection(2);
                }
                else if (choosed.equals("vegetarisch")){
                    subspinner2.setSelection(1);
                }
                Log.d("SL", "Pos 1");
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //Methode um schnell einen  Spinner zu erstellen
    public void setSpinner (Spinner spinner, String [] array){
        int itemRes = android.R.layout.simple_spinner_dropdown_item;
        ArrayAdapter adapter = new ArrayAdapter(ctx, itemRes, array);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        gradText.setText("Wert: "+progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

}



