package com.example.eaesaxala2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class KochenGewaehlt extends AppCompatActivity implements View.OnClickListener {

    ImageButton vegan;
    ImageButton veggie;
    ImageButton meat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kochen_gewaehlt);
        setTheme(R.style.AppTheme);

        //UI
        vegan = findViewById(R.id.VEGAN_B);
        veggie = findViewById(R.id.VEGGI_B);
        meat = findViewById(R.id.MEAT_B);
        vegan.setOnClickListener(this);
        veggie.setOnClickListener(this);
        meat.setOnClickListener(this);

        //Titel
        setTitle("Rezept hinzufügen");

    }

    @Override
    public void onClick(View v) {
        Intent detail_viewInt = new Intent(getApplicationContext(), RezeptHinzufuegen.class);
        if(v==vegan){
            detail_viewInt.putExtra("CHOOSE", "vegan2");
        }
        else if(v==veggie){
            detail_viewInt.putExtra("CHOOSE", "vegetarisch");
        }
        else if (v==meat){
            detail_viewInt.putExtra("CHOOSE", "nicht vegan2");
        }
        startActivity(detail_viewInt);
    }
}
