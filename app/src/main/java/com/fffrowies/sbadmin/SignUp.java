package com.fffrowies.sbadmin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fffrowies.sbadmin.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText edtPhone, edtName, edtPassword;
    Button btnSignUp;

    ProgressBar progressBar;
    TextView txvProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        txvProgressBar = (TextView) findViewById(R.id.txvProgressBar);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                txvProgressBar.setText("Please waiting...");

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Check if user phone is already registered
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            txvProgressBar.setText("");

                            Toast.makeText(SignUp.this, "Phone Number already registered", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progressBar.setVisibility(View.INVISIBLE);
                            txvProgressBar.setText("");

                            User user = new User(edtName.getText().toString(), edtPassword.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);

                            Toast.makeText(SignUp.this, "Sign Up successfully !", Toast.LENGTH_SHORT).show();
                            finish();
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
