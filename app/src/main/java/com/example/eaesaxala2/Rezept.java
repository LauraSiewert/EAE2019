package com.example.eaesaxala2;

import java.util.ArrayList;

public class Rezept {
    public String name;
    public String foto;
    public int zeit;
    public int schwierigkeitsgrad;
    public int bewertung;
    public String vorgehensweise;
    public String hauptkategorie;
    public String unterkategorie;
    public ArrayList <Zutaten> zutaten;

    public Rezept(String name, String foto, int zeit, int schwierigkeitsgrad, int bewertung, String vorgehensweise, String hauptkategorie, String unterkategorie, ArrayList<Zutaten> zutaten) {
        this.name = name;
        this.foto = foto;
        this.zeit = zeit;
        this.schwierigkeitsgrad = schwierigkeitsgrad;
        this.bewertung = bewertung;
        this.vorgehensweise = vorgehensweise;
        this.hauptkategorie = hauptkategorie;
        this.unterkategorie = unterkategorie;
        this.zutaten = zutaten;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFoto() {
        return Integer.parseInt(foto);
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getZeit() {
        return zeit;
    }

    public void setZeit(int zeit) {
        this.zeit = zeit;
    }

    public int getSchwierigkeitsgrad() {
        return schwierigkeitsgrad;
    }

    public void setSchwierigkeitsgrad(int schwierigkeitsgrad) {
        this.schwierigkeitsgrad = schwierigkeitsgrad;
    }

    public int getBewertung() {
        return bewertung;
    }

    public void setBewertung(int bewertung) {
        this.bewertung = bewertung;
    }

    public String getVorgehensweise() {
        return vorgehensweise;
    }

    public void setVorgehensweise(String vorgehensweise) {
        this.vorgehensweise = vorgehensweise;
    }

    public String getHauptkategorie() {
        return hauptkategorie;
    }

    public void setHauptkategorie(String hauptkategorie) {
        this.hauptkategorie = hauptkategorie;
    }

    public String getUnterkategorie() {
        return unterkategorie;
    }

    public void setUnterkategorie(String unterkategorie) {
        this.unterkategorie = unterkategorie;
    }

    public ArrayList<Zutaten> getZutaten() {
        return zutaten;
    }

    public void setZutaten(ArrayList<Zutaten> zutaten) {
        this.zutaten = zutaten;
    }
}