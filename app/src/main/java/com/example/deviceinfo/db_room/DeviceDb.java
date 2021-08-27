package com.example.deviceinfo.db_room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Contains the fields in the database.
 */
@Entity
public class DeviceDb {

  @PrimaryKey(autoGenerate = true)
  public int id;

  @ColumnInfo
  public String android_id;

  @ColumnInfo
  public String model;

  @ColumnInfo
  public String manufacturer;

  @ColumnInfo
  public String user;

  @ColumnInfo
  public String product;

  @ColumnInfo
  public String android_version;

  @ColumnInfo
  public String userId;

  @ColumnInfo
  public String implementationid;

  @ColumnInfo
  public String trafficSource;

  @ColumnInfo
  public String userClass;



}

