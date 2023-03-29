package fr.lucas.dessinmobileapp.formes;

import android.graphics.Canvas;

import fr.lucas.dessinmobileapp.Outils;

public class FormeLigne extends Forme implements Cloneable {
    private float xFin;
    private float yFin;

    public FormeLigne(float xDeb, float yDeb, float xFin, float yFin, int couleur) {
        super(xDeb, yDeb, couleur);

        this.xFin = xFin;
        this.yFin = yFin;
    }

    public FormeLigne(String[] tabS) {
        this(Float.parseFloat(tabS[1]), Float.parseFloat(tabS[2]), Float.parseFloat(tabS[3]),
                Float.parseFloat(tabS[4]), Integer.parseInt(tabS[5]));
    }

    @Override
    public void dessiner(Canvas canvas) {
        canvas.drawLine(xDeb, yDeb, xFin, yFin, style);
    }

    @Override
    public String toString() {
        String sRet = "";

        sRet += Outils.LIGNE        + ";" +
                xDeb                + ";" +
                yDeb                + ";" +
                xFin                + ";" +
                yFin                + ";" +
                style.getColor()    ;

        return sRet;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
