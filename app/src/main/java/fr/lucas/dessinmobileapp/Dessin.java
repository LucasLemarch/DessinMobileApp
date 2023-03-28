package fr.lucas.dessinmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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
	// composants xml
	private DrawerLayout layoutOutils;
	private GridLayout grilleCoul;
	private View coulActuel;
	private ImageView imgActuel;
	private ImageButton btnCarre;
	private ImageButton btnCercle;
	private CheckBox cbPlein;

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
		affecterCouleurs();


		btnAjoutCoul = new Button(this);
		btnAjoutCoul.setText("+");
		btnAjoutCoul.setLayoutParams(param);
		btnAjoutCoul.setOnClickListener(this);

		grilleCoul.addView(btnAjoutCoul);


	}


	// ouverture menu
	public void ouvrirOutils(View v) {
		layoutOutils.open();
	}

	public void fermerOutils(View v) {
		layoutOutils.close();
	}

	// couleurs
	private void affecterCouleurs() {
		for (int i = 0 ; i < alCoul.size() ; i++)
			tabBtnCoul[i].setBackgroundTintList(ColorStateList.valueOf(alCoul.get(i)));
	}

	public void onOk(AmbilWarnaDialog dialog, int color) {
		if (alCoul.size() < NB_BTN_COUL) {
			tabBtnCoul[alCoul.size()].setBackgroundTintList(ColorStateList.valueOf(color));
			alCoul.add(color);
		}
		else {
			alCoul.remove(0);
			alCoul.add(color);
			affecterCouleurs();
		}
	}

	public void onCancel(AmbilWarnaDialog dialog) {
		Toast.makeText(this, "Aucune couleur séléctionné", Toast.LENGTH_SHORT).show();
	}

	public void onClick(View view) {
		if (view == btnAjoutCoul)
			colorChooser.show();

		for (int i = 0 ; i < NB_BTN_COUL ; i++)
			if (view == tabBtnCoul[i] && i < alCoul.size()) {
				coulSelect = i;
				coulActuel.setBackgroundColor(alCoul.get(i));
			}
	}

	// outils
	public void selectionnerLigne(View view) {
		imgActuel.setImageResource(R.drawable.ligne);
		outilSelect = Outils.LIGNE;
	}

	public void selectionnerCarre(View view) {
		if (cbPlein.isChecked())
			imgActuel.setImageResource(R.drawable.carre_rempli);
		else
			imgActuel.setImageResource(R.drawable.carre);

		outilSelect = Outils.CARRE;
	}

	public void selectionnerCercle(View view) {
		if (cbPlein.isChecked())
			imgActuel.setImageResource(R.drawable.cercle_rempli);
		else
			imgActuel.setImageResource(R.drawable.cercle);

		outilSelect = Outils.CERCLE;
	}

	public void changementPlein(View view) {
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

	public void retour(View view) {
		//TODO
	}

	public void effacer(View view) {
		//TODO
	}
}