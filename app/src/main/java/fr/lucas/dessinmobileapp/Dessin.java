package fr.lucas.dessinmobileapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

public class Dessin extends AppCompatActivity {

	DrawerLayout layoutOutils;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dessin);

		layoutOutils = findViewById(R.id.choixOutils);


	}


	public void ouvrirOutils(View v) {
		layoutOutils.open();
	}

	public void fermerOutils(View v) {
		layoutOutils.close();
	}
}