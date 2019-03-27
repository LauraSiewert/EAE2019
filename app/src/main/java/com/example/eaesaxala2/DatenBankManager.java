package com.example.eaesaxala2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class DatenBankManager extends SQLiteOpenHelper {


    public static final int DATENBANK_VERSION = 7;
    public static final String KOCHBUCH_DATENBANK = "Kochbuch.db";
    //Rezept-Tabelle
    public static final String TABELLE_REZEPT = "rezept";
    public static final String SPALTE_REZEPT_ID = "_id";
    public static final String SPALTE_REZEPT_NAME = "name";
    public static final String SPALTE_REZEPT_BILD = "bild";
    public static final String SPALTE_REZEPT_ZEIT = "zeit";
    public static final String SPALTE_REZEPT_SCHWIERIGKEITSGRAD = "schwierigkeitsgrad";
    public static final String SPALTE_REZEPT_BEWERTUNG = "bewertung";
    public static final String SPALTE_REZEPT_VORGEHENSWEISE = "vorgehensweise";
    public static final String SPALTE_REZEPT_HAUPTKATEGORIE = "hauptkategorie";
    public static final String SPALTE_REZEPT_UNTERKATEGORIE = "unterkategorie";
    public static final String SPALTE_REZEPT_LIKED = "liked";

    //Zutaten-Tabelle
    public static final String TABELLE_ZUTATEN = "zutaten";
    public static final String SPALTE_ZUTATEN_ID = "z_id";
    public static final String SPALTE_ZUTATEN_NAME = "name";
    public static final String SPALTE_MENGE = "menge";
    public static final String SPALTE_EINHEIT = "einheit";


    public DatenBankManager (Context cxt){
        super(cxt, KOCHBUCH_DATENBANK, null, DATENBANK_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABELLE_REZEPT + "(" +
                        SPALTE_REZEPT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SPALTE_REZEPT_NAME + " TEXT, " +
                        SPALTE_REZEPT_BILD +" TEXT, " +
                        SPALTE_REZEPT_BEWERTUNG + " INTEGER, "+
                        SPALTE_REZEPT_SCHWIERIGKEITSGRAD + " INTEGER, " +
                        SPALTE_REZEPT_VORGEHENSWEISE + " TEXT, "+
                        SPALTE_REZEPT_ZEIT + " INTEGER, " +
                        SPALTE_REZEPT_HAUPTKATEGORIE + " TEXT, "+
                        SPALTE_REZEPT_UNTERKATEGORIE + " TEXT, "+
                        SPALTE_REZEPT_LIKED + " INTEGER "+
                        ")"
        );
        db.execSQL(
                "CREATE TABLE " + TABELLE_ZUTATEN + "(" +
                        SPALTE_ZUTATEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SPALTE_ZUTATEN_NAME + " TEXT, " +
                        SPALTE_REZEPT_ID +" INTEGER NOT NULL, " +
                        SPALTE_MENGE + "REAL, "+
                        SPALTE_EINHEIT + "TEXT, "+
                        "FOREIGN KEY("+ SPALTE_REZEPT_ID + ") REFERENCES "+ TABELLE_ZUTATEN+"("+SPALTE_REZEPT_ID +")"+
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELLE_ZUTATEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLE_REZEPT);
        onCreate(db);
    }

    //Methoden für Rezept


    public long insertRezept (String name, String bild, int bewertung, int schwierigkeitsgrad, String vorgehensweise, double zeit, String hauptkategorie, String unterkategorie, int liked){
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(SPALTE_REZEPT_NAME, name);
        neueZeile.put(SPALTE_REZEPT_BILD, bild);
        neueZeile.put(SPALTE_REZEPT_BEWERTUNG, bewertung);
        neueZeile.put(SPALTE_REZEPT_SCHWIERIGKEITSGRAD, schwierigkeitsgrad);
        neueZeile.put(SPALTE_REZEPT_ZEIT, zeit);
        neueZeile.put(SPALTE_REZEPT_HAUPTKATEGORIE, hauptkategorie);
        neueZeile.put(SPALTE_REZEPT_UNTERKATEGORIE, unterkategorie);
        neueZeile.put(SPALTE_REZEPT_LIKED, 0);
        SQLiteDatabase db = this.getWritableDatabase();

        long newRowId = db.insert(DatenBankManager.TABELLE_REZEPT, null, neueZeile);

        //Gibt neue ID zurück
        return newRowId;
    }

    public Cursor selectAllRezepte (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor meinZeiger = db.rawQuery("SELECT * FROM "+ TABELLE_REZEPT,null);
        meinZeiger.moveToFirst();
        return meinZeiger;
    }

    public Rezept selectRezept (String rezeptnr){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor meinZeiger = db.rawQuery("SELECT * FROM "+ TABELLE_REZEPT+ " WHERE "+ SPALTE_REZEPT_ID + " = "+ Integer.parseInt(rezeptnr), null);
        String name ="";
        String foto ="";
        int zeit=0;
        int schwierigkeitsgrad=0;
        int bewertung=0;
        String vorgehensweise="";
        String hauptkategorie="";
        String unterkategorie="";
        ArrayList<Zutaten> zutaten= selectZutatenFürRezept(rezeptnr);
        if(meinZeiger.moveToFirst()){
            name = (String) meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_REZEPT_NAME));
            foto = (String) meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_REZEPT_BILD));
            zeit = meinZeiger.getInt(meinZeiger.getColumnIndex(SPALTE_REZEPT_ZEIT));
            schwierigkeitsgrad = meinZeiger.getInt(meinZeiger.getColumnIndex(SPALTE_REZEPT_SCHWIERIGKEITSGRAD));
            bewertung = meinZeiger.getInt(meinZeiger.getColumnIndex(SPALTE_REZEPT_BEWERTUNG));
            vorgehensweise = (String) meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_REZEPT_VORGEHENSWEISE));
            hauptkategorie = (String) meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_REZEPT_HAUPTKATEGORIE));
            unterkategorie = (String) meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_REZEPT_UNTERKATEGORIE));
        }
        Rezept rezept = new Rezept(name, foto, zeit, schwierigkeitsgrad, bewertung, vorgehensweise, hauptkategorie, unterkategorie, zutaten);
        if (rezept != null){
            return rezept;
        }else{
            Zutaten zutat = new Zutaten("Test", 32, "Test");
            ArrayList<Zutaten> zutaten2 = new ArrayList<>();
            zutaten2.add(zutat);
            Rezept test = new Rezept("test","test", 42, 1, 1, "Test", "Test", "Test", zutaten2);
            return test;
        }

    }

    public void updateLike (String rezeptnr, int like){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPALTE_REZEPT_LIKED, like);
        String where = SPALTE_REZEPT_ID + "=?";
        String[] whereArg = new String [] {rezeptnr};
        db.update(TABELLE_REZEPT, values, where, whereArg);
    }


    public Cursor selectRezeptByHauptKategorie(String hauptkategorie){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor meinZeiger = db.rawQuery("SELECT * FROM "+ TABELLE_REZEPT+ " WHERE "+ SPALTE_REZEPT_HAUPTKATEGORIE + " = '"+ hauptkategorie+"'", null);
        meinZeiger.moveToFirst();
        return meinZeiger;
    }

    public Cursor selectRezeptByUnterkategorie(String unterkategorie){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor meinZeiger = db.rawQuery("SELECT * FROM "+ TABELLE_REZEPT+ " WHERE "+ SPALTE_REZEPT_UNTERKATEGORIE + " = '"+ unterkategorie+"'", null);
        meinZeiger.moveToFirst();
        return meinZeiger;
    }

    public Cursor selectRezeptByUnterkategorieUndHauptkategorie (String unterkategorie, String hauptkategorie){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor meinZeiger = db.rawQuery("SELECT * FROM "+ TABELLE_REZEPT+ " WHERE "+ SPALTE_REZEPT_UNTERKATEGORIE + " = '"+ unterkategorie+"' AND "+ SPALTE_REZEPT_HAUPTKATEGORIE + " = '"+ hauptkategorie+"'", null);
        meinZeiger.moveToFirst();
        return meinZeiger;
    }

    public void deleteRezept(int rezeptnr){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = SPALTE_REZEPT_ID + "=?";
        String[] whereArg = new String[]{Integer.toString(rezeptnr)};
        db.delete(TABELLE_REZEPT, where, whereArg);
    }

    public String getRezeptName(int rezeptnr) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor meinZeiger = db.rawQuery("SELECT " + SPALTE_REZEPT_NAME + " FROM "+  TABELLE_REZEPT +" WHERE "+ SPALTE_REZEPT_ID + " = " + rezeptnr, null);
        String titel = "";
        if(meinZeiger.moveToFirst()){
            titel = (String) meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_REZEPT_NAME));
        }
        return titel;
    }


    //Methoden für Zutaten
    public long insertZutaten (String name, Double menge, String einheit, String rezeptnr){
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(SPALTE_ZUTATEN_NAME, name);
        neueZeile.put(SPALTE_REZEPT_ID, rezeptnr);
        neueZeile.put(SPALTE_EINHEIT, einheit);
        neueZeile.put(SPALTE_MENGE, menge);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABELLE_ZUTATEN, null, neueZeile);

        long newRowId = db.insert(DatenBankManager.TABELLE_REZEPT, null, neueZeile);

        //Gibt neue ID zurück
        return newRowId;
    }

    public ArrayList <Zutaten> selectZutatenFürRezept (String rezeptnr){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor meinZeiger = db.rawQuery("SELECT * FROM "+ TABELLE_ZUTATEN + " WHERE "+ SPALTE_REZEPT_ID + " = "+ Integer.parseInt(rezeptnr), null);
        ArrayList<Zutaten> zutaten= new ArrayList<>();
        if(meinZeiger.moveToFirst()){
            zutaten.get(0).setName(meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_ZUTATEN_NAME)));
            zutaten.get(0).setEinheit(meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_EINHEIT)));
            zutaten.get(0).setMenge(meinZeiger.getDouble(meinZeiger.getColumnIndex(SPALTE_MENGE)));
        }
        return zutaten;
    }

    public void deleteZutatenFürRezept (int rezeptnr){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = SPALTE_REZEPT_ID + "=?";
        String[] whereArg = new String[]{Integer.toString(rezeptnr)};
        db.delete(TABELLE_ZUTATEN, where, whereArg);
    }
}
