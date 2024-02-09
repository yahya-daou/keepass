package com.example.keepass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.widget.SearchView;

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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {
    TextView text;
    Button logoutbtn;
    SearchView searchView;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ListView listView ;  //circularImageView
    FirebaseUser user;

    ImageView img;

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
        searchView =findViewById(R.id.searchView);
        text=findViewById(R.id.textotedit);
        listView = findViewById(R.id.listView);
        logoutbtn=findViewById(R.id.logoutbtn);
        img=findViewById(R.id.circularImageView);
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
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                finish();

            }
        });




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

                        if (document.contains("application")) {
                            String mail = document.getString("curent_user").toString();
                            String application;
                            if(mail != null && mail.equals(user.getEmail())){
                                application = document.getString("application");


                                maListe.add(application);
                            }




                        }
                    }



                    MyCustomAdapter adapter = new MyCustomAdapter(MainActivity.this, maListe);


                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {

                            adapter.getFilter().filter(newText);
                            listView.setAdapter(adapter);
                            return true;
                        }
                    });



                    listView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {

                });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = (String) parent.getItemAtPosition(position);



                db.collection("les_pass")
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {

                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                                if (document.contains("application")) {
                                    String mail = document.getString("curent_user").toString();
                                    String pass ;

                                    String application ;
                                    String URL;
                                    String nomUtilisateur;
                                    application = document.getString("application");
                                    AlertDialog.Builder monpupup = new AlertDialog.Builder(MainActivity.this);
                                    if(mail != null && mail.equals(user.getEmail()) && application.equals(selectedItem)){


                                        pass = document.getString("mot_de_passe");
                                        String pass2 = "";
                                        SecretKey loadedKey = CryptoUtils.loadKey(MainActivity.this, mail);
                                        if (loadedKey != null) {


                                            String decryptedData = CryptoUtils.decrypt(loadedKey, pass);
                                            pass2 = decryptedData;
                                        } else {

                                            Toast.makeText(MainActivity.this, "Impossible de charger la clé pour décrypter le mot de passe", Toast.LENGTH_SHORT).show();
                                        }



                                        //
                                        nomUtilisateur = document.getString("nom-utilisateur");
                                        URL = document.getString("url");


                                        ImageView imageView = new ImageView(MainActivity.this);

                                        imageView.setImageResource(R.drawable.key);


                                        int imageSizeInPixels = (int) TypedValue.applyDimension(
                                                TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());


                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                imageSizeInPixels, imageSizeInPixels
                                        ));

                                        LinearLayout layout = new LinearLayout(MainActivity.this);
                                        layout.setOrientation(LinearLayout.VERTICAL);
                                        layout.setGravity(Gravity.CENTER);
                                        layout.addView(imageView);
                                        TextView textView = new TextView(MainActivity.this);

                                        textView.setText("      url:     " + URL + "\n\n      password:     " + pass2+"\n\n      nom application  :  " + application+"\n\n      nom d'utilisateur  :  " + nomUtilisateur+"\n\n      mail:    " + mail+"\n"+"\n");


                                        textView.setTextIsSelectable(true);
                                        layout.addView(textView);





                                        monpupup.setView(layout);
                                        Button modifierButton = new Button(MainActivity.this);
                                        modifierButton.setText("Modifier");
                                        modifierButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                                        layout.addView(modifierButton);



                                        Button delete = new Button(MainActivity.this);
                                        delete.setText("delete");
                                        delete.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                                        layout.addView(delete);
                                        delete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                CollectionReference lesPassCollection = db.collection("les_pass");


                                                String nomUtilisateurToDelete = application;


                                                Query query = lesPassCollection
                                                .whereEqualTo("application", nomUtilisateurToDelete)
                                                .whereEqualTo("curent_user", user.getEmail());

                                                query.get().addOnSuccessListener(queryDocumentSnapshots -> {

                                                    if (!queryDocumentSnapshots.isEmpty()) {

                                                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                                                        documentSnapshot.getReference().delete().addOnSuccessListener(aVoid -> {
                                                            restartActivity();


                                                        }).addOnFailureListener(e -> {


                                                        });
                                                    } else {

                                                    }
                                                }).addOnFailureListener(e -> {


                                                });



                                                Toast.makeText(MainActivity.this, "Password deleted", Toast.LENGTH_SHORT).show();
                                            }

                                        });
                                        modifierButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent intent= new Intent(getApplicationContext(), Modify.class);
                                                intent.putExtra("application", application);
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
                                        params.y = 50;
                                        monpupup.create().getWindow().setAttributes(params);


                                        monpupup.setTitle("the entry for application   "+application);
                                        monpupup.show();





                                    }



                                }

                            }



                        });












            }
        });
    }




// ...














    }
