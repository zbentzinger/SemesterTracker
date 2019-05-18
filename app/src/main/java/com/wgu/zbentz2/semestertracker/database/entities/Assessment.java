package com.wgu.zbentz2.semestertracker.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
    tableName = "assessments",
    foreignKeys = @ForeignKey(
        entity = Course.class,
        parentColumns = "id",
        childColumns = "course_id",
        onDelete = CASCADE
    )
)
public class Assessment implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    private long course_id;
    private String name;
    private String type;
    private String due_date;
    private int notifications;

    public Assessment(long course_id,
                      String name,
                      String type,
                      String due_date,
                      int notifications) {

        this.course_id = course_id;
        this.name = name;
        this.type = type;
        this.due_date = due_date;
        this.notifications = notifications;

    }

    public long getId() {

        return id;

    }

    public void setId(long id) {

        this.id = id;

    }

    public long getCourse_id() {

        return course_id;

    }

    public void setCourse_id(long course_id) {

        this.course_id = course_id;

    }

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public String getType() {

        return type;

    }

    public void setType(String type) {

        this.type = type;

    }

    public String getDue_date() {

        return due_date;

    }

    public void setDue_date(String due_date) {

        this.due_date = due_date;

    }

    public int getNotifications() {

        return notifications;

    }

    public void setNotifications(int notifications) {

        this.notifications = notifications;

    }

    @Override public String toString() {

        return this.name;

    }
}
