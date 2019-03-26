package com.example.eaesaxala2;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Zutaten implements Parcelable {
    public String name;
    public double menge;
    public String einheit;
    public static final Parcelable.Creator CREATOR = new Creator <Zutaten>(){
        @Override
        public Zutaten createFromParcel(Parcel source) {
            Zutaten CREATOR_ZUTAT = new Zutaten();
            CREATOR_ZUTAT.name = source.readString();
            CREATOR_ZUTAT.menge = source.readDouble();
            CREATOR_ZUTAT.einheit = source.readString();
            return  CREATOR_ZUTAT;
        }

        @Override
        public Zutaten[] newArray(int size) {
            return new Zutaten[size];
        }
    };

    public Zutaten(String name, double menge, String einheit) {
        this.name = name;
        this.menge = menge;
        this.einheit = einheit;
    }

    public Zutaten(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMenge() {
        return menge;
    }

    public void setMenge(double menge) {
        this.menge = menge;
    }

    public String getEinheit() {
        return einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(menge);
        dest.writeString(einheit);
    }
}
