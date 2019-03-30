package com.example.eaesaxala2;

public class ItemObject {
    private int id;
    private String word;
    private String meaning;
    private String bild;
    private int bewertung;
    private String idR;

    public ItemObject(int id, String word, String bild, int bewertung, String idR) {

        this.id = id;
        this.word = word;
        this.bewertung= bewertung;
        this.bild = bild;
        this.idR = idR;

    }

    public ItemObject(int id, String word, String meaning) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;

    }

    public String getBild() {
        return bild;
    }

    public int getBewertung() {
        return bewertung;
    }

    public String getIdR() {
        return idR;
    }

    public int getId() { return id; }

    public String getWord() { return word; }

    public String getMeaning() { return meaning; }
}
