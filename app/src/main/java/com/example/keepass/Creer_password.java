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

import java.util.HashMap;
import java.util.Map;

public class Creer_password extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth auth;
    TextInputEditText email;
    TextInputEditText url_web;
    FirebaseFirestore db;
    TextInputEditText password;
    TextInputEditText utilisateur;
    Button btn_creer;
    ImageView img;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_password);
        auth=FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        user=auth.getCurrentUser();
        password=findViewById(R.id.password);
        utilisateur=findViewById(R.id.nom_user);
        email=findViewById(R.id.email);
        url_web=findViewById(R.id.site);
        url_web.setText("https://");
        btn_creer=findViewById(R.id.btn_creer);
        img=findViewById(R.id.logo_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();

            }
        });






        email.setText(user.getEmail());
        email.setKeyListener(null);


        btn_creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom=utilisateur.getText().toString();
                String pass=password.getText().toString();
                String url=url_web.getText().toString();
                String mail=email.getText().toString();


                Map<String,Object> pass_word=new HashMap<>();
                pass_word.put("curent_user",mail);
                pass_word.put("nom-utilisateur",nom);
                pass_word.put("url",url);
                pass_word.put("mot_de_passe",pass);
                db.collection("les_pass")
                        .add(pass_word);
                Toast.makeText(Creer_password.this,"password creates succesfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
                finish();
            }
        });




    }
}