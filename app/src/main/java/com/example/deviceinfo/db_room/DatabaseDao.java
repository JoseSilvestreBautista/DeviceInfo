package com.example.deviceinfo.db_room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

/**
 * Actions to manipulate data.
 */
@Dao
public interface DatabaseDao {

  @Query("SELECT * FROM deviceDb")
  List<DeviceDb> getDevice();

  @Insert
  void insertDevice(DeviceDb... deviceDbs);

  @Query("DELETE FROM deviceDb")
  void deleteAll();


}
