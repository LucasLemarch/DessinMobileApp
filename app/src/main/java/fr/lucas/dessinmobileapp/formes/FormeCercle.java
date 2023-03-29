package fr.lucas.dessinmobileapp.formes;

import android.graphics.Canvas;
import android.graphics.Paint;

import fr.lucas.dessinmobileapp.Outils;

public class FormeCercle extends Forme implements Cloneable {
    private float rayon;
    private boolean estPlein;

    public FormeCercle(float xDeb, float yDeb, float rayon, int couleur, boolean estPlein) {
        super(xDeb, yDeb, couleur);

        this.rayon = rayon;
        this.estPlein = estPlein;

        if (estPlein)
            this.style.setStyle(Paint.Style.FILL);
        else
            this.style.setStyle(Paint.Style.STROKE);
    }

    public FormeCercle(String[] tabS) {
        this(Float.parseFloat(tabS[1]), Float.parseFloat(tabS[2]), Float.parseFloat(tabS[3]),
                Integer.parseInt(tabS[4]), Boolean.parseBoolean(tabS[5]));
    }

    @Override
    public void dessiner(Canvas canvas) {
        canvas.drawCircle(xDeb, yDeb, rayon, style);
    }

    @Override
    public String toString() {
        String sRet = "";

        sRet += Outils.CERCLE       + ";" +
                xDeb                + ";" +
                yDeb                + ";" +
                rayon               + ";" +
                style.getColor()    + ";" +
                estPlein            ;

        return sRet;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
