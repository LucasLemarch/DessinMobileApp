package fr.lucas.dessinmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;

public class Dessin extends AppCompatActivity implements OnClickListener, OnAmbilWarnaListener
{
	// CONSTANTES POUR LE BUNDLE
	// id de l'outil actuel
	public static final String BUNDLE_OUTIL = "bundleOutil";
	// id de la couleur actuel
	public static final String BUNDLE_COULEUR = "bundleCouleur";
	// boolean en rapport avec les formes pleines
	public static final String BUNDLE_EST_PLEIN = "bundleEstPlein";
	// liste des couleurs
	public static final String BUNDLE_LST_COULEURS = "bundleLstCouleurs";
	// dessin en cours
	public static final String BUNDLE_DESSIN_ACTUEL = "bundleDessinActuel";
	// dessin de fin à enregistrer
	public static final String BUNDLE_EXTRA_DESSIN_ACTUEL = "bundleExtraDessinActuel";

	// COMPOSANTS RECUPERE DU XML
	// layout avec le choix des outils et des couleurs
	private DrawerLayout layoutOutils;
	// grille des couleurs
	private GridLayout grilleCoul;
	// affichage de la couleur actuelle
	private View coulActuel;
	// affichage de l'outil actuel
	private ImageView imgActuel;
	// bouton de l'outil carré
	private ImageButton btnCarre;
	// bouton de l'outil cercle
	private ImageButton btnCercle;
	// case à cocher pour le remplissage des formes
	private CheckBox cbPlein;
	// view avec les dessins actuels
	private ZoneDessin viewDessin;

	// VARIABLES POUR LA GESTION DES COULEURS
	// nombre de couleurs enregistré maximum
	private final int NB_BTN_COUL = 9;
	// liste des couleurs enregistrée
	private ArrayList<Integer> alCoul;
	// popup de choix de couleur
	private AmbilWarnaDialog colorChooser;
	// liste des boutons de choix de couleur
	private Button[] tabBtnCoul;
	// bouton pour ajouter une couleur aux couleurs enregistrées
	private Button btnAjoutCoul;
	// indice de la couleur actuelle
	private int coulSelect;

