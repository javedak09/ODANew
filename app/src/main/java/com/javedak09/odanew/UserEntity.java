package com.javedak09.odanew;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by javed.khan on 11/23/2017.
 */
@Entity(tableName = "users")
public class UserEntity {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo
    String username;
    @ColumnInfo
    String passwd;

    public String getUserName() {
        return username;
    }

    public void setUserName(String usr) {
        this.username = usr;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String pass) {
        this.passwd = pass;
    }

}