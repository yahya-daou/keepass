package com.example.keepass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView text;
    Button logoutbtn;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ListView listView ;
    FirebaseUser user;
    Button cre_button;
    private MainActivity activite;
    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text=findViewById(R.id.textotedit);
        listView = findViewById(R.id.listView);
        logoutbtn=findViewById(R.id.logoutbtn);
        cre_button=findViewById(R.id.creer_password);
        db = FirebaseFirestore.getInstance();

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if(user==null){
            Intent intent=new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else{


        }


        cre_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Creer_password.class);
                startActivity(intent);
                finish();

            }
        });


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();

            }
        });





        // ...

        List<String> maListe = new ArrayList<>();

        db.collection("les_pass")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Vous pouvez vérifier l'existence du champ avant de le récupérer
                        if (document.contains("nom-utilisateur")) {
                            String mail = document.getString("curent_user").toString();
                            String nomUtilisateur;
                            if(mail != null && mail.equals(user.getEmail())){
                                nomUtilisateur = document.getString("nom-utilisateur");


                                maListe.add(nomUtilisateur);
                            }


                            // Assurez-vous que "nomUtilisateur" n'est pas nul avant de l'ajouter à la liste

                        }
                    }

                    // Maintenant, "maListe" contient tous les noms d'utilisateurs
                    // Vous pouvez faire ce que vous voulez avec cette liste ici

                    // Créez un adaptateur ArrayAdapter
                    MyCustomAdapter adapter = new MyCustomAdapter(MainActivity.this, maListe);




// Appliquez l'adaptateur à votre ListView
                    listView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    // Échec de la récupération des documents
                    // Gérez les erreurs ici, par exemple, affichez un message d'erreur
                });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupération de l'élément sélectionné
                String selectedItem = (String) parent.getItemAtPosition(position);



                db.collection("les_pass")
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {

                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                // Vous pouvez vérifier l'existence du champ avant de le récupérer
                                if (document.contains("nom-utilisateur")) {
                                    String mail = document.getString("curent_user").toString();
                                    String pass ;
                                    String URL;
                                    String nomUtilisateur;
                                    nomUtilisateur = document.getString("nom-utilisateur");
                                    AlertDialog.Builder monpupup = new AlertDialog.Builder(MainActivity.this);
                                    if(mail != null && mail.equals(user.getEmail()) && nomUtilisateur.equals(selectedItem)){


                                        pass = document.getString("mot_de_passe");
                                        URL = document.getString("url");

                                        // Configuration de l'image (à remplacer par votre image)
                                        ImageView imageView = new ImageView(MainActivity.this);
                                        // Configuration de l'image (à remplacer par votre image)
                                        imageView.setImageResource(R.drawable.key);

                                        // Convertir 100dp en pixels
                                        int imageSizeInPixels = (int) TypedValue.applyDimension(
                                                TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

                                        // Définir la taille de l'ImageView
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                imageSizeInPixels, imageSizeInPixels
                                        ));
                                        // Création d'un layout pour contenir le texte et l'image
                                        LinearLayout layout = new LinearLayout(MainActivity.this);
                                        layout.setOrientation(LinearLayout.VERTICAL);
                                        layout.setGravity(Gravity.CENTER);
                                        layout.addView(imageView);
                                        TextView textView = new TextView(MainActivity.this);
                                        // Configuration du texte
                                        textView.setText("      url:     " + URL + "\n\n      password:     " + pass+"\n\n      nom d'utilisateur  :  " + nomUtilisateur+"\n\n      mail:    " + mail+"\n"+"\n");

                                        // Ajout du TextView au layout
                                        textView.setTextIsSelectable(true);
                                        layout.addView(textView);



                                        // Ajout du layout à la boîte de dialogue

                                        monpupup.setView(layout);
                                        Button modifierButton = new Button(MainActivity.this);
                                        modifierButton.setText("Modifier");
                                        modifierButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                                        layout.addView(modifierButton);


                                        // Set the text for the button
                                        Button delete = new Button(MainActivity.this);
                                        delete.setText("delete");
                                        delete.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                                        layout.addView(delete);// Set the text for the button
                                        delete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Handle the "Delete" button click action here
                                                // You can implement the logic to delete the selected item or perform other actions


                                                // Get a reference to the Firestore collection

                                                CollectionReference lesPassCollection = db.collection("les_pass");

// Specify the "nom-utilisateur" value of the document you want to delete
                                                String nomUtilisateurToDelete = nomUtilisateur;

// Create a query to find the document with the specified "nom-utilisateur"
                                                Query query = lesPassCollection
                                                .whereEqualTo("nom-utilisateur", nomUtilisateurToDelete)
                                                .whereEqualTo("curent_user", user.getEmail());

                                                query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                                                    // Check if any documents match the query
                                                    if (!queryDocumentSnapshots.isEmpty()) {
                                                        // Get the reference to the first matching document
                                                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                                                        // Delete the document
                                                        documentSnapshot.getReference().delete().addOnSuccessListener(aVoid -> {
                                                            restartActivity();
                                                            // Document successfully deleted

                                                        }).addOnFailureListener(e -> {
                                                            // Handle errors here

                                                        });
                                                    } else {
                                                        // Document with the specified "nom-utilisateur" value not found

                                                    }
                                                }).addOnFailureListener(e -> {
                                                    // Handle errors here

                                                });



                                                Toast.makeText(MainActivity.this, "Delete button clicked", Toast.LENGTH_SHORT).show();
                                            }

                                        });
                                        modifierButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Handle the "Delete" button click action here
                                                // You can implement the logic to delete the selected item or perform other actions
                                                Intent intent= new Intent(getApplicationContext(), Modify.class);
                                                intent.putExtra("nomUtilisateur", nomUtilisateur);
                                                startActivity(intent);
                                                finish();

                                                Toast.makeText(MainActivity.this, "modifier button clicked", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                        int marginInPixels = (int) TypedValue.applyDimension(
                                                TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                                        monpupup.create().getWindow().getDecorView().setPadding(
                                                marginInPixels, marginInPixels, marginInPixels, marginInPixels);
                                        WindowManager.LayoutParams params = monpupup.create().getWindow().getAttributes();
                                        params.y = 50; // Modifier la valeur selon vos besoins
                                        monpupup.create().getWindow().setAttributes(params);


                                        monpupup.setTitle("the entry for user   "+nomUtilisateur);
                                        monpupup.show();





                                    }



                                    // Assurez-vous que "nomUtilisateur" n'est pas nul avant de l'ajouter à la liste

                                }

                            }


                            // Maintenant, "maListe" contient tous les noms d'utilisateurs
                            // Vous pouvez faire ce que vous voulez avec cette liste ici

                            // Créez un adaptateur ArrayAdapter

                        });










                // Affichage du toast personnalisé

            }
        });
    }




// ...














    }
