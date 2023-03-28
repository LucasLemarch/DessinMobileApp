package fr.lucas.dessinmobileapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;
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

	// gestion des couleurs
	private final int NB_BTN_COUL = 9;
	private ArrayList<Integer> alCoul;
	private AmbilWarnaDialog colorChooser;
	private Button[] tabBtnCoul;
	private Button btnAjoutCoul;
	private int coulSelect;





	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dessin);

		layoutOutils = findViewById(R.id.choixOutils);
		grilleCoul = findViewById(R.id.grilleCouleurs);
		coulActuel = findViewById(R.id.couleurActuel);

		colorChooser = new AmbilWarnaDialog(this, 0, this);
		alCoul = new ArrayList<Integer>(Arrays.asList(Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE));
		coulSelect = 0;

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

	// liste couleurs
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




	@Override
	public void onClick(View view) {
		if (view == btnAjoutCoul)
			colorChooser.show();

		for (int i = 0 ; i < NB_BTN_COUL ; i++)
			if (view == tabBtnCoul[i] && i < alCoul.size()) {
				coulSelect = i;
				coulActuel.setBackgroundColor(alCoul.get(i));
				Toast.makeText(this, "couleur : " + i, Toast.LENGTH_SHORT).show();
			}
	}
}