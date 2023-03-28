package fr.lucas.dessinmobileapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class ZoneDessin extends View implements OnTouchListener, OnClickListener
{
    private Dessin dessin;

    public ZoneDessin(Context context, AttributeSet attrs) {
        super(context, attrs);

        //dessin = (Dessin) context;


        this.setOnTouchListener(this);
        this.setOnClickListener(this);
    }

    public void onDraw(Canvas canvas) {
        canvas.drawCircle(50, 50, 75, new Paint());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        return false;
    }

    @Override
    public void onClick(View view) {

    }
}
