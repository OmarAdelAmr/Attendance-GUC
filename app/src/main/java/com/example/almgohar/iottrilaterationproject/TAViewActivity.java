package com.example.almgohar.iottrilaterationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.almgohar.iottrilaterationproject.others.TeachingAssistant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TAViewActivity extends AppCompatActivity implements View.OnClickListener
{


    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private Button buttonEditSchedule;
    private Button buttonViewAttendance;
    private Button buttonCancelTut;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taview);

        firebaseAuth = firebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        final String uuid = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("TAs").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    if (child.getKey().equals(uuid))
                    {
                        TeachingAssistant value = child.getValue(TeachingAssistant.class);
                        //temp(value);

                        break;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        buttonLogout = (Button) findViewById(R.id.buttonLogoutTA);
        buttonEditSchedule = (Button) findViewById(R.id.buttonShowScheduleTA);
        buttonViewAttendance = (Button) findViewById(R.id.buttonShowAttendance);
        buttonCancelTut = (Button) findViewById(R.id.buttonCancelTut);

        buttonLogout.setOnClickListener(this);
        buttonEditSchedule.setOnClickListener(this);
        buttonViewAttendance.setOnClickListener(this);
        buttonCancelTut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == buttonLogout)
        {
            firebaseAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if(v == buttonEditSchedule)
        {
            startActivity(new Intent(this, TAEditScheduleActivity.class));
            finish();
        } else if(v==buttonViewAttendance)
        {
            startActivity(new Intent(this, ShowAttendanceActivity.class));
            finish();
        } else if (v == buttonCancelTut)
        {

            finish();
        }
    }
}
