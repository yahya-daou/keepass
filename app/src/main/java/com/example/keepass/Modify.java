package com.example.keepass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Modify extends AppCompatActivity {
    TextView text;

    FirebaseUser user;
    FirebaseAuth auth;
    TextInputEditText email;
    TextInputEditText url_web;
    FirebaseFirestore db;
    TextInputEditText password;
    TextInputEditText utilisateur;
    Button btn_creer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        Bundle extras = getIntent().getExtras();
        String nomUtilisateurCopie;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        user = auth.getCurrentUser();
        password = findViewById(R.id.password);
        utilisateur = findViewById(R.id.nom_user);
        email = findViewById(R.id.email);
        url_web = findViewById(R.id.site);

        btn_creer = findViewById(R.id.btn_creer);

        if (extras != null) {
            nomUtilisateurCopie = extras.getString("nomUtilisateur", "");
        } else {
            nomUtilisateurCopie = "hello";
        }

        utilisateur.setText(nomUtilisateurCopie);
        Query query = db.collection("les_pass")
                .whereEqualTo("curent_user", user.getEmail())
                .whereEqualTo("nom-utilisateur", nomUtilisateurCopie);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Assurez-vous que la liste de documents n'est pas vide
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                    // Obtenez les données du document
                    String mdp = documentSnapshot.getString("mot_de_passe");
                    String URL = documentSnapshot.getString("url");
                    password.setText(mdp);

                    // ...

                    // Faites ce que vous voulez avec les valeurs des champs
                    // Par exemple, mettez à jour les EditText
                    email.setText(user.getEmail());

                    url_web.setText(URL);

                    // ...
                } else {
                    // Aucun document correspondant aux critères
                }
            } else {
                // Échec de la récupération des documents
                // Gérez les erreurs ici
            }
        });


        btn_creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Vérifiez si des documents correspondent à la requête
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Obtenez la référence du premier document correspondant
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);



                            String documentId = documentSnapshot.getId();

                            // ...



                            // Obtenez les nouvelles valeurs des champs
                            String pass = password.getText().toString();
                            String utuli =  utilisateur.getText().toString();
                            String mail = email.getText().toString();
                            String URL =  url_web.getText().toString();
                            // ...

                            // Obtenez la référence du document que vous souhaitez mettre à jour
                            DocumentReference documentRef = db.collection("les_pass").document(documentId);

                            // Mise à jour du document
                            documentRef.update("mot_de_passe", pass, "nom-utilisateur", utuli,"url", URL)
                                    .addOnSuccessListener(aVoid -> {
                                        // La mise à jour a réussi
                                    })
                                    .addOnFailureListener(e -> {
                                        // La mise à jour a échoué
                                    });
                        } else {
                            // Aucun document correspondant aux critères
                        }
                    } else {
                        // Échec de la récupération des documents
                        // Gérez les erreurs ici
                    }
                });
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }
}
