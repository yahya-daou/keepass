package com.example.keepass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;


import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Profile extends AppCompatActivity {


    private TextInputEditText password, repeated;
    private Button change;
    private TextView mail1 , pays1 , phone;
    FirebaseFirestore db;
    FirebaseUser user1;
    FirebaseAuth auth;
    Button retour;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        password=findViewById(R.id.pass);
        mail1=findViewById(R.id.mail);
        auth=FirebaseAuth.getInstance();
        phone=findViewById(R.id.number1);
        retour=findViewById(R.id.button);


        user1=auth.getCurrentUser();

        pays1=findViewById(R.id.pays);
        db = FirebaseFirestore.getInstance();

        repeated=findViewById(R.id.rep);
        change=findViewById(R.id.buttonChange);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });




        db.collection("Profile")
                .whereEqualTo("curent_user", user1.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String pays = document.getString("pays");
                                String numero = document.getString("phone");
                                phone.setText(user1.getEmail());
                                pays1.setText(pays);
                               mail1.setText(numero);




                            }
                        } else {

                        }
                    }
                });







        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String pass = password.getText().toString();
                String rep = repeated.getText().toString();



                if(pass.equals(rep)){

                    user.updatePassword(pass)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Profile.this, "Password changer", Toast.LENGTH_SHORT).show();

                                    }
                                    else {Toast.makeText(Profile.this, "Password not changer", Toast.LENGTH_SHORT).show();}
                                }
                            });






                }










            }
        });
















    }
}