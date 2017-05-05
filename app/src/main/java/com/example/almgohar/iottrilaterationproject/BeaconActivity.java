package com.example.almgohar.iottrilaterationproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.almgohar.iottrilaterationproject.others.Attendance;
import com.example.almgohar.iottrilaterationproject.others.Student;
import com.example.almgohar.iottrilaterationproject.others.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BeaconActivity extends AppCompatActivity implements View.OnClickListener
{
    NewPositionReceiver newPositionReceiver;
    private Button buttonBack;
    private DatabaseReference databaseReference;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView t4;
    private TextView t5;
    private TextView t6;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        newPositionReceiver = new NewPositionReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BeaconService.update_action);
        registerReceiver(newPositionReceiver, intentFilter);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent i = new Intent(this, BeaconService.class);
        startService(i);

        buttonBack = (Button) findViewById(R.id.buttonBeaconBack);
        buttonBack.setOnClickListener(this);

        t1 = (TextView) findViewById(R.id.majorID);
        t2 = (TextView) findViewById(R.id.distanceID);
        t3 = (TextView) findViewById(R.id.mainTextView);
        t4 = (TextView) findViewById(R.id.mainTextViewRoom);
        t5 = (TextView) findViewById(R.id.textViewPercentage);
        t6 = (TextView) findViewById(R.id.nameID);
    }

    public void updateLocation(String maj, String dis, String header, String name, String room, String percent)
    {

        t1.setText("Major: " + maj);
        t2.setText("Distance: " + dis);
        t6.setText("Name: " + name);
        t4.setText("Room: " + room);
        t5.setText("Percentage: " + percent);




    }

    public void saveAttendance()
    {

        firebaseAuth = firebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        Attendance attendance = new Attendance(user.getEmail(), "IoT");
        databaseReference.child("Attendance").child(user.getUid()).setValue(attendance);
    }

    @Override
    public void onClick(View view)
    {
        if (view == buttonBack)
        {
            startActivity(new Intent(this, StudentViewActivity.class));
            finish();
        }
    }

    private class NewPositionReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context arg0, Intent arg1)
        {
            // TODO Auto-generated method stub
            String beaconMajor = arg1.getStringExtra("major");
            String beaconDistance = arg1.getStringExtra("distance");
            String beaconHeader = arg1.getStringExtra("mainHeader");
            String name = arg1.getStringExtra("name");
            String room = arg1.getStringExtra("room");
            String percent = arg1.getStringExtra("percentage");
            updateLocation(beaconMajor, beaconDistance, beaconHeader, name, room, percent);
        }
    }

}
