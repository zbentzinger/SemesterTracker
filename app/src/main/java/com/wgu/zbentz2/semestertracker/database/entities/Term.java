package com.wgu.zbentz2.semestertracker.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "terms")
public class Term implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    private String name;
    private String start_date;
    private String end_date;

    public Term(String name, String start_date, String end_date) {

        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;

    }

    public long getId() {

        return id;

    }

    public String getName() {

        return name;

    }

    public String getStart_date() {

        return start_date;

    }

    public String getEnd_date() {

        return end_date;

    }

    public void setId(long id) {

        this.id = id;

    }

    public void setName(String name) {

        this.name = name;

    }

    public void setStart_date(String start_date) {

        this.start_date = start_date;

    }

    public void setEnd_date(String end_date) {

        this.end_date = end_date;

    }

}