	// indice de l'outil de actuel
	private int outilSelect;


	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dessin);

		// Récupération des composants XML
		layoutOutils = findViewById(R.id.choixOutils);
		grilleCoul = findViewById(R.id.grilleCouleurs);
		coulActuel = findViewById(R.id.couleurActuel);
		imgActuel = findViewById(R.id.imageActuel);
		btnCarre = findViewById(R.id.btnCarre);
		btnCercle = findViewById(R.id.btnCercle);
		cbPlein = findViewById(R.id.cbPlein);
		viewDessin = findViewById(R.id.viewDessin);

		// Récupératon et chargement du dessin envoyé par l'Accueil
		String dessinACharger = this.getIntent().getStringExtra(Dessin.BUNDLE_EXTRA_DESSIN_ACTUEL);

		if (!dessinACharger.equals("")) { //si dessin recup => chargment + suppression des données
			this.getIntent().putExtra(Dessin.BUNDLE_EXTRA_DESSIN_ACTUEL, "");
			viewDessin.chargerDessinActuel(dessinACharger);
		}

		// Assimilation des valeurs par défault
		alCoul = new ArrayList<Integer>(Arrays.asList(Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE));
		coulSelect = 0;
		outilSelect = Outils.LIGNE;

		// Création des composants
		colorChooser = new AmbilWarnaDialog(this, 0, this);

		tabBtnCoul = new Button[NB_BTN_COUL];
		LayoutParams param = new LayoutParams(150, 150);

		for (int i = 0 ; i < NB_BTN_COUL ; i++) {
			tabBtnCoul[i] = new Button(this);
			tabBtnCoul[i].setLayoutParams(param);
			tabBtnCoul[i].setOnClickListener(this);

			grilleCoul.addView(tabBtnCoul[i]);
		}

		btnAjoutCoul = new Button(this);
		btnAjoutCoul.setText("+");
		btnAjoutCoul.setLayoutParams(param);
		btnAjoutCoul.setOnClickListener(this);

		grilleCoul.addView(btnAjoutCoul);

		majIHM();
	}

	public void onSaveInstanceState(Bundle b) {
		b.putInt(Dessin.BUNDLE_OUTIL, outilSelect);
		b.putInt(Dessin.BUNDLE_COULEUR, coulSelect);
		b.putBoolean(Dessin.BUNDLE_EST_PLEIN, cbPlein.isChecked());
		b.putIntegerArrayList(Dessin.BUNDLE_LST_COULEURS, alCoul);
		b.putString(Dessin.BUNDLE_DESSIN_ACTUEL, viewDessin.sauvegarderDessinActuel());

		super.onSaveInstanceState(b);
	}

	public void onRestoreInstanceState(Bundle b) {
		super.onRestoreInstanceState(b);

		outilSelect = b.getInt(Dessin.BUNDLE_OUTIL);
		coulSelect = b.getInt(Dessin.BUNDLE_COULEUR);
		cbPlein.setChecked(b.getBoolean(Dessin.BUNDLE_EST_PLEIN));
		alCoul = b.getIntegerArrayList(Dessin.BUNDLE_LST_COULEURS);
		viewDessin.chargerDessinActuel(b.getString(Dessin.BUNDLE_DESSIN_ACTUEL));
		majIHM();
	}

	// Méthode appelé quand la touche retour est enfoncé
	public void onBackPressed() {
		if (layoutOutils.isOpen())
			// Si la touche est enfoncé pendant que layout des outils est ouvert, on le ferme
			layoutOutils.close();
		else
			// Sinon on affiche un popup pour être sur qu'il veut quitter
			Dessin.this.quitterActivite(null);
	}

	// Getters
	public int getOutilSelect() { return outilSelect; }
	public int getCoul() { return alCoul.get(coulSelect); }
	public boolean getEstPlein() { return cbPlein.isChecked(); }

	// Mise à jour du layout à outils
	private void majIHM() {
		// maj palette couleurs
		for (int i = 0 ; i < alCoul.size() ; i++)
			tabBtnCoul[i].setBackgroundTintList(ColorStateList.valueOf(alCoul.get(i)));

		// maj couleur selectionné
		coulActuel.setBackgroundColor(alCoul.get(coulSelect));

		// maj outils
		if (cbPlein.isChecked()) {
			btnCarre.setImageResource(R.drawable.carre_rempli);
			btnCercle.setImageResource(R.drawable.cercle_rempli);

			if (outilSelect == Outils.CARRE) imgActuel.setImageResource(R.drawable.carre_rempli);
			if (outilSelect == Outils.CERCLE) imgActuel.setImageResource(R.drawable.cercle_rempli);
		}
		else {
			btnCarre.setImageResource(R.drawable.carre);
			btnCercle.setImageResource(R.drawable.cercle);

			if (outilSelect == Outils.CARRE) imgActuel.setImageResource(R.drawable.carre);
			if (outilSelect == Outils.CERCLE) imgActuel.setImageResource(R.drawable.cercle);
		}
	}

	// GESTION DU MENU
	// Méthode appelé par le bouton "Choisir un outil" qui ouvre le menu
	public void ouvrirOutils(View v) {
		layoutOutils.open();
	}

	// Méthode appelé par le bouton "Fermer" qui fermer le menu
	public void fermerOutils(View v) {
		layoutOutils.close();
	}

	// GESTION DES COULEURS
	// Méthode appelé par la popup d'ajout de couleur si une couleur à été choisi
	public void onOk(AmbilWarnaDialog dialog, int color) {
		if (alCoul.size() < NB_BTN_COUL) {
			tabBtnCoul[alCoul.size()].setBackgroundTintList(ColorStateList.valueOf(color));
			alCoul.add(color);
		}
		else {
			alCoul.remove(0);
			alCoul.add(color);
		}

		coulSelect = alCoul.indexOf(color);
		majIHM();
	}

	// Méthode appelé par la popup d'ajout de couleur si aucune couleur à été choisi
	public void onCancel(AmbilWarnaDialog dialog) {
		Toast.makeText(this, "Aucune couleur séléctionné", Toast.LENGTH_SHORT).show();
	}

	// Méthode appelé lorsqu'un bouton de la grille des couleurs à été actionné
	public void onClick(View view) {
		// bouton ajouter couleur
		if (view == btnAjoutCoul)
			colorChooser.show();

		// boutons selection de couleur
		for (int i = 0 ; i < NB_BTN_COUL ; i++)
			if (view == tabBtnCoul[i] && i < alCoul.size())
				coulSelect = i;

		majIHM();
	}

	// GESTION DES OUTILS
	// Méthode appelé par le bouton Ligne
	public void selectionnerLigne(View view) {
		imgActuel.setImageResource(R.drawable.ligne);
		outilSelect = Outils.LIGNE;
	}

	// Méthode appelé par le bouton Carré
	public void selectionnerCarre(View view) {
		outilSelect = Outils.CARRE;
		majIHM();
	}

	// Méthode appelé par le bouton Cercle
	public void selectionnerCercle(View view) {
		outilSelect = Outils.CERCLE;
		majIHM();
	}

	// Méthode appelé par la case à cocher pour le remplissage des formes
	public void changementPlein(View view) {
		majIHM();
	}

	// Méthode appelé par le bouton Retour
	public void retour(View view) {
		if (!viewDessin.retour())
			Toast.makeText(this, "Votre dessin est déjà vide", Toast.LENGTH_SHORT).show();
	}

	// Méthode appelé par le bouton Effacer
	public void effacer(View view) {
		// Création d'un popup
		Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Etes-vous sûr de vouloir tout effacer ?");
		builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				viewDessin.effacer();
			}
		});
		builder.setNegativeButton("Non", null);

		// Afficher la popup
		builder.show();
	}

	// Méthode appelé par le bouton "Quitter"
	public void quitterActivite(View view) {
		// Affichage d'un popup pour être sur que l'utilisateur veut quitter
		new AlertDialog.Builder(this)
				.setMessage("Voulez-vous vraiment quitter l'application ?")
				.setCancelable(false)
				.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Si oui, on termine l'activitée actuelle en envoyant le dessin actuel en résultat
						Intent intent = new Intent();
						intent.putExtra(Dessin.BUNDLE_EXTRA_DESSIN_ACTUEL, viewDessin.sauvegarderDessinActuel());
						Dessin.this.setResult(RESULT_OK, intent);
						Dessin.this.finish();
					}
				})
				.setNegativeButton("Non", null)
				.show();
	}
}