package fr.lucas.dessinmobileapp.formes;

import android.graphics.Canvas;
import android.graphics.Paint.Style;

import fr.lucas.dessinmobileapp.Outils;

public class FormeCarre extends Forme implements Cloneable {
    private float xFin;
    private float yFin;
    private boolean estPlein;

    public FormeCarre(float xDeb, float yDeb, float xFin, float yFin, int couleur, boolean estPlein) {
        super(xDeb, yDeb, couleur);

        this.xFin = xFin;
        this.yFin = yFin;
        this.estPlein = estPlein;

        if (estPlein)
            this.style.setStyle(Style.FILL);
        else
            this.style.setStyle(Style.STROKE);
    }

    public FormeCarre(String[] tabS) {
        this(Float.parseFloat(tabS[1]), Float.parseFloat(tabS[2]), Float.parseFloat(tabS[3]),
                Float.parseFloat(tabS[4]), Integer.parseInt(tabS[5]), Boolean.parseBoolean(tabS[6]));
    }

    @Override
    public void dessiner(Canvas canvas) {
        canvas.drawRect(xDeb, yDeb, xFin, yFin, style);
    }

    @Override
    public String toString() {
        String sRet = "";

        sRet += Outils.CARRE        + ";" +
                xDeb                + ";" +
                yDeb                + ";" +
                xFin                + ";" +
                yFin                + ";" +
                style.getColor()    + ";" +
                estPlein            ;

        return sRet;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
