package com.wgu.zbentz2.semestertracker.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
    tableName = "courses",
    foreignKeys = @ForeignKey(
        entity = Term.class,
        parentColumns = "id",
        childColumns = "term_id",
        onDelete = CASCADE
    )
)
public class Course implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    private long term_id;
    private String name;
    private String start_date;
    private String end_date;
    private String mentor_name;
    private String mentor_phone;
    private String mentor_email;
    private String status;
    private int notifications;

    public Course(long term_id,
                  String name,
                  String start_date,
                  String end_date,
                  String mentor_name,
                  String mentor_phone,
                  String mentor_email,
                  String status,
                  int notifications) {

        this.term_id = term_id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.mentor_name = mentor_name;
        this.mentor_phone = mentor_phone;
        this.mentor_email = mentor_email;
        this.status = status;
        this.notifications = notifications;

    }

    public long getTerm_id() {

        return term_id;

    }

    public void setTerm_id(long term_id) {

        this.term_id = term_id;

    }

    public long getId() {

        return id;

    }

    public void setId(long id) {

        this.id = id;

    }

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public String getStart_date() {

        return start_date;

    }

    public void setStart_date(String start_date) {

        this.start_date = start_date;

    }

    public String getEnd_date() {

        return end_date;

    }

    public void setEnd_date(String end_date) {

        this.end_date = end_date;

    }

    public String getMentor_name() {

        return mentor_name;

    }

    public void setMentor_name(String mentor_name) {

        this.mentor_name = mentor_name;

    }

    public String getMentor_phone() {

        return mentor_phone;

    }

    public void setMentor_phone(String mentor_phone) {

        this.mentor_phone = mentor_phone;

    }

    public String getMentor_email() {

        return mentor_email;

    }

    public void setMentor_email(String mentor_email) {

        this.mentor_email = mentor_email;

    }

    public String getStatus() {

        return status;

    }

    public void setStatus(String status) {

        this.status = status;

    }

    public int getNotifications() {

        return notifications;

    }

    public void setNotifications(int notifications) {

        this.notifications = notifications;

    }

}
