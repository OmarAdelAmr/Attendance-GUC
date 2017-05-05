package com.example.almgohar.iottrilaterationproject;

import android.app.Service;
import android.content.Intent;

import java.util.Calendar;

import android.os.IBinder;

import android.os.RemoteException;
import android.util.Log;


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

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class BeaconService extends Service implements BeaconConsumer
{
    private BeaconManager beaconManager;
    private static final String TAG = "BeaconReferenceApp";
    final static String update_action = "UPDATE_ACTION";
    private Map<String, String> majorToRoom;
    private Map<String, String> slots;
    private FirebaseAuth firebaseAuth;
    private Student student;
    private DatabaseReference databaseReference;
    private int globalCounter = 900;
    private int attendanceCounter = 1;

    public BeaconService()
    {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));

        beaconManager.bind(this);
        //TODO do something useful

        majorToRoom = new HashMap<>();
        slots = new HashMap<>();
        majorToRoom.put("10999", "C6.104");
        slots.put("1st", "8:30-10:00");
        databaseReference = FirebaseDatabase.getInstance().getReference();


        readStudentsData();

        checkConditions();

        return Service.START_NOT_STICKY;

        //        return Service.START_NOT_STICKY;
    }

    public void readStudentsData()
    {

        firebaseAuth = firebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        final String uuid = user.getUid();


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
    public IBinder onBind(Intent intent)
    {
        //TODO for communication return IBinder implementation
        return null;
    }

    public int checkConditions()
    {
        if (isCancelled)
        {
            return Service.STOP_FOREGROUND_DETACH;
        }
        return Service.START_NOT_STICKY;
    }

    public void getCurrentTut()
    {

        databaseReference.child("Students").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children)
                {

                        TeachingAssistant teachingAssistant = child.getValue(TeachingAssistant.class);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    @Override
    public void onBeaconServiceConnect()
    {
        try
        {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            //beaconManager.startRangingBeaconsInRegion(new Region());
        } catch (RemoteException e)
        {
        }


        beaconManager.setRangeNotifier(new RangeNotifier()
                                       {
                                           @Override
                                           public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, Region region)
                                           {
                                               Thread thread = new Thread(new Runnable()
                                               {
                                                   @Override
                                                   public void run()
                                                   {
                                                       try
                                                       {
                                                           Calendar calendar = Calendar.getInstance();
                                                           int day = calendar.get(Calendar.DAY_OF_WEEK);


                                                           if (beacons.size() > 0)
                                                           {

                                                               double minDistance = 1000.0;
                                                               String major = "";
                                                               String distanceNo = "";
                                                               String name = "";
                                                               String uuid = "";

                                                               Iterator<Beacon> iterator = beacons.iterator();
                                                               while (iterator.hasNext())
                                                               {
                                                                   Beacon tempBeacon = iterator.next();
                                                                   if (tempBeacon.getDistance() < minDistance)
                                                                   {
                                                                       minDistance = tempBeacon.getDistance();
                                                                       name = tempBeacon.getBluetoothName();
                                                                       major = tempBeacon.getId2() + "";
                                                                       distanceNo = tempBeacon.getDistance() + "";
                                                                   }

                                                               }

                                                               if (major.equals("10999"))
                                                               {
                                                                   attendanceCounter++;
                                                               }


                                                               Log.i("MyTag", majorToRoom.get(major));
                                                               Intent tempIntent = new Intent();
                                                               tempIntent.setAction(update_action);
                                                               tempIntent.putExtra("major", major);
                                                               tempIntent.putExtra("distance", distanceNo);
                                                               tempIntent.putExtra("name", name);
                                                               tempIntent.putExtra("mainHeader", "Nearest Beacon");
                                                               tempIntent.putExtra("room", majorToRoom.get(major));
                                                               tempIntent.putExtra("percentage", attendanceCounter * 1.0 / globalCounter + "");
                                                               sendBroadcast(tempIntent);


                                                           } else
                                                           {
                                                               Intent tempIntent = new Intent();
                                                               tempIntent.setAction(update_action);
                                                               tempIntent.putExtra("major", "N/A");
                                                               tempIntent.putExtra("distance", "N/A");
                                                               tempIntent.putExtra("name", "N/A");
                                                               tempIntent.putExtra("room", "N/A");
                                                               tempIntent.putExtra("mainHeader", "No Beacons Detected");
                                                               tempIntent.putExtra("percentage", "0.0");

                                                               sendBroadcast(tempIntent);
                                                           }

                                                       } catch (Exception e)
                                                       {
                                                           e.printStackTrace();
                                                       }
                                                   }

                                               });
                                               thread.start();
                                           }
                                       }

        );
        beaconManager.setMonitorNotifier(new

                                                 MonitorNotifier()
                                                 {
                                                     @Override
                                                     public void didEnterRegion(Region region)
                                                     {
                                                         Log.i(TAG, "I just saw an beacon for the first time!");
                                                     }

                                                     @Override
                                                     public void didExitRegion(Region region)
                                                     {
                                                         Log.i(TAG, "I no longer see an beacon");
                                                     }

                                                     @Override
                                                     public void didDetermineStateForRegion(int state, Region region)
                                                     {
                                                         Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
                                                     }
                                                 }

        );
    }


    private boolean isCancelled = false;
}
