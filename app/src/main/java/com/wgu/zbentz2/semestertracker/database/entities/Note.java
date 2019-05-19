package com.wgu.zbentz2.semestertracker.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
    tableName = "notes",
    foreignKeys = @ForeignKey(
        entity = Course.class,
        parentColumns = "id",
        childColumns = "course_id",
        onDelete = CASCADE
    )
)
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    private long course_id;
    private String note_name;
    private String note_body;

    public Note(long course_id, String note_name, String note_body) {

        this.course_id = course_id;
        this.note_name = note_name;
        this.note_body = note_body;

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

    public String getNote_name() {

        return note_name;

    }

    public void setNote_name(String note_name) {

        this.note_name = note_name;

    }

    public String getNote_body() {

        return note_body;

    }

    public void setNote_body(String note_body) {

        this.note_body = note_body;

    }

    @Override public String toString() {

        return this.note_name;

    }

}
