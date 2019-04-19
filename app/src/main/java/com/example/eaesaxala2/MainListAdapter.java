package com.example.eaesaxala2;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MainListAdapter extends CursorAdapter {

    LayoutInflater mainListLayoutInflater;
    int itemLayout;
    String [] from;
    int [] to;


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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String image = cursor.getString(cursor.getColumnIndex(from[0]));
        ImageView imageView = (ImageView) view.findViewById(to[0]);
        Bitmap bitmap = BitmapFactory.decodeFile(image);
        imageView.setImageBitmap(bitmap);

        String text1 = cursor.getString(cursor.getColumnIndex(from[1]));
        TextView textView = (TextView)view.findViewById(to[1]);
        textView.setText(text1);

        String text2 = cursor.getString(cursor.getColumnIndex(from[2]));
        TextView textView2 = (TextView)view.findViewById(to[2]);
        textView2.setText(text2);

        int rate = cursor.getInt(cursor.getColumnIndex(from[3]));
        RatingBar rating = (RatingBar) view.findViewById(to[3]);
        if(!(rate==0)){
            rating.setNumStars(rate);
            Drawable stars = rating.getProgressDrawable();
            stars.setTint( Color.parseColor("#ADCD58"));
        }
        //Ratingbar auschalten, so dass sie nur angezeigt wird.
        rating.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

       rating.setFocusable(false);
    }
}
