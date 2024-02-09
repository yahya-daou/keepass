package com.example.keepass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

public class Creer_password extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth auth;
    TextInputEditText email;
    TextInputEditText url_web;
    FirebaseFirestore db;
    TextInputEditText password;
    TextInputEditText utilisateur;
    TextInputEditText nom_app;
    Button btn_creer;
    ImageView img;
    SecretKey loadedKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_password);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        user = auth.getCurrentUser();
        password = findViewById(R.id.password);
        utilisateur = findViewById(R.id.nom_user);
        nom_app = findViewById(R.id.appli);
        email = findViewById(R.id.email);
        url_web = findViewById(R.id.site);
        url_web.setText("https://");
        btn_creer = findViewById(R.id.btn_creer);
        img = findViewById(R.id.logo_img);
        loadedKey = CryptoUtils.loadKey(Creer_password.this, user.getEmail());
        if(loadedKey == null) {
            loadedKey = CryptoUtils.generateKey();
            CryptoUtils.saveKey(Creer_password.this, user.getEmail(), loadedKey);
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        email.setText(user.getEmail());
        email.setKeyListener(null);

        btn_creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = utilisateur.getText().toString();
                String pass = password.getText().toString();
                String site = nom_app.getText().toString();
                String url = url_web.getText().toString();
                String mail = email.getText().toString();

                // Génération de la clé pour cet utilisateur

                String encodedString = null;
                if (loadedKey != null) {
                    // Utilisation de la clé pour crypter le mot de passe
                    byte[] encryptedData = CryptoUtils.encrypt(loadedKey, pass);
                    if (encryptedData != null) {
                        encodedString = Base64.getEncoder().encodeToString(encryptedData);
                    }
                }

                if (pass.length() > 4 && encodedString != null) {
                    Map<String, Object> pass_word = new HashMap<>();
                    pass_word.put("curent_user", mail);
                    pass_word.put("nom-utilisateur", nom);
                    pass_word.put("url", url);
                    pass_word.put("application", site);
                    pass_word.put("mot_de_passe", encodedString);

                    db.collection("les_pass")
                            .add(pass_word)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(Creer_password.this, "Password created successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Creer_password.this, "Failed to create password", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(Creer_password.this, "Password must be longer than 4 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
