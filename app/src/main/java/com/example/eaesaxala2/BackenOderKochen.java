package com.example.eaesaxala2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class BackenOderKochen extends AppCompatActivity implements View.OnClickListener {

    ImageButton kochen;
    ImageButton backen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backen_oder_kochen);

        //Button registrieren
        kochen = findViewById(R.id.KOCHEN_B);
        backen = findViewById(R.id.BACKEN_B);
        kochen.setOnClickListener(this);
        backen.setOnClickListener(this);

        setTitle("Rezept hinzufügen");

    }

    @Override
    public void onClick(View v) {
        if(v==kochen){
            Intent detail_viewInt = new Intent(getApplicationContext(), KochenGewaehlt.class);
            startActivity(detail_viewInt);
        }
        else{
            Intent detail_viewInt = new Intent(getApplicationContext(), BackenGewaehlt.class);
            startActivity(detail_viewInt);
        }
    }
}
