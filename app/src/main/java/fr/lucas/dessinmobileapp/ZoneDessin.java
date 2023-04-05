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
    // Activité Dessin pour les getters
    private Dessin dessin;
    // Liste des formes déssinés
    private LinkedList<Forme> llFormes;
    // Forme en train d'être dessiné
    private Forme formeEnCours;
    // Point de départ de la forme en train d'être déssiné
    private Point pointDepart;
    // Valeur x enregistré par le touch listener
    private float x;
    // Valeur y enregistré par le touch listener
    private float y;

    public ZoneDessin(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Récupération de Dessin
        dessin = (Dessin) context;

        // Initialisation de la liste des formes
        llFormes = new LinkedList<Forme>();

        // Initialisation des variables pour la forme en train d'être dessiné
        formeEnCours = null;
        pointDepart = null;

        // Ajout des listeners
        this.setOnTouchListener(this);
        this.setOnClickListener(this);
    }

    // Transforme la liste des formes en String
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

    // Transforme un String donné en liste des formes
    public void chargerDessinActuel(String dessinActuel) {
        String[] tabDessinActuel = dessinActuel.split("\n");
        for (String ligne : tabDessinActuel) {
            String[] tabS = ligne.split(";");

            if (tabS.length > 1)
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

    // Permet de supprimer la dernière forme dessiné
    public boolean retour() {
        if (llFormes.isEmpty())
            return false;

        llFormes.removeLast();
        this.invalidate();
        return true;
    }

    // Permet d'effacer toutes les formes dessiné
    public void effacer() {
        llFormes.clear();
        this.invalidate();
    }

    public void onDraw(Canvas canvas) {
        // dessin des formes enregistrer
        Forme forme;
        Iterator<Forme> itFormes = llFormes.iterator();
        int i = 0;
        while(itFormes.hasNext()) {
            forme = itFormes.next();
            forme.dessiner(canvas);
            i++;
        }
        System.out.println("NB FORMES : " + i);

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
        if (formeEnCours != null) {
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
}
