package com.example.almgohar.iottrilaterationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


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

public class TAEditScheduleActivity extends AppCompatActivity implements View.OnClickListener
{

    private FirebaseAuth firebaseAuth;
    private TeachingAssistant teachingAssistant;

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
        setContentView(R.layout.activity_taedit_schedule);

        firebaseAuth = firebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        final String uuid = user.getUid();

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

        //        defineButtons();


    }

    public void defineButtons()
    {

    }

    public void configureButtons()
    {
        //TODO add the rest of buttons

        buttonThu1st = (Button) findViewById(R.id.buttonThu1st);
        buttonThu2nd = (Button) findViewById(R.id.buttonThu2nd);
        buttonThu3rd = (Button) findViewById(R.id.buttonThu3rd);
        buttonThu4th = (Button) findViewById(R.id.buttonThu4th);
        buttonThu5th = (Button) findViewById(R.id.buttonThu5th);

        buttonEdit = (Button) findViewById(R.id.buttonEditSchedule);
        buttonEdit.setOnClickListener(this);

        Map<String, Tutorial> temp = teachingAssistant.getSchedule();
        Iterator it = temp.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, Tutorial> pair = (Map.Entry) it.next();
            if (pair.getKey().equals("Thursday1st Slot"))
            {
                buttonThu1st.setText(pair.getValue().getCourseName() + "\n" + pair.getValue().getLocation());
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    //    public void editSchedule()
    //    {
    //        buttonThu1st = (Button) findViewById(R.id.buttonThu1st);
    //        buttonThu2nd = (Button) findViewById(R.id.buttonThu2nd);
    //        buttonThu3rd = (Button) findViewById(R.id.buttonThu3rd);
    //        buttonThu4th = (Button) findViewById(R.id.buttonThu4th);
    //        buttonThu5th = (Button) findViewById(R.id.buttonThu5th);
    //
    //        buttonThu1st.setOnClickListener(this);
    //        buttonThu2nd.setOnClickListener(this);
    //        buttonThu3rd.setOnClickListener(this);
    //        buttonThu4th.setOnClickListener(this);
    //        buttonThu5th.setOnClickListener(this);
    //    }

    //    public void addToSchedule(String x)
    //    {
    //        Log.d("MyTag", x);
    //    }

    @Override
    public void onClick(View v)
    {
        if (v == buttonEdit)
        {
            startActivity(new Intent(this, EditScheduleActivity.class));
            finish();
        }
    }
}
