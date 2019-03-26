package com.example.eaesaxala2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailRezept extends AppCompatActivity {
    DatenBankManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rezept);

        //Navigationbar ändern
        setTitle("Rezept Details");

        TextView rezeptId = findViewById(R.id.REZEPT_ID);
        Intent i = getIntent();
        String id =  i.getStringExtra("id");
        Log.d("SL", "das ist die ID:"+id);
        rezeptId.setText(id);

        if (id != null){
            TextView name = findViewById(R.id.NAMEDETAIL);
            /*Rezept rezept =  db.selectRezept("1");
            name.setText("Hallo");*/
        }


        /*ImageView foto = findViewById(R.id.FOTODETAIl);
        foto.setImageResource(rezept.getFoto());*/
    }
}
