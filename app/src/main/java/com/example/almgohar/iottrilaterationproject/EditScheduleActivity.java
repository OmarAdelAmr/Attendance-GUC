package com.example.almgohar.iottrilaterationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;


import com.example.almgohar.iottrilaterationproject.others.TeachingAssistant;
import com.example.almgohar.iottrilaterationproject.others.Tutorial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditScheduleActivity extends AppCompatActivity implements View.OnClickListener
{

    private Spinner spinnerDay;
    private Spinner spinnerSlot;
    private Spinner spinnerCourse;
    private Spinner spinnerRoom;
    private Spinner spinnerTeamNumber;

    private Button buttonAdd;
    private Button buttonSave;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private TeachingAssistant teachingAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        spinnerDay = (Spinner) findViewById(R.id.spinnerDays);
        spinnerSlot = (Spinner) findViewById(R.id.spinnerSlots);
        spinnerCourse = (Spinner) findViewById(R.id.spinnerCourses);
        spinnerRoom = (Spinner) findViewById(R.id.spinnerRooms);
        spinnerTeamNumber = (Spinner) findViewById(R.id.spinnerTeams);

        buttonAdd = (Button) findViewById(R.id.buttonAddCourse);
        buttonSave = (Button) findViewById(R.id.buttonSaveChanges);

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
                        teachingAssistant = child.getValue(TeachingAssistant.class);
                        buttonAdd.setOnClickListener(EditScheduleActivity.this);
                        buttonSave.setOnClickListener(EditScheduleActivity.this);
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

    private void addCourseToSchedule()
    {
        String day = spinnerDay.getSelectedItem().toString().trim();
        String slot = spinnerSlot.getSelectedItem().toString().trim();
        String course = spinnerCourse.getSelectedItem().toString().trim();
        String location = spinnerRoom.getSelectedItem().toString().trim();
        String teamNumber = spinnerTeamNumber.getSelectedItem().toString().trim();
        //        Log.d("MyTag", day + slot);
        //        Log.d("MyTag", teachingAssistant.equals(null)+"");
        //        Log.d("MyTag", teachingAssistant.getSchedule().equals(null)+"");
        teachingAssistant.getSchedule().put(day + slot, new Tutorial(course, day, slot, location, teamNumber));

        commitChanges();
    }

    public void commitChanges()
    {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("schedule", teachingAssistant.getSchedule());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("TAs").child(firebaseAuth.getCurrentUser().getUid()).updateChildren(childUpdates);
        //        check();
    }

    public void check()
    {
        //TODO remove after check for change

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
                        teachingAssistant = child.getValue(TeachingAssistant.class);
                        Log.d("MyTag", teachingAssistant.getSchedule().get("Sunday1st Slot").getCourseName());
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


    @Override
    public void onClick(View v)
    {
        if (v == buttonAdd)
        {
            addCourseToSchedule();
        } else if (v == buttonSave)
        {
            startActivity(new Intent(this, TAEditScheduleActivity.class));
            finish();
        }
    }
}
