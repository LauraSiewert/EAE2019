package com.example.eaesaxala2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class BackenGewaehlt extends AppCompatActivity implements View.OnClickListener {

    ImageButton vegan;
    ImageButton notVegan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backen_gewaehlt);
        setTheme(R.style.AppTheme);

        //UI
        vegan = findViewById(R.id.VEGAN_B2);
        notVegan = findViewById(R.id.VEGGI_B2);
        vegan.setOnClickListener(this);
        notVegan.setOnClickListener(this);

        //Titel
        setTitle("Rezept hinzufügen");

    }

    @Override
    public void onClick(View v) {
        if(v==vegan){
            Intent detail_viewInt = new Intent(getApplicationContext(), RezeptHinzufuegen.class);
            detail_viewInt.putExtra("CHOOSE", "vegan");
            startActivity(detail_viewInt);
        }
        else if (v==notVegan){
            Intent detail_viewInt = new Intent(getApplicationContext(), RezeptHinzufuegen.class);
            detail_viewInt.putExtra("CHOOSE", "nicht vegan");
            startActivity(detail_viewInt);
        }

    }
}
