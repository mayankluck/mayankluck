package com.example.adminpannel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class MainActivity extends AppCompatActivity {

    private EditText usernameid, password;
    private TextView forgetPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);

        usernameid = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        Button userLogin = findViewById(R.id.login);
        forgetPassword = findViewById(R.id.forgotPassword);


        pd = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = usernameid.getText().toString().trim();
                String upass = password.getText().toString().trim();

                if (uname.isEmpty()) {
                    usernameid.setError("Please Enter email Id as User Name");
                    usernameid.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(uname).matches()) {
                    usernameid.setError("Please enter valid Email Id");
                    usernameid.requestFocus();
                    return;
                }
                if (upass.length() < 6) {
                    password.setError("Please enter valid Password");
                    password.requestFocus();
                    return;
                }

                pd.setMessage("Logging...");
                pd.show();
                mAuth.signInWithEmailAndPassword(uname, upass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @Nullable Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user.isEmailVerified()) {
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Admin");
                                        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    if (snapshot.child("flag").getValue().toString().equals("admin")) {
                                                        startActivity(new Intent(MainActivity.this, MainadminActivity.class));
                                                        MainActivity.this.finish();
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "Not a valid credentials", Toast.LENGTH_SHORT).show();
                                                        FirebaseAuth.getInstance().signOut();
                                                        pd.dismiss();
                                                        usernameid.setText("");
                                                        password.setText("");

                                                    }
                                                } else {
                                                    Toast.makeText(MainActivity.this, "User Not Exists", Toast.LENGTH_SHORT).show();
                                                    pd.dismiss();
                                                    usernameid.setText("");
                                                    password.setText("");

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    } else {
                                        pd.dismiss();
                                        user.sendEmailVerification();
                                        Toast.makeText(MainActivity.this, "Check your email to verify your account", Toast.LENGTH_SHORT).show();
                                        password.setText("");
                                    }
                                } else {
                                    pd.dismiss();

                                    Toast.makeText(MainActivity.this, "Please Check Your Password\nFail to Login", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(MainActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.forgetpassword_layout))
                        .setExpanded(true, 1100)
                        .setInAnimation(R.anim.abc_fade_in)
                        .setOutAnimation(R.anim.abc_fade_out)
                        .create();

                FirebaseAuth mAuth;
                View dp = dialogPlus.getHolderView();
                EditText edtEmail = dp.findViewById(R.id.edt_reset_email);
                Button btnResetPassword = dp.findViewById(R.id.btn_reset_password);
                Button btnBack = dp.findViewById(R.id.btn_back);
                dialogPlus.show();
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogPlus.dismiss();
                    }
                });

                mAuth = FirebaseAuth.getInstance();
                btnResetPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = edtEmail.getText().toString().trim();

                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mAuth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                                            usernameid.setText("");
                                            password.setText("");
                                            dialogPlus.dismiss();

                                        } else {
                                            Toast.makeText(MainActivity.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                                            usernameid.setText("");
                                            password.setText("");
                                            dialogPlus.dismiss();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Server Error Please try After some time.", Toast.LENGTH_SHORT).show();
                                usernameid.setText("");
                                password.setText("");
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           startActivity(new Intent(MainActivity.this, MainadminActivity.class));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(MainActivity.this, MainadminActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}