package com.example.almgohar.iottrilaterationproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.almgohar.iottrilaterationproject.others.Student;
import com.example.almgohar.iottrilaterationproject.others.TeachingAssistant;
import com.example.almgohar.iottrilaterationproject.others.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterCompletionActivity extends AppCompatActivity implements View.OnClickListener
{

    private FirebaseAuth firebaseAuth;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private Spinner spinnerUserType;
    private Button buttonSaveInfo;


    private ProgressDialog progressDialog;

    //private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_completion);

        firebaseAuth = firebaseAuth.getInstance();

        spinnerUserType = (Spinner) findViewById(R.id.spinnerStudentTA);
        editTextEmail = (EditText) findViewById(R.id.editTextEmailReg);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswordReg);
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonSaveInfo = (Button) findViewById(R.id.buttonSaveInfo);

        progressDialog = new ProgressDialog(this);
        buttonSaveInfo.setOnClickListener(this);
    }


    private void registerUser()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                progressDialog.dismiss();
                if (task.isSuccessful())
                {
                    Toast.makeText(RegisterCompletionActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = task.getResult().getUser();
                    completeRegistration(user);
                } else
                {
                    Toast.makeText(RegisterCompletionActivity.this, "Failed to Register, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void completeRegistration(FirebaseUser user)
    {
        String name = editTextName.getText().toString().trim();
        String userType = spinnerUserType.getSelectedItem().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        if (userType.equals("Student"))
        {
            User userInformation = new Student(name, email);
            databaseReference.child("Students").child(user.getUid()).setValue(userInformation);
            finish();
            startActivity(new Intent(getApplicationContext(), StudentViewActivity.class));
        } else
        {
            User userInformation = new TeachingAssistant(name, email);
            databaseReference.child("TAs").child(user.getUid()).setValue(userInformation);
            finish();
            startActivity(new Intent(getApplicationContext(), TAViewActivity.class));
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v == buttonSaveInfo)
        {
            registerUser();
        }
    }


}
