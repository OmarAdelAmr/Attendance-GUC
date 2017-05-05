package com.example.almgohar.iottrilaterationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.almgohar.iottrilaterationproject.others.Student;
import com.example.almgohar.iottrilaterationproject.others.TeachingAssistant;
import com.example.almgohar.iottrilaterationproject.others.Tutorial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

public class StudentScheduleActivity extends AppCompatActivity implements View.OnClickListener
{

    private FirebaseAuth firebaseAuth;
    private Student student;

    private Button buttonThu1st;
    private Button buttonThu2nd;
    private Button buttonThu3rd;
    private Button buttonThu4th;
    private Button buttonThu5th;


    private Button buttonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_schedule);

        firebaseAuth = firebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        final String uuid = user.getUid();

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
                        configureButtons();
                        //TODO show scedule in buttons
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

    public void configureButtons()
    {
        //TODO add the rest of buttons

        buttonThu1st = (Button) findViewById(R.id.buttonThu1sts);
        buttonThu2nd = (Button) findViewById(R.id.buttonThu2nds);
        buttonThu3rd = (Button) findViewById(R.id.buttonThu3rds);
        buttonThu4th = (Button) findViewById(R.id.buttonThu4ths);
        buttonThu5th = (Button) findViewById(R.id.buttonThu5ths);

        buttonEdit = (Button) findViewById(R.id.buttonEditScheduleS);
        buttonEdit.setOnClickListener(this);

        final Map<String, String> temp = student.getSchedule();
        Iterator it = temp.entrySet().iterator();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("TAs").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    TeachingAssistant teachingAssistant = child.getValue(TeachingAssistant.class);
                    Map<String, Tutorial> taSched = teachingAssistant.getSchedule();
                    Iterator it2 = taSched.entrySet().iterator();
                    while (it2.hasNext())
                    {
                        Map.Entry<String, Tutorial> pair = (Map.Entry) it2.next();
                        if (temp.containsKey(pair.getValue().getCourseName()))
                        {
                            //TODO add the rest
                            if (pair.getKey().equals("Thursday1st Slot"))
                            {
                                buttonThu1st.setText(pair.getValue().getCourseName() + "\n" + pair.getValue().getLocation());
                            }
                        }
                        it2.remove(); // avoids a ConcurrentModificationException
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
        if (v == buttonEdit)
        {
            startActivity(new Intent(this, com.example.almgohar.iottrilaterationproject.StudentAddCoursesActivity.class));
            finish();
        }
    }
}
