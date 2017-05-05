package com.example.almgohar.iottrilaterationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.almgohar.iottrilaterationproject.others.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentViewActivity extends AppCompatActivity implements View.OnClickListener
{

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonBeaconActivity;
    private Button buttonShowSchedule;


    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view);

        firebaseAuth = firebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        buttonLogout = (Button) findViewById(R.id.buttonLogoutS);
        buttonShowSchedule = (Button) findViewById(R.id.buttonShowScheduleS);
        buttonBeaconActivity = (Button) findViewById(R.id.buttonShowBeaconActivity);

        buttonLogout.setOnClickListener(this);

        buttonShowSchedule.setOnClickListener(this);
        buttonBeaconActivity.setOnClickListener(this);

        final String uuid = firebaseAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Query queryRef = databaseReference.orderByChild("fullName");
        Log.d("myTag", queryRef.toString());


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Students").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    if (child.getKey().equals(uuid))
                    {
                        Student value = child.getValue(Student.class);
                        temp(value);

                        break;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });


    }

    private void temp(Student value)
    {
        textViewUserEmail.setText("Welcome " + value.getName());

    }


    private void editSchedule()
    {
        startActivity(new Intent(this, StudentScheduleActivity.class));
        finish();

    }

    @Override
    public void onClick(View v)
    {
        if (v == buttonLogout)
        {
            firebaseAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (v == buttonShowSchedule)
        {
            editSchedule();
        } else if(v == buttonBeaconActivity)
        {
            startActivity(new Intent(this, BeaconActivity.class));
            finish();
        }
    }
}