package com.example.keepass;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private TextInputEditText edit_email, edit_password, pays, phone;
    private Button buttonReg, impgup;
    private FirebaseAuth mAuth;
    private ProgressBar probar;
    private FirebaseFirestore db;
    private ImageView img;
    private TextView clicklogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edit_email = findViewById(R.id.email);
        edit_password = findViewById(R.id.password);
        impgup = findViewById(R.id.chooseImageButton);
        mAuth = FirebaseAuth.getInstance();
        probar = findViewById(R.id.progressbar);
        db = FirebaseFirestore.getInstance();
        img = findViewById(R.id.logo_img);
        phone = findViewById(R.id.phone);
        pays = findViewById(R.id.country);
        buttonReg = findViewById(R.id.btn_register);
        clicklogin = findViewById(R.id.clicklog);



        clicklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                probar.setVisibility(View.VISIBLE);
                email = edit_email.getText().toString();
                password = edit_password.getText().toString();
                String num = phone.getText().toString();
                String countr = pays.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Enter a valid password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                probar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Register.this, "Authentication succeed.", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();




                                    String num=phone.getText().toString();
                                    String countr=pays.getText().toString();


                                    Map<String, Object> profil = new HashMap<>();
                                    profil.put("curent_user", email);
                                    profil.put("pays", countr);
                                    profil.put("phone", num);

                                    db.collection("Profile")
                                            .add(profil)
                                            .addOnSuccessListener(documentReference -> {
                                                Toast.makeText(Register.this, "Password created successfully", Toast.LENGTH_SHORT).show();

                                            });


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }






}
