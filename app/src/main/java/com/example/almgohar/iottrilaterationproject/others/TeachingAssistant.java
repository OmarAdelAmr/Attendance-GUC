package com.example.almgohar.iottrilaterationproject.others;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omar on 4/23/17.
 */

public class TeachingAssistant extends User {

    private Map<String, Tutorial> schedule = new HashMap<>();

    public TeachingAssistant()
    {
        super();
    }

    public TeachingAssistant(String name, String email) {
        super(name, email);
    }

    public Map<String, Tutorial> getSchedule()
    {
        return schedule;
    }

    public void setSchedule(Map<String, Tutorial> schedule)
    {
        this.schedule = schedule;
    }
}
