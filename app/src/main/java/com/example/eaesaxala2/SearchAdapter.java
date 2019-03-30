package com.example.eaesaxala2;

import android.content.Context;
import android.database.Cursor;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.eaesaxala2.ItemObject;
import com.example.eaesaxala2.R;

import java.util.List;

public class SearchAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<ItemObject> listItemStorage;
    LayoutInflater mainListLayoutInflater;
    int itemLayout;
    String [] from;
    int [] to;

    public SearchAdapter(Context context, List<ItemObject> customizedListView) {
        layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listItemStorage = customizedListView;
    }

    @Override
    public int getCount() { return listItemStorage.size(); }

    @Override
    public Object getItem(int position) { return listItemStorage.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;
        if(convertView == null){
            listViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.list_item, parent, false);
            listViewHolder.dictionaryWord = (TextView)convertView.findViewById(R.id.REZEPT_NAME_SEARCH);
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }
        listViewHolder.dictionaryWord.setText(listItemStorage.get(position).getWord());

        RatingBar rating = (RatingBar)convertView.findViewById(R.id.REZEPT_BEWERTUNG_SEARCH);
        int rate = listItemStorage.get(position).getBewertung();
        rating.setNumStars(rate);
        //Ratingbar auschalten, so dass sie nur angezeigt wird.
        rating.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        rating.setFocusable(false);

        ImageView image = (ImageView)convertView.findViewById(R.id.REZEPT_BILD_SEARCH);
        String imageInhalt = listItemStorage.get(position).getBild();
        //image.setImageResource(Integer.parseInt(imageInhalt));

        TextView idR = (TextView)convertView.findViewById(R.id.REZEPT_ID_LIST_SEARCH);
        String idRInhalt = listItemStorage.get(position).getIdR();
        idR.setText(idRInhalt);

        return convertView;

    }

    static class ViewHolder{
        TextView dictionaryWord;
    }

}

