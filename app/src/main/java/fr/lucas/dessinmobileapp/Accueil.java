package fr.lucas.dessinmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Accueil extends AppCompatActivity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);
	}

	public void nouveauDessin(View v) {
		Intent intent = new Intent(Accueil.this, Dessin.class);
		Accueil.this.startActivity(intent);
	}
}