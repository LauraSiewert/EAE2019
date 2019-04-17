package com.example.eaesaxala2;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
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


//TO-DO:
//Aussehen anpassen
//Warum ist vorgehensweise immer null?
//Prüfung, wenn etwas nicht eingegeben wird, dass die App nicht abstürzt


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
    ListView  zutatenListe;
    //Test

    @RequiresApi(api = Build.VERSION_CODES.M)
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

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        //ZutatenListeActions
        zutatenListe = (ListView) findViewById(R.id.ZUTATEN_LISTE);
        registerForContextMenu(zutatenListe);

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

    public void rateMe(View view){
        Toast.makeText(getApplicationContext(),
                String.valueOf(bewertung.getRating()), Toast.LENGTH_LONG).show();
    }

    //Kamera
    private void setPic() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        foto.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d("Sl", "Image file wurde kreiert");
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("Sl", "ERROOOOOOR yeaaah");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d("Sl", "photofile ist nicht null");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                Log.d("SL", "PhotoURI"+ photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
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

            long id = db.insertRezept(nameRezept, currentPhotoPath, schwierigkeitsgradWert, bewertungWert, vorgehensweise ,zeit,mainspinner2.getSelectedItem().toString(), subspinner2.getSelectedItem().toString(), 0);
            String newId = Long.toString(id);

            //Alle Zutaten hinzufügen
            for (int i=0; i<neueZutaten.size(); i++){
                db.insertZutaten(neueZutaten.get(i).name, neueZutaten.get(i).menge, neueZutaten.get(i).einheit, newId);
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
            dispatchTakePictureIntent();
            //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(intent,CAM_REQUEST);
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
                    zutatenListe = findViewById(R.id.ZUTATEN_LISTE);
                    ZutatenAdapter mAdapter = new ZutatenAdapter(ctx, neueZutaten);
                    zutatenListe.setAdapter(mAdapter);
                    setListViewHeightBasedOnChildren(zutatenListe);

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

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ITEM_DELETE:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                neueZutaten.remove((int) info.position);
                updateListe();
                setListViewHeightBasedOnChildren(zutatenListe);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String rezeptName = neueZutaten.get(info.position).name;

        MenuItem deleteTexView = menu.findItem(R.id.ITEM_DELETE);
        deleteTexView.setTitle(rezeptName + " löschen");
    }

    public void updateListe() {
        zutatenListe = findViewById(R.id.ZUTATEN_LISTE);
        ZutatenAdapter mAdapter = new ZutatenAdapter(ctx, neueZutaten);
        zutatenListe.setAdapter(mAdapter);
    }

    /*public void checkInformation(){
        if()
    }*/
}



