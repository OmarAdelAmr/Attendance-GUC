package com.example.almgohar.iottrilaterationproject.others;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omar on 4/23/17.
 */

public class Student extends User
{

    private Map<String, String> schedule = new HashMap<>();

    public Student()
    {
        super();
    }

    public Student(String name, String email)
    {
        super(name, email);
    }

    public Map<String, String> getSchedule()
    {
        return schedule;
    }

    public void setSchedule(Map<String, String> schedule)
    {
        this.schedule = schedule;
    }
}
