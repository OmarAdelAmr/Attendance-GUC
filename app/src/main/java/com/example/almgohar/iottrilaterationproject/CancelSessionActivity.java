package com.example.almgohar.iottrilaterationproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class CancelSessionActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button buttonCancel;
    private Spinner spinnerCourse;
    private Spinner spinnerDay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_session);

        buttonCancel = (Button) findViewById(R.id.buttonCancelTut);
        spinnerCourse = (Spinner) findViewById(R.id.spinnerCoursesCan);
        spinnerDay = (Spinner) findViewById(R.id.spinnerDaysCan);


    }

    @Override
    public void onClick(View view)
    {

    }
}
