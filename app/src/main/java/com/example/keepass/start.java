package com.example.keepass;

import static java.lang.Thread.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Rediriger vers l'activité de connexion (login)
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish(); // Fermer l'écran de lancement pour éviter qu'il n'apparaisse avec le bouton "Retour"
            }
        }, 4000); // Délai en millisecondes (2 secondes dans cet exemple)


    }
}