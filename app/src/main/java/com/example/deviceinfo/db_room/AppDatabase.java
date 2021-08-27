package com.example.deviceinfo.db_room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Main access point for the room database.
 */
@Database(entities = {DeviceDb.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {

  public abstract DatabaseDao DatabaseDao();

  private static AppDatabase INSTANCE;

  public static AppDatabase getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE = Room
          .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "JoseDevice")
          .allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }
    return INSTANCE;
  }


}
