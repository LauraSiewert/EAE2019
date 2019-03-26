package com.example.eaesaxala2;


import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MainListAdapter extends CursorAdapter implements View.OnClickListener {

    LayoutInflater mainListLayoutInflater;
    int itemLayout;
    String [] from;
    int [] to;
    ImageButton imageView2;
    int image2;


    public MainListAdapter (Context ctx, int itemLayout, Cursor c, String [] from, int [] to, int flags){
        super(ctx, c, flags);
        mainListLayoutInflater = LayoutInflater.from(ctx);
        this.itemLayout = itemLayout;
        this.from = from;
        this.to = to;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mainListLayoutInflater.inflate(itemLayout, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int image = cursor.getInt(cursor.getColumnIndex(from[0]));
        ImageView imageView = (ImageView) view.findViewById(to[0]);
        imageView.setImageResource(image);

        String text1 = cursor.getString(cursor.getColumnIndex(from[1]));
        TextView textView = (TextView)view.findViewById(to[1]);
        textView.setText(text1);

        String text2 = cursor.getString(cursor.getColumnIndex(from[2]));
        TextView textView2 = (TextView)view.findViewById(to[2]);
        textView2.setText(text2);

        int rate = cursor.getInt(cursor.getColumnIndex(from[3]));
        RatingBar rating = (RatingBar) view.findViewById(to[3]);
        rating.setNumStars(rate);

        image2 = cursor.getInt(cursor.getColumnIndex(from[4]));
        imageView2 = (ImageButton) view.findViewById(to[4]);
        imageView2.setOnClickListener(this);
        if (image2 == 0){
            imageView2.setBackgroundResource(R.drawable.like);
        }
        if(image2 == 1){
            imageView2.setBackgroundResource(R.drawable.liked);
        }

    }

    @Override
    public void onClick(View v) {
        if (v==imageView2){
            if(image2==0){
                image2=1;
                imageView2.setBackgroundResource(R.drawable.liked);
            }
            else if(image2==1){
                image2=0;
                imageView2.setBackgroundResource(R.drawable.like);
            }
        }
    }
}
