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
	// constantes
	public static final String BUNDLE_OUTIL = "bundleOutil";
	public static final String BUNDLE_COULEUR = "bundleCouleur";
	public static final String BUNDLE_EST_PLEIN = "bundleEstPlein";
	public static final String BUNDLE_LST_COULEURS = "bundleLstCouleurs";
	public static final String BUNDLE_DESSIN_ACTUEL = "bundleDessinActuel";
	public static final String BUNDLE_EXTRA_DESSIN_ACTUEL = "bundleExtraDessinActuel";

	// composants xml
	private DrawerLayout layoutOutils;
	private GridLayout grilleCoul;
	private View coulActuel;
	private ImageView imgActuel;
	private ImageButton btnCarre;
	private ImageButton btnCercle;
	private CheckBox cbPlein;
	private ZoneDessin viewDessin;

	// gestion des couleurs
	private final int NB_BTN_COUL = 9;
	private ArrayList<Integer> alCoul;
	private AmbilWarnaDialog colorChooser;
	private Button[] tabBtnCoul;
	private Button btnAjoutCoul;
	private int coulSelect;

	// gestion outils
	private int outilSelect;


	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dessin);

		layoutOutils = findViewById(R.id.choixOutils);
		grilleCoul = findViewById(R.id.grilleCouleurs);
		coulActuel = findViewById(R.id.couleurActuel);
		imgActuel = findViewById(R.id.imageActuel);
		btnCarre = findViewById(R.id.btnCarre);
		btnCercle = findViewById(R.id.btnCercle);
		cbPlein = findViewById(R.id.cbPlein);
		viewDessin = findViewById(R.id.viewDessin);

		colorChooser = new AmbilWarnaDialog(this, 0, this);
		alCoul = new ArrayList<Integer>(Arrays.asList(Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE));
		coulSelect = 0;
		outilSelect = Outils.LIGNE;


		// Création des composants
		LayoutParams param = new LayoutParams(150, 150);

		tabBtnCoul = new Button[NB_BTN_COUL];
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

	public void onDestroy() {
		super.onDestroy();

		Intent intent = new Intent();
		intent.putExtra(Dessin.BUNDLE_EXTRA_DESSIN_ACTUEL, viewDessin.sauvegarderDessinActuel());
		setResult(RESULT_OK, intent);
		finish();
	}

	public int getOutilSelect() { return outilSelect; }
	public int getCoul() { return alCoul.get(coulSelect); }
	public boolean getEstPlein() { return cbPlein.isChecked(); }

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

	// ouverture menu
	public void ouvrirOutils(View v) {
		layoutOutils.open();
	}

	public void fermerOutils(View v) {
		layoutOutils.close();
	}

	// couleurs
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

	public void onCancel(AmbilWarnaDialog dialog) {
		Toast.makeText(this, "Aucune couleur séléctionné", Toast.LENGTH_SHORT).show();
	}

	public void onClick(View view) {
		if (view == btnAjoutCoul)
			colorChooser.show();

		for (int i = 0 ; i < NB_BTN_COUL ; i++)
			if (view == tabBtnCoul[i] && i < alCoul.size())
				coulSelect = i;

		majIHM();
	}

	// outils
	public void selectionnerLigne(View view) {
		imgActuel.setImageResource(R.drawable.ligne);
		outilSelect = Outils.LIGNE;
	}

	public void selectionnerCarre(View view) {
		outilSelect = Outils.CARRE;
		majIHM();
	}

	public void selectionnerCercle(View view) {
		outilSelect = Outils.CERCLE;
		majIHM();
	}

	public void changementPlein(View view) {
		majIHM();
	}

	public void retour(View view) {
		if (!viewDessin.retour())
			Toast.makeText(this, "Votre dessin est déjà vide", Toast.LENGTH_SHORT).show();
	}

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
}