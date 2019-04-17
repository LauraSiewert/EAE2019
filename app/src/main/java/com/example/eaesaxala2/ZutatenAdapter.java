package com.example.eaesaxala2;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ZutatenAdapter extends ArrayAdapter {
    Context context;
    ArrayList <Zutaten> zutaten = new ArrayList<>();

    public ZutatenAdapter (Context context,  ArrayList<Zutaten> list) {
        super(context, 0 , list);
        this.context = context;
        this.zutaten = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.zutaten_list_item,parent,false);

        Zutaten currentZutat = zutaten.get(position);

        TextView textView = (TextView)listItem.findViewById(R.id.ZUTATENAME_LISTE);
        textView.setText(currentZutat.name);

        TextView textView2 = (TextView)listItem.findViewById(R.id.ZUTATENMENGE_LISTE);
        Double menge = currentZutat.getMenge();
        textView2.setText(menge.toString());

        TextView textView3= (TextView)listItem.findViewById(R.id.ZUTATENEINHEIT_LISTE);
        textView3.setText(currentZutat.einheit);

        return listItem;
    }
}
