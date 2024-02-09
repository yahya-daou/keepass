package com.example.keepass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Base64;

import javax.crypto.SecretKey;

public class Modify extends AppCompatActivity {
    TextView text;

    FirebaseUser user;
    FirebaseAuth auth;
    ImageView img;
    TextInputEditText email;
    TextInputEditText url_web;
    FirebaseFirestore db;
    TextInputEditText password;
    TextInputEditText appli;
    TextInputEditText utilisateur;
    Button btn_creer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        Bundle extras = getIntent().getExtras();
        String application;
        img=findViewById(R.id.logo_img);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        user = auth.getCurrentUser();
        password = findViewById(R.id.password);
        utilisateur = findViewById(R.id.nom_user);
        email = findViewById(R.id.email);
        url_web = findViewById(R.id.site);
        appli = findViewById(R.id.application);

        btn_creer = findViewById(R.id.btn_creer);
        SecretKey loadedKey = CryptoUtils.loadKey(Modify.this, user.getEmail());

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });




        if (extras != null) {
            application = extras.getString("application", "");
        } else {
            application = "hello";
        }

        utilisateur.setText(application);
        Query query = db.collection("les_pass")
                .whereEqualTo("curent_user", user.getEmail())
                .whereEqualTo("application", application);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {

                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);


                    String mdp = documentSnapshot.getString("mot_de_passe");
                    String nomutilisateur = documentSnapshot.getString("nom-utilisateur");
                    String URL = documentSnapshot.getString("url");

                    String pass2 = "";

                    if (loadedKey != null && mdp!= null) {


                        String decryptedData = CryptoUtils.decrypt(loadedKey, mdp);
                        pass2 = decryptedData;
                    } else {
                        System.out.println("hello");

                    }




                    password.setText( pass2);
                    utilisateur.setText(nomutilisateur);
                    appli.setText(application);


                    email.setText(user.getEmail());

                    url_web.setText(URL);

                } else {

                }
            } else {

            }
        });


        btn_creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {

                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);



                            String documentId = documentSnapshot.getId();


                            String pass = password.getText().toString();
                            String utuli =  utilisateur.getText().toString();

                            String URL =  url_web.getText().toString();
                            String apl =  appli.getText().toString();

                            String encodedString = null;
                            if (loadedKey != null) {

                                byte[] encryptedData = CryptoUtils.encrypt(loadedKey, pass);
                                if (encryptedData != null) {
                                    encodedString = Base64.getEncoder().encodeToString(encryptedData);
                                }
                            }





                            DocumentReference documentRef = db.collection("les_pass").document(documentId);


                            documentRef.update("mot_de_passe", encodedString, "nom-utilisateur", utuli,"url", URL,"application",apl)
                                    .addOnSuccessListener(aVoid -> {

                                    })
                                    .addOnFailureListener(e -> {

                                    });
                        } else {

                        }
                    } else {

                    }
                });
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }
}
