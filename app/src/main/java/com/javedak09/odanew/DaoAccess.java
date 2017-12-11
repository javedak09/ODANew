package com.javedak09.odanew;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by javed.khan on 11/23/2017.
 */

@Dao
public interface DaoAccess {

    @Insert
    void insertRecord(UserEntity entity);

    @Query("SELECT * FROM users")
    List<UserEntity> fetchAllUsers();

    @Query("SELECT * FROM users")
    UserEntity fetchAllUsersNew();

    @Query("SELECT * FROM users WHERE id =:college_id")
    UserEntity getSingleRecord(int college_id);
}