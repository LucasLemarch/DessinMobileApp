package fr.lucas.dessinmobileapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Accueil extends AppCompatActivity {
	// requestCode pour récupérer le résultat de l'activité Dessin
	private static final int ACCUEIL_ACTIVITY_CODE = 32;

	// Constantes pour stocker les SharedPreferences
	private static final String SHARED_PREF_INFO = "sharedPrefInfo";
	private static final String SHARED_PREF_INFO_DESSIN_ACTUEL = "sharedPrefInfoDessinActuel";

	// Dernier dessin enregistré au format texte
	private String dessinActuel;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);

		// Récupération du dernier dessin enregistré dans les SharedPreferences ou une chaine vide
		dessinActuel = getSharedPreferences(Accueil.SHARED_PREF_INFO, MODE_PRIVATE)
				.getString(Accueil.SHARED_PREF_INFO_DESSIN_ACTUEL, "");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Vérification que l'activité dessin s'est terminé correctement
		if (Accueil.ACCUEIL_ACTIVITY_CODE == requestCode && RESULT_OK == resultCode) {
			// Récupération du dernier dessin
			String nouveauDessin = data.getStringExtra(Dessin.BUNDLE_EXTRA_DESSIN_ACTUEL);

			// Changement du dessin enregistré dans les SharedPreferences
			this.getSharedPreferences(Accueil.SHARED_PREF_INFO, MODE_PRIVATE)
					.edit()
					.putString(Accueil.SHARED_PREF_INFO_DESSIN_ACTUEL, nouveauDessin)
					.apply();

			this.recreate();
		}
	}

	// Méthode appelé par le bouton "Nouveau dessin"
	public void nouveauDessin(View v) {
		Intent intent = new Intent(Accueil.this, Dessin.class);
		intent.putExtra(Dessin.BUNDLE_EXTRA_DESSIN_ACTUEL, "");

		// Démarre l'activité Dessin à partir d'un dessin vide
		this.startActivityForResult(intent, Accueil.ACCUEIL_ACTIVITY_CODE);
	}

	// Méthode appelé par le bouton "Reprendre dessin"
	public void reprendreDessin(View v) {
		Intent intent = new Intent(Accueil.this, Dessin.class);
		intent.putExtra(Dessin.BUNDLE_EXTRA_DESSIN_ACTUEL, dessinActuel);

		// Démarre l'activité Dessin à partir d'un dessin enregistré dans les SharedPreferences
		this.startActivityForResult(intent, Accueil.ACCUEIL_ACTIVITY_CODE);
	}

	// Méthode appelé par le bouton "Quitter"
	public void quitter(View v) {
		finish();
	}
}