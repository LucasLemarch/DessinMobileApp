package fr.lucas.dessinmobileapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import fr.lucas.dessinmobileapp.formes.Forme;
import fr.lucas.dessinmobileapp.formes.FormeCarre;
import fr.lucas.dessinmobileapp.formes.FormeCercle;
import fr.lucas.dessinmobileapp.formes.FormeLigne;

public class ZoneDessin extends View implements OnTouchListener, OnClickListener
{
    private Dessin dessin;
    private LinkedList<Forme> llFormes;
    private Forme formeEnCours;
    private Point pointDepart;
    private float x;
    private float y;

    public ZoneDessin(Context context, AttributeSet attrs) {
        super(context, attrs);

        dessin = (Dessin) context;
        llFormes = new LinkedList<Forme>();
        formeEnCours = null;
        pointDepart = null;

        /*llFormes.add(new FormeLigne(50, 50, 300, 300, Color.BLUE));
        llFormes.add(new FormeCarre(50, 250, 300, 300, Color.BLUE, true));
        llFormes.add(new FormeCarre(550, 700, 300, 500, Color.RED, false));
        llFormes.add(new FormeCercle(800, 800, 50, Color.GREEN, false));
        llFormes.add(new FormeCercle(500, 500, 25, Color.YELLOW, true));*/

        this.setOnTouchListener(this);
        this.setOnClickListener(this);
    }

    public String sauvegarderDessinActuel() {
        Forme forme;
        String dessinActuel = "";
        Iterator<Forme> itFormes = llFormes.iterator();

        while(itFormes.hasNext()) {
            forme = itFormes.next();
            dessinActuel += forme.toString() + "\n";
        }

        return dessinActuel;
    }

    public void chargerDessinActuel(String dessinActuel) {
        String[] tabDessinActuel = dessinActuel.split("\n");
        for (String ligne : tabDessinActuel) {
            String[] tabS = ligne.split(";");

            switch (Integer.parseInt(tabS[0])) {
                case Outils.LIGNE:
                    llFormes.add(new FormeLigne(tabS));
                    break;
                case Outils.CARRE:
                    llFormes.add(new FormeCarre(tabS));
                    break;
                case Outils.CERCLE:
                    llFormes.add(new FormeCercle(tabS));
                    break;
            }
        }
    }

    public boolean retour() {
        if (llFormes.isEmpty())
            return false;

        llFormes.removeLast();
        this.invalidate();
        return true;
    }

    public void effacer() {
        llFormes.clear();
        this.invalidate();
    }

    public void onDraw(Canvas canvas) {
        // dessin des formes enregistrer
        Forme forme;
        Iterator<Forme> itFormes = llFormes.iterator();
        while(itFormes.hasNext()) {
            forme = itFormes.next();
            forme.dessiner(canvas);
        }

        // dessin de la forme en cours de création si elle existe
        if (formeEnCours != null)
            formeEnCours.dessiner(canvas);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        x = motionEvent.getX();
        y = motionEvent.getY();

        // premier touch
        if (pointDepart == null)
            pointDepart = new Point((int) x, (int) y);

        // dessin de la forme en cours
        switch (dessin.getOutilSelect()) {
            case Outils.LIGNE :
                formeEnCours = new FormeLigne(
                        pointDepart.x, pointDepart.y, x, y, dessin.getCoul());
                break;
            case Outils.CARRE :
                formeEnCours = new FormeCarre(
                        pointDepart.x, pointDepart.y, x, y, dessin.getCoul(), dessin.getEstPlein());
                break;
            case Outils.CERCLE :
                float rayon = (float) Math.sqrt(
                        Math.pow(x - pointDepart.x, 2) + Math.pow(y - pointDepart.y, 2));

                formeEnCours = new FormeCercle(
                        pointDepart.x, pointDepart.y, rayon, dessin.getCoul(), dessin.getEstPlein());
                break;
        }

        this.invalidate();
        return false;
    }

    @Override
    public void onClick(View view) {
        // on place la forme dessiné dans la liste de forme
        try {
            llFormes.add((Forme) formeEnCours.clone());
        } catch (CloneNotSupportedException e) {
            System.out.println("Erreur lors du clonage");
        }

        formeEnCours = null;
        pointDepart = null;
        this.invalidate();
    }
}
