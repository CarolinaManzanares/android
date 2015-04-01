package com.carolinamanzanares.todolistf.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cursomovil on 23/03/15.
 */
public class ToDo implements Parcelable{

    private String task;
    private Date created;
    private int mData;

    public ToDo(Date created) {
        this.created = created;
    }

    public ToDo(String task) {

        this.task = task;
        this.created = new Date();
    }

    public ToDo(String task, Date created) {

        this.task = task;
        this.created = created;
    }

    public Date getCreated() {
        return created;
    }

    public String getCreatedFormated() {

        SimpleDateFormat sdt = new SimpleDateFormat("yyyy/MM/dd");
        return sdt.format(created);
    }

    public String getTask() {

        return task;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(task);
        dest.writeLong(created.getTime());

    }

    private ToDo(Parcel in) {
        task = in.readString();
        created = new Date(in.readLong());
    }

    public static final ToDo.Creator<ToDo> CREATOR
            = new ToDo.Creator<ToDo>() {
        public ToDo createFromParcel(Parcel in) {
            return new ToDo(in);
        }

        public ToDo[] newArray(int size) {
            return new ToDo[size];
        }
    };



}
