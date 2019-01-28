package com.fffrowies.sbadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fffrowies.sbadmin.Common.Common;
import com.fffrowies.sbadmin.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSignIn;

    ProgressBar progressBar;
    TextView txvProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        txvProgressBar = (TextView) findViewById(R.id.txvProgressBar);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                txvProgressBar.setText("Please waiting...");

                table_user.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Check if user exists in database
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {

                            progressBar.setVisibility(View.INVISIBLE);
                            txvProgressBar.setText("");

                            //Get user information
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            user.setPhone(edtPhone.getText().toString());
                            if (user.getPassword().equals(edtPassword.getText().toString())) {

                                Intent homeIntent = new Intent(SignIn.this, Home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {
                                Toast.makeText(SignIn.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {

                            progressBar.setVisibility(View.INVISIBLE);
                            txvProgressBar.setText("");

                            Toast.makeText(SignIn.this, "User not exist, please Sign Up", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
