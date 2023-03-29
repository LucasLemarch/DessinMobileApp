package fr.lucas.dessinmobileapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Accueil extends AppCompatActivity {

	private static final int ACCUEIL_ACTIVITY_CODE = 32;
	private static final String SHARED_PREF_INFO = "sharedPrefInfo";
	private static final String SHARED_PREF_INFO_DESSIN_ACTUEL = "sharedPrefInfoDessinActuel";
	private String dessinActuel;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);

		dessinActuel = getSharedPreferences(Accueil.SHARED_PREF_INFO, MODE_PRIVATE)
				.getString(Accueil.SHARED_PREF_INFO_DESSIN_ACTUEL, "");

		Toast.makeText(this, dessinActuel, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Toast.makeText(this,"infos recu " + resultCode, Toast.LENGTH_SHORT).show();
		if (Accueil.ACCUEIL_ACTIVITY_CODE == requestCode && RESULT_OK == resultCode) {
			String nouveauDessin = data.getStringExtra(Dessin.BUNDLE_EXTRA_DESSIN_ACTUEL);
			Toast.makeText(this,"nv : " + nouveauDessin, Toast.LENGTH_SHORT).show();

			this.getSharedPreferences(Accueil.SHARED_PREF_INFO, MODE_PRIVATE)
					.edit()
					.putString(Accueil.SHARED_PREF_INFO_DESSIN_ACTUEL, nouveauDessin)
					.apply();

			this.recreate();
		}
	}

	public void nouveauDessin(View v) {
		Intent intent = new Intent(Accueil.this, Dessin.class);
		this.startActivityForResult(intent, Accueil.ACCUEIL_ACTIVITY_CODE);
	}

	public void reprendreDessin(View v) {


	}

	public void quitter(View v) {

	}
}