package fr.lucas.dessinmobileapp.formes;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Forme implements Cloneable {
    protected float xDeb;
    protected float yDeb;
    protected Paint style;

    public Forme(float xDeb, float yDeb, int couleur) {
        this.xDeb = xDeb;
        this.yDeb = yDeb;

        this.style = new Paint();
        this.style.setColor(couleur);
        this.style.setStrokeWidth(5); // taille par defaut
    }

    public abstract void dessiner(Canvas canvas);
    public abstract String toString();

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
