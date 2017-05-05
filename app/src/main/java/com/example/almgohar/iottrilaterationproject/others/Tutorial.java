package com.example.almgohar.iottrilaterationproject.others;

/**
 * Created by omar on 5/4/17.
 */

public class Tutorial
{
    private String courseName;
    private String day;
    private String slot;
    private String location;
    private String teamNumber;

    public Tutorial()
    {

    }

    public Tutorial(String courseName, String day, String slot, String location, String teamNumber)
    {
        this.courseName = courseName;
        this.day = day;
        this.slot = slot;
        this.location = location;
        this.teamNumber = teamNumber;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public String getDay()
    {
        return day;
    }

    public void setDay(String day)
    {
        this.day = day;
    }

    public String getSlot()
    {
        return slot;
    }

    public void setSlot(String slot)
    {
        this.slot = slot;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getTeamNumber()
    {
        return teamNumber;
    }

    public void setTeamNumber(String teamNumber)
    {
        this.teamNumber = teamNumber;
    }
}
