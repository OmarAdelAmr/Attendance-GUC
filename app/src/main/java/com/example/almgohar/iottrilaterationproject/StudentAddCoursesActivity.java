package com.example.almgohar.iottrilaterationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;


import com.example.almgohar.iottrilaterationproject.others.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class StudentAddCoursesActivity extends AppCompatActivity implements View.OnClickListener
{

    private Spinner spinnerCourse;
    private Spinner spinnerTeamNumber;

    private Button buttonAdd;
    private Button buttonSave;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add_courses);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        spinnerCourse = (Spinner) findViewById(R.id.spinnerCoursesS);
        spinnerTeamNumber = (Spinner) findViewById(R.id.spinnerTeamsS);

        buttonAdd = (Button) findViewById(R.id.buttonAddCourseS);
        buttonSave = (Button) findViewById(R.id.buttonSaveChangesS);

        final String uuid = firebaseAuth.getCurrentUser().getUid();

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
                        student = child.getValue(Student.class);
                        buttonAdd.setOnClickListener(StudentAddCoursesActivity.this);
                        buttonSave.setOnClickListener(StudentAddCoursesActivity.this);
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
        String course = spinnerCourse.getSelectedItem().toString().trim();
        String teamNumber = spinnerTeamNumber.getSelectedItem().toString().trim();
        //        Log.d("MyTag", day + slot);
        //        Log.d("MyTag", teachingAssistant.equals(null)+"");
        //        Log.d("MyTag", teachingAssistant.getSchedule().equals(null)+"");
        student.getSchedule().put(course, teamNumber);

        commitChanges();
    }

    public void commitChanges()
    {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("schedule", student.getSchedule());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Students").child(firebaseAuth.getCurrentUser().getUid()).updateChildren(childUpdates);
        //        check();
    }

    @Override
    public void onClick(View v)
    {
        if (v == buttonAdd)
        {
            addCourseToSchedule();
        } else if (v == buttonSave)
        {
            startActivity(new Intent(this, StudentScheduleActivity.class));
            finish();
        }
    }
}
