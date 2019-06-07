package com.example.eaesaxala2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RezeptEdit extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    DatenBankManager db;
    Context ctx = this;

    ImageView foto;
    String image2;
    Button imageButton;
    Spinner mainSpinner;
    Spinner subSpinner;
    EditText nameText;
    EditText zeitText;
    ImageButton addZutat;
    ListView zutatenListe;
    SeekBar schwierigkeit;
    RatingBar ratingbar;
    EditText vorgehen;
    Button cancel;
    Button ok;
    String [] unterkategorie = new String[]{"vegan", "nicht vegan", "vegetarisch", "nicht vegetarisch"};
    String [] hauptkategorie = new String []{"Backen", "Kochen"};
    ArrayList <Zutaten> zutaten = new ArrayList<>();
    String id;
    String haupt;
    String unter;
    int nameLänge = 0;
    int mengeLänge = 0;
    int einheitLänge = 0;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezept_edit);
        setTheme(R.style.AppTheme);

        //Tastatur nicht direkt anzeigen
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        db = new DatenBankManager(this);

        //get Views
        foto = findViewById(R.id.FOTO_EDIT);
        imageButton = findViewById(R.id.B_FOTO_EDIT);
        mainSpinner = findViewById(R.id.MAIN_SPINNER2_EDIT);
        setSpinner(mainSpinner,hauptkategorie);
        subSpinner = findViewById(R.id.SUB_SPINNER2_EDIT);
        mainSpinner.setOnItemSelectedListener(RezeptEdit.this);
        nameText = findViewById(R.id.NAME_EDITTEXT_EDIT);
        zeitText = findViewById(R.id.ZEIT_EDITTEXT_EDIT);
        addZutat = findViewById(R.id.B_ZUTAT_EDIT);
        zutatenListe = findViewById(R.id.ZUTATEN_LISTE_EDIT);
        schwierigkeit = findViewById(R.id.SCHWIERIGKEITSGRAD_EDIT);
        ratingbar = findViewById(R.id.BEWERRTUNG_EDIT);
        vorgehen = findViewById(R.id.VORGEHENSWEISE_EDITTEXT_EDIT);
        cancel = findViewById(R.id.CANCEL_EDIT);
        ok = findViewById(R.id.OK_EDIT);

        //Titel
        setTitle("Rezept bearbeiten");

        Intent i = getIntent();
        id = i.getStringExtra("id");


        //Daten in View laden
        if (!(id.equals(null))) {

            Rezept rezept = db.selectRezept(id);
            nameText.setText(rezept.getName());

            haupt = rezept.getHauptkategorie();
            unter = rezept.getUnterkategorie();

            setDefaultSpinner(haupt);

            image2 = rezept.foto;


            Bitmap bitmap = BitmapFactory.decodeFile(rezept.foto);
            foto.setImageBitmap(bitmap);

            zeitText.setText(Integer.toString(rezept.getZeit()));

            ratingbar.setRating(rezept.getBewertung());

            schwierigkeit.setProgress(rezept.getSchwierigkeitsgrad());
            schwierigkeit.setOnSeekBarChangeListener(this);
            vorgehen.setText(rezept.vorgehensweise);

            zutaten = rezept.getZutaten();
            ZutatenAdapter mAdapter = new ZutatenAdapter(ctx, zutaten);
            zutatenListe.setAdapter(mAdapter);
            setListViewHeightBasedOnChildren(zutatenListe);

            registerForContextMenu(zutatenListe);

        }

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        addZutat.setOnClickListener(this);

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ITEM_DELETE:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Toast.makeText(ctx, zutaten.get(info.position).name + " gelöscht.", Toast.LENGTH_SHORT).show();
                zutaten.remove(info.position);
                zutatenListe.invalidateViews();
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

        MenuItem deleteTexView = menu.findItem(R.id.ITEM_DELETE);
        deleteTexView.setTitle(zutaten.get(info.position).name + " löschen");
    }

    //Kamera
    private void setPic() {

        // Get the dimensions of the View
        int targetW = foto.getWidth();
        int targetH = foto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
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

    @Override
    public void onClick(View v) {
        if(v == ok) {
            Log.d("XL", "ok pressed");
            if (checkInformation(currentPhotoPath,nameText, zeitText, zutatenListe,vorgehen)){
                String nameRezept = nameText.getText().toString();
                int zeit = Integer.parseInt(zeitText.getText().toString());
                String vorgehensweise = vorgehen.getText().toString();
                int schwierigkeitsgradWert = schwierigkeit.getProgress();
                int bewertungWert = (int) ratingbar.getRating();

                int rezeptnr = Integer.parseInt(id);
                db.deleteZutatenFürRezept(rezeptnr);

                //Alle Zutaten hinzufügen
                for (int i=0; i<zutaten.size(); i++){
                    db.insertZutaten(zutaten.get(i).name, zutaten.get(i).menge, zutaten.get(i).einheit, id);
                }

                //Alle gespeicherten Zutaten löschen, da nicht mehr gebraucht.
                zutaten.clear();

                //Activity wechseln
                Intent detail_viewInt = new Intent(getApplicationContext(), DetailRezept.class);
                detail_viewInt.putExtra("id", id);
                startActivity(detail_viewInt);

                db.updateRezept(id, nameRezept, currentPhotoPath, bewertungWert, schwierigkeitsgradWert, vorgehensweise ,zeit,mainSpinner.getSelectedItem().toString(), subSpinner.getSelectedItem().toString(), 0);

                //TO-DO: hier Toast, dass es tatsächlich gespeichert wurdes
                Toast saveToast = Toast.makeText(this, "Dein Rezept wurde gespeichert.", Toast.LENGTH_SHORT);
                saveToast.show();
            }
        } else if (v == cancel) {
            Intent detail_viewInt = new Intent(getApplicationContext(), DetailRezept.class);
            detail_viewInt.putExtra("id", id);
            startActivity(detail_viewInt);
        } else if (v == imageButton) {
            dispatchTakePictureIntent();
        } else if (v == addZutat){
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
                    zutaten.add(zutat);


                    //Zutaten hinzufügen
                    zutatenListe = findViewById(R.id.ZUTATEN_LISTE_EDIT);
                    ZutatenAdapter mAdapter = new ZutatenAdapter(ctx, zutaten);
                    zutatenListe.setAdapter(mAdapter);
                    setListViewHeightBasedOnChildren(zutatenListe);

                    nameLänge=0;
                    mengeLänge=0;
                    einheitLänge=0;

                }
            });
            dialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog alertDialog = dialog.create();

            dialog.create();
            alertDialog.show();

            //Hinzufügen-Button enablen, bis alles ausgefüllt ist
            final Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            okButton.setEnabled(false);
            final EditText nameEdit = (EditText) layout.findViewById(R.id.NAME);
            nameEdit.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    nameLänge = nameEdit.getText().length();
                    checkDialog(nameLänge, mengeLänge, einheitLänge, okButton);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            final EditText mengeEdit = (EditText) layout.findViewById(R.id.MENGE);
            mengeEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mengeLänge = mengeEdit.getText().toString().length();
                    checkDialog(nameLänge, mengeLänge, einheitLänge, okButton);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            final EditText einheitEdit = (EditText) layout.findViewById(R.id.EINHEIT);
            einheitEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    einheitLänge = einheitEdit.getText().length();
                    checkDialog(nameLänge, mengeLänge, einheitLänge, okButton);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        }

    }

    //Methode zum prüfen, ob alle Fedler ausgefüllt sind

    public void checkDialog (int nameLänge, int mengeLänge, int einheitLänge, Button okButton){
        if ((nameLänge==0)&&(mengeLänge==0)&&(einheitLänge==0)) {
            okButton.setEnabled(false);
        }
        else if ((nameLänge!=0)&&(mengeLänge!=0)&&(einheitLänge!=0)){
            okButton.setEnabled(true);

            //Zurück auf 0 setzen
            nameLänge=0;
            mengeLänge=0;
            einheitLänge=0;
        }
    }

    public boolean checkInformation(String image, EditText titel, EditText zeit, ListView zutaten, EditText zubereitung){
        Log.d("XL",image + titel.getText().toString() + zeit.getText().toString() + zubereitung.getText().toString() + zutaten.toString());
        Button ok = (Button)findViewById(R.id.OK_EDIT);
        ok.isEnabled();

        if(image == null){
            currentPhotoPath = image2;
        }

        if((titel.getText().toString().matches(""))||(zeit.getText().toString().matches(""))||(zutaten.equals(null))||(zubereitung.getText().toString().matches(""))){
            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Es fehlen Informationen");
            builder.setMessage("Bitte trage die fehlenden Informationen ein.");
            dialog = builder.create();
            dialog.show();
            return false;
        }
        else{
            ok.isClickable();
            return true;
        }
    }

    //Methode um schnell einen  Spinner zu erstellen
    public void setSpinner (Spinner spinner, String [] array){
        int itemRes = android.R.layout.simple_spinner_dropdown_item;
        ArrayAdapter adapter = new ArrayAdapter(ctx, itemRes, array);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int itemRes = android.R.layout.simple_spinner_dropdown_item;
        //Möglichkeiten für Hauptkategoriespinner im Dialog
        if (position==0) {
            String[] inhalt = new String[2];
            inhalt[0] = unterkategorie[0];
            inhalt[1] = unterkategorie[1];
            setSpinner(subSpinner, inhalt);

            //Ausgewählte Unterkategorie setzen
            if (unter.equals("vegan")){
                subSpinner.setSelection(0);
            }
            else if (unter.equals("nicht vegan")){
                subSpinner.setSelection(1);
            }
        } else if (position == 1) {
            String[] inhalt = new String[3];
            inhalt[0] = unterkategorie[0];
            inhalt[1] = unterkategorie[2];
            inhalt[2] = unterkategorie[1];
            setSpinner(subSpinner, inhalt);

            //Ausgewählte Unterkategorie setzen
            if (unter.equals("vegan")){
                subSpinner.setSelection(0);
            }
            else if (unter.equals("nicht vegan")){
                subSpinner.setSelection(2);
            }
            else if (unter.equals("vegetarisch")){
                subSpinner.setSelection(1);
            }

        }

    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        TextView gradText = findViewById(R.id.GRAD_TEXT_EDIT);
        if(progress==0){
            gradText.setText("leicht");
        }
        else if(progress==1){
            gradText.setText("mittel");
        }
        else if(progress==2){
            gradText.setText("schwer");
        }
        else{
            gradText.setText("k.A.");
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Methode um den Default-Wert des Spinners zu setzen
    public void setDefaultSpinner (String choose){
        if (choose.equals("Backen")){//Backen vegan
            mainSpinner.setSelection(0);
        }
        else if (choose.equals("Kochen")) {//Backen nicht vegan
            mainSpinner.setSelection(1);

        } else if (choose.equals("leer")){
        }
    }

}
