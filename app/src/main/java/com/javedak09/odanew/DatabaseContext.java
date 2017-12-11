package com.javedak09.odanew;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by javed.khan on 11/23/2017.
 */

@Database(entities = {UserEntity.class}, version = 1)
public abstract class DatabaseContext extends RoomDatabase {

    private static DatabaseContext INSTANCE;
    static RoomDatabase.Callback rdc;

    public abstract DaoAccess daoAccess();

    public static DatabaseContext getAppDatabase(final Context context) {

        rdc = new RoomDatabase.Callback() {
            public void onCreate(SupportSQLiteDatabase db) {
                String SQL_CREATE_USERS = "CREATE TABLE users IF NOT EXISTS ("
                        + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + " username TEXT,"
                        + " passwd TEXT"
                        + ");";
            }

            public void onOpen(SupportSQLiteDatabase db) {
                INSTANCE = Room.databaseBuilder(context,
                        DatabaseContext.class, "users.db").addCallback(rdc).allowMainThreadQueries().build();
            }
        };

        if (INSTANCE == null) {
            INSTANCE =
                    /*Room.databaseBuilder(context.getApplicationContext(), DatabaseContext.class, "user.db")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();*/

                    Room.databaseBuilder(context,
                            DatabaseContext.class, "users.db").addCallback(rdc).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }


    public static void destroyInstance() {
        INSTANCE = null;
    }
}